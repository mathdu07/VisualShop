package fr.mathdu07.visualshop.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.AdminShop;
import fr.mathdu07.visualshop.shop.Shop;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e == null) return;
		
		Player p = e.getPlayer();
		VsPlayer vp = VsPlayer.getPlayer(p);
		
		vp.handlePlayerEvent(e);
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			
			if (vp.shouldCreateShop()) {
				
				if (!Shop.shopExistsAt(b) && b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
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
				
				if (Shop.shopExistsAt(b)) {
					
					if (AdminShop.class.isInstance(Shop.getShop(b)) && !p.hasPermission(VsPermissions.ADMIN_DELETE)) {
						p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
						vp.setWouldDeleteShop(false);
						return;
					}
					
					//TODO Check when it's a player shop
					
					Shop.getShop(b).delete();
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_SHOP_DESTRUCTION.value));
					vp.setWouldDeleteShop(false);
					
				} else
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_MISSING.value));
				
				e.setCancelled(true);
				
			}
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (e == null)	return;
		
		final VsPlayer p = VsPlayer.getPlayer(e.getPlayer());
		p.handlePlayerEvent(e);
	}

}
