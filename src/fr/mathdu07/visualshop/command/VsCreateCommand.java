package fr.mathdu07.visualshop.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.mathdu07.visualshop.ShopManager;

public class VsCreateCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Mauvais usage : " + ChatColor.WHITE + getUsage()); // TEMPLATE
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
						sender.sendMessage(ChatColor.RED + "Item inconnu : " + ChatColor.ITALIC + args[1]); //TEMPLATE
						return;
					}
				}
				
			} else {
				is = player.getItemInHand();
				is.setAmount(1);
			}
			
			if (is.getType() == null || is.getType() == Material.AIR) {
				sender.sendMessage(ChatColor.RED + "The material of the shop can not be null");	//TEMPLATE
				return;
			}
			
			sender.sendMessage("Click gauche sur un bloc pour créer le commerce");
			ShopManager.assignPlayerShop(player, is, price);
			
			
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + args[0] + " n'est pas un nombre"); //TEMPLATE
		}
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getUsage() {
		return "/visualshop create <price> {item/this}";
	}

	@Override
	public String getDescription() {
		return "Creer un commerce visuel"; // TEMPLATE
	}

	@Override
	public Permission getPermission() {
		return new Permission("visualshop.create", PermissionDefault.OP);
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
