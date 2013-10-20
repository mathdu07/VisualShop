package fr.mathdu07.visualshop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VsPlayer {
	
	private static Map<String, VsPlayer> players = new HashMap<>();
	
	private final Player bukkitPlayer;
	
	/**
	 * The itemstack of the shop that the player would create
	 * If null the player will not create shop
	 */
	private ItemStack createShopIS = null;
	/**
	 * The price of the shop that the player would create
	 * If null the player will not create shop
	 */
	private double createShopPrice = 0d;
	
	/**
	 * Whether the player would delete the clicked shop
	 */
	private boolean wouldDeleteShop = false;
	
	private VsPlayer(Player p) {
		this.bukkitPlayer = p;
	}
	
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}
	
	/**
	 * Assign a shop to the player 
	 * @param is - shop's itemstack
	 * @param price - shop's price
	 */
	public void assignShopCreation(ItemStack is, double price) {
		this.createShopIS = is;
		this.createShopPrice = price;
	}
	
	/**
	 * @return if the player would create a shop at the clicked block
	 */
	public boolean shouldCreateShop() {
		return createShopIS != null && createShopPrice != 0;
	}
	
	/**
	 * @param b - the block where create the shop
	 * @return the shop that the player create
	 */
	public Shop createShop(Block b) {
		Shop s = new Shop(createShopPrice, createShopIS, b.getLocation());
		assignShopCreation(null, 0);
		
		return s;
	}
	
	/**
	 * @return if the shop selected shoud be deleted
	 */
	public boolean shouldDeleteShop() {
		return wouldDeleteShop;
	}
	
	/**
	 * Set if the next selected shop should be deleted
	 * @param deleteShop - whether the next shop should be deleted
	 */
	public void setWouldDeleteShop(boolean deleteShop) {
		this.wouldDeleteShop = deleteShop;
	}
	
	/**
	 * @param player - the bukkit player
	 * @return the visual shop player
	 */
	public static VsPlayer getPlayer(Player player) {
		VsPlayer p = players.get(player.getName());
		
		if (p == null) {
			p = new VsPlayer(player);
			players.put(player.getName(), p);
		}
		
		return p;
	}
	
}
