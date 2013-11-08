package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsNegativeOrNullValueException;
import fr.mathdu07.visualshop.player.VsPlayer;
import fr.mathdu07.visualshop.player.ability.SetPriceAbility;

public class VsSetPriceCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BAD_USAGE.value).replace("{USAGE}", getUsage()));
			return;
		}
		
		try {
			double price = Double.parseDouble(args[0]);
			
			final VsPlayer p = VsPlayer.getPlayer((Player) sender);
			p.addAbility(new SetPriceAbility(p, price));
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().DIV_SET_PRICE.value));
		} catch (NumberFormatException e) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_NUMERIC.value).replace("{VALUE}", args[0]));
		} catch (VsNegativeOrNullValueException e) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NUMBER_NEGATIVE.value));
		}

	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_SET_PRICE.value);
	}

	@Override
	public String getPermission() {
		return "";
	}

	@Override
	public String getName() {
		return "setprice";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"price", "sp"};
	}


	@Override
	protected boolean needPermission() {
		return false;
	}

	@Override
	public String getUsage() {
		return super.getUsage() + " <price>";
	}

}
