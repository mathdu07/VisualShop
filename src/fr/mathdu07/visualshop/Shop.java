package fr.mathdu07.visualshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.config.Config;

public class Shop implements ConfigurationSerializable {
	
	private static final Set<Shop> shops = new HashSet<Shop>(); 
	
	private double pricePerUnit;
	private final ItemStack item;
	private Location location;
	private Item itemEntity;
	
	public Shop (double pricePerUnit, Material item, Location loc) {
		this(pricePerUnit, new ItemStack(item), loc);
	}
	
	@SuppressWarnings("deprecation")
	public Shop (double pricePerUnit, int itemID, Location loc) {
		this(pricePerUnit, new ItemStack(itemID), loc);
	}
	
	public Shop (double pricePerUnit, ItemStack itemstack, Location loc) {
		this.pricePerUnit = pricePerUnit;
		this.item = itemstack;
		this.location = loc;
		
		spawnItem();
		updateItemPositionLater();
		shops.add(this);
	}
	
	private void updateItemPositionLater() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(VisualShop.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				update();
			}
			
		}, 20);
	}
	
	/**
	 * Spawn the entity item that stands for the shop
	 * @return if the item has spawned, false if there was already an item entity
	 */
	protected boolean spawnItem() {
		if (itemEntity != null)
			return false;
		
		itemEntity = location.getWorld().dropItem(location.clone().add(0.5, 1.2, 0.5), item);
		return true;
	}
	
	/**
	 * Despawn the entity item that standed for the shop
	 * @return if the item has been removed, false if there was not
	 */
	protected boolean despawnItem() {
		if (itemEntity == null)
			return false;
		
		itemEntity.remove();
		return true;
	}
	
	/**
	 * Update the shop by teleporting the item entity to its coordinates
	 */
	public void update() {
		itemEntity.teleport(location.clone().add(0.5, 1.2, 0.5));
	}

	/**
	 * @return the pricePerUnit
	 */
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	/**
	 * @param pricePerUnit the pricePerUnit to set
	 */
	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * @return a clone of the itemstack
	 */
	public ItemStack getItem() {
		return item.clone();
	}
	
	/**
	 * @return the item entity associated to the shop
	 */
	public Item getItemEntity() {
		return itemEntity;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("price", pricePerUnit);
		map.put("item", item);
		map.put("world", location.getWorld().getName());
		map.put("x", location.getBlockX());
		map.put("y", location.getBlockY());
		map.put("z", location.getBlockZ());
		
		return map;
	}
	
	public static Shop deserialize(Map<String, Object> map) {
		World world = Bukkit.getWorld((String) map.get("world"));
		
		int x = (int) map.get("x"), y = (int) map.get("y"), z = (int) map.get("z");
		
		return new Shop((double) map.get("price"), (ItemStack) map.get("item"), new Location(world, x, y, z));
	}
	
	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return the shop at the given location or null
	 */
	public static Shop getShopAt(World world, int x, int y, int z) {
		Iterator<Shop> it = shops.iterator();
		
		while (it.hasNext()) {
			Shop shop = it.next();
			Location loc = shop.location;
			
			if (loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z && loc.getWorld().equals(world))
				return shop;
		}
		
		return null;
	}
	
	/**
	 * @param loc
	 * @return the shop at the given location or null
	 */
	public static Shop getShopAt(Location loc) {
		return getShopAt(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
	/**
	 * @param loc
	 * @return if there is a shop at the given location or not
	 */
	public static boolean hasShopAt(Location loc) {
		return getShopAt(loc) != null;
	}
	
	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return if there is a shop at the given location or not
	 */
	public static boolean hasShopAt(World world, int x, int y, int z) {
		return getShopAt(world, x, y, z) != null;
	}
	
	/**
	 * @return an iterator to the shops
	 */
	public static Iterator<Shop> getShops() {
		return shops.iterator();
	}
	
	/**
	 * Deletes the shop
	 * @param location - the location of the shop
	 * @return if the shop has been deleted
	 */
	public static boolean deleteShop(Location loc) {
		Iterator<Shop> it = shops.iterator();
		
		while (it.hasNext()) {
			Shop s = it.next();
			
			if (s.getLocation().equals(loc)) {
				s.itemEntity.remove();
				shops.remove(s);
				
				if (VisualShop.getVSConfig().getBooleanProperty(Config.LOG_SHOP_CREATION))
					VisualShop.info("Shop deleted : " + s);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Clear the set of the shop
	 * Used when the plugin is disabled
	 */
	public static void removeShops() {
		shops.clear();
	}
	
	/**
	 * @return whether the given item entity is owned to a shop
	 */
	public static boolean isItemOwnedToAShop(Item i) {
		for (Shop s : shops) {
			if (s.getItemEntity() == null)
				continue;
			
			if (s.getItemEntity().getUniqueId().equals(i.getUniqueId()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * @param chunk - the chucks were seek shops
	 * @return all the shops in the given chunk
	 */
	public static Shop[] getShopsAt(Chunk chunk) {
		Iterator<Shop> it = shops.iterator();
		List<Shop> array = new ArrayList<Shop>();
		
		while (it.hasNext()) {
			Shop s = it.next();
			
			if (chunk.getWorld().equals(s.getLocation().getWorld())
				&& Math.floor(s.getLocation().getBlockX() / 16d) == chunk.getX()
				&& Math.floor(s.getLocation().getBlockZ() / 16d) == chunk.getZ())
				array.add(s);
		}
		
		return array.toArray(new Shop[array.size()]);
	}
	
	@Override
	public String toString() {
		return "[item=" + item.getType() + ";price=" + pricePerUnit + 
				";x=" + location.getBlockX() + ";y=" + location.getBlockY() + ";z=" + location.getBlockZ() + "]";
	}
}
