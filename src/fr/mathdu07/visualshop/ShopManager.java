package fr.mathdu07.visualshop;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopManager {
	
	private static Map<Player, ItemStack> playerIS = new HashMap<>();
	private static Map<Player, Double> playerPrice = new HashMap<>();
	
	/** 
	 * Assigns the future creation of a shop to a player
	 * so that he will create a shop when he click on a block
	 * @param p - the player that will create a shop
	 * @param is - the shop's itemstack
	 * @param price - the shop's price per unit
	 */
	public static void assignPlayerShop(Player p, ItemStack is, double price) {
		playerIS.put(p, is);
		playerPrice.put(p, price);
	}
	
	/**
	 * @param p
	 * @return the itemstack for the creation of the shop
	 */
	public static ItemStack getISAssignedByPlayer(Player p) {
		return  playerIS.get(p);
	}
	
	/**
	 * @param p
	 * @return the price per unit for the creation of the shop
	 */
	public static double getPriceAssignedByPlayer(Player p) {
		return  playerPrice.get(p);
	}
	
	public static boolean willPlayerCreateShop(Player p) {
		return playerIS.containsKey(p) || playerPrice.containsKey(p);
	}
	
	/**
	 * Removes the shop that the player should have create
	 * @param p
	 */
	public static void removePlayer(Player p) {
		playerIS.remove(p);
		playerPrice.remove(p);
	}
	
	protected static void resetPlayers() {
		playerIS.clear();
		playerPrice.clear();
	}

}
