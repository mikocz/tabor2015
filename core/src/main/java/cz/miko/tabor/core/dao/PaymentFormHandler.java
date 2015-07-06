package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.PaymentForm;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class PaymentFormHandler extends EnumOrdinalTypeHandler<PaymentForm> {

	public PaymentFormHandler() {
		super(PaymentForm.class);
	}
}
