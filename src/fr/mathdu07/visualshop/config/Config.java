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
	
	/**
	 * The maximum time after a purchase/sell that the player can undo a transaction
	 * In seconds
	 */
	public final Property<Integer> UNDO_MAX_TIME;
	
	/**
	 * Whether log or not transactions
	 */
	public final Property<Boolean> LOG_TRANSACTIONS;
	
	
	/**
	 * The host of the mysql server
	 */
	public final Property<String> MYSQL_HOST;
	
	/**
	 * The port that the mysql server listens
	 */
	public final Property<Integer> MYSQL_PORT;
	
	/**
	 * The login name of the mysql server
	 */
	public final Property<String> MYSQL_LOGIN;
	
	/**
	 * The password for the login of the mysql server
	 */
	public final Property<String> MYSQL_PASSWORD;
	
	/**
	 * The database of the mysql server that the plugin use
	 */
	public final Property<String> MYSQL_DATABASE;
	
	/**
	 * The prefix of the tables used by the plugin
	 */
	public final Property<String> MYSQL_TABLE_PREFIX;

	public Config(VisualShop plugin) {
		super(plugin);
		
		properties.add(ENABLED = new Property<Boolean>("enabled", true));
		properties.add(DEBUG = new Property<Boolean>("debug", false));
		properties.add(UPDATE_DELTA = new Property<Integer>("update-time", 60));
		properties.add(PROTECT_SHOPS = new Property<Boolean>("protect-shops", true));
		properties.add(LOG_SHOP_CREATION = new Property<Boolean>("log-shop-creation", true));
		properties.add(UNDO_MAX_TIME = new Property<Integer>("undo-max-time", 120));
		properties.add(LOG_TRANSACTIONS = new Property<Boolean>("log-transactions", true));
		
		properties.add(MYSQL_HOST = new Property<String>("mysql.host", "localhost"));
		properties.add(MYSQL_PORT = new Property<Integer>("mysql.port", 3306));
		properties.add(MYSQL_LOGIN = new Property<String>("mysql.user", "login_name"));
		properties.add(MYSQL_PASSWORD = new Property<String>("mysql.password", "user_password"));
		properties.add(MYSQL_DATABASE = new Property<String>("mysql.database", "minecraft"));
		properties.add(MYSQL_TABLE_PREFIX = new Property<String>("mysql.table-prefix", "vs_"));
		
		initConfig(plugin, "config.yml");
	}

}
