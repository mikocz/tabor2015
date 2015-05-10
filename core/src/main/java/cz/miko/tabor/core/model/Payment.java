package cz.miko.tabor.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends Entity {

	private BigDecimal amount;
	private Date paymentDate;
	private String type;
	private PaymentForm paymentForm;
	private PaymentType paymentType;
}
