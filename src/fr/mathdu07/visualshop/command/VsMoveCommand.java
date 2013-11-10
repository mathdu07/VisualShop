package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.player.ability.MoveShopAbility;

public class VsMoveCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		final VsPlayer p = VsPlayer.getPlayer((Player) sender);
		
		p.addAbility(new MoveShopAbility(p));
		sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_MOVE_1.value));
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_MOVE.value);
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getName() {
		return "move";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"mv"};
	}

	@Override
	protected boolean needPermission() {
		return false;
	}

}
