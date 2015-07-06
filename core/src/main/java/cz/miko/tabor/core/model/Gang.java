package cz.miko.tabor.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Gang extends Entity {
	private String name;
	private Integer campId;
}
