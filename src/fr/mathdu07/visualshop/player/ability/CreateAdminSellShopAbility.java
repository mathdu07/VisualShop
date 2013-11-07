package fr.mathdu07.visualshop.player.ability;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.shop.AdminSellShop;
import fr.mathdu07.visualshop.shop.Shop;

public class CreateAdminSellShopAbility extends CreateShopAbility {
	
	/**
	 * Make the player to create an Admin Sell Shop
	 * at the next left clicked block
	 * @param player
	 * @param price
	 * @param is
	 * @throws VsNegativeOrNullValueException if the price is negative or equals to 0
	 */
	public CreateAdminSellShopAbility(VsPlayer player, double price, ItemStack is) throws VsNegativeOrNullValueException {
		super(player, price, is);
	}

	@Override
	protected Shop createShop(Block b) {
		Shop s = null;
		
		try {
			s = new AdminSellShop(price, is, b);
		} catch (VsNegativeOrNullValueException e) {e.printStackTrace();}
		
		return s;
	}

}
