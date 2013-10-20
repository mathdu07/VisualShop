package fr.mathdu07.visualshop.listener;

import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
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
					p.sendMessage(ChatColor.GREEN + "Commerce crée avec succès");
					
					//TODO Add option disable creation of shop log in the config
					VisualShop.info("Shop created :" + shop);
				} else if (!b.getWorld().getBlockAt(b.getLocation().add(new Vector(0, 1, 0))).getType().equals(Material.AIR))
					p.sendMessage(ChatColor.RED + "Le bloc d'au dessus doit être de l'air"); //TEMPLATE
				else 
					p.sendMessage(ChatColor.RED + "Il y a déjà un commerce ici !"); //TEMPLATE
				e.setCancelled(true);
			} else if (vp.shouldDeleteShop()) {
				
				if (Shop.hasShopAt(b.getLocation())) {
					Shop.deleteShop(b.getLocation());
					p.sendMessage(ChatColor.GREEN + "Shop deleted !"); //TEMPLATE
					vp.setWouldDeleteShop(false);
					
				} else
					p.sendMessage(ChatColor.RED + "Il n'y a pas de commerce ici"); //TEMPLATE
				
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
					if (resp.transactionSuccess()) {
						p.sendMessage(ChatColor.GREEN + "Achat de 1 " + ChatColor.AQUA + shop.getItem().getType() + ChatColor.GREEN + " réussi"); //TEMPLATE
						p.updateInventory();
					}
					else {
						p.getInventory().remove(is);
						p.sendMessage(ChatColor.RED + "Erreur lors de l'achat : " + resp.errorMessage); //TEMPLATE
					}
				} else
					p.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent (" + shop.getPricePerUnit() * 1 + ")."); //TEMPLATE
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
