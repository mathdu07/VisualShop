package fr.mathdu07.visualshop.player.ability;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.Shop;

public abstract class CreateShopAbility extends VsPlayerAbility {

	protected final double price;
	protected final ItemStack is;
	
	/**
	 * Gives the ability to the player to create
	 * a shop at the new block left clicked 
	 * @param player
	 * @param price
	 * @param is
	 * @throws VsNegativeOrNullValueException if the price is negative or equals to 0
	 */
	public CreateShopAbility(VsPlayer player, double price, ItemStack is) throws VsNegativeOrNullValueException {
		super(player);
		this.price = price;
		this.is = is;
		
		if (price <= 0)
			throw new VsNegativeOrNullValueException(price);
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent e) {
		final Player p = player.getBukkitPlayer();
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			final Block b = e.getClickedBlock();
				
			if (!Shop.shopExistsAt(b) && b.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.AIR)) {
				final Shop shop = createShop(b);
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_SHOP_CREATION.value));
					
				if (VisualShop.getVSConfig().LOG_SHOP_CREATION.value)
					VisualShop.info("Shop created : " + shop);
				
			} else if (!b.getWorld().getBlockAt(b.getLocation().add(new Vector(0, 1, 0))).getType().equals(Material.AIR))
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NO_AIR_ABOVE.value));
			else 
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_SHOP_PRESENT.value));
			
			e.setCancelled(true);
			remove();
		}

	}
	
	protected abstract Shop createShop(Block b);

	@Override
	public void onPlayerPickUp(PlayerPickupItemEvent e) {
		
	}

}
