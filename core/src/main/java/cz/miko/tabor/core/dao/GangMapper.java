package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Gang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface GangMapper {
	List<Gang> getGangListByCamp(@Param("campId") int campId);

	void insertGang(Gang gang);

	void updateGang(Gang gang);
}
