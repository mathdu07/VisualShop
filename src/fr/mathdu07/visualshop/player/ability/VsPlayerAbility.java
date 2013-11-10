package fr.mathdu07.visualshop.player.ability;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.player.VsPlayer;

public abstract class VsPlayerAbility {
	
	protected final VsPlayer player;
	
	public VsPlayerAbility(final VsPlayer player) {
		this.player = player;
	}
	
	/**
	 * Called when a player interacts with a block or an item
	 * @param e - the bukkit event
	 * @return if next abilities should be called
	 */
	public abstract boolean onPlayerInteract(PlayerInteractEvent e);
	
	/**
	 * Called when a player picks up an item entity
	 * @param e - the bukkit event
	 * @return if next abilities should be called
	 */
	public abstract boolean onPlayerPickUp(PlayerPickupItemEvent e);
	
	protected void remove() {
		player.removeAbility(this);
	}

}
