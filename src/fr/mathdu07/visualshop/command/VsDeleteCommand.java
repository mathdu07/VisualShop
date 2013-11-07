package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.player.ability.DeleteShopAbility;

public class VsDeleteCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
			return;
		}

		VsPlayer p = VsPlayer.getPlayer((Player) sender);
		p.getBukkitPlayer().sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_DELETE_SHOP.value));
		p.addAbility(new DeleteShopAbility(p));
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_DELETE.value);
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"del", "remove", "rm"};
	}

	@Override
	protected boolean needPermission() {
		return false;
	}

}
