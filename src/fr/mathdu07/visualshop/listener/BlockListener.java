package fr.mathdu07.visualshop.listener;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.mathdu07.visualshop.Shop;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockDestroyed(BlockBreakEvent e) {
		if (e == null)	return;
		
		Block b = e.getBlock();
		
		if (Shop.hasShopAt(b.getLocation())) {
			e.getPlayer().sendMessage(ChatColor.RED + "Vous ne pouvez pas casser un shop"); //TEMPLATE
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlaced (BlockPlaceEvent e) {
		if (e == null)	return;
		
		Block b = e.getBlock();
		
		if (Shop.hasShopAt(b.getWorld(), b.getX(), b.getY() - 1, b.getZ()))
			e.setCancelled(true);
	}

}
