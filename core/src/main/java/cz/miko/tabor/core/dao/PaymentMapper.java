package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Payment;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface PaymentMapper {

	void insertPayment(Payment payment);

	void updatePayment(Payment payment);

	List<Payment> getPaymentsByMap(Map<String,Object> params);

	void removePaymentByApplicationId(Integer applicationId);
}
