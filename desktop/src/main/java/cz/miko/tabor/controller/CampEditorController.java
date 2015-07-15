package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Camp;
import cz.miko.tabor.core.service.CampManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class CampEditorController extends AbstractController {

	@Autowired
	@Getter(AccessLevel.PROTECTED)
	private CampManager campManager;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Stage dialogStage;

	/**
	 * Entity to store
	 */
	@Getter(AccessLevel.PROTECTED)
	private Camp camp;

	@FXML
	private TextField nameField;
	@FXML
	private TextField priceField;

	@FXML
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Button storeButton;

	private void populateCampByForm() {
		if (camp == null) {
			camp = new Camp();
		}
		camp.setName(nameField.getText());
		camp.setPrice(new BigDecimal(priceField.getText()));
	}

	public void setCamp(Camp camp) {
		this.camp = camp;
		if (camp!=null) {
			nameField.setText(camp.getName());
			priceField.setText(camp.getPrice().toString());
		} else {
			clearForm();
		}
	}

	private void clearForm() {
		nameField.setText("");
		priceField.setText("");
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (StringUtils.isEmpty(nameField.getText())) {
			errorMessage += "Zadejte název táboru!\n";
		}
		if (StringUtils.isEmpty(priceField.getText())) {
			errorMessage += "Zadejte cenu táboru!\n";
		}
		if (!StringUtils.isEmpty(priceField.getText())) {
			try {
			    new BigDecimal(priceField.getText());
			} catch(NumberFormatException e) {
				errorMessage += "Zadejte cenu ve správném formátu!\n";
			}
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
			populateCampByForm();
			campManager.storeCamp(camp);
			dialogStage.close();
			this.setDialogStage(null);
			this.setCamp(null);
		}
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/campEditor.fxml";
	}


	public void handleCancel(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setCamp(null);
	}
}
