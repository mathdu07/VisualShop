package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.config.Templates;

public abstract class VsSubCommand {

	public final void onCommand(CommandSender sender, String label, String[] args) {
		
		if (onlyPlayer() && !(sender instanceof Player)){
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_CMD_ONLY_PLAYER.value));
			return;
		}
		
		if (!sender.hasPermission(getPermission()) && needPermission()){
			sender.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_PERMISSION.value));
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
	
	protected String getCommandAndAliases(VsSubCommand cmd) {
		String str = cmd.getName();
		
		if (cmd.getAliases() == null)
			return str;
		
		for (String alias : cmd.getAliases())
			str += ", " + alias;
		
		return str;
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
	 * @return if the sub command need the permissions returned by <code>getPermissions()</code> to be executed
	 * @see VsSubCommand#getPermission()
	 */
	protected boolean needPermission() {
		return true;
	}
	/**
	 * @return the usage of the sub command, displayed on help
	 */
	public String getUsage() {
		return "/visualshop <" + getCommandAndAliases(this) + ">";  
	}
	/**
	 * @return description of the command, displayed on help
	 */
	public abstract String getDescription();

	/**
	 * @return permission needed to execute the command
	 */
	public abstract String getPermission();
	/**
	 * @return the name of the sub command
	 */
	public abstract String getName();
	
	/**
	 * @return the aliases of the sub command
	 */
	public abstract String[] getAliases();
}
