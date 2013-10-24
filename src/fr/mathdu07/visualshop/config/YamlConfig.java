package fr.mathdu07.visualshop.config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mathdu07.visualshop.VisualShop;

public abstract class YamlConfig {
	
	protected  FileConfiguration config;
	protected File file;
	protected Set<Property<? extends Object>> properties;
	
	protected YamlConfig (VisualShop plugin){			
		plugin.getDataFolder().mkdirs();
		properties = new HashSet<Property<? extends Object>>();
	}	
	
	protected void initConfig(VisualShop plugin, String fileName){
		file = new File(plugin.getDataFolder(), fileName);
		config = new YamlConfiguration();
		
		plugin.getDataFolder().mkdirs();
		reload();
	}
	
	public void reload(){
		
		try {
			if (file.exists() && file.isFile())
				load();
			save();
		} catch (IOException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("unchecked")
	protected void load() throws IOException, InvalidConfigurationException{
		config.load(file);
		
		Iterator<Property<? extends Object>> it = properties.iterator();
		while (it.hasNext()) {
			Property<Object> prop = (Property<Object>) it.next();
			prop.value = config.get(prop.key, prop.defaultValue);
		}
	}
	
	public void save() throws IOException {
		Iterator<Property<? extends Object>> it = properties.iterator();
		while (it.hasNext()) {
			Property<Object> prop = (Property<Object>) it.next();
			config.set(prop.key, prop.value);
		}
		
		config.save(file);
	}

}
