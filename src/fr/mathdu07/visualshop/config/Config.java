package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.VisualShop;

public class Config extends YamlConfig {
	
	/**
	 * Should the plugin be enabled or not ?
	 * Boolean property
	 */
	public static final String ENABLED = "enabled";
	/**
	 * Is the plugin in debug ?
	 * Boolean property
	 */
	public static final String DEBUG = "debug";
	
	/**
	 * How long should you wait between two updates
	 * in seconds
	 * Int property
	 */
	public static final String UPDATE_DELTA = "update-time";
	
	/**
	 * Should the plugin protects shops from entities ?
	 * Like explosions, enderman pickup ...
	 * Boolean property
	 */
	public static final String PROTECT_SHOPS = "protect-shops";

	public Config(VisualShop plugin) {
		super(plugin, "config.yml");
	}

	@Override
	protected void loadProperties() {
		booleanMap.put(ENABLED, config.getBoolean(ENABLED));
		booleanMap.put(DEBUG, config.getBoolean(DEBUG));
		intMap.put(UPDATE_DELTA, config.getInt(UPDATE_DELTA));
		booleanMap.put(PROTECT_SHOPS, config.getBoolean(PROTECT_SHOPS));
	}

	@Override
	protected void saveProperties() {
		
	}

}
