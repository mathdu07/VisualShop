package fr.mathdu07.visualshop.ability;

import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mathdu07.visualshop.shop.Shop;

public class ShopItemWontDespawnAbility implements VsEntityAbility {

	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		
	}

	public void onEntityExplode(EntityExplodeEvent e) {
		
	}

	public void onItemDespawn(ItemDespawnEvent e) {
		if (Shop.shopOwnsItem(e.getEntity()))
			e.setCancelled(true);
	}

}
