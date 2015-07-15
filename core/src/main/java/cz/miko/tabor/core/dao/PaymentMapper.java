package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Payment;
import cz.miko.tabor.core.model.PaymentForm;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
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
	void removePaymentById(Integer id);

	BigDecimal getPaymentTotal(@NotNull @Param("campId") Integer campId, @Nullable @Param("paymentForm") PaymentForm paymentForm);
}
