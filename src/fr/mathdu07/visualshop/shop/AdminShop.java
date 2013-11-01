package fr.mathdu07.visualshop.shop;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;

public abstract class AdminShop extends Shop {

	public AdminShop(double pricePerUnit, ItemStack itemstack, Block block)	throws VsNegativeOrNullValueException {
		super(pricePerUnit, itemstack, block);
	}

	public AdminShop(double pricePerUnit, Material item, Block block) throws VsNegativeOrNullValueException {
		super(pricePerUnit, item, block);
	}

	public AdminShop(UUID uid, double pricePerUnit, ItemStack itemstack, Block block) throws VsNegativeOrNullValueException {
		super(uid, pricePerUnit, itemstack, block);
	}

}
