package cz.miko.tabor.core.dao;

import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.ApplicationDetail;
import cz.miko.tabor.core.model.Sex;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public interface ApplicationMapper {

	void insertApplication(Application application);
	void updateApplication(Application application);

	List<ApplicationDetail> getFullApplications(Map<String,Object> params);

	void removeApplication(int applicationId);

	Integer getApplicationCount(@NotNull @Param("campId") Integer campId,@Nullable @Param("sex")Sex sex);
}
