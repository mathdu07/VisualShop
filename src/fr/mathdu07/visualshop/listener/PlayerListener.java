package fr.mathdu07.visualshop.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import fr.mathdu07.visualshop.player.VsPlayer;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e == null) return;
		
		Player p = e.getPlayer();
		VsPlayer vp = VsPlayer.getPlayer(p);
		
		vp.handlePlayerEvent(e);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (e == null)	return;
		
		final VsPlayer p = VsPlayer.getPlayer(e.getPlayer());
		p.handlePlayerEvent(e);
	}

}
