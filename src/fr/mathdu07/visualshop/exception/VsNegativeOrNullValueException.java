package fr.mathdu07.visualshop.exception;

public class VsNegativeOrNullValueException extends VisualShopException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3189841220521629874L;

	public VsNegativeOrNullValueException() {
		super("The value cannot be negative or 0");
	}
	
	public VsNegativeOrNullValueException(String message) {
		super(message);
	}

}
