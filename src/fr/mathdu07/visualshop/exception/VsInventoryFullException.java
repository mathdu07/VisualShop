package fr.mathdu07.visualshop.exception;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VsInventoryFullException extends VsInventoryException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4518174438522908316L;
	
	private final ItemStack isExceed;

	public VsInventoryFullException(Inventory inv, ItemStack is) {
		super("The inventory can't store : " + is.getAmount() + " " + is.getType(), inv);
		this.isExceed = is;
	}

	/**
	 * @return the isExceed
	 */
	public ItemStack getIsExceed() {
		return isExceed.clone();
	}
	
}
