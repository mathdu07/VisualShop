package fr.mathdu07.visualshop;

/**
 * The class that stores all the plugin's permissions
 */
public class VsPermissions {
	
	//TODO Check also the permission related to the shop type
	/**
	 * Give the ability to use any type of shops
	 */
	public static final String COMMON_USE = "visualshop.common.use";
	
	/**
	 * Give the ability to undo previous transactions 
	 */
	public static final String COMMON_UNDO = "visualshop.common.undo";
	
	
	/**
	 * Allow to create player sell shops
	 */
	public static final String COMMON_CREATE_SELL = "visualshop.common.create.sell";
	
	/**
	 * Allow to create player buy shops
	 */
	public static final String COMMON_CREATE_BUY = "visualshop.common.create.buy";
	
	/**
	 * Auto complete the sign when placed against a shop
	 */
	public static final String COMMON_SIGN = "visualshop.common.sign";
	
	/**
	 * Allow to create admin sell shops
	 */
	public static final String ADMIN_CREATE_SELL = "visualshop.admin.create.sell";
	
	/**
	 * Allow to create admin buy shops
	 */
	public static final String ADMIN_CREATE_BUY = "visualshop.admin.create.buy";
	
	/**
	 * Give the ability to delete any type of shops
	 */
	public static final String ADMIN_DELETE = "visualshop.admin.delete";
	
	/**
	 * Allow to reload the plugin
	 */
	public static final String ADMIN_RELOAD = "visualshop.admin.reload"; 
	
	/**
	 * See the current toggled params or set its
	 */
	public static final String ADMIN_TOGGLE = "visualshop.admin.toggle"; 
	
	/**
	 * See if advanced is toggled, or set it
	 */
	public static final String ADMIN_TOGGLE_ADVANCED = "visualshop.admin.toggle.advanced";

}
