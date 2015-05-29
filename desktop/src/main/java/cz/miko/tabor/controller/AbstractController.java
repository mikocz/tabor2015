package cz.miko.tabor.controller;

import cz.miko.tabor.TaborApp;
import cz.miko.tabor.config.MainAppHolder;
import cz.miko.tabor.config.SpringFXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractController implements Controller {

	@Setter
	private Node view;

	@Autowired
	private MainAppHolder mainAppHolder;

	@Override
	public Node getView() {
		if (view == null) {
			SpringFXMLLoader.load(getFxmlConfig());
			afterViewInit();
		}
		return view;
	}

	protected void afterViewInit() {
		// to overwrite
	}

	protected TaborApp getTaborApp() {
		if (mainAppHolder == null) {
			mainAppHolder = TaborApp.APPLICATION_CONTEXT.getBean(MainAppHolder.class);
		}
		return mainAppHolder.getTaborApp();
	}

	protected Stage getPrimaryStage() {
		return getTaborApp().getPrimaryStage();
	}

	protected void showErrorDialog(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(getPrimaryStage());
		alert.setTitle("Upozornění!");
		alert.setHeaderText("Prosím doplňte správná data.");
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}



	protected abstract String getFxmlConfig();
}
