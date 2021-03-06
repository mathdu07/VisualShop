package fr.mathdu07.visualshop.exception;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VsNoItemInInventoryException extends VsInventoryException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4113314709118789496L;
	
	private final ItemStack is;

	public VsNoItemInInventoryException(Inventory inv, ItemStack is) {
		super("The item is not in the inventory : " + is.getType(), inv);
		this.is = is;
	}
	
	public ItemStack getItemStack() {
		return is.clone();
	}
	
	

}
