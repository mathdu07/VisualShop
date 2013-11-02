package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.VsPlayer;
import fr.mathdu07.visualshop.VsTransaction;
import fr.mathdu07.visualshop.config.Templates;

public class VsTransactionsCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		VsPlayer p = VsPlayer.getPlayer((Player) sender);
		
		final VsTransaction[] trans = p.getTransactions();
		
		if (trans.length == 0) {
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().TRANSACTIONS_NOTHING.value));
			return;
		}
		
		final int transPerPage = 5, pages = (int) Math.ceil(trans.length/(transPerPage * 1.0));
		int page = 1;
		
		if (args.length >= 1) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_INTEGER.value).replace("{VALUE}", args[0]));
				return;
			}
		}
		
		sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().TRANSACTIONS_PREFIX.value).replace("{PAGE}", Integer.toString(page)).replace("{PAGE_MAX}", Integer.toString(pages)));
		
		for (int i = (page - 1) * transPerPage ; i < trans.length && i < page * transPerPage; i++)
			trans[i].toString(sender, true);
		
		sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().TRANSACTIONS_SUFFIX.value).replace("{PAGE}", Integer.toString(page)).replace("{PAGE_MAX}", Integer.toString(pages)));

	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_TRANSACTIONS.value);
	}

	@Override
	public String getPermission() {
		return VsPermissions.COMMON_TRANSACTIONS;
	}

	@Override
	public String getName() {
		return "transactions";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"log", "list"};
	}

	@Override
	public String getUsage() {
		return super.getUsage() + " [page]";
	}

}
