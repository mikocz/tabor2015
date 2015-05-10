package cz.miko.tabor.controller;

import cz.miko.tabor.TaborApp;
import cz.miko.tabor.config.MainAppHolder;
import cz.miko.tabor.config.SpringFXMLLoader;
import javafx.scene.Node;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
public abstract class AbstractController implements Controller {

	private Node view;

	@Autowired
	private MainAppHolder mainAppHolder;

	@Override
	public Node getView() {
		if (view == null) {
			SpringFXMLLoader.load(getFxmlConfig());
		}
		return view;
	}

	protected TaborApp getTaborApp() {
		if (mainAppHolder == null) {
			mainAppHolder = TaborApp.APPLICATION_CONTEXT.getBean(MainAppHolder.class);
		}
		return mainAppHolder.getTaborApp();
	}

	protected abstract String getFxmlConfig();
}
