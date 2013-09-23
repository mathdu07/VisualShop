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

	public Config(VisualShop plugin) {
		super(plugin, "config.yml");
	}

	@Override
	protected void loadProperties() {
		booleanMap.put(ENABLED, config.getBoolean("enabled"));
		booleanMap.put(DEBUG, config.getBoolean("debug"));
		
		//TODO Config
	}

}
