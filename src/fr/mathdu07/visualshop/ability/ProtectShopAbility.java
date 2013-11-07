package fr.mathdu07.visualshop.ability;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.shop.Shop;

public class ProtectShopAbility implements VsBlockAbility, VsEntityAbility {

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

	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		final Block b = e.getBlock();
		
		if (Shop.shopExistsAt(b) && VisualShop.getVSConfig().PROTECT_SHOPS.value)
			e.setCancelled(true);
		
	}

	public void onEntityExplode(EntityExplodeEvent e) {
		final List<Block> blocks = e.blockList();
		
		for (int i = 0; i < blocks.size(); i++) {
			
			if (Shop.shopExistsAt(blocks.get(i))) {
				if (VisualShop.getVSConfig().PROTECT_SHOPS.value) {
					e.setCancelled(true);
					break;
				}
				else
					Shop.getShop(blocks.get(i)).delete();
			}
		}
	}

	public void onItemDespawn(ItemDespawnEvent e) {
		
	}

}
