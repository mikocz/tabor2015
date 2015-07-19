package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.Gang;
import cz.miko.tabor.core.model.User;
import cz.miko.tabor.core.service.ApplicationManager;
import cz.miko.tabor.core.service.UserManager;
import cz.miko.tabor.extension.SelectKeyComboBoxListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationEditorController extends AbstractController {

	@Autowired
	@Getter(AccessLevel.PROTECTED)
	private ApplicationManager applicationManager;

	@Autowired
	@Getter(AccessLevel.PROTECTED)
	private UserManager userManager;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Stage dialogStage;

	/**
	 * Entity to store
	 */
	@Getter(AccessLevel.PROTECTED)
	private Application application;

	@FXML
	private GridPane mainGridPanel;

	@FXML
	private TextField codeField;
	@FXML
	private TextField preCodeField;
	@FXML
	private TextField priceField;
	@FXML
	private SelectKeyComboBoxListener userComboBox;
	private ComboBox<User> userCombo;
	@FXML
	private ComboBox<Gang> gangComboBox;
	@FXML
	private TextArea noteField;


	@FXML
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Button storeButton;

	@FXML
	private void initialize() {
		initGangComboBox();

		iniUserCombo();

		GridPane.setConstraints(userCombo, 1, 2, 1, 1, HPos.LEFT, VPos.TOP);
		mainGridPanel.add(userCombo, 1, 2);
	}

	private void iniUserCombo() {
		ObservableList<User> users = FXCollections.observableArrayList(userManager.getUsers("LASTNAME"));
		userCombo = new ComboBox(users);
		userCombo.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override
			public ListCell call(ListView param) {
				return new ListCell<User>() {
					@Override
					protected void updateItem(User item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
							setText(item.getLastName() + " " + item.getFirstName() + (item.getBirthday() != null ? " " + simpleDateFormat.format(item.getBirthday()) : ""));
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
		//selected value showed in combo box
		userCombo.setConverter(new StringConverter<User>() {
			@Override
			public String toString(User user) {
				if(user == null) {
					return null;
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
					return user.getLastName() + " " + user.getFirstName() + (user.getBirthday() != null ? " " + simpleDateFormat.format(user.getBirthday()) : "");
				}
			}

			@Override
			public User fromString(String userId) {
				return null;
			}
		});

		userComboBox = new SelectKeyComboBoxListener(userCombo);
	}

	private void initGangComboBox() {
		ObservableList<Gang> gangs =
				FXCollections.observableArrayList(applicationManager.getGangListByCamp(getTaborApp().getActiveCamp().getId()));
		gangComboBox.setItems(gangs);
		gangComboBox.setCellFactory(new Callback<ListView<Gang>, ListCell<Gang>>() {
			@Override
			public ListCell call(ListView param) {
				return new ListCell<Gang>() {
					@Override
					protected void updateItem(Gang gang, boolean empty) {
						super.updateItem(gang, empty);
						if(gang != null) {
							setText(gang.getName());
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
		gangComboBox.setConverter(new StringConverter<Gang>() {
			@Override
			public String toString(Gang gang) {
				if (gang!=null) {
					return gang.getName();
				} else {
					return null;
				}
			}

			@Override
			public Gang fromString(String string) {
				return null;
			}
		});
	}


	public void handleOk(ActionEvent actionEvent) {
		if (isInputValid()) {
			populateApplicationByForm();
			applicationManager.storeApplication(application);
			this.setApplication(null);
			codeField.requestFocus();
		}
	}

	private void populateApplicationByForm() {
		if (application == null) {
			application = new Application();
		}
		if (application.getCampId()==null){
			application.setCampId(getTaborApp().getActiveCamp().getId());
		}
		application.setCode(codeField.getText());
		application.setPreCode(preCodeField.getText());
		application.setNote(noteField.getText());
		application.setUserId(userCombo.getValue().getId());
		if (StringUtils.isNotEmpty(priceField.getText())) {
			application.setPrice(new BigDecimal(priceField.getText()));
		} else {
			application.setPrice(null);
		}
		if (gangComboBox.getSelectionModel().getSelectedItem()!=null) {
			application.setGangId(gangComboBox.getSelectionModel().getSelectedItem().getId());
		} else {
			application.setGangId(null);
		}
	}

	public void setApplication(Application application) {
		this.application = application;
		if (application!=null) {
			codeField.setText(application.getCode());
			preCodeField.setText(application.getPreCode());
			noteField.setText(application.getNote());
			if (application.getPrice()!=null) {
				priceField.setText(application.getPrice().toString());
			}
			userCombo.getSelectionModel().clearSelection();
			userCombo.getItems().stream().filter(user -> Objects.equals(user.getId(), application.getUserId()))
					.forEach(user -> userCombo.getSelectionModel().select(user));
			gangComboBox.getSelectionModel().clearSelection();
			gangComboBox.getItems().stream().filter(gang -> Objects.equals(gang.getId(), application.getGangId()))
					.forEach(gang -> gangComboBox.getSelectionModel().select(gang));
		} else {
			clearForm();
		}
	}

	private void clearForm() {
		codeField.setText("");
		preCodeField.setText("");
		noteField.setText("");
		priceField.setText("");
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (StringUtils.isEmpty(preCodeField.getText())) {
			errorMessage += "Zadejte předběžný kód přihlášky!\n";
		}
		if (!StringUtils.isEmpty(preCodeField.getText())) {
			Application application = applicationManager.getApplicationByCampAndPreCode(getActiveCampIdId(), preCodeField.getText());
			if (application!=null){
				errorMessage += "Předběžný kód přihlášky je již použitý!\n";
			}
		}
		if (StringUtils.isEmpty(codeField.getText())) {
			errorMessage += "Zadejte kód přihlášky!\n";
		}
		if (userCombo.getValue()==null) {
			errorMessage += "Vyberte účastníka!\n";
		}
		if(errorMessage.length() == 0) {
			return true;
		}
		else {
			// Show the error message.
			showErrorDialog(errorMessage);

			return false;
		}
	}

	public void handleOkAndClose(ActionEvent actionEvent) {
		if (isInputValid()) {
			populateApplicationByForm();
			applicationManager.storeApplication(application);
			dialogStage.close();
			this.setDialogStage(null);
			this.setApplication(null);
		}
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/applicationEditor.fxml";
	}


	public void handleCancel(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setApplication(null);
	}
}
