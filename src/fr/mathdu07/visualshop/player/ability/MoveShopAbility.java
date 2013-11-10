package fr.mathdu07.visualshop.player.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.Shop;

public class MoveShopAbility extends VsPlayerAbility {

	private Shop selected;
	
	public MoveShopAbility(VsPlayer player) {
		super(player);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			final Block b = e.getClickedBlock();
			final Player p = player.getBukkitPlayer();
			
			e.setCancelled(true);
			if (selected == null) {
				final Shop s = Shop.getShop(b);
				
				if (s == null) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_MISSING.value));
					remove();
					return true;
				} else if (!s.ownsShop(player)) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_NOT_OWNED.value));
					remove();
					return false;
				}
				
				selected = s;				
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_MOVE_2.value));
				return false;
			} else {
				
				if (Shop.shopExistsAt(b)) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_PRESENT.value));
					remove();
					return false;
				} else if (!b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NO_AIR_ABOVE.value));
					remove();
					return true;
				}
				
				selected.move(b);
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_MOVE.value));
				remove();
				return false;
			}
		}
		
		return true;

	}

	@Override
	public boolean onPlayerPickUp(PlayerPickupItemEvent e) {
		return true;		
	}

}
