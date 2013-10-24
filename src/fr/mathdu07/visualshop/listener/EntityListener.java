package fr.mathdu07.visualshop.listener;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Config;

public class EntityListener implements Listener {
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e == null) return;
		
		Block b = e.getBlock();
		
		if (Shop.hasShopAt(b.getLocation()) && VisualShop.getVSConfig().getBooleanProperty(Config.PROTECT_SHOPS))
			e.setCancelled(true);
		
	}
	
	@EventHandler
	public void onEntityExplode (EntityExplodeEvent e) {
		if (e == null)	return;
		
		if (VisualShop.getVSConfig().getBooleanProperty(Config.PROTECT_SHOPS)) {
			
			List<Block> blocks = e.blockList();
			for (int i = 0; i < blocks.size(); i++) {
				
				if (Shop.hasShopAt(blocks.get(i).getLocation())) {
					if (VisualShop.getVSConfig().getBooleanProperty(Config.PROTECT_SHOPS)) {
						e.setCancelled(true);
						break;
					}
					else
						Shop.deleteShop(blocks.get(i).getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityPrime (ExplosionPrimeEvent e){
		if (e == null)	return;
		
		final Entity ent = e.getEntity();
		
		if (VisualShop.getVSConfig().getBooleanProperty(Config.PROTECT_SHOPS)) {
			
			for (Entity entity : ent.getNearbyEntities(e.getRadius(), e.getRadius(), e.getRadius())) {
				
				if (entity instanceof Item) {
					Item i = (Item) entity;
					
					if (Shop.isItemOwnedToAShop(i)) {
						e.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDespawn (ItemDespawnEvent e) {
		if (e == null)	return;
		
		if (Shop.isItemOwnedToAShop(e.getEntity()))
			e.setCancelled(true);
	}

}
