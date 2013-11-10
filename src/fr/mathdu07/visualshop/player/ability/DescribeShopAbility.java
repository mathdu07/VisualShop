package fr.mathdu07.visualshop.player.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.SellShop;
import fr.mathdu07.visualshop.shop.Shop;

public class DescribeShopAbility extends VsPlayerAbility {

	public DescribeShopAbility(VsPlayer player) {
		super(player);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			final Block b = e.getClickedBlock();
			final Player p = player.getBukkitPlayer();
			
			if (p.getItemInHand().getType().equals(Material.SIGN) && p.hasPermission(VsPermissions.COMMON_SIGN))
				return true;
			
			if (Shop.shopExistsAt(b)) {
				final Shop shop = Shop.getShop(b);
				
				List<String> infos = (player.toggleAdvanced() ? VisualShop.getTemplates().SHOP_INFO_ADVANCED.value : VisualShop.getTemplates().SHOP_INFO.value);
				player.getBukkitPlayer().sendMessage(Templates.listToArray(
						Templates.replaceStrArray(
						Templates.replaceStrArray(
						Templates.replaceStrArray(
						Templates.replaceStrArray(		
						Templates.replaceStrArray(Templates.colorStrArray(infos),
						"{PRICE}", Double.toString(shop.getPricePerUnit())),
						"{ITEM}", shop.getItem().getType().toString()),
						"{UUID}", shop.getUUID().toString()),
						"{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value), //TODO Check if it's a player shop
						"{TYPE}", (SellShop.class.isInstance(shop) ? VisualShop.getTemplates().SHOP_SELL.value : VisualShop.getTemplates().SHOP_BUY.value))));
				
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
