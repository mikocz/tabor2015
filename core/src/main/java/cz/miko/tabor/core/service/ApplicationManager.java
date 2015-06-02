package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.ApplicationMapper;
import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.ApplicationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationManager {

	@Autowired
	private ApplicationMapper applicationMapper;

	@Transactional
	public void storeApplication(Application application) {
		if (application.getId() == null) {
			applicationMapper.insertApplication(application);
		} else {
			applicationMapper.updateApplication(application);
		}
	}

	public List<ApplicationDetail> getApplicationDetailForActualCamp(Integer campId) {
		Map<String, Object> params =  new HashMap<>();
		params.put("campId", campId);
		return applicationMapper.getFullApplications(params);
	}
}
