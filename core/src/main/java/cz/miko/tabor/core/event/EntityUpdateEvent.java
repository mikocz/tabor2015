package cz.miko.tabor.core.event;

import cz.miko.tabor.core.model.Entity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class EntityUpdateEvent extends AbstractTaborEvent {

	@Getter
	private final CrudOperation operation;
	@Getter
	private final Class entityClass;
	@Getter
	private final Entity entity;

	/**
	 * Create a new ApplicationEvent.
	 *  @param source the component that published the event (never {@code null})
	 * @param entityClass
	 * @param operation
	 * @param entity
	 */
	public EntityUpdateEvent(Object source, @NotNull Class<? extends Entity> entityClass, @NotNull CrudOperation operation, Entity entity) {
		super(source);
		this.entityClass = entityClass;
		this.operation = operation;
		this.entity = entity;
	}
}
