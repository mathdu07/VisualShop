package fr.mathdu07.visualshop.player.ability;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.AdminShop;
import fr.mathdu07.visualshop.shop.Shop;

public class DeleteShopAbility extends VsPlayerAbility {

	public DeleteShopAbility(VsPlayer player) {
		super(player);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			final Block b = e.getClickedBlock();
			final Player p = player.getBukkitPlayer();
			
			if (Shop.shopExistsAt(b)) {
					
				if (AdminShop.class.isInstance(Shop.getShop(b)) && !p.hasPermission(VsPermissions.ADMIN_DELETE)) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
					remove();
					return false;
				}
				
				//TODO Check when it's a player shop
				
				Shop.getShop(b).delete();
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_SHOP_DESTRUCTION.value));
			} else
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_MISSING.value));
				
			e.setCancelled(true);
			remove();
		}
		
		return true;
	}

	@Override
	public boolean onPlayerPickUp(PlayerPickupItemEvent e) {
		return true;
	}

}
