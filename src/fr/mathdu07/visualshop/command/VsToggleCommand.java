package fr.mathdu07.visualshop.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;

public class VsToggleCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		VsPlayer p = VsPlayer.getPlayer((Player) sender);
		
		if (args.length == 0) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().TOGGLE_SEE.value));
			sender.sendMessage("Advanced: " + ChatColor.BLUE + (p.toggleAdvanced() ? VisualShop.getTemplates().TOGGLE_TRUE.value : VisualShop.getTemplates().TOGGLE_FALSE.value));
		}
		else if (args.length == 1) {
			if (!args[0].equalsIgnoreCase("advanced")) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
				return;
			}
			
			if (args[0].equalsIgnoreCase("advanced"))
				sender.sendMessage("Advanced: " + ChatColor.BLUE + (p.toggleAdvanced() ? VisualShop.getTemplates().TOGGLE_TRUE.value : VisualShop.getTemplates().TOGGLE_FALSE.value));
		} else if (args.length == 2) {
			if ((!args[0].equalsIgnoreCase("advanced"))
					|| (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
				return;
			}
			
			boolean toggle = Boolean.parseBoolean(args[1]);
			
			if (args[0].equalsIgnoreCase("advanced")) {
				p.toggleAdvanced(toggle);
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().TOGGLE_CONFIRMED.value.replace("{TOGGLE}", "advanced").
						replace("{VALUE}", (p.toggleAdvanced() ? VisualShop.getTemplates().TOGGLE_TRUE.value : VisualShop.getTemplates().TOGGLE_FALSE.value))));
			}
		}
		else
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_TOGGLE.value);
	}

	@Override
	public String getPermission() {
		return VsPermissions.ADMIN_TOGGLE;
	}

	@Override
	public String getName() {
		return "toggle";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getUsage() {
		return super.getUsage() + " [advanced] [true|false]";
	}

}
