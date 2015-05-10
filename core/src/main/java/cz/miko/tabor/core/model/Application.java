package cz.miko.tabor.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Application extends Entity {

	private String preCode;
	private String code;

	private Integer campId;
	private Integer userId;
	private BigDecimal price;

	private String note;
}
