package cz.miko.tabor.config;

import cz.miko.tabor.TaborApp;
import cz.miko.tabor.core.event.CampActiveEvent;
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
public class MainAppHolder implements ApplicationListener<CampActiveEvent> {

	private TaborApp taborApp;

	@Override
	public void onApplicationEvent(CampActiveEvent event) {
		Camp camp = event.getCamp();
		this.taborApp.setActiveCamp(camp);
		this.taborApp.setApplicationTitle(camp);
	}
}
