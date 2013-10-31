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
	
	//TODO Check also the permission related to the shop type
	/**
	 * Give the ability to create any type of shops
	 */
	public static final String COMMON_CREATE = "visualshop.common.create";
	
	/**
	 * Give the ability to delete any type of shops
	 */
	public static final String COMMON_DELETE = "visualshop.common.delete";
	
	/**
	 * Auto complete the sign when placed against a shop
	 */
	public static final String COMMON_SIGN = "visualshop.common.sign";
	
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
