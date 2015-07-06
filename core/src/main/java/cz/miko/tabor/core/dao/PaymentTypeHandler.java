package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.PaymentType;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class PaymentTypeHandler extends EnumOrdinalTypeHandler<PaymentType> {

	public PaymentTypeHandler() {
		super(PaymentType.class);
	}
}
