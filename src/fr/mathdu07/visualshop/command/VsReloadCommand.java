package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
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
	public String getPermission() {
		return VsPermissions.ADMIN_RELOAD;
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
