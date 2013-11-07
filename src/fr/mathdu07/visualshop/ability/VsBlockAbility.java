package fr.mathdu07.visualshop.ability;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public interface VsBlockAbility {

	public void onBlockBroken(BlockBreakEvent e);
	
	public void onBlockPlaced (BlockPlaceEvent e);
	
	public void onSignChange (SignChangeEvent e);
}
