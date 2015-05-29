package cz.miko.tabor.core.event;

import cz.miko.tabor.core.model.Camp;
import lombok.Getter;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class CampActiveEvent extends AbstractTaborEvent {

	@Getter
	private final Camp camp;

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the component that published the event (never {@code null})
	 * @param camp
	 */
	public CampActiveEvent(Object source, Camp camp) {
		super(source);
		this.camp = camp;
	}
}
