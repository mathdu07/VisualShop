package fr.mathdu07.visualshop.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class VsSubCommand {

	public final void onCommand(CommandSender sender, String label, String[] args) {
		
		if (!onlyPlayer() && !(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "Command cannot be performed by the console"); //TEMPLATE
			return;
		}
		
		if (!sender.hasPermission(getPermission())){
			sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de faire cela");//TEMPLATE
			return;
		}
		
		if (args.length >= 2){
			if (args[1].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
				sender.sendMessage(getDescription());
				return;
			}
		}
		
		String[] args2 = new String[args.length - 1];
		for (int i = 0; i < args2.length; i++)
			args2[i] = args[i + 1];
		
		performCommand(sender, args2);
		
	}
	
	/**
	 * Execute the command
	 * @param sender - the sender (console, player ...) that has performed the command
	 * @param args - arguments of the command
	 */
	public abstract void performCommand(CommandSender sender, String[] args);
	
	/**
	 * @return is the command can be only performed by a player
	 */
	protected abstract boolean onlyPlayer();
	
	/**
	 * @return the usage of the sub command, displayed on help
	 */
	public abstract String getUsage();
	/**
	 * @return description of the command, displayed on help
	 */
	public abstract String getDescription();

	/**
	 * @return permission needed to execute the command
	 */
	public abstract Permission getPermission();
	/**
	 * @return the name of the sub command
	 */
	public abstract String getName();
	
	/**
	 * @return the aliases of the sub command
	 */
	public abstract String[] getAliases();
}
