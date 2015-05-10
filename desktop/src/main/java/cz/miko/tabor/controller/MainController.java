package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainController extends AbstractController {

	@Autowired
	private PersonOverviewController personController;

	@Autowired
	private PersonalEditorController personalEditorController;

	@Autowired
	private ApplicationOverviewController applicationOverviewController;

	@FXML
	public void showPersonOverview(ActionEvent actionEvent) {
		getTaborApp().getRootLayout().setCenter(personController.getView());
	}

	@FXML
	public void showApplicationOverview(ActionEvent actionEvent) {
		getTaborApp().getRootLayout().setCenter(applicationOverviewController.getView());
	}

	@FXML
	public void showNewPersonEditorDialog(ActionEvent actionEvent) {
		showPersonEditorDialog(null);
	}

	public boolean showPersonEditorDialog(User person) {
		// Load the fxml file and create a new stage for the popup dialog.
		Node personalEditorControllerView = personalEditorController.getView();
		if (personalEditorControllerView.getScene()!=null) {
			personalEditorControllerView.getScene().setRoot(new Region());
		}
		personalEditorController.setUser(person);

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		if (person!=null && person.getId()!=null) {
			dialogStage.setTitle("Editace " + person.getDisplayName());
		} else {
			dialogStage.setTitle("Nová osoba");
		}
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(getTaborApp().getPrimaryStage());
		Scene scene = new Scene((Parent)personalEditorControllerView);
		dialogStage.setScene(scene);

		personalEditorController.setDialogStage(dialogStage);

		// Set the dialog icon.
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
				() -> personalEditorController.getStoreButton().fire());
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();


		return personalEditorController.isOkClicked();
	}

	public void handleAbout(ActionEvent actionEvent) {

	}

	public PersonOverviewController getPersonController() {
		if(personController == null) {

		}
		return personController;
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/rootLayout.fxml";
	}
}
