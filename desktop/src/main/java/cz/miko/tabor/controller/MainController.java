package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.Payment;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainController extends AbstractController {

	@Autowired
	private PersonOverviewController personController;

	@Autowired
	private PersonalEditorController personalEditorController;

	@Autowired
	private ApplicationEditorController applicationEditorController;

	@Autowired
	private ApplicationPaymentController applicationPaymentController;

	@Autowired
	private CampController campController;

	@Autowired
	private GangController gangController;

	@Autowired
	private ApplicationOverviewController applicationOverviewController;

	@FXML
	public void showPersonOverview(ActionEvent actionEvent) {
		activeController(personController);
	}

	@FXML
	public void showApplicationOverview(ActionEvent actionEvent) {
		activeController(applicationOverviewController);
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

	public void showNewApplicationEditorDialog(ActionEvent actionEvent) {
		showApplicationEditorDialog(null);
	}

	public void showApplicationEditorDialog(Application application) {
		Node applicationEditorControllerView = applicationEditorController.getView();
		if (applicationEditorControllerView.getScene()!=null) {
			applicationEditorControllerView.getScene().setRoot(new Region());
		}
		applicationEditorController.setApplication(application);

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		if (application!=null && application.getId()!=null) {
			dialogStage.setTitle("Editace přihlášky " + application.getCode());
			applicationEditorController.getStoreButton().setVisible(false);
		} else {
			dialogStage.setTitle("Nová přihláška");
			applicationEditorController.getStoreButton().setVisible(true);
		}

		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(getTaborApp().getPrimaryStage());
		Scene scene = new Scene((Parent)applicationEditorControllerView);
		dialogStage.setScene(scene);

		applicationEditorController.setDialogStage(dialogStage);

		// Set the dialog icon.
		dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
				() -> applicationEditorController.getStoreButton().fire());

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				dialogStage.close();
			}
		});
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}

	public void showPaymentEditorDialog(@NotNull Application application, @NotNull Payment payment) {

		Node applicationPaymentControllerView = applicationPaymentController.getView();
		if (applicationPaymentControllerView.getScene()!=null) {
			applicationPaymentControllerView.getScene().setRoot(new Region());
		}
		applicationPaymentController.setApplication(application);
		applicationPaymentController.setPayment(payment);

		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Platba přihlášky " + application.getCode());
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(getTaborApp().getPrimaryStage());
		Scene scene = new Scene((Parent)applicationPaymentControllerView);
		dialogStage.setScene(scene);

		applicationPaymentController.setDialogStage(dialogStage);

		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
				() -> applicationEditorController.getStoreButton().fire());

		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				dialogStage.close();
			}
		});

		dialogStage.showAndWait();
	}

	public void showCampOverview(ActionEvent actionEvent) {
		activeController(campController);
	}

	public void showGangOverView(ActionEvent actionEvent) {
		activeController(gangController);
	}

	protected void activeController(AbstractController controller) {
		setViewToRootLayout(controller.getView());
		if (controller instanceof AbstractOverviewController) {
			((AbstractOverviewController)controller).populateDataTable();
		}
		//setToolBar(null);
		getToolBar().getItems().clear();
		List toolBarNodes = controller. getToolBarNodes();
		if (toolBarNodes!=null) {
			getToolBar().getItems().addAll(toolBarNodes);
		}
	}

	private void setViewToRootLayout(Node view) {
		getTaborApp().getRootLayout().setCenter(view);
	}

	public void handleAbout(ActionEvent actionEvent) {

	}

	@Override
	protected String getFxmlConfig() {
		return "/view/rootLayout.fxml";
	}
}
