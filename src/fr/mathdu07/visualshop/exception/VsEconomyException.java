package fr.mathdu07.visualshop.exception;

import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VsEconomyException extends VisualShopException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1586841095697107337L;
	
	public final ResponseType result;
	public final String errorMsg;

	public VsEconomyException(String errorMsg, ResponseType type) {
		super(errorMsg);
		this.errorMsg = errorMsg;
		this.result = type;
	}

}
