package fr.mathdu07.visualshop.exception;

public class VsNotEnoughMoneyException extends VisualShopException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6283390910261407512L;
	
	public final double moneyRequired, moneyAvailable;
	
	/**
	 * An exception when the balance has not enough money
	 * @param moneyRequired
	 * @param moneyAvailable - the amount of money in the balance
	 */
	public VsNotEnoughMoneyException(double moneyRequired, double moneyAvailable) {
		super(moneyRequired + "$ needed, but only " + moneyAvailable + " available");
		this.moneyRequired = moneyRequired;
		this.moneyAvailable = moneyAvailable;
	}

}
