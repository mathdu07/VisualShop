package fr.mathdu07.visualshop.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mathdu07.visualshop.VisualShop;

public class EntityListener implements Listener {
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e == null) return;
		
		VisualShop.getInstance().handleEntityEvent(e);		
	}
	
	@EventHandler
	public void onEntityExplode (EntityExplodeEvent e) {
		if (e == null)	return;
		
		VisualShop.getInstance().handleEntityEvent(e);
	}
	
	@EventHandler
	public void onItemDespawn (ItemDespawnEvent e) {
		if (e == null)	return;
		
		VisualShop.getInstance().handleEntityEvent(e);
	}

}
