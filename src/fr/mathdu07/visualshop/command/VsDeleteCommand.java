package fr.mathdu07.visualshop.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import fr.mathdu07.visualshop.VsPlayer;

public class VsDeleteCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			sender.sendMessage(ChatColor.RED + "Mauvais usage : " + ChatColor.GRAY + getUsage()); //TEMPLATE
			return;
		}

		VsPlayer p = VsPlayer.getPlayer((Player) sender);
		p.getBukkitPlayer().sendMessage("Click gauche pour supprimer le commerce"); //TEMPLATE
		p.setWouldDeleteShop(true);
	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Supprimer un commerce"; // TEMPLATE
	}

	@Override
	public Permission getPermission() {
		return new Permission("visualshop.delete", PermissionDefault.OP);
	}

	@Override
	public String getName() {
		return "delete";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"del", "remove", "rm"};
	}

}
