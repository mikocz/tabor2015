package cz.miko.tabor.core.model;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public enum PaymentType {

	DEPOSIT("záloha"),BALANCE_PAYMENT("doplatek");

	private final String text;

	PaymentType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
