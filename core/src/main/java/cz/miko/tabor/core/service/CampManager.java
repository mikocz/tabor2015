package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.CampMapper;
import cz.miko.tabor.core.event.CampActiveEvent;
import cz.miko.tabor.core.model.Camp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class CampManager implements ApplicationEventPublisherAware {

	@Autowired()
	private CampMapper campMapper;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private ApplicationEventPublisher applicationEventPublisher;

	public void storeCamp(Camp camp) {
		if(camp.getId() == null) {
			campMapper.insertCamp(camp);
		}
		else {
			campMapper.updateCamp(camp);
		}
	}

	@Transactional
	public void setActiveCamp(Camp camp) {
		campMapper.setDeactiveCampForAllCamps();
		campMapper.setActiveCamp(camp.getId());
		applicationEventPublisher.publishEvent(new CampActiveEvent(this,camp));
	}

	public List<Camp> getCamps() {
		return campMapper.getCamps();
	}


	public Camp getActiveCamp() {
		return campMapper.getActiveCamp();
	}
}
