package fr.mathdu07.visualshop.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPlayer;
import fr.mathdu07.visualshop.VsTransaction;
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
				int amount = 1;
				VsTransaction transaction = new VsTransaction(shop, amount, vp);
				transaction.applyTransaction();
				
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
