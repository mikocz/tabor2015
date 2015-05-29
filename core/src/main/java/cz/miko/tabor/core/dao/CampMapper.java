package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Camp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface CampMapper {
	void insertCamp(Camp camp);

	void updateCamp(Camp camp);

	void setActiveCamp(@Param("campId") Integer campId);

	void setDeactiveCampForAllCamps();

	List<Camp> getCamps();

	Camp getActiveCamp();
}
