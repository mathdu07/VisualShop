package fr.mathdu07.visualshop.exception;

import org.bukkit.inventory.Inventory;

public abstract class VsInventoryException extends VisualShopException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1628881274588300145L;
	
	public final Inventory inventory;

	public VsInventoryException(String message, Inventory inv) {
		super(message);
		this.inventory = inv;
	}

}
