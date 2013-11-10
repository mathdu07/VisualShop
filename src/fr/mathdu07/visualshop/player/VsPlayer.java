package fr.mathdu07.visualshop.player;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsTransaction;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsInventoryFullException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsNotEnoughMoneyException;
import fr.mathdu07.visualshop.exception.VsNullException;
import fr.mathdu07.visualshop.exception.VsTooLateException;
import fr.mathdu07.visualshop.player.ability.DescribeShopAbility;
import fr.mathdu07.visualshop.player.ability.DoShopTransactionAbility;
import fr.mathdu07.visualshop.player.ability.NoPickupShopAbility;
import fr.mathdu07.visualshop.player.ability.VsPlayerAbility;

public class VsPlayer {
	
	private static Map<String, VsPlayer> players = new HashMap<>();
	
	/**
	 * The player's name
	 */
	private final String name;
		
	/**
	 * If the player is in advanced mode
	 * Used to display more info about shops (like uid)
	 */
	private boolean toggleAdvanced = false;
	
	private final Stack<VsTransaction> transactions;
	
	/**
	 * The abilities of the player <br />
	 * E.g: NoPickupShopAbility to block player taking shop's item entity
	 */
	private final List<VsPlayerAbility> abilities;
	
	/**
	 * A stack of the abilities that needs to be removed
	 */
	private final Stack<VsPlayerAbility> abilitiesToRemove;
	
	private VsPlayer(Player p) {
		this(p.getName());
	}
	
	private VsPlayer(String playerName) {
		this.name = playerName;
		this.transactions = new Stack<VsTransaction>();
		this.abilities = new ArrayList<VsPlayerAbility>();
		this.abilitiesToRemove = new Stack<VsPlayerAbility>();
		
		addAbilities();
	}
	
	private void addAbilities() {
		abilities.add(new NoPickupShopAbility(this));
		abilities.add(new DescribeShopAbility(this));
		abilities.add(new DoShopTransactionAbility(this));
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
	 * Handles the player event
	 * @param e - PlayerEvent
	 */
	public void handlePlayerEvent(PlayerEvent e) {
		while (!abilitiesToRemove.isEmpty())
			abilities.remove(abilitiesToRemove.pop());
		
		if (!e.getPlayer().getName().equals(name))
			return;
		
		Iterator<VsPlayerAbility> it = abilities.iterator();
		while (it.hasNext()) {
			VsPlayerAbility ability = it.next();
			
			if (e instanceof PlayerInteractEvent) {
				if (!ability.onPlayerInteract((PlayerInteractEvent) e))
					break;
			} else if (e instanceof PlayerPickupItemEvent) {
				if(!ability.onPlayerPickUp((PlayerPickupItemEvent) e))
					break;
			}
		}
	}
	
	/**
	 * Adds the player ability
	 * @param ability
	 */
	public void addAbility(VsPlayerAbility ability) {
		abilities.add(0, ability);
	}
	
	/**
	 * @param ability
	 * @return if the player has the given ability
	 */
	public boolean containsAbility(VsPlayerAbility ability) {
		return abilities.contains(ability);
	}
	
	/**
	 * Remove the player ability at the next PlayerEvent
	 * @param ability
	 */
	public void removeAbility(VsPlayerAbility ability) {
		abilitiesToRemove.push(ability);
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
