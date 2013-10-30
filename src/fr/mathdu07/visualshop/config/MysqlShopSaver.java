package fr.mathdu07.visualshop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;

public class MysqlShopSaver implements ShopSaver {
	
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
		
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + "admin_sell_shop"; // TODO Check shop type
		Map<String, Object> data = shop.serialize();
		
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			boolean exec = st.execute("INSERT INTO " + table + " VALUES (" + //TODO Serialize itemstack
					"'" + data.get("uid") + "'," + data.get("price") + ",'" + "WIP" + "','" + 
					data.get("world") + "'," + data.get("x") + "," + data.get("y") + "," + data.get("z") + ");");
			
			return exec;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateShop(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteShop(Shop shop) {
		if (!isShopSaved(shop))
			return false;
		
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + "admin_sell_shop"; // TODO Check shop type
		
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			boolean deleted = st.execute("DELETE FROM " + table + " WHERE uid='" + shop.getUid() + "';");
			return deleted;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadShops() {
		// TODO Auto-generated method stub
		return false;
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
		String table = VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + "admin_sell_shop";
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
		st.execute("CREATE TABLE IF NOT EXISTS " + VisualShop.getVSConfig().MYSQL_TABLE_PREFIX.value + "admin_sell_shop ("
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
