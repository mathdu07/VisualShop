package fr.mathdu07.visualshop.config;

import java.util.Set;
import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;

public class ShopSaver extends YamlConfig {

	public ShopSaver(VisualShop plugin) {
		super(plugin, "shops.yml");
	}

	@Override
	protected void loadProperties() {
		ConfigurationSection sec = config.getConfigurationSection("shop");
		
		if (sec != null) {
			Set<String> keys = sec.getKeys(false);
					
			for (String key : keys) {
				VisualShop.debug("Config key : " + key);
				sec.get(key);
			}
		}
		

	}

	@Override
	protected void saveProperties() {		
		Iterator<Shop> it = Shop.getShops();
		
		for (int i = 0; it.hasNext(); i++)
			config.set("shop." + i, it.next());
		
	}

}
