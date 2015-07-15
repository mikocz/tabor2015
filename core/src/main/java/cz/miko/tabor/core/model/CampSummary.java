package cz.miko.tabor.core.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
public class CampSummary {

	private Integer applicationCount;
	private Integer applicationGirlCount;
	private Integer applicationBoyCount;
	private Integer applicationPaidCount;

	private BigDecimal paymentTotal;
	private BigDecimal paymentCashTotal;
	private BigDecimal paymentTransferTotal;
}

