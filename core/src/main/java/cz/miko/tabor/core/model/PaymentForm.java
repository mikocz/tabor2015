package cz.miko.tabor.core.model;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public enum PaymentForm {

	CASH("hotově"),TRANSFER("převodem");

	private final String text;

	PaymentForm(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
