package fr.mathdu07.visualshop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsTooLateException;

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
		this.buying = true;
		timestamp = System.currentTimeMillis();
		
		if (writer == null) {
			try {
				writer = new BufferedWriter(new FileWriter(new File(VisualShop.getInstance().getDataFolder(), "transactions.log"), true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Apply the transaction by giving/taking the money
	 * and taking/giving the item
	 * @return if the transaction is a success
	 */
	public boolean applyTransaction() {
		final Economy eco = VisualShop.getInstance().getEconomy();
		final Player p = player.getBukkitPlayer();
		
		if (buying) {
			
			if (!p.hasPermission(new Permission("visualshop.common.use", PermissionDefault.TRUE))) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
				return false;
			}
			
			if (eco.has(p.getName(), cost)) {
				Map<Integer, ItemStack> exceed = p.getInventory().addItem(is);
				if (!exceed.isEmpty()) {
					ItemStack isExceed = is.clone();
					isExceed.setAmount(is.getAmount() - exceed.size());
					p.getInventory().remove(isExceed);
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_FULL.value));
					return false;
				}
				
				EconomyResponse resp = eco.withdrawPlayer(p.getName(), cost);
				if (resp.transactionSuccess()) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_TRANSACTION.value).
							replace("{AMOUNT}", Integer.toString(is.getAmount())).replace("{ITEM}", shop.getItem().getType().toString()).
							replace("{PRICE}", Double.toString(cost)).replace("{$}", eco.currencyNamePlural()));
					p.updateInventory();
					player.addTransaction(this);
					try {
						writer.write("New " + this);
						writer.newLine();
					} catch (IOException e) {e.printStackTrace();}
					
					
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
			//TODO Support sell transaction
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
	
	public void undoTransation() throws VsTooLateException, VsNoItemInInventoryException, VsEconomyException {
		if (!canUndoTransaction())
			throw new VsTooLateException("Transaction too old, can not be undone");
		
		final Player p = player.getBukkitPlayer();
		final Economy eco = VisualShop.getInstance().getEconomy();
		
		if (buying) {
			if (!p.getInventory().containsAtLeast(is, is.getAmount()))
				throw new VsNoItemInInventoryException(is);
			
			EconomyResponse result = eco.depositPlayer(p.getName(), cost);
			
			if (!result.transactionSuccess())
				throw new VsEconomyException(result.errorMessage, result.type);
			
			p.getInventory().removeItem(is);
			try {
				writer.write("Undo " + this);
				writer.newLine();
			} catch (IOException e) {e.printStackTrace();}
			
		} else {
			//TODO Support undo sell transaction
		}
	}

	@Override
	public String toString() {
		return "VsTransaction [item=" + is.getType().toString() + ",amount=" + is.getAmount() + ", cost=" + cost +
				", player=" + player.getBukkitPlayer().getName() + ", timestamp=" + timestamp
				+ ", type=" + (buying ? "purchase" : "sale") + "]";
	}
	
	public static void saveLog() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
