package fr.mathdu07.visualshop;

import org.bukkit.plugin.java.JavaPlugin;

import fr.mathdu07.visualshop.listener.EntityListener;
import fr.mathdu07.visualshop.listener.PlayerListener;

/**
 * Main Visual Shop's class
 */
public class VisualShop extends JavaPlugin {

	private PlayerListener playerListener;
	private EntityListener entityListener;
	
	private static VisualShop instance;

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {		
		getServer().getPluginManager().registerEvents(entityListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
	}
	
	@Override
	public void onLoad() {
		instance = this;
		
		playerListener = new PlayerListener();
		entityListener = new EntityListener();
	}
	
	public static VisualShop getInstance() {
		return instance;
	}

}
