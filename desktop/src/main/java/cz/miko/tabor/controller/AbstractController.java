package cz.miko.tabor.controller;

import cz.miko.tabor.TaborApp;
import cz.miko.tabor.config.MainAppHolder;
import cz.miko.tabor.config.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractController<T> implements Controller {

	@Setter
	private Node view;

	@Autowired
	private MainAppHolder mainAppHolder;

	@Override
	public Node getView() {
		SpringFXMLLoader.load(getFxmlConfig());
		afterViewInit();
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

	protected ToolBar getToolBar() {
		return getTaborApp().getToolBar();
	}

	protected void setToolBar(ToolBar toolBar) {
		getTaborApp().setToolBar(toolBar);
	}

	public List<Node> getToolBarNodes() {
		return null;
	}

	@NotNull
	protected Button getNewButton(String text, EventHandler<ActionEvent> action) {
		Button newApplicationButton = new Button(text);
		newApplicationButton.setOnAction(action);
		return newApplicationButton;
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

	protected Integer getActiveCampIdId() {
		return getTaborApp().getActiveCamp().getId();
	}


	protected abstract String getFxmlConfig();
}
