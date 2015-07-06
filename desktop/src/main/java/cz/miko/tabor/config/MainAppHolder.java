package cz.miko.tabor.config;

import cz.miko.tabor.TaborApp;
import cz.miko.tabor.core.event.CrudOperation;
import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.Camp;
import lombok.Data;
import org.springframework.context.ApplicationListener;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
public class MainAppHolder implements ApplicationListener<EntityUpdateEvent> {

	private TaborApp taborApp;

	@Override
	public void onApplicationEvent(EntityUpdateEvent event) {
		if (event.getEntityClass() == Camp.class && CrudOperation.ACTIVE.equals(event.getOperation())) {
			Camp camp = (Camp)event.getEntity();
			this.taborApp.setActiveCamp(camp);
			this.taborApp.setApplicationTitle(camp);
		}
	}
}
