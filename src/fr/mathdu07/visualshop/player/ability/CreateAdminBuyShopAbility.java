package fr.mathdu07.visualshop.player.ability;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.AdminBuyShop;
import fr.mathdu07.visualshop.shop.Shop;

public class CreateAdminBuyShopAbility extends CreateShopAbility {

	private final double price;
	private final ItemStack is;
	
	/**
	 * Make the player to create an Admin Buy Shop
	 * at the next left clicked block
	 * @param player
	 * @param price
	 * @param is
	 * @throws VsNegativeOrNullValueException if the price is negative or equals to 0
	 */
	public CreateAdminBuyShopAbility(VsPlayer player, double price, ItemStack is) throws VsNegativeOrNullValueException {
		super(player, price, is);
		this.price = price;
		this.is = is;
	}
	
	@Override
	protected Shop createShop(Block b) {
		Shop s = null;
		
		try {
			s = new AdminBuyShop(price, is, b);
		} catch (VsNegativeOrNullValueException e) {e.printStackTrace();}
		
		return s;
	}

}
