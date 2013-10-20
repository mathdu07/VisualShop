package fr.mathdu07.visualshop.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.mathdu07.visualshop.VisualShop;

public class VsCommandExecutor implements CommandExecutor {
	
	private List<VsSubCommand> subCommands = new ArrayList<VsSubCommand>();
	
	public VsCommandExecutor() {
		subCommands.add(new VsCreateCommand());
		subCommands.add(new VsDeleteCommand());
	}

	public boolean onCommand(CommandSender send, Command cmd, String label, String[] args) {
		if (args.length != 0){
			
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
		
		send.sendMessage(ChatColor.RED + "Sous-commande inconnue, tapez /" + label + " help"); //TEMPLATE
		return true;
	}

}
