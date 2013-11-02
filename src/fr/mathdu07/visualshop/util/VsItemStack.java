package fr.mathdu07.visualshop.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VsItemStack {
	
	/**
	 * @param s - the string to parse in ItemStack
	 * @return the itemstack parsed from the string, or null if impossible
	 */
	public static ItemStack parseItemStack(String s) {
		ItemStack is = null;
		
		if (s.matches("^[0-9]+(:[0-9]+)?$")) {
			
			 if (s.contains(":")) {
				 String[] datas = s.split(":");
				 int id = Integer.parseInt(datas[0]), metadata = Integer.parseInt(datas[1]);
				 is = new ItemStack(id, 1, (short) metadata);
			 } else
				 is = new ItemStack(Integer.parseInt(s), 1);
		} else {
			Material material = Material.getMaterial(s.toUpperCase());
			
			if (material != null)
				is = new ItemStack(material);
		}
		
		return is;
	}

}
