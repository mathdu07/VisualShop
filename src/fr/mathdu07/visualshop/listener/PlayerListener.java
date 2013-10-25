package fr.mathdu07.visualshop.listener;

import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPlayer;
import fr.mathdu07.visualshop.config.Templates;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e == null) return;
		
		Player p = e.getPlayer();
		VsPlayer vp = VsPlayer.getPlayer(p);
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if (vp.shouldCreateShop()) {
				
				if (Shop.getShopAt(b.getLocation()) == null && b.getWorld().getBlockAt(b.getLocation().add(new Vector(0, 1, 0))).getType().equals(Material.AIR)) {
					Shop shop = vp.createShop(b);
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_SHOP_CREATION.value));
					
					if (VisualShop.getVSConfig().LOG_SHOP_CREATION.value)
						VisualShop.info("Shop created : " + shop);
				} else if (!b.getWorld().getBlockAt(b.getLocation().add(new Vector(0, 1, 0))).getType().equals(Material.AIR))
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NO_AIR_ABOVE.value));
				else 
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_PRESENT.value));
				e.setCancelled(true);
			} else if (vp.shouldDeleteShop()) {
				
				if (Shop.hasShopAt(b.getLocation())) {
					Shop.deleteShop(b.getLocation());
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_SHOP_DESTRUCTION.value));
					vp.setWouldDeleteShop(false);
					
				} else
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_MISSING.value));
				
				e.setCancelled(true);
				
			} else if (Shop.getShopAt(b.getLocation()) != null) {
				Shop shop = Shop.getShopAt(b.getLocation());
				
				p.sendMessage(Templates.listToArray(
						Templates.replaceStrArray(
						Templates.replaceStrArray(Templates.colorStrArray(VisualShop.getTemplates().SHOP_INFO.value),
						"{PRICE}", Double.toString(shop.getPricePerUnit())),
						"{ITEM}", shop.getItem().getType().toString())));
				//p.sendMessage("Owner :");
				e.setCancelled(true);
			}
		} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if (Shop.hasShopAt(b.getLocation())) {
				
				Shop shop = Shop.getShopAt(b.getLocation());
				Economy eco = VisualShop.getInstance().getEconomy();
				int amount = 1;
				double price = shop.getPricePerUnit() * amount;
				
				if (eco.has(p.getName(), price)) {
					ItemStack is = shop.getItem();
					is.setAmount(amount);
					
					Map<Integer, ItemStack> exceed = p.getInventory().addItem(is);
					if (!exceed.isEmpty()) {
						is.setAmount(amount - exceed.size());
						p.getInventory().remove(is);
						p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_FULL.value));
						return;
					}
					
					EconomyResponse resp = eco.withdrawPlayer(p.getName(), price);
					if (resp.transactionSuccess()) {
						p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_TRANSACTION.value).
								replace("{AMOUNT}", Integer.toString(amount)).replace("{ITEM}", shop.getItem().getType().toString()).
								replace("{PRICE}", Double.toString(price)).replace("{$}", eco.currencyNamePlural()));
						p.updateInventory();
					}
					else {
						p.getInventory().remove(is);
						p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BUY_ECO.value).replace("{ERROR}", resp.errorMessage));
					}
				} else
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_ENOUGH_MONEY.value).replace("{PRICE}", Double.toString(price)));
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (e == null)	return;
		
		final Item i = e.getItem();
		
		if (Shop.isItemOwnedToAShop(i)) {
			e.setCancelled(true);
			i.setPickupDelay(2000);
		}
	}

}
