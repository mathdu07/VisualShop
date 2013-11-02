package fr.mathdu07.visualshop.config.old;

import java.io.IOException;
import java.util.Set;
import java.util.Iterator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import fr.mathdu07.visualshop.shop.Shop;
import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.YamlConfig;

public class ShopSaver extends YamlConfig {

	public ShopSaver(VisualShop plugin) {
		super(plugin);
		initConfig(plugin, "shops.yml");
	}

	@Override
	protected void load() throws IOException, InvalidConfigurationException {
		super.load();
		
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
	public void save() throws IOException {
		super.save();
		
		Iterator<Shop> it = Shop.getShops();
		
		for (int i = 0; it.hasNext(); i++)
			config.set("shop." + i, it.next());
	}

}
