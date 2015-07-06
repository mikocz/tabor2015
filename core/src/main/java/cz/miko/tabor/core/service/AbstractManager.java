package cz.miko.tabor.core.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractManager implements ApplicationEventPublisherAware {

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private ApplicationEventPublisher applicationEventPublisher;

}
