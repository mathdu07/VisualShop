package fr.mathdu07.visualshop.shop;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;

public class AdminBuyShop extends AdminShop implements BuyShop {

	public AdminBuyShop(double pricePerUnit, ItemStack itemstack, Block block) throws VsNegativeOrNullValueException {
		super(pricePerUnit, itemstack, block);
	}

	public AdminBuyShop(double pricePerUnit, Material item, Block block) throws VsNegativeOrNullValueException {
		super(pricePerUnit, item, block);
	}

	public AdminBuyShop(UUID uid, double pricePerUnit, ItemStack itemstack,	Block block) throws VsNegativeOrNullValueException {
		super(uid, pricePerUnit, itemstack, block);
	}
	
	public static AdminBuyShop deserialize(Map<String, Object> map) {
		World world = Bukkit.getWorld((String) map.get("world"));
		UUID uid = UUID.fromString((String) map.get("uid"));
		int x = (int) map.get("x"), y = (int) map.get("y"), z = (int) map.get("z");
		
		try {
			return new AdminBuyShop(uid, (double) map.get("price"), (ItemStack) map.get("item"), world.getBlockAt(x, y, z));
		} catch (VsNegativeOrNullValueException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean canUse(VsPlayer p) {
		return p.getBukkitPlayer().hasPermission(VsPermissions.COMMON_USE_ADMIN_BUY);
	}

	public int getMaxAmount() {
		return -1;
	}

}
