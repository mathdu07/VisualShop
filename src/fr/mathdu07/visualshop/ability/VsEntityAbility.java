package fr.mathdu07.visualshop.ability;

import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

public interface VsEntityAbility {

	public void onEntityChangeBlock(EntityChangeBlockEvent e);
	
	public void onEntityExplode (EntityExplodeEvent e);
	
	public void onItemDespawn (ItemDespawnEvent e);
	
}
