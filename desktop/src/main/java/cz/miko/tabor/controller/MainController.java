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
	private CampController campController;

	@Autowired
	private ApplicationOverviewController applicationOverviewController;

	@FXML
	public void showPersonOverview(ActionEvent actionEvent) {
		setViewToRootLayout(personController.getView());
	}

	@FXML
	public void showApplicationOverview(ActionEvent actionEvent) {
		setViewToRootLayout(applicationOverviewController.getView());
	}

	@FXML
	public void showNewPersonEditorDialog(ActionEvent actionEvent) {
		showPersonEditorDialog(new User());
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
			dialogStage.setTitle("Editace osoby " + person.getDisplayName());
			personalEditorController.getStoreButton().setVisible(false);
		} else {
			dialogStage.setTitle("Nová osoba");
			personalEditorController.getStoreButton().setVisible(true);
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

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				dialogStage.close();
			}
		});
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();


		return personalEditorController.isOkClicked();
	}

	public void showCampOverview(ActionEvent actionEvent) {
		setViewToRootLayout(campController.getView());
	}

	private void setViewToRootLayout(Node view) {
		getTaborApp().getRootLayout().setCenter(view);
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
