package fr.mathdu07.visualshop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

public class Shop implements ConfigurationSerializable {
	
	private static final Set<Shop> shops = new HashSet<Shop>(); 
	
	private float pricePerUnit;
	private final ItemStack item;
	private Location location;
	
	public Shop (float pricePerUnit, Material item, Location loc) {
		this.pricePerUnit = pricePerUnit;
		this.item = new ItemStack(item);
		this.location = loc;
		
		shops.add(this);
	}
	
	@SuppressWarnings("deprecation")
	public Shop (float pricePerUnit, int itemID, Location loc) {
		this.pricePerUnit = pricePerUnit;
		this.item = new ItemStack(itemID);
		this.location = loc;
		
		shops.add(this);
	}
	
	public Shop (float pricePerUnit, ItemStack itemstack, Location loc) {
		this.pricePerUnit = pricePerUnit;
		this.item = itemstack;
		this.location = loc;
		
		shops.add(this);
	}

	/**
	 * @return the pricePerUnit
	 */
	public float getPricePerUnit() {
		return pricePerUnit;
	}

	/**
	 * @param pricePerUnit the pricePerUnit to set
	 */
	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * @return the item
	 */
	public ItemStack getItem() {
		return item;
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
		map.put("location", location);
		
		return map;
	}
	
	public static Shop deserialize(Map<String, Object> map) {
		return new Shop((float) map.get("price"), (ItemStack) map.get("item"), (Location) map.get("location"));
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
	
	static {
		 ConfigurationSerialization.registerClass(Shop.class);
	}

}
