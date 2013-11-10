package fr.mathdu07.visualshop.player.ability;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.Shop;

public class SetPriceAbility extends VsPlayerAbility {
	
	private final double price;

	/**
	 * Ability that changes the price of the next shop clicked
	 * @param player
	 * @param price
	 * @throws VsNegativeOrNullValueException
	 */
	public SetPriceAbility(VsPlayer player, double price) throws VsNegativeOrNullValueException {
		super(player);
		this.price = price;
		
		if (price <= 0)
			throw new VsNegativeOrNullValueException(price);
	}

	@Override
	public boolean onPlayerInteract(PlayerInteractEvent e) {
		
		if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			final Block b = e.getClickedBlock();
			final Shop s = Shop.getShop(b);
			final Player p = player.getBukkitPlayer();
			
			if (s != null) {
				
				if (s.ownsShop(player)) {
					s.setPricePerUnit(price);
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_PRICE_SET.value).replace("{PRICE}", Double.toString(price)));
				} else
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
				
			} else
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_MISSING.value));
			
			e.setCancelled(true);
			remove();
			
			return false;
		}
		
		return true;
	}

	@Override
	public boolean onPlayerPickUp(PlayerPickupItemEvent e) {
		return true;
	}

}
