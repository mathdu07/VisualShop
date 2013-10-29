package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.Shop;

public interface ShopSaver {
	
	public boolean addShop(Shop shop);
	
	public boolean isShopSaved(Shop shop);
	
	public boolean updateShop(Shop shop);
	
	public boolean deleteShop(Shop shop);
	
	public boolean loadShops();
	
	public void reloadShops();

}
