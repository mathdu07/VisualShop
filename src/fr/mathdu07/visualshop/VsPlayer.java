package fr.mathdu07.visualshop;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsTooLateException;

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
	
	private Stack<VsTransaction> transactions;
	
	private VsPlayer(Player p) {
		this.bukkitPlayer = p;
		this.transactions = new Stack<VsTransaction>();
	}
	
	/**
	 * Add the transaction to the player
	 * @param trans - the transaction done
	 */
	public void addTransaction(VsTransaction trans) {
		transactions.push(trans);
	}
	
	/**
	 * Undo the last transaction of the player
	 * @throws VsTooLateException
	 * @throws VsNoItemInInventoryException
	 * @throws VsEconomyException
	 * @return the transaction undone
	 */
	public VsTransaction undoLastTransaction() throws VsTooLateException, VsNoItemInInventoryException, VsEconomyException {
		VsTransaction trans = transactions.peek();
		
		trans.undoTransation();
		return transactions.pop();
	}
	
	public int undoTransactions(int transactionCount) {
		
		for (int i = 0; i < transactionCount; i++) {
			
			VsTransaction t = transactions.peek();
			try {
				undoLastTransaction();
			} catch (VsTooLateException e) {
				//TODO Add time max to undo in config
				return i;
			} catch (VsNoItemInInventoryException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_NO_ITEM.value).replace("{ITEM}", t.is.getType().toString()));
				return i;
			} catch (VsEconomyException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BUY_ECO.value).replace("{ERROR}", e.errorMsg));
				return i;
			}
		}
		
		return transactionCount;
	}
	
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}
	
	/**
	 * Assign a shop to the player 
	 * @param is - shop's itemstack
	 * @param price - shop's price
	 * @throws VsNegativeOrNullValueException if the price is smaller or equal to 0
	 */
	public void assignShopCreation(ItemStack is, double price) throws VsNegativeOrNullValueException {
		if (price <= 0)
			throw new VsNegativeOrNullValueException();
		
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
		Shop s = null;
		
		try {
			s = new Shop(createShopPrice, createShopIS, b.getLocation());
			this.createShopIS = null;
			this.createShopPrice = 0.d;
		} catch (VsNegativeOrNullValueException e) {e.printStackTrace();}
		
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
