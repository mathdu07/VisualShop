package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.VisualShop;

public class Config extends YamlConfig {
	
	/**
	 * Should the plugin be enabled or not ?
	 * Boolean property
	 */
	public final Property<Boolean> ENABLED;
	/**
	 * Is the plugin in debug ?
	 * Boolean property
	 */
	public final Property<Boolean> DEBUG;
	
	/**
	 * How long should you wait between two updates
	 * in seconds
	 * Int property
	 */
	public final Property<Integer> UPDATE_DELTA;
	
	/**
	 * Should the plugin protects shops from entities ?
	 * Like explosions, enderman pickup ...
	 * Boolean property
	 */
	public final Property<Boolean> PROTECT_SHOPS;
	
	/**
	 * Should the plugin log when a shop is created/deleted
	 * Boolean property 
	 */
	public final Property<Boolean> LOG_SHOP_CREATION;

	public Config(VisualShop plugin) {
		super(plugin);
		
		properties.add(ENABLED = new Property<Boolean>("enabled", true));
		properties.add(DEBUG = new Property<Boolean>("debug", false));
		properties.add(UPDATE_DELTA = new Property<Integer>("update-time", 60));
		properties.add(PROTECT_SHOPS = new Property<Boolean>("protect-shops", true));
		properties.add(LOG_SHOP_CREATION = new Property<Boolean>("log-shop-creation", true));
		
		initConfig(plugin, "config.yml");
	}

}
