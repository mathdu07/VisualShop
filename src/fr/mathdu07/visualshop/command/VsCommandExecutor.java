package fr.mathdu07.visualshop.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;

public class VsCommandExecutor implements CommandExecutor {
	
	private List<VsSubCommand> subCommands = new ArrayList<VsSubCommand>();
	
	public VsCommandExecutor() {
		subCommands.add(new VsAdminSellCommand());
		subCommands.add(new VsDeleteCommand());
		subCommands.add(new VsReloadCommand());
		subCommands.add(new VsUndoCommand());
		subCommands.add(new VsToggleCommand());
	}

	public boolean onCommand(CommandSender send, Command cmd, String label, String[] args) {
		if (args.length != 0){
			
			if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
				showHelp(send, label, args);
				return true;
			}
			
			for (VsSubCommand sub : subCommands){
				if (sub.getName().equalsIgnoreCase(args[0]) || VisualShop.containsIgnoreCase(sub.getAliases(), args[0])){
					sub.onCommand(send, label, args);
					return true;
				}
			}
		}
		else {
			send.sendMessage(ChatColor.AQUA + "VisualShop" + ChatColor.WHITE + " v" + ChatColor.YELLOW + VisualShop.getInstance().getDescription().getVersion()
					+ ChatColor.WHITE + " by " + ChatColor.DARK_GREEN + "mathdu07");
			return true;
		}
		
		send.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_UNKNOWN_COMMAND.value).replace("{LABEL}", label));
		return true;
	}
	
	/**
	 * Shows the help to the command sender
	 * @param send
	 * @param label
	 * @param args
	 */
	private void showHelp(CommandSender send, String label, String[] args) {
		final int commandsPerPage = 8, pages = (int) Math.ceil(subCommands.size()/(commandsPerPage * 1.0));
		int page = 1;
		
		if (args.length >= 2) {
			try {
				page = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				send.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_INTEGER.value).replace("{VALUE}", args[1]));
			}
		}
		
		send.sendMessage(Templates.colorStr(VisualShop.getTemplates().HELP_PREFIX.value).replace("{PAGE}", Integer.toString(page)).replace("{PAGE_MAX}", Integer.toString(pages)));
		for (int i = (page - 1) * commandsPerPage ; i < subCommands.size() && i < page * commandsPerPage; i++)  {
			VsSubCommand sub = subCommands.get(i);
			send.sendMessage(sub.getUsage() + " " + ChatColor.GRAY + sub.getDescription());
		}
		send.sendMessage(Templates.colorStr(VisualShop.getTemplates().HELP_SUFFIX.value).replace("{PAGE}", Integer.toString(page)).replace("{PAGE_MAX}", Integer.toString(pages)));
	}

}
