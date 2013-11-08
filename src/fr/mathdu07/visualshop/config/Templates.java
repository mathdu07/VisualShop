package fr.mathdu07.visualshop.config;

import java.util.ArrayList;
import java.util.List;

import fr.mathdu07.visualshop.VisualShop;

public class Templates extends YamlConfig {
	
	public final Property<String> ERR_UNKNOWN_COMMAND;
	public final Property<String> ERR_NOT_INTEGER;
	public final Property<String> ERR_NOT_NUMERIC;
	public final Property<String> ERR_BAD_USAGE;
	public final Property<String> ERR_UNKNOWN_ITEM;
	public final Property<String> ERR_ITEM_AIR;
	public final Property<String> ERR_NUMBER_NEGATIVE;
	public final Property<String> ERR_CMD_ONLY_PLAYER;
	public final Property<String> ERR_NOT_PERMISSION;
	public final Property<String> ERR_BREAK_SHOP;
	public final Property<String> ERR_NO_AIR_ABOVE;
	public final Property<String> ERR_SHOP_PRESENT;
	public final Property<String> ERR_SHOP_MISSING;
	public final Property<String> ERR_INV_FULL;
	public final Property<String> ERR_BUY_ECO;
	public final Property<String> ERR_SELL_ECO;
	public final Property<String> ERR_NOT_ENOUGH_MONEY;
	public final Property<String> ERR_INV_NO_ITEM;
	public final Property<String> ERR_UNDO_TOO_LATE;
	public final Property<String> ERR_NOTHING_UNDO;
	
	public final Property<String> DIV_CREATE_SHOP;
	public final Property<String> DIV_DELETE_SHOP;
	public final Property<String> DIV_SET_PRICE;
	
	public final Property<String> HELP_PREFIX;
	public final Property<String> HELP_SUFFIX;
	
	public final Property<String> TRANSACTIONS_PREFIX;
	public final Property<String> TRANSACTIONS_SUFFIX;
	public final Property<String> TRANSACTIONS_NOTHING;
	
	public final Property<String> CMD_ADMIN_SELL;
	public final Property<String> CMD_ADMIN_BUY;
	public final Property<String> CMD_DELETE;
	public final Property<String> CMD_RELOAD;
	public final Property<String> CMD_UNDO;
	public final Property<String> CMD_TOGGLE;
	public final Property<String> CMD_TRANSACTIONS;
	public final Property<String> CMD_SET_PRICE;
	
	public final Property<String> CONFIRMED_RELOAD;
	public final Property<String> CONFIRMED_SHOP_CREATION;
	public final Property<String> CONFIRMED_SHOP_DESTRUCTION;
	public final Property<String> CONFIRMED_TRANSACTION_BUY;
	public final Property<String> CONFIRMED_TRANSACTION_SELL;
	public final Property<String> CONFIRMED_UNDO;
	public final Property<String> CONFIRMED_UNDOS;
	public final Property<String> CONFIRMED_PRICE_SET;
	
	public final Property<String> TOGGLE_SEE;
	public final Property<String> TOGGLE_CONFIRMED;
	public final Property<String> TOGGLE_TRUE;
	public final Property<String> TOGGLE_FALSE;
	
	public final Property<String> SHOP_SELL;
	public final Property<String> SHOP_BUY;
	public final Property<String> SHOP_ADMIN;
	public final Property<String> SHOP_SIGN_1;
	public final Property<String> SHOP_SIGN_2;
	public final Property<String> SHOP_SIGN_3;
	public final Property<String> SHOP_SIGN_4;
	
	public final Property<List<String>> SHOP_INFO;
	public final Property<List<String>> SHOP_INFO_ADVANCED;

	public Templates(VisualShop plugin) {
		super(plugin);
		
		properties.add(ERR_UNKNOWN_COMMAND = new Property<String>("error.unknown-command", "&4Sous-commande inconnue, tapez /{LABEL} help"));
		properties.add(ERR_NOT_INTEGER = new Property<String>("error.not-integer", "&4{VALUE} n'est pas un entier"));
		properties.add(ERR_NOT_NUMERIC = new Property<String>("error.not-numeric", "&4{VALUE} n'est pas un nombre"));
		properties.add(ERR_BAD_USAGE = new Property<String>("error.bad-usage", "&4Mauvais usage, la syntaxe est : &f{USAGE}"));
		properties.add(ERR_UNKNOWN_ITEM = new Property<String>("error.unknown-item", "&4Item inconnu : &o{ITEM}"));
		properties.add(ERR_ITEM_AIR = new Property<String>("error.item-air", "&4Vous devez sélectionner un item autre que de l'air"));
		properties.add(ERR_NUMBER_NEGATIVE = new Property<String>("error.number-negative", "&4Le nombre ne peut ni être égal à 0, ni négatif"));
		properties.add(ERR_CMD_ONLY_PLAYER = new Property<String>("error.cmd-only-player", "&4La commande ne peut être effectué seuleument par des joueurs"));
		properties.add(ERR_NOT_PERMISSION = new Property<String>("error.not-permission", "&4Vous n'avez pas la permission de faire cela"));
		properties.add(ERR_BREAK_SHOP = new Property<String>("error.break-shop", "&4Vous ne pouvez pas casser un commerce"));
		properties.add(ERR_NO_AIR_ABOVE = new Property<String>("error.no-air-above", "&4Le bloc d'au-dessus doit être de l'air"));
		properties.add(ERR_SHOP_PRESENT = new Property<String>("error.shop-present", "&4Il y a déjà un commerce ici !"));
		properties.add(ERR_SHOP_MISSING = new Property<String>("error.shop-missing", "&4Il n'y a pas de commerce ici"));
		properties.add(ERR_INV_FULL = new Property<String>("error.inventory-full", "&4Votre inventaire est complet, achat non pris en compte"));
		properties.add(ERR_BUY_ECO = new Property<String>("error.buy-economy", "&cErreur lors de l'achat : &4{ERROR}&c"));
		properties.add(ERR_SELL_ECO = new Property<String>("error.sell-economy", "&cErreur lors de la vente : &4{ERROR}&c"));
		properties.add(ERR_NOT_ENOUGH_MONEY = new Property<String>("error.not-enough-money", "&4Vous n'avez pas assez d'argent (Cout : &c{PRICE}&4)"));
		properties.add(ERR_INV_NO_ITEM = new Property<String>("error.inventory-no-item", "&4Vous n'avez pas de {ITEM} dans votre inventaire"));
		properties.add(ERR_UNDO_TOO_LATE = new Property<String>("error.undo-too-late", "&4Vous ne pouvez annuler une transaction datant de plus de {TIME} secondes"));
		properties.add(ERR_NOTHING_UNDO = new Property<String>("error.nothing-to-undo", "&4Aucune transaction à annuler"));
		
		properties.add(DIV_CREATE_SHOP = new Property<String>("divers.create-shop", "Click gauche sur un bloc pour créer le commerce"));
		properties.add(DIV_DELETE_SHOP = new Property<String>("divers.delete-shop", "Click gauche pour supprimer le commerce"));
		properties.add(DIV_SET_PRICE = new Property<String>("divers.set-price", "Click gauche sur le commerce pour modifier son prix"));
		
		properties.add(HELP_PREFIX = new Property<String>("help.prefix", "&9---------------&f VisualShop Aide {PAGE}/{PAGE_MAX} &9------------------"));
		properties.add(HELP_SUFFIX = new Property<String>("help.suffix", "&9--------------------------------------------------"));
		
		properties.add(TRANSACTIONS_PREFIX = new Property<String>("transactions.prefix", "&9----------------&f Transactions {PAGE}/{PAGE_MAX} &9------------------"));
		properties.add(TRANSACTIONS_SUFFIX = new Property<String>("transactions.suffix", "&9--------------------------------------------------"));
		properties.add(TRANSACTIONS_NOTHING = new Property<String>("transactions.nothing", "Il n'y a aucune transactions récentes"));
		
		properties.add(CMD_ADMIN_SELL = new Property<String>("command.admin-sell", "Créer un commerce admin de vente"));
		properties.add(CMD_ADMIN_BUY = new Property<String>("command.admin-buy", "Créer un commerce admin d'achat"));
		properties.add(CMD_DELETE = new Property<String>("command.delete", "Supprimer un commerce"));
		properties.add(CMD_RELOAD = new Property<String>("command.reload", "Recharge le plugin"));
		properties.add(CMD_UNDO = new Property<String>("command.undo", "Annuler une ou plusieurs transactions"));
		properties.add(CMD_TOGGLE = new Property<String>("command.toggle", "Affiche les différents paramètres, et permet de les régler"));
		properties.add(CMD_TRANSACTIONS = new Property<String>("command.transactions", "Affiche l'historique des dernières transactions"));
		properties.add(CMD_SET_PRICE = new Property<String>("command.set-price", "Change le prix d'un commerce"));
		
		properties.add(CONFIRMED_RELOAD = new Property<String>("confirmed.reload", "&aVisualShop rechargé !"));
		properties.add(CONFIRMED_SHOP_CREATION = new Property<String>("confirmed.shop-creation", "&aCommerce crée avec succès"));
		properties.add(CONFIRMED_SHOP_DESTRUCTION = new Property<String>("confirmed.shop-destruction", "&aCommerce supprimé avec succès"));
		properties.add(CONFIRMED_TRANSACTION_BUY = new Property<String>("confirmed.transaction-purchase", "&aAchat de &7{AMOUNT}&a &9{ITEM}&a pour &6{PRICE}&a {$} réussi"));
		properties.add(CONFIRMED_TRANSACTION_SELL = new Property<String>("confirmed.transaction-sale", "&aVente de &7{AMOUNT}&a &9{ITEM}&a pour &6{PRICE}&a {$} réussie"));
		properties.add(CONFIRMED_UNDO = new Property<String>("confirmed.undo", "&aVous avez annuler la transaction de &7{AMOUNT}&a &9{ITEM}&a pour &6{PRICE}&a {$}"));
		properties.add(CONFIRMED_UNDOS = new Property<String>("confirmed.undos", "&aVous avez annulé les {NUMBER} dernières transactions"));
		properties.add(CONFIRMED_PRICE_SET = new Property<String>("confirmed.price-set", "&aVous avez mis le prix à {PRICE}"));
		
		properties.add(TOGGLE_SEE = new Property<String>("toggle.see-toggles", "Liste des options :"));
		properties.add(TOGGLE_CONFIRMED = new Property<String>("toggle.confirmed", "&aL'option {TOGGLE} a été mis sur &9{VALUE}"));
		properties.add(TOGGLE_TRUE = new Property<String>("toggle.true", "vrai"));
		properties.add(TOGGLE_FALSE = new Property<String>("toggle.false", "faux"));
		
		properties.add(SHOP_SELL = new Property<String>("shop.sell", "Vente"));
		properties.add(SHOP_BUY = new Property<String>("shop.buy", "Achat"));
		properties.add(SHOP_ADMIN = new Property<String>("shop.admin-name", "Serveur"));
		properties.add(SHOP_SIGN_1 = new Property<String>("shop.sign.1", "{SHOP} de"));
		properties.add(SHOP_SIGN_2 = new Property<String>("shop.sign.2", "{ITEM}"));
		properties.add(SHOP_SIGN_3 = new Property<String>("shop.sign.3", "Prix: &3{PRICE}"));
		properties.add(SHOP_SIGN_4 = new Property<String>("shop.sign.4", "De {OWNER}"));
		
		List<String> shopInfos = new ArrayList<String>();
		shopInfos.add("&7+---------------[&fShop&7]---------------+");
		shopInfos.add("Type : &e{TYPE}");
		shopInfos.add("Prix à l'unité : &e{PRICE}");
		shopInfos.add("Item : &e{ITEM}");
		shopInfos.add("Propriétaire: &e{OWNER}");
		shopInfos.add("&7+-----------------------------------+");
		properties.add(SHOP_INFO = new Property<List<String>>("shop.info", shopInfos));
		
		List<String> shopInfosAdv = new ArrayList<String>();
		shopInfosAdv.add("&7+----------[&fShop Adv. mode&7]-----------+");
		shopInfosAdv.add("UID : &e{UUID}");
		shopInfosAdv.add("Type : &e{TYPE}");
		shopInfosAdv.add("Prix à l'unité : &e{PRICE}");
		shopInfosAdv.add("Item : &e{ITEM}");
		shopInfosAdv.add("Propriétaire: &e{OWNER}");
		shopInfosAdv.add("&7+-----------------------------------+");
		properties.add(SHOP_INFO_ADVANCED = new Property<List<String>>("shop.info-advanced", shopInfosAdv));
		
		initConfig(plugin, "templates.yml");
	}
	
	public static String colorStr(String str) {
		return str.replace("&", "§");
	}
	
	public static String[] colorStrArray (String[] array) {
		return replaceStrArray(array, "&", "§");
	}
	
	public static String[] replaceStrArray(String[] array, String target, String replacement) {
		String[] result = new String[array.length];
		
		for (int i = 0; i < array.length; i++)
			result[i] = array[i].replace(target, replacement);
		
		return result;
	}
	
	public static List<String> colorStrArray (List<String> array) {
		return replaceStrArray(array, "&", "§");
	}
	
	public static List<String> replaceStrArray(List<String> array, String target, String replacement) {
		List<String> result = new ArrayList<String>();
		
		for (int i = 0; i < array.size(); i++)
			result.add(array.get(i).replace(target, replacement));
		
		return result;
	}
	
	public static String[] listToArray(List<String> list) {
		String[] array = new String[list.size()];
		
		for (int i = 0; i < array.length; i++)
			array[i] = list.get(i);
			
		return array;
	}

}
