package fr.mathdu07.visualshop.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.VsPlayer;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;

public class VsCreateCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
			return;
		}
		
		Player player = (Player) sender;
		try {
			double price = Double.parseDouble(args[0]);
			ItemStack is;
			
			if (args.length >= 2) {
				
				if (args[1].matches("^[0-9]+(:[0-9]+)?$")) {
					
					 if (args[1].contains(":")) {
						 String[] datas = args[1].split(":");
						 int id = Integer.parseInt(datas[0]), metadata = Integer.parseInt(datas[1]);
						 is = new ItemStack(id, 1, (short) metadata);
					 } else
						 is = new ItemStack(Integer.parseInt(args[1]), 1);
				} else {
					Material material = Material.getMaterial(args[1].toUpperCase());
					
					if (material != null)
						is = new ItemStack(material);
					else {
						sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_UNKNOWN_ITEM.value).replace("{ITEM}", args[1]));
						return;
					}
				}
				
			} else {
				is = player.getItemInHand().clone();
				is.setAmount(1);
			}
			
			if (is.getType() == null || is.getType() == Material.AIR) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_ITEM_AIR.value));
				return;
			}
			
			
			try {
				VsPlayer.getPlayer(player).assignAdminSellShopCreation(is, price);
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_CREATE_SHOP.value));
			} catch (VsNegativeOrNullValueException e) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NUMBER_NEGATIVE.value));
			}
			
			
		} catch (NumberFormatException e) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_INTEGER.value).replace("{VALUE}", args[0]));
		}
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getUsage() {
		return super.getUsage() + " <price> {item}";
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_CREATE.value);
	}

	@Override
	public String getPermission() {
		return VsPermissions.COMMON_CREATE;
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

}
