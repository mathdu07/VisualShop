package fr.mathdu07.visualshop.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import fr.mathdu07.visualshop.Shop;
import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;

public class BlockListener implements Listener {
	
	private Map<Block, Shop> signsPlaced = new HashMap<Block, Shop>();
	
	@EventHandler
	public void onBlockDestroyed(BlockBreakEvent e) {
		if (e == null)	return;
		
		Block b = e.getBlock();
		
		if (Shop.hasShopAt(b.getLocation())) {
			e.getPlayer().sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BREAK_SHOP.value));
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlaced (BlockPlaceEvent e) {
		if (e == null)	return;
		
		Block b = e.getBlock();
		
		if (Shop.hasShopAt(e.getBlockAgainst().getLocation()) && b.getState() instanceof Sign
				&& e.getPlayer().hasPermission(VsPermissions.COMMON_SIGN)) //TODO Check if the player own the shop
			signsPlaced.put(b, Shop.getShopAt(e.getBlockAgainst().getLocation()));
		
		if (Shop.hasShopAt(b.getWorld(), b.getX(), b.getY() - 1, b.getZ()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void OnSignChange (SignChangeEvent e) {
		Block b = e.getBlock();
		Shop s = signsPlaced.get(b);
		
		if (s == null)
			return;
		
		e.setLine(0, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_1.value).replace("{SHOP}", VisualShop.getTemplates().SHOP_SELL.value).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(1, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_2.value).replace("{SHOP}", VisualShop.getTemplates().SHOP_SELL.value).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(2, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_3.value).replace("{SHOP}", VisualShop.getTemplates().SHOP_SELL.value).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(3, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_4.value).replace("{SHOP}", VisualShop.getTemplates().SHOP_SELL.value).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		
		signsPlaced.remove(b);
	}

}
