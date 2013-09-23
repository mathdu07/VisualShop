package fr.mathdu07.visualshop.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.mathdu07.visualshop.VisualShop;

public abstract class YamlConfig {
	
	protected  FileConfiguration config;
	protected File file;
	protected HashMap<String, Integer> intMap = new HashMap<String, Integer>();
	protected HashMap<String, Double> doubleMap = new HashMap<String, Double>();
	protected HashMap<String, Boolean> booleanMap = new HashMap<String, Boolean>();
	protected HashMap<String, String> strMap = new HashMap<String, String>();
	protected HashMap<String, List<String>> listMap = new HashMap<String, List<String>>();
	protected HashMap<String, Object> objMap = new HashMap<String, Object>();
	
	protected YamlConfig (VisualShop plugin){			
		plugin.getDataFolder().mkdirs();
	}
	
	protected YamlConfig (VisualShop plugin, String fileName){
		initConfig(plugin, fileName);
	}
	
	protected void initConfig(VisualShop plugin, String fileName){
		initConfig(plugin, fileName, fileName, true);
	}
	
	protected void initConfig(VisualShop plugin, String fileName, boolean clear){
		initConfig(plugin, fileName, fileName, clear);
	}
	
	
	protected void initConfig(VisualShop plugin, String fileName, String originalName, boolean clear){
		file = new File(plugin.getDataFolder(), fileName);
		FileConfiguration conf = new YamlConfiguration();
		try {
			conf.load(getClass().getResourceAsStream(originalName));
		} catch (IOException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
		config = new YamlConfiguration();
		config.setDefaults(conf);
		config.options().copyDefaults(true);
		
		plugin.getDataFolder().mkdirs();
		try {
			load(clear);
		} catch (IOException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
	}
	
	public void reload(){
		
		try {
			load();
		} catch (IOException e) {e.printStackTrace();}
		catch (InvalidConfigurationException e) {e.printStackTrace();}
	}
	
	protected void load() throws IOException, InvalidConfigurationException{
		load(true);
	}
	
	protected void load (boolean clear) throws IOException, InvalidConfigurationException{
		if (clear)
			clear();
		
		if (file.exists())
			config.load(file);
		
		loadProperties();
		
		config.save(file);
	}
	
	protected void clear(){
		intMap.clear();
		doubleMap.clear();
		booleanMap.clear();
		strMap.clear();
		listMap.clear();
		objMap.clear();
	}
	
	protected abstract void loadProperties();
	
	public int getIntProperty (String key){
		return intMap.get(key);
	}
	
	public double getDoubleProperty (String key){
		return doubleMap.get(key);
	}
	
	public boolean getBooleanProperty (String key){
		return booleanMap.get(key);
	}
	
	public String getStrProperty (String key){
		return strMap.get(key);
	}
	
	public List<String> getListProperty (String key){
		return listMap.get(key);
	}
	
	public Object getObjProperty (String key){
		return objMap.get(key);
	}

}
