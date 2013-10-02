package fr.mathdu07.visualshop.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.ShopManager;
import fr.mathdu07.visualshop.VisualShop;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e == null) return;
		
		Player p = e.getPlayer();
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if (ShopManager.willPlayerCreateShop(p)) {
				
				if (Shop.getShopAt(b.getLocation()) == null) {
					Shop shop = new Shop(ShopManager.getPriceAssignedByPlayer(p), ShopManager.getISAssignedByPlayer(p), b.getLocation());
					p.sendMessage(ChatColor.GREEN + "Commerce crée avec succès");
					ShopManager.removePlayer(p);
					
					//TODO Add option disable creation of shop log in the config
					VisualShop.info("Shop created :" + shop);
				} else 
					p.sendMessage(ChatColor.RED + "Il y a déjà un commerce ici !"); //TEMPLATE
				e.setCancelled(true);
			} else if (Shop.getShopAt(b.getLocation()) != null) {
				Shop shop = Shop.getShopAt(b.getLocation());
				//TEMPLATE (s)
				p.sendMessage(ChatColor.GRAY + "+---------------[" + ChatColor.WHITE + "Shop" + ChatColor.GRAY + "]---------------+");
				p.sendMessage("Prix à l'unité : " + ChatColor.YELLOW + shop.getPricePerUnit());
				p.sendMessage("Item : " + ChatColor.YELLOW + shop.getItem().getType());
				p.sendMessage(ChatColor.GRAY + "+-----------------------------------+");
				//p.sendMessage("Owner :");
				e.setCancelled(true);
			}
		}
	}

}
