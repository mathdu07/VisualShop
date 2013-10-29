package fr.mathdu07.visualshop.config;

import fr.mathdu07.visualshop.Shop;

public interface ShopSaver {
	
	/**
	 * Add the shop to the database
	 * @param shop - the shop to add to the database
	 * @return if it was successful
	 */
	public boolean addShop(Shop shop);
	
	/**
	 * @param shop - the shop to add to the database
	 * @return if the shops is present in the database
	 */
	public boolean isShopSaved(Shop shop);
	
	/**
	 * Update the shop properties
	 * @param shop
	 * @return if it was successful
	 */
	public boolean updateShop(Shop shop);
	
	/**
	 * Delete the shop from the database
	 * @param shop
	 * @return
	 */
	public boolean deleteShop(Shop shop);
	
	/**
	 * Loads all the shops from the database
	 * @return if the shops have could be loaded
	 */
	public boolean loadShops();
	
	/**
	 * Removes all in-game shops and then
	 * Loads all the shops from the database
	 * @return if the shops have could be loaded
	 */
	public void reloadShops();
	
	public void onEnable();
	
	public void onDisable();

}
