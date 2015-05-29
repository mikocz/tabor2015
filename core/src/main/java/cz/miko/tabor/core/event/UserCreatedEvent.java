package cz.miko.tabor.core.event;

import cz.miko.tabor.core.model.User;
import lombok.Getter;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class UserCreatedEvent extends AbstractTaborEvent {

	private static final long serialVersionUID = 6155437535911847244L;

	@Getter
	private final User user;

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the component that published the event (never {@code null})
	 * @param user
	 */
	public UserCreatedEvent(Object source, User user) {
		super(source);
		this.user = user;
	}
}
