package fr.mathdu07.visualshop.ability;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.shop.AdminShop;
import fr.mathdu07.visualshop.shop.SellShop;
import fr.mathdu07.visualshop.shop.Shop;

public class SignShopAbility implements VsBlockAbility {
	
	private static final Map<Block, Shop> signsToEdit = new HashMap<>();


	public void onBlockBroken(BlockBreakEvent e) {

	}

	public void onBlockPlaced(BlockPlaceEvent e) {
		final Block b = e.getBlock();
		final Shop s = Shop.getShop(e.getBlockAgainst());
		final Player p = e.getPlayer();
		
		if (s != null && b.getState() instanceof Sign && shouldCompleteSign(s, p)) 
			signsToEdit.put(b, Shop.getShop(e.getBlockAgainst()));

	}
	
	private boolean shouldCompleteSign(final Shop s, final Player p) {
		boolean ownShop = false;
		
		if (s instanceof AdminShop)
			ownShop = p.hasPermission(VsPermissions.ADMIN_CREATE_SELL) || p.hasPermission(VsPermissions.ADMIN_CREATE_BUY);
		//TODO Check if the player owns the shop
		//else if (s instanceof PlayerShop)
		//	ownShop = ...
			
		return p.hasPermission(VsPermissions.COMMON_SIGN) && ownShop;
	}

	public void onSignChange(SignChangeEvent e) {
		final Block b = e.getBlock();
		final Shop s = signsToEdit.get(b);
		
		if (s == null)
			return;
		
		e.setLine(0, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_1.value).replace("{SHOP}", (SellShop.class.isInstance(s) ? VisualShop.getTemplates().SHOP_SELL.value : VisualShop.getTemplates().SHOP_BUY.value)).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(1, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_2.value).replace("{SHOP}", (SellShop.class.isInstance(s) ? VisualShop.getTemplates().SHOP_SELL.value : VisualShop.getTemplates().SHOP_BUY.value)).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(2, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_3.value).replace("{SHOP}", (SellShop.class.isInstance(s) ? VisualShop.getTemplates().SHOP_SELL.value : VisualShop.getTemplates().SHOP_BUY.value)).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		e.setLine(3, Templates.colorStr(VisualShop.getTemplates().SHOP_SIGN_4.value).replace("{SHOP}", (SellShop.class.isInstance(s) ? VisualShop.getTemplates().SHOP_SELL.value : VisualShop.getTemplates().SHOP_BUY.value)).
				replace("{PRICE}", Double.toString(s.getPricePerUnit())).replace("{OWNER}", VisualShop.getTemplates().SHOP_ADMIN.value).
				replace("{ITEM}", s.getItem().getType().toString()));
		
		signsToEdit.remove(b);

	}

}
