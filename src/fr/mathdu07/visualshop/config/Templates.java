package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.VisualShop;

public class Templates extends YamlConfig {

	public Templates(VisualShop plugin) {
		super(plugin);
		
		
		
		initConfig(plugin, "templates.yml");
	}

}
