package fr.mathdu07.visualshop.block.ability;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.shop.Shop;

public class ProtectShopAbility implements VsBlockAbility {

	public void onBlockBroken(BlockBreakEvent e) {
		final Block b = e.getBlock();
		
		if (Shop.shopExistsAt(b)) {
			e.getPlayer().sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BREAK_SHOP.value));
			e.setCancelled(true);
		}

	}

	public void onBlockPlaced(BlockPlaceEvent e) {
		final Block b = e.getBlock();
		
		if (Shop.shopExistsAt(b.getLocation().add(0, -1, 0).getBlock()))
			e.setCancelled(true);
	}

	public void onSignChange(SignChangeEvent e) {
		
	}

}
