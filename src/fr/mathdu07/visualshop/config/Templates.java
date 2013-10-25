package fr.mathdu07.visualshop.config;

import java.util.ArrayList;
import java.util.List;

import fr.mathdu07.visualshop.VisualShop;

public class Templates extends YamlConfig {
	
	public final Property<String> ERR_UNKNOWN_COMMAND;
	public final Property<String> ERR_NOT_INTEGER;
	public final Property<String> ERR_BAD_USAGE;
	public final Property<String> ERR_UNKNOWN_ITEM;
	public final Property<String> ERR_ITEM_AIR;
	public final Property<String> ERR_PRICE_NEGATIVE;
	public final Property<String> ERR_CMD_ONLY_PLAYER;
	public final Property<String> ERR_NOT_PERMISSION;
	public final Property<String> ERR_BREAK_SHOP;
	public final Property<String> ERR_NO_AIR_ABOVE;
	public final Property<String> ERR_SHOP_PRESENT;
	public final Property<String> ERR_SHOP_MISSING;
	public final Property<String> ERR_INV_FULL;
	public final Property<String> ERR_BUY_ECO;
	public final Property<String> ERR_NOT_ENOUGH_MONEY;
	
	public final Property<String> DIV_CREATE_SHOP;
	public final Property<String> DIV_DELETE_SHOP;
	
	public final Property<String> HELP_PREFIX;
	public final Property<String> HELP_SUFFIX;
	
	public final Property<String> CMD_CREATE;
	public final Property<String> CMD_DELETE;
	public final Property<String> CMD_RELOAD;
	
	public final Property<String> CONFIRMED_RELOAD;
	public final Property<String> CONFIRMED_SHOP_CREATION;
	public final Property<String> CONFIRMED_SHOP_DESTRUCTION;
	public final Property<String> CONFIRMED_TRANSACTION;
	
	public final Property<List<String>> SHOP_INFO;

	public Templates(VisualShop plugin) {
		super(plugin);
		
		properties.add(ERR_UNKNOWN_COMMAND = new Property<String>("error.unknown-command", "&4Sous-commande inconnue, tapez /{LABEL} help"));
		properties.add(ERR_NOT_INTEGER = new Property<String>("error.not-integer", "&4{VALUE} n'est pas un entier"));
		properties.add(ERR_BAD_USAGE = new Property<String>("error.bad-usage", "&4Mauvais usage, la syntaxe est : &f{USAGE}"));
		properties.add(ERR_UNKNOWN_ITEM = new Property<String>("error.unknown-item", "&4Item inconnu : &o{ITEM}"));
		properties.add(ERR_ITEM_AIR = new Property<String>("error.item-air", "&4Vous devez sélectionner un item autre que de l'air"));
		properties.add(ERR_PRICE_NEGATIVE = new Property<String>("error.price-negative", "&4Le prix ne peut ni être égal à 0, ni négatif"));
		properties.add(ERR_CMD_ONLY_PLAYER = new Property<String>("error.cmd-only-player", "&4La commande ne peut être effectué seuleument par des joueurs"));
		properties.add(ERR_NOT_PERMISSION = new Property<String>("error.not-permission", "&4Vous n'avez pas la permission de faire cela"));
		properties.add(ERR_BREAK_SHOP = new Property<String>("error.break-shop", "&4Vous ne pouvez pas casser un commerce"));
		properties.add(ERR_NO_AIR_ABOVE = new Property<String>("error.no-air-above", "&4Le bloc d'au-dessus doit être de l'air"));
		properties.add(ERR_SHOP_PRESENT = new Property<String>("error.shop-present", "&4Il y a déjà un commerce ici !"));
		properties.add(ERR_SHOP_MISSING = new Property<String>("error.shop-missing", "&4Il n'y a pas de commerce ici"));
		properties.add(ERR_INV_FULL = new Property<String>("error.inventory-full", "&4Votre inventaire est complet, achat non pris en compte"));
		properties.add(ERR_BUY_ECO = new Property<String>("error.buy-economy", "&cErreur lors de l'achat : &4{ERROR}&c"));
		properties.add(ERR_NOT_ENOUGH_MONEY = new Property<String>("error.not-enough-money", "&4Vous n'avez pas assez d'argent (Cout : &c{PRICE}&4)"));
		
		properties.add(DIV_CREATE_SHOP = new Property<String>("divers.create-shop", "Click gauche sur un bloc pour créer le commerce"));
		properties.add(DIV_DELETE_SHOP = new Property<String>("divers.delete-shop", "Click gauche pour supprimer le commerce"));
		
		properties.add(HELP_PREFIX = new Property<String>("help.prefix", "&9---------------&f VisualShop Aide {PAGE}/{PAGE_MAX} &9------------------"));
		properties.add(HELP_SUFFIX = new Property<String>("help.suffix", "&9--------------------------------------------------"));
		
		properties.add(CMD_CREATE = new Property<String>("command.create", "Créer un commerce"));
		properties.add(CMD_DELETE = new Property<String>("command.delete", "Supprimer un commerce"));
		properties.add(CMD_RELOAD = new Property<String>("command.reload", "Recharge le plugin"));
		
		properties.add(CONFIRMED_RELOAD = new Property<String>("confirmed.reload", "&aVisualShop rechargé !"));
		properties.add(CONFIRMED_SHOP_CREATION = new Property<String>("confirmed.shop-creation", "&aCommerce crée avec succès"));
		properties.add(CONFIRMED_SHOP_DESTRUCTION = new Property<String>("confirmed.shop-destruction", "&aCommerce supprimé avec succès"));
		properties.add(CONFIRMED_TRANSACTION = new Property<String>("confirmed.transaction", "&aAchat de &7{AMOUNT}&a &9{ITEM}&a pour &6{PRICE}&a {$} réussi"));
		
		List<String> shopInfos = new ArrayList<String>();
		shopInfos.add("&7+---------------[&fShop&7]---------------+");
		shopInfos.add("Prix à l'unité : &e{PRICE}");
		shopInfos.add("Item : &e{ITEM}");
		shopInfos.add("&7+-----------------------------------+");
		properties.add(SHOP_INFO = new Property<List<String>>("shop.info", shopInfos));
		
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
