package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;

public class VsReloadCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		VisualShop.getInstance().reload();
		sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_RELOAD.value));
	}

	@Override
	protected boolean onlyPlayer() {
		return false;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_RELOAD.value);
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
