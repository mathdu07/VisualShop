package fr.mathdu07.visualshop.shop;

import java.util.Iterator;

public class ShopTask implements Runnable {

	public void run() {
		Iterator<Shop> shops = Shop.getShops();
		
		while (shops.hasNext())
			shops.next().update();

	}

}
