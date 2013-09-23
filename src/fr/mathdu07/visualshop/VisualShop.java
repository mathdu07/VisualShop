package fr.mathdu07.visualshop;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import fr.mathdu07.visualshop.config.Config;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.listener.EntityListener;
import fr.mathdu07.visualshop.listener.PlayerListener;

/**
 * Main Visual Shop's class
 */
public class VisualShop extends JavaPlugin {

	private PlayerListener playerListener;
	private EntityListener entityListener;
	private Config config;
	private Templates templates;
	
	private boolean debug = false;
	
	private static VisualShop instance;

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		
		if (!config.getBooleanProperty(Config.ENABLED)) {
			warn("Disabling the plugin, specified by the config");
			onDisable();
			this.setEnabled(false);
		}
		
		getServer().getPluginManager().registerEvents(entityListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
	}
	
	@Override
	public void onLoad() {
		instance = this;
		
		playerListener = new PlayerListener();
		entityListener = new EntityListener();
		
		config = new Config(this);
		templates = new Templates(this);
		
		debug = config.getBooleanProperty(Config.DEBUG);
	}
	
	public static Config getVSConfig() {
		return instance.config;
	}
	
	public static Templates getTemplates() {
		return instance.templates;
	}
	
	public static VisualShop getInstance() {
		return instance;
	}
	
	public static void info(String msg) {
		instance.getLogger().log(Level.INFO, msg);
	}
	
	public static void warn(String msg) {
		instance.getLogger().log(Level.WARNING, msg);
	}
	
	public static void severe(String msg) {
		instance.getLogger().log(Level.SEVERE, msg);
	}
	
	public static void debug(String msg) {
		if (instance.debug)
			instance.getLogger().log(Level.INFO, "[DEBUG] " + msg);
	}

}
