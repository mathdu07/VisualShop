package fr.mathdu07.visualshop.player;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsTransaction;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsInventoryFullException;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsNotEnoughMoneyException;
import fr.mathdu07.visualshop.exception.VsNullException;
import fr.mathdu07.visualshop.exception.VsTooLateException;
import fr.mathdu07.visualshop.shop.AdminBuyShop;
import fr.mathdu07.visualshop.shop.AdminSellShop;
import fr.mathdu07.visualshop.shop.Shop;

public class VsPlayer {
	
	private static Map<String, VsPlayer> players = new HashMap<>();
	
	private final String name;
	
	/**
	 * The class that defines the type of shop the player would create
	 */
	private Class<? extends Shop> createShopClass = null;
	
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
	
	/**
	 * If the player is in advanced mode
	 * Used to display more info about shops (like uid)
	 */
	private boolean toggleAdvanced = false;
	
	private Stack<VsTransaction> transactions;
	
	private VsPlayer(Player p) {
		this(p.getName());
	}
	
	private VsPlayer(String playerName) {
		this.name = playerName;
		this.transactions = new Stack<VsTransaction>();
	}
	
	/**
	 * @return an array of player's transaction copy
	 */
	public VsTransaction[] getTransactions() {
		VsTransaction[] array = new VsTransaction[transactions.size()];
		transactions.copyInto(array);
		
		return array;
	}
	
	/**
	 * Add the transaction to the player
	 * @param trans - the transaction done
	 */
	public void addTransaction(VsTransaction trans) {
		transactions.push(trans);
	}
	
	public VsTransaction getLastTransaction() {
		try {
			return transactions.peek();
		} catch (EmptyStackException e) {
			return null;
		}
	}
	
	/**
	 * Undo the last transaction of the player
	 * @throws VsTooLateException
	 * @throws VsNoItemInInventoryException
	 * @throws VsEconomyException
	 * @return the transaction undone
	 * @throws VsNotEnoughMoneyException 
	 * @throws VsInventoryFullException 
	 */
	public VsTransaction undoLastTransaction() throws VsTooLateException, VsNoItemInInventoryException, VsEconomyException, VsNullException, VsInventoryFullException, VsNotEnoughMoneyException {
		try {
			VsTransaction trans = transactions.peek();
			trans.undoTransation();
		} catch (EmptyStackException e) {
			throw new VsNullException();
		}
		
		return transactions.pop();
	}
	
	public int undoTransactions(int transactionCount) {
		Player bukkitPlayer = getBukkitPlayer();
		
		for (int i = 0; i < transactionCount; i++) {
			
			VsTransaction t = getLastTransaction();
			try {
				undoLastTransaction();
			} catch (VsTooLateException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_UNDO_TOO_LATE.value).replace("{TIME}", Integer.toString(VisualShop.getVSConfig().UNDO_MAX_TIME.value)));
				return i;
			} catch (VsNoItemInInventoryException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_NO_ITEM.value).replace("{ITEM}", t.is.getType().toString()));
				return i;
			} catch (VsEconomyException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BUY_ECO.value).replace("{ERROR}", e.errorMsg));
				return i;
			} catch (VsNullException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOTHING_UNDO.value));
				return i;
			} catch (VsInventoryFullException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_FULL.value));
				return i;
			} catch (VsNotEnoughMoneyException e) {
				bukkitPlayer.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_ENOUGH_MONEY.value).replace("{PRICE}", Double.toString(e.moneyRequired)));
				return i;
			}
		}
		
		return transactionCount;
	}
	
	/**
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}
	
	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	/**
	 * @return if the player is in advanced mode
	 */
	public boolean toggleAdvanced() {
		return toggleAdvanced;
	}
	
	/**
	 * Set if the player is in advanced mode
	 * @param toggle - is in advanced mode
	 */
	public void toggleAdvanced(boolean toggle) {
		this.toggleAdvanced = toggle;
	}
	
	/**
	 * Assign an admin sell shop to the player 
	 * @param is - shop's itemstack
	 * @param price - shop's price
	 * @throws VsNegativeOrNullValueException if the price is smaller or equal to 0
	 */
	public void assignAdminSellShopCreation(ItemStack is, double price) throws VsNegativeOrNullValueException {
		if (price <= 0)
			throw new VsNegativeOrNullValueException();
		
		this.createShopIS = is;
		this.createShopPrice = price;
		this.createShopClass = AdminSellShop.class;
	}
	
	/**
	 * Assign an amdin buy shop to the player 
	 * @param is - shop's itemstack
	 * @param price - shop's price
	 * @throws VsNegativeOrNullValueException if the price is smaller or equal to 0
	 */
	public void assignAdminBuyShopCreation(ItemStack is, double price) throws VsNegativeOrNullValueException {
		if (price <= 0)
			throw new VsNegativeOrNullValueException();
		
		this.createShopIS = is;
		this.createShopPrice = price;
		this.createShopClass = AdminBuyShop.class;
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
			if (createShopClass.equals(AdminSellShop.class))
				s = new AdminSellShop(createShopPrice, createShopIS, b);
			else if (createShopClass.equals(AdminBuyShop.class))
				s = new AdminBuyShop(createShopPrice, createShopIS, b);
			else
				VisualShop.debug("Unknown shop class to create : " + createShopClass);
				
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
