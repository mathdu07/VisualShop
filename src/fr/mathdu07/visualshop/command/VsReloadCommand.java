package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.mathdu07.visualshop.VisualShop;

public class VsReloadCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		VisualShop.getInstance().reload();
		sender.sendMessage("VisualShop reloaded !"); //TEMPLATE
	}

	@Override
	protected boolean onlyPlayer() {
		return false;
	}

	@Override
	public String getDescription() {
		return "Recharge le plugin"; //TEMPLATE
	}

	@Override
	public Permission getPermission() {
		return new Permission("visualshop.admin.reload", PermissionDefault.OP);
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

}
