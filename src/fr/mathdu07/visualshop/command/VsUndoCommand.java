package fr.mathdu07.visualshop.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mathdu07.visualshop.VisualShop;
import fr.mathdu07.visualshop.VsPermissions;
import fr.mathdu07.visualshop.VsPlayer;
import fr.mathdu07.visualshop.VsTransaction;
import fr.mathdu07.visualshop.config.Templates;
import fr.mathdu07.visualshop.exception.VsEconomyException;
import fr.mathdu07.visualshop.exception.VsNoItemInInventoryException;
import fr.mathdu07.visualshop.exception.VsNullException;
import fr.mathdu07.visualshop.exception.VsTooLateException;

public class VsUndoCommand extends VsSubCommand {

	@Override
	public void performCommand(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		VsPlayer vp = VsPlayer.getPlayer(p);
		
		if (args.length == 0) {
			VsTransaction t = vp.getLastTransaction();
			try {
				vp.undoLastTransaction();
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_UNDO.value).replace("{AMOUNT}", Integer.toString(t.is.getAmount())).
						replace("{ITEM}", t.is.getType().toString()).replace("{PRICE}", Double.toString(t.cost)).
						replace("{$}", VisualShop.getInstance().getEconomy().currencyNameSingular()));
			} catch (VsTooLateException e) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_UNDO_TOO_LATE.value).replace("{TIME}", Integer.toString(VisualShop.getVSConfig().UNDO_MAX_TIME.value)));
			} catch (VsNoItemInInventoryException e) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_INV_NO_ITEM.value).replace("{ITEM}", t.is.getType().toString()));
			} catch (VsEconomyException e) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_BUY_ECO.value).replace("{ERROR}", e.errorMsg));
			} catch (VsNullException e) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOTHING_UNDO.value));
			}
			
		} else {
			try {
				int count = Integer.parseInt(args[0]);
				if (count <= 0) {
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NUMBER_NEGATIVE.value));
					return;
				}
				int undone = vp.undoTransactions(count);
				
				if (undone > 0)
					p.sendMessage(Templates.colorStr(VisualShop.getTemplates().CONFIRMED_UNDOS.value).replace("{NUMBER}", Integer.toString(undone)));
			} catch (NumberFormatException e) {
				p.sendMessage(Templates.colorStr(VisualShop.getTemplates().ERR_NOT_INTEGER.value).replace("{VALUE}", args[0]));
			}
		}

	}

	@Override
	protected boolean onlyPlayer() {
		return true;
	}

	@Override
	public String getDescription() {
		return Templates.colorStr(VisualShop.getTemplates().CMD_UNDO.value);
	}

	@Override
	public String getPermission() {
		return VsPermissions.COMMON_UNDO;
	}

	@Override
	public String getName() {
		return "undo";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getUsage() {
		return super.getUsage() + " [number]";
	}

}
