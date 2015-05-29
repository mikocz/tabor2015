package cz.miko.tabor.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractTaborEvent extends ApplicationEvent {

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the component that published the event (never {@code null})
	 */
	public AbstractTaborEvent(Object source) {
		super(source);
	}
}
