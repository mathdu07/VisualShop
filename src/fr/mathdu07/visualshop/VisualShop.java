package fr.mathdu07.visualshop;

import java.io.IOException;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mathdu07.visualshop.config.Config;
import fr.mathdu07.visualshop.config.ShopSaver;
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
	private Economy economy;
	private ShopSaver shopSaver;
	
	private boolean debug = false;
	
	private static VisualShop instance;

	@Override
	public void onDisable() {
		try {
			shopSaver.save();
		} catch (IOException e) {e.printStackTrace();}
		
		Shop.removeShops();
	}

	@Override
	public void onEnable() {
		
		if (!config.getBooleanProperty(Config.ENABLED)) {
			warn("Disabling the plugin, specified by the config");
			this.setEnabled(false);
			return;
		}
		
		if (!registerEconomy()) {
			severe("Could not find economy plugin, is Vault installed ?");
			severe("Disabling the plugin ...");
			this.setEnabled(false);
			return;
		}
		
		shopSaver = new ShopSaver(this);
		
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
	
	public Economy getEconomy() {
		return economy;
	}
	
	private boolean registerEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
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
	
	static {
		ConfigurationSerialization.registerClass(Shop.class);
	}

}
