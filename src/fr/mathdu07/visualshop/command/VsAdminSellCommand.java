package fr.mathdu07.visualshop.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.util.VsItemStack;

public class VsAdminSellCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
			return;
		}
		
		VsPlayer p = VsPlayer.getPlayer((Player) sender);
		
		try {
			double price = Double.parseDouble(args[0]);
			ItemStack is;
			
			if (args.length >= 2) {
				is = VsItemStack.parseItemStack(args[1]);
				
				if (is == null) {
					sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_UNKNOWN_ITEM.value).replace("{ITEM}", args[1]));
					return;
				}
				
			} else {
				is = p.getBukkitPlayer().getItemInHand().clone();
				is.setAmount(1);
			}
			
			if (is.getType() == null || is.getType() == Material.AIR) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_ITEM_AIR.value));
				return;
			}
			
			try {
				p.assignAdminSellShopCreation(is, price);
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_CREATE_SHOP.value));
			} catch (VsNegativeOrNullValueException e) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NUMBER_NEGATIVE.value));
			}
			
		} catch (NumberFormatException e) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_NUMERIC.value).replace("{VALUE}", args[0]));
		}
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_ADMIN_SELL.value);
	}

	@Override
	public String getPermission() {
		return VsPermissions.ADMIN_CREATE_SELL;
	}

	@Override
	public String getName() {
		return "adminsell";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"asell", "as"};
	}


	@Override
	public String getUsage() {
		return super.getUsage() + " <price> {item}";
	}

}
