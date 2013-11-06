package fr.mathdu07.visualshop.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import fr.mathdu07.visualshop.VisualShop;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockDestroyed(BlockBreakEvent e) {
		if (e == null)	return;
		
		VisualShop.getInstance().handleBlockEvent(e);
	}
	
	@EventHandler
	public void onBlockPlaced (BlockPlaceEvent e) {
		if (e == null)	return;
		
		VisualShop.getInstance().handleBlockEvent(e);
	}
	
	@EventHandler
	public void onSignChange (SignChangeEvent e) {
		if (e == null);
		
		VisualShop.getInstance().handleBlockEvent(e);
	}

}
