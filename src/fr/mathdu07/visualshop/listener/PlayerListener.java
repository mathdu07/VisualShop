package fr.mathdu07.visualshop.listener;

import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
		} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if (Shop.hasShopAt(b.getLocation())) {
				
				Shop shop = Shop.getShopAt(b.getLocation());
				Economy eco = VisualShop.getInstance().getEconomy();
				
				if (eco.has(p.getName(), shop.getPricePerUnit() * 1)) {
					ItemStack is = shop.getItem();
					is.setAmount(1);
					
					Map<Integer, ItemStack> exceed = p.getInventory().addItem(is);
					if (!exceed.isEmpty()) {
						is.setAmount(1 - exceed.size());
						p.getInventory().remove(is);
						p.sendMessage(ChatColor.RED + "Votre inventaire est complet, achat non pris en compte.");//TEMPLATE
						return;
					}
					
					EconomyResponse resp = eco.withdrawPlayer(p.getName(), shop.getPricePerUnit() * 1);
					if (resp.transactionSuccess())
						p.sendMessage(ChatColor.GREEN + "Achat de 1 " + ChatColor.AQUA + shop.getItem().getType() + ChatColor.GREEN + " réussi"); //TEMPLATE
					else {
						p.getInventory().remove(is);
						p.sendMessage(ChatColor.RED + "Erreur lors de l'achat : " + resp.errorMessage); //TEMPLATE
					}
				} else
					p.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent (" + shop.getPricePerUnit() * 1 + ")."); //TEMPLATE
			}
		}
	}

}
