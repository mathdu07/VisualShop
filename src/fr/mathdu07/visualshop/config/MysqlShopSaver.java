package fr.mathdu07.visualshop.config;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;

public class MysqlShopSaver implements ShopSaver {
	
	private static final String TABLE_ADMIN_SELL_SHOP = "admin_sell_shop";
	
	private final String host;
	private final String login;
	private final String password;
	private final String database;
	private final int port;
	
	private Connection conn;
	
	public MysqlShopSaver (String host, String login, String password, String database, int port) {
		this.host = host;
		this.login = login;
		this.password = password;
		this.database = database;
		this.port = port;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			VisualShop.severe("Cannot find mysql driver, please check if the MySQL library is present in directory libs");
			e.printStackTrace();
		}
	}

	public boolean addShop(Shop shop) {
		if (isShopSaved(shop))
			return false;
		
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + TABLE_ADMIN_SELL_SHOP; // TODO Check shop type
		Map<String, Object> data = shop.serialize();
		
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + " VALUES (?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, (String) data.get("uid"));
			ps.setDouble(2, (double) data.get("price"));
			ps.setBytes(3, serializeItemstack((ItemStack) data.get("item")));
			ps.setString(4, (String) data.get("world"));
			ps.setInt(5, (int) data.get("x"));
			ps.setInt(6, (int) data.get("y"));
			ps.setInt(7, (int) data.get("z"));
			
			boolean exec = ps.execute();
			ps.close();
			return exec;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private byte[] serializeItemstack(ItemStack is) {
		Map<String, Object> data = is.serialize();
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			
			byte[] output = baos.toByteArray();
			return output;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private ItemStack deserializeItemstack(byte[] array) {
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(array)));
			Map<String, Object> data = (Map<String, Object>) ois.readObject();
			ois.close();
			
			return ItemStack.deserialize(data);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean updateShop(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteShop(Shop shop) {
		if (!isShopSaved(shop))
			return false;
		
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + TABLE_ADMIN_SELL_SHOP; // TODO Check shop type
		
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			boolean deleted = st.execute("DELETE FROM " + table + " WHERE uid='" + shop.getUid() + "';");
			st.close();
			return deleted;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadShops() {
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet adminSellShops = st.executeQuery("SELECT * FROM " + VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + TABLE_ADMIN_SELL_SHOP);
			loadAdminSellShop(adminSellShops);
			
			st.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void loadAdminSellShop(ResultSet result) throws SQLException {
		while (result.next()) {
			String uid = result.getString("uid");
			double price = result.getDouble("price");
			ItemStack is = deserializeItemstack(result.getBytes("itemstack"));
			String world = result.getString("world");
			int x = result.getInt("x"), y = result.getInt("y"), z = result.getInt("z");
			
			Map<String, Object> map = new HashMap<>(); 
			map.put("uid", uid);
			map.put("price", price);
			map.put("item", is);
			map.put("world", world);
			map.put("x", x);
			map.put("y", y);
			map.put("z", z);
			Shop.deserialize(map);
		}
	}

	public void reloadShops() {
		Shop.removeShops();
		if (!loadShops())
			VisualShop.severe("Can't load shops from MySQL after plugin reloading");
	}

	public boolean isShopSaved(Shop shop) {
		if (shop == null)
			return false;
		
		boolean exists = false;		
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + TABLE_ADMIN_SELL_SHOP;
		//TODO Check shop type
		
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet result = st.executeQuery("SELECT * FROM " + table + " WHERE uid='" + shop.getUid() + "';");
			exists = result.first();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return exists;
	}
	
	private void createTables() throws SQLException {
		Statement st = conn.createStatement();
		st.execute("CREATE TABLE IF NOT EXISTS " + VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + TABLE_ADMIN_SELL_SHOP + " ("
				+ "uid CHAR(36) NOT NULL,"
				+ "price DOUBLE NOT NULL DEFAULT 0.0,"
				+ "itemstack BLOB NOT NULL,"
				+ "world VARCHAR(64) NOT NULL DEFAULT 'world',"
				+ "x INT NOT NULL DEFAULT 0,"
				+ "y INT NOT NULL DEFAULT 64,"
				+ "z INT NOT NULL DEFAULT 0,"
				+ "PRIMARY KEY (uid)) ENGINE=INNODB;");
		st.close();
	}

	@Override
	public void onEnable() {
		try {
			VisualShop.info("Connecting to the mysql server ...");
			long timestamp = System.currentTimeMillis();
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database, login, password);
			VisualShop.info("Connection done in " + (System.currentTimeMillis() - timestamp) + " ms.");
			createTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!loadShops())
			VisualShop.severe("Can't load shops from MySQL");
		
	}

	@Override
	public void onDisable() {
		Shop.removeShops();
		
		try {
			VisualShop.info("Disconnect from MySQL server");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
