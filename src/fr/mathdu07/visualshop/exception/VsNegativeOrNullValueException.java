package fr.mathdu07.visualshop.exception;

public class VsNegativeOrNullValueException extends VisualShopException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3189841220521629874L;
	
	/**
	 * The value which is negative/null
	 */
	public final double value;

	public VsNegativeOrNullValueException(double value) {
		this("The value cannot be negative or 0", value);
	}
	
	public VsNegativeOrNullValueException(String message, double value) {
		super(message);
		this.value = value;
	}

}
