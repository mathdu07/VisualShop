package fr.mathdu07.visualshop.player.ability;

import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.Shop;

public class NoPickupShopAbility extends VsPlayerAbility {

	public NoPickupShopAbility(VsPlayer player) {
		super(player);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		return true;
	}

	@Override
	public boolean onPlayerPickUp(PlayerPickupItemEvent e) {
		final Item i = e.getItem();
		
		if (Shop.shopOwnsItem(i)) {
			e.setCancelled(true);
			i.setPickupDelay(2000);
		}
		
		return true;
	}

}
