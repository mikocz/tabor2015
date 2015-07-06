package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.GangMapper;
import cz.miko.tabor.core.model.Gang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class GangManager {

	@Autowired
	private GangMapper gangMapper;

	public void storeGang(Gang gang) {
		if(gang.getId() == null) {
			gangMapper.insertGang(gang);
		}
		else {
			gangMapper.updateGang(gang);
		}
	}

	public List<Gang> getGangListCamp(int campId) {
		return gangMapper.getGangListByCamp(campId);
	}
}
