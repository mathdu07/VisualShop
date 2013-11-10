package fr.mathdu07.visualshop.player.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.VsTransaction;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.Shop;

public class DoShopTransactionAbility extends VsPlayerAbility {

	public DoShopTransactionAbility(VsPlayer player) {
		super(player);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Block b = e.getClickedBlock();
			final Player p = player.getBukkitPlayer();
			
			if (p.getItemInHand().getType().equals(Material.SIGN) && p.hasPermission(VsPermissions.COMMON_SIGN))
				return true;
			
			if (Shop.shopExistsAt(b)) {
				
				Shop shop = Shop.getShop(b);
				int amount = 1;
				VsTransaction transaction = new VsTransaction(shop, amount, player);
				transaction.applyTransaction();
				
				e.setCancelled(true);
			}
		}
		
		return true;

	}

	@Override
	public boolean onPlayerPickUp(PlayerPickupItemEvent e) {
		return true;
	}

}
