package fr.mathdu07.visualshop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsInventoryFullException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsNotEnoughMoneyException;
import fr.mathdu07.visualshop.exception.VsTooLateException;
import fr.mathdu07.visualshop.shop.SellShop;
import fr.mathdu07.visualshop.shop.Shop;

public class VsTransaction {
	
	public final ItemStack is;
	public final double cost;
	public final Shop shop;
	public final VsPlayer player;
	public final long timestamp;
	public final boolean buying;
	
	private static BufferedWriter writer;
	
	/**
	 * A VisualShop Transaction
	 * @param shop
	 * @param amount - the amount of item
	 * @param player
	 * @param buyingOrSelling - true if it's a purchase or false if it's a sale
	 */
	public VsTransaction(Shop shop, int amount, VsPlayer player) {
		this.shop = shop;
		this.is = shop.getItem().clone();
		is.setAmount(amount);
		this.cost = shop.getPricePerUnit() * amount;
		this.player = player;
		this.buying = (SellShop.class.isInstance(shop));
		timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Apply the transaction by giving/taking the money
	 * and taking/giving the item
	 * @return if the transaction is a success
	 */
	public boolean applyTransaction() {
		final Economy eco = VisualShop.getInstance().getEconomy();
		final Player p = player.getBukkitPlayer();
		
		if (!shop.canUse(VsPlayer.getPlayer(p))) {
			p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
			return false;
		}
		
		if (buying) {
			
			if (eco.has(p.getName(), cost)) {
				Map<Integer, ItemStack> exceed = p.getInventory().addItem(is);
				if (!exceed.isEmpty()) {
					ItemStack isExceed = is.clone();
					isExceed.setAmount(is.getAmount() - exceed.size());
					p.getInventory().remove(isExceed);
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_FULL.value));
					return false;
				}
				
				//TODO Pay to the owner if it's a Player Shop
				EconomyResponse resp = eco.withdrawPlayer(p.getName(), cost);
				if (resp.transactionSuccess()) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_TRANSACTION_BUY.value).
							replace("{AMOUNT}", Integer.toString(is.getAmount())).replace("{ITEM}", shop.getItem().getType().toString()).
							replace("{PRICE}", Double.toString(cost)).replace("{$}", eco.currencyNamePlural()));
					p.updateInventory();
					player.addTransaction(this);
					
					if (VisualShop.getVSConfig().LOG_TRANSACTIONS.value) {
						try {
							writer.write("New " + this);
							writer.newLine();
						} catch (IOException e) {e.printStackTrace();}
					}
					
					
					return true;
				}
				else {
					p.getInventory().remove(is);
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BUY_ECO.value).replace("{ERROR}", resp.errorMessage));
					return false;
				}
			} else
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_ENOUGH_MONEY.value).replace("{PRICE}", Double.toString(cost)));
			
			return false;
			
		} else {
			
			if (!p.getInventory().containsAtLeast(is, is.getAmount())) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_NO_ITEM.value).replace("{ITEM}", is.getType().toString()));
				return false;
			}
			
			//TODO Check for player shop if the shop's owner has enough money
			//TODO Check if the shop can take the item
			p.getInventory().removeItem(is);
			EconomyResponse resp = eco.depositPlayer(p.getName(), cost);
			if (resp.transactionSuccess()) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_TRANSACTION_SELL.value).
						replace("{AMOUNT}", Integer.toString(is.getAmount())).replace("{ITEM}", shop.getItem().getType().toString()).
						replace("{PRICE}", Double.toString(cost)).replace("{$}", eco.currencyNamePlural()));
				
				p.updateInventory();
				player.addTransaction(this);
				
				if (VisualShop.getVSConfig().LOG_TRANSACTIONS.value) {
					try {
						writer.write("New " + this);
						writer.newLine();
					} catch (IOException e) {e.printStackTrace();}
				}
				
				return true;
			} else {
				p.getInventory().addItem(is);
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SELL_ECO.value).replace("{ERROR}", resp.errorMessage));
			}
			
			return false;
		}

	}
	
	/**
	 * @return if the transaction can be undone
	 */
	public boolean canUndoTransaction() {
		if (VisualShop.getVSConfig().UNDO_MAX_TIME.value == -1)
			return true;
		else
			return (System.currentTimeMillis() - timestamp) <= VisualShop.getVSConfig().UNDO_MAX_TIME.value * 1000;
	}
	
	public void undoTransation() throws VsTooLateException, VsNoItemInInventoryException, VsEconomyException, VsNotEnoughMoneyException, VsInventoryFullException {
		if (!canUndoTransaction())
			throw new VsTooLateException("Transaction too old, can not be undone");
		
		final Player p = player.getBukkitPlayer();
		final Economy eco = VisualShop.getInstance().getEconomy();
		
		if (buying) {
			if (!p.getInventory().containsAtLeast(is, is.getAmount()))
				throw new VsNoItemInInventoryException(p.getInventory(), is);
			
			EconomyResponse result = eco.depositPlayer(p.getName(), cost);
			
			if (!result.transactionSuccess())
				throw new VsEconomyException(result.errorMessage, result.type);
			
			p.getInventory().removeItem(is);
			if (VisualShop.getVSConfig().LOG_TRANSACTIONS.value) {
				try {
					writer.write("Undo " + this);
					writer.newLine();
				} catch (IOException e) {e.printStackTrace();}
			}
			
		} else {
			if (!eco.has(p.getName(), cost))
				throw new VsNotEnoughMoneyException(cost, eco.getBalance(p.getName()));
				
			Map<Integer, ItemStack> exceed = p.getInventory().addItem(is);
			if (!exceed.isEmpty()) {
				ItemStack isExceed = is.clone();
				isExceed.setAmount(is.getAmount() - exceed.size());
				p.getInventory().remove(isExceed);
				throw new VsInventoryFullException(p.getInventory(), is);
			}
			
			EconomyResponse result = eco.withdrawPlayer(player.getName(), cost);
			if (!result.transactionSuccess()) {
				p.getInventory().remove(is);
				throw new VsEconomyException(result.errorMessage, result.type);
			}
			
			if (VisualShop.getVSConfig().LOG_TRANSACTIONS.value) {
				try {
					writer.write("Undo " + this);
					writer.newLine();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}

	@Override
	public String toString() {
		return "VsTransaction [item=" + is.getType().toString() + ",amount=" + is.getAmount() + ", cost=" + cost +
				", player=" + player.getBukkitPlayer().getName() + ", timestamp=" + timestamp
				+ ", type=" + (buying ? "purchase" : "sale") + "]";
	}
	
	protected static void startLog() {
		try {
			writer = new BufferedWriter(new FileWriter(new File(VisualShop.getInstance().getDataFolder(), "transactions.log"), true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void saveLog() {
		if (writer == null)
			return;
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
