package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Sex;
import cz.miko.tabor.core.model.User;
import cz.miko.tabor.core.service.UserManager;
import cz.miko.tabor.extension.RequiredTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static cz.miko.tabor.core.service.TaborUtils.toDate;
import static cz.miko.tabor.core.service.TaborUtils.toLocalDate;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class PersonalEditorController extends AbstractController {

	@Autowired
	private UserManager userManager;

	@Getter(AccessLevel.PROTECTED)
	private User user;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Stage dialogStage;

	@FXML
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Button storeButton;

	private boolean okClicked = false;

	@FXML
	private RequiredTextField firstNameField;
	@FXML
	private RequiredTextField lastNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField cityField;
	@FXML
	private DatePicker birthdayField;
	@FXML
	private TextField phoneField;
	@FXML
	private TextField emailField;
	@FXML
	private TextArea noteField;
	@FXML
	private RadioButton maleField;
	@FXML
	private RadioButton femaleField;

	@FXML
	private void initialize() {

	}

	public void setUser(User user) {
		this.user = user;
		if (user!=null) {
			firstNameField.setText(user.getFirstName());
			lastNameField.setText(user.getLastName());
			streetField.setText(user.getAddress());
			cityField.setText(user.getCity());
			postalCodeField.setText(user.getPostCode());
			birthdayField.setValue(toLocalDate(user.getBirthday()));
			emailField.setText(user.getEmail());
			phoneField.setText(user.getPhone());
			noteField.setText(user.getNote());
			if (Sex.MALE.equals(user.getSex())) {
				maleField.setSelected(true);
				femaleField.setSelected(false);
			} if (Sex.FEMALE.equals(user.getSex())) {
				maleField.setSelected(false);
				femaleField.setSelected(true);
			}
		}
	}

	private void clearUser() {
		firstNameField.setText("");
		lastNameField.setText("");
		streetField.setText("");
		cityField.setText("");
		postalCodeField.setText("");
		birthdayField.setValue(null);
		emailField.setText(null);
		phoneField.setText(null);
		noteField.setText(null);
		maleField.setSelected(false);
		femaleField.setSelected(false);
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/personEditor.fxml";
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void handleOk(ActionEvent actionEvent) {
		if (isInputValid()) {
			populateUserByForm();
			userManager.storeUser(user);
			this.setUser(null);
			clearUser();
			firstNameField.requestFocus();
		}
	}

	private void populateUserByForm() {
		if (user == null) {
			user = new User();
		}
		user.setFirstName(firstNameField.getText());
		user.setLastName(lastNameField.getText());
		user.setBirthday(toDate(birthdayField.getValue()));
		user.setAddress(streetField.getText());
		user.setCity(cityField.getText());
		user.setPostCode(postalCodeField.getText());
		if (maleField.isSelected()) {
			user.setSex(Sex.MALE);
		} else if (femaleField.isSelected()) {
			user.setSex(Sex.MALE);
		}
		user.setNote(noteField.getText());
		user.setEmail(emailField.getText());
		user.setPhone(phoneField.getText());
	}

	public void handleOkAndClose(ActionEvent actionEvent) {
		if (isInputValid()) {
			populateUserByForm();
			userManager.storeUser(user);
			dialogStage.close();
			this.setDialogStage(null);
			this.setUser(null);
		}
	}

	private boolean isInputValid() {
		StringBuilder errorMessage = new StringBuilder();
		if (StringUtils.isEmpty(firstNameField.getText())) {
			// TODO MKO add error message
			return false;
		}
		return true;
	}

	public void handleCancel(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setUser(null);
	}
}
