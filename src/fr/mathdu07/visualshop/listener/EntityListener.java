package fr.mathdu07.visualshop.listener;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.shop.Shop;

public class EntityListener implements Listener {
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e == null) return;
		
		Block b = e.getBlock();
		
		if (Shop.shopExistsAt(b) && VisualShop.getVSConfig().PROTECT_SHOPS.value)
			e.setCancelled(true);
		
	}
	
	@EventHandler
	public void onEntityExplode (EntityExplodeEvent e) {
		if (e == null)	return;
		
		if (VisualShop.getVSConfig().PROTECT_SHOPS.value) {
			
			List<Block> blocks = e.blockList();
			for (int i = 0; i < blocks.size(); i++) {
				
				if (Shop.shopExistsAt(blocks.get(i))) {
					if (VisualShop.getVSConfig().PROTECT_SHOPS.value) {
						e.setCancelled(true);
						break;
					}
					else
						Shop.getShop(blocks.get(i)).delete();
				}
			}
		}
	}
	
	@EventHandler
	public void onItemDespawn (ItemDespawnEvent e) {
		if (e == null)	return;
		
		if (Shop.shopOwnsItem(e.getEntity()))
			e.setCancelled(true);
	}

}
