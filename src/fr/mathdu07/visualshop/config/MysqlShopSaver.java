package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;

public class MysqlShopSaver implements ShopSaver {
	
	public MysqlShopSaver () {
		if (!loadShops())
			VisualShop.severe("Can't load shops from MySQL");
	}

	public boolean addShop(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateShop(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteShop(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean loadShops() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reloadShops() {
		Shop.removeShops();
		if (!loadShops())
			VisualShop.severe("Can't load shops from MySQL after plugin reloading");
	}

	public boolean isShopSaved(Shop shop) {
		// TODO Auto-generated method stub
		return false;
	}

}
