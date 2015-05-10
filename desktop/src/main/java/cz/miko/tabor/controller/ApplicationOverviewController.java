package cz.miko.tabor.controller;

import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationOverviewController extends AbstractController {

	@Override
	protected String getFxmlConfig() {
		return "/view/applicationOverview.fxml";
	}
}
