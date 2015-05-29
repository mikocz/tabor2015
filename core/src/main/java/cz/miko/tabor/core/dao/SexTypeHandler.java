package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Sex;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class SexTypeHandler extends EnumOrdinalTypeHandler<Sex> {

	public SexTypeHandler() {
		super(Sex.class);
	}
}
