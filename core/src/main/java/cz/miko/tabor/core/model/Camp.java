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
public class Camp extends Entity {

	private String name;
	private BigDecimal price;
	private Date from;
	private Date to;
}
