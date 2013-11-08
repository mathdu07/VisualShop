package fr.mathdu07.visualshop.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;

public abstract class Shop implements ConfigurationSerializable{
	
	private static final List<Shop> shops = new ArrayList<Shop>(); 
	
	private final UUID uuid;
	private double pricePerUnit;
	private final ItemStack item;
	private Block block;
	private Item itemEntity;
	
	/**
	 * Creates an abstract Shop
	 * @param pricePerUnit - the price of one item
	 * @param item - the item to sell/buy
	 * @param bloc - the block where is the shop
	 * @throws VsNegativeOrNullValueException if pricePerUnit < 0
	 */
	public Shop (double pricePerUnit, Material item, Block block) throws VsNegativeOrNullValueException {
		this(pricePerUnit, new ItemStack(item), block);
	}
	
	/**
	 * Creates an abstract Shop
	 * @param pricePerUnit - the price of one item
	 * @param itemstack - the item to sell/buy
	 * @param bloc - the block where is the shop
	 * @throws VsNegativeOrNullValueException if pricePerUnit < 0
	 */
	public Shop (double pricePerUnit, ItemStack itemstack, Block block) throws VsNegativeOrNullValueException {
		this(UUID.randomUUID(), pricePerUnit, itemstack, block);
	}
	
	/**
	 * Creates an abstract Shop with the given UUID
	 * @param uid - the UUID of the shop
	 * @param pricePerUnit - the price of one item
	 * @param itemstack - the item to sell/buy
	 * @param bloc - the block where is the shop
	 * @throws VsNegativeOrNullValueException if pricePerUnit < 0
	 */
	public Shop (UUID uid, double pricePerUnit, ItemStack itemstack, Block block) throws VsNegativeOrNullValueException {
		if (pricePerUnit <= 0)
			throw new VsNegativeOrNullValueException(pricePerUnit);
		
		this.uuid = uid;
		this.pricePerUnit = pricePerUnit;
		this.item = itemstack;
		this.block = block;
		
		spawnItem();
		updateItemPositionLater();
		VisualShop.getShopSaver().addShop(this);
		shops.add(this);
	}
	
	/**
	 * Launch a bukkit task to update the stop
	 */
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
	public boolean spawnItem() {
		if (itemEntity != null)
			return false;
		
		itemEntity = block.getWorld().dropItem(block.getLocation().add(0.5, 1.2, 0.5), item.clone());
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
		
		if (itemEntity != null)
			itemEntity.teleport(block.getLocation().add(0.5, 1.2, 0.5));
		else
			spawnItem();
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
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
		VisualShop.getShopSaver().updateShop(this);
	}

	/**
	 * @return the UUID
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * @return the item sold/bought
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * @return the itemEntity which stands for the shop
	 */
	public Item getItemEntity() {
		return itemEntity;
	}
	
	/**
	 * @return the shop's location
	 */
	public Location getLocation() {
		return block.getLocation();
	}
	
	/**
	 * Move the shop to the given location
	 * @param location - the new location of the shop
	 */
	public void move(Location location) {
		move(location.getBlock());
	}
	
	/**
	 * Move the shop to the given block
	 * @param block - the new block where is the shop
	 */
	public void move(Block block) {
		this.block = block;
		VisualShop.getShopSaver().updateShop(this);
	}
	
	/**
	 * Delete the shop
	 */
	public void delete() {
		despawnItem();
		shops.remove(this);
		VisualShop.getShopSaver().deleteShop(this);
		
		if (VisualShop.getVSConfig().LOG_SHOP_CREATION.value)
			VisualShop.info("Shop deleted : " + this);
	}
	
	/**
	 * @param p
	 * @return if the player owns the shop
	 */
	public abstract boolean ownsShop(VsPlayer p); 
	
	/**
	 * @param p - the player to check
	 * @return if the player can use the shop, buy/sell the item ...
	 */
	public abstract boolean canUse(VsPlayer p);
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Shop ? ((Shop)o).uuid.equals(this.uuid) : false);
	}
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("uid", uuid.toString());
		map.put("price", pricePerUnit);
		map.put("item", item);
		map.put("world", block.getWorld().getName());
		map.put("x", block.getX());
		map.put("y", block.getY());
		map.put("z", block.getZ());
		
		return map;
	}
	
	// To implement on children
	/*public static Shop deserialize(Map<String, Object> map) {
		World world = Bukkit.getWorld((String) map.get("world"));
		UUID uid = UUID.fromString((String) map.get("uid"));
		int x = (int) map.get("x"), y = (int) map.get("y"), z = (int) map.get("z");
		
		try {
			return new Shop(uid, (double) map.get("price"), (ItemStack) map.get("item"), world.getBlockAt(x, y, z));
		} catch (VsNegativeOrNullValueException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	/**
	 * @return an iterator of the registered shops
	 */
	public static Iterator<Shop> getShops() {
		return shops.iterator();
	}
	
	/**
	 * @param block - the block of the shop
	 * @return the shop at the given block, or null if not found
	 */
	public static Shop getShop(Block block) {
		
		for (Shop s : shops) {
			if (s.block.equals(block))
				return s;
		}
		
		return null;
	}
	
	/**
	 * @param uuid - the UUID of the shop
	 * @return the shop with the given UUId, or null if doesn't exist
	 */
	public static Shop getShop(UUID uuid) {
		
		for (Shop s : shops) {
			if (s.uuid.equals(uuid))
				return s;
		}
		
		return null;
	}
	
	/**
	 * @param itemEntity
	 * @return the shop that owns the item entity, or null
	 */
	public static Shop getShop(Item itemEntity) {
		
		for (Shop s : shops) {
			if (s.itemEntity.equals(itemEntity))
				return s;
		}
		
		return null;
	}
	
	/**
	 * @param block
	 * @return if a shop exists at the given block
	 */
	public static boolean shopExistsAt(Block block) {
		return getShop(block) != null;
	}
	
	/**
	 * @param uuid
	 * @return if a shop has the given UUID
	 */
	public static boolean shopExists(UUID uuid) {
		return getShop(uuid) != null;
	}
	
	/**
	 * @param item
	 * @return if the item belongs to a shop
	 */
	public static boolean shopOwnsItem(Item item) {
		return getShop(item) != null;
	}
	
	/**
	 * Remove all shops from the list
	 */
	public static void clearShopList() {
		for (Shop s : shops) {
			s.despawnItem();
		}
		
		shops.clear();
	}
	
	/**
	 * Deserialize an old shop (older than 1.1)
	 * Will be removed soon
	 * @param map
	 * @return New AdminSellShop from the old
	 */
	@Deprecated
	public static Shop deserialize(Map<String, Object> map) {
		World world = Bukkit.getWorld((String) map.get("world"));
		int x = (int) map.get("x"), y = (int) map.get("y"), z = (int) map.get("z");
		
		try {
			return new AdminSellShop((double) map.get("price"), (ItemStack) map.get("item"), world.getBlockAt(x, y, z));
		} catch (VsNegativeOrNullValueException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public String toString() {
		return "Shop [uuid=" + uuid + ", price=" + pricePerUnit
				+ ", item=" + item.getType() + ", world=" + block.getWorld().getName()
				+ ", x=" + block.getX() + ", y=" + block.getY() + ", z=" + block.getZ() + "]";
	}

}
