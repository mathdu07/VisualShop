package fr.mathdu07.visualshop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import fr.mathdu07.visualshop.ability.ProtectShopAbility;
import fr.mathdu07.visualshop.ability.ShopItemWontDespawnAbility;
import fr.mathdu07.visualshop.ability.SignShopAbility;
import fr.mathdu07.visualshop.ability.VsBlockAbility;
import fr.mathdu07.visualshop.ability.VsEntityAbility;
import fr.mathdu07.visualshop.command.VsCommandExecutor;
import fr.mathdu07.visualshop.config.Config;
import fr.mathdu07.visualshop.config.MysqlShopSaver;
import fr.mathdu07.visualshop.config.ShopSaver;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.listener.BlockListener;
import fr.mathdu07.visualshop.listener.EntityListener;
import fr.mathdu07.visualshop.listener.PlayerListener;
import fr.mathdu07.visualshop.shop.AdminBuyShop;
import fr.mathdu07.visualshop.shop.AdminSellShop;
import fr.mathdu07.visualshop.shop.ShopTask;

/**
 * Main Visual Shop's class
 */
public class VisualShop extends JavaPlugin {

	private PlayerListener playerListener;
	private EntityListener entityListener;
	private BlockListener blockListener;
	private VsCommandExecutor command;
	private Config config;
	private Templates templates;
	private Economy economy;
	private ShopSaver shopSaver;
	
	private boolean debug = false;
	private BukkitTask task;
	private Set<VsBlockAbility> blockAbilities;
	private Set<VsEntityAbility> entityAbilities;
	
	private static VisualShop instance;

	@Override
	public void onDisable() {
		if (config.LOG_TRANSACTIONS.value)
			VsTransaction.saveLog();
		
		if (shopSaver != null)
			shopSaver.onDisable();
		
		if (task != null)
			task.cancel();
	}

	@Override
	public void onEnable() {
		
		if (!config.ENABLED.value) {
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
		
		if (config.MYSQL_LOGIN.value.equals(config.MYSQL_LOGIN.defaultValue)) {
			warn("You must specify the MySQL login in order to use this plugin");
			this.setEnabled(false);
			return;
		}
		
		shopSaver = new MysqlShopSaver(config.MYSQL_HOST.value, config.MYSQL_LOGIN.value, config.MYSQL_PASSWORD.value, 
				config.MYSQL_DATABASE.value, config.MYSQL_PORT.value);
		
		getServer().getPluginManager().registerEvents(entityListener, this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		getServer().getPluginManager().registerEvents(blockListener, this);
	
		initAbilities();
		
		command = new VsCommandExecutor();
		getServer().getPluginCommand("visualshop").setExecutor(command);
		
		postEnable();
	}
	
	private void initAbilities() {
		ProtectShopAbility psa = new ProtectShopAbility();
		
		blockAbilities = new HashSet<VsBlockAbility>();
		blockAbilities.add(new SignShopAbility());
		blockAbilities.add(psa);
		
		entityAbilities = new HashSet<VsEntityAbility>();
		entityAbilities.add(new ShopItemWontDespawnAbility());
		entityAbilities.add(psa);
	}
	
	private void postEnable () {		
		shopSaver.onEnable();
		
		if (config.MYSQL_CONVERT_OLD.value) {
			convertOldShops();
			config.MYSQL_CONVERT_OLD.value = false;
			try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		task = getServer().getScheduler().runTaskTimer(this, new ShopTask(), 20l, config.UPDATE_DELTA.value * 20l);
		if (config.LOG_TRANSACTIONS.value)
			VsTransaction.startLog();
	}
	
	private void convertOldShops() {
		info("Converting old shops storage system ...");
		File old = new File(getDataFolder(), "shops.yml.old"), now = new File(getDataFolder(), "shops.yml");
		
		try {
			Files.copy(now.toPath(), old.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			BufferedReader reader = new BufferedReader(new FileReader(old));
			BufferedWriter writer = new BufferedWriter(new FileWriter(now));
			String line = null;
			
			while ((line = reader.readLine()) != null) 
				writer.write(line.replace("fr.mathdu07.visualshop.Shop", "fr.mathdu07.visualshop.shop.Shop") + System.lineSeparator());
			
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new fr.mathdu07.visualshop.config.old.ShopSaver(this);
		now.delete();
		info("Converted !");
	}
	
	@Override
	public void onLoad() {		
		instance = this;
		
		playerListener = new PlayerListener();
		entityListener = new EntityListener();
		blockListener = new BlockListener();
		
		config = new Config(this);
		templates = new Templates(this);
		debug = config.DEBUG.value;		
	}
	
	public void reload() {
		onDisable();
		postEnable();
		info("Reloading configs");
		config.reload();
		debug = config.DEBUG.value;
		templates.reload();
		shopSaver.reloadShops();
		info("Reloading done !");
	}
	
	/**
	 * Handles the block event
	 * @param e
	 */
	public void handleBlockEvent(BlockEvent e) {
		Iterator<VsBlockAbility> it = blockAbilities.iterator();
		
		while (it.hasNext()) {
			VsBlockAbility ability = it.next();
			
			if (BlockBreakEvent.class.isInstance(e))
				ability.onBlockBroken((BlockBreakEvent) e);
			else if (BlockPlaceEvent.class.isInstance(e))
				ability.onBlockPlaced((BlockPlaceEvent) e);
			else if (SignChangeEvent.class.isInstance(e))
				ability.onSignChange((SignChangeEvent) e);
		}
	}
	
	/**
	 * 
	 * @param e
	 */
	public void handleEntityEvent(EntityEvent e) {
		Iterator<VsEntityAbility> it = entityAbilities.iterator();
		
		while (it.hasNext()) {
			VsEntityAbility ability = it.next();
			
			if (e instanceof EntityChangeBlockEvent)
				ability.onEntityChangeBlock((EntityChangeBlockEvent) e);
			else if (e instanceof EntityExplodeEvent)
				ability.onEntityExplode((EntityExplodeEvent) e);
			else if (e instanceof ItemDespawnEvent)
				ability.onItemDespawn((ItemDespawnEvent) e);
		}
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
	
	/**
	 * 
	 * @param array - list of string
	 * @param key - string to find
	 * @return whether the array contains the string given with ignore case
	 */
	public static boolean containsIgnoreCase(String[] array, String key){
		if (array == null) return false;
		
		for (String str : array){
			
			if (str.equalsIgnoreCase(key))
				return true;
		}
		
		return false;
	}
	
	public static Config getVSConfig() {
		return instance.config;
	}
	
	public static Templates getTemplates() {
		return instance.templates;
	}
	
	public static ShopSaver getShopSaver() {
		return instance.shopSaver;
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
	
	/**
	 * @param world - the world where find the item entity
	 * @param uid - the unique uid of the item entity
	 * @return the item entity or null if not found
	 */
	public static Item getItemEntityByUID(World world, UUID uid){
		
		for (Item i : world.getEntitiesByClass(Item.class)) {
			if (i.getUniqueId() == uid)
				return i;
		}
		
		return null;
	}
	
	static {
		ConfigurationSerialization.registerClass(AdminSellShop.class);
		ConfigurationSerialization.registerClass(AdminBuyShop.class);
	}

}
