package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.Payment;
import cz.miko.tabor.core.model.PaymentForm;
import cz.miko.tabor.core.model.PaymentType;
import cz.miko.tabor.core.service.ApplicationManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static cz.miko.tabor.core.service.TaborUtils.toDate;
import static cz.miko.tabor.core.service.TaborUtils.toLocalDate;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationPaymentController extends AbstractController {

	@Autowired
	@Getter(AccessLevel.PROTECTED)
	private ApplicationManager applicationManager;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Stage dialogStage;

	/**
	 * Entity to store
	 */
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Application application;
	/**
	 * Entity to store
	 */
	@Getter(AccessLevel.PROTECTED)
	private Payment payment;

	@FXML
	private TextField amountField;
	@FXML
	private DatePicker paymentDateField;
	@FXML
	private ComboBox<PaymentType> paymentTypeComboBox;
	@FXML
	private ComboBox<PaymentForm> paymentFormComboBox;

	@FXML
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Button storeButton;

	@FXML
	private void initialize() {
		paymentTypeComboBox.setItems(FXCollections.observableArrayList(PaymentType.values()));
		paymentFormComboBox.setItems(FXCollections.observableArrayList(PaymentForm.values()));
	}

	private void populatePaymentByForm() {
		if (payment == null) {
			payment = new Payment();
		}
		if (payment.getApplicationId()==null){
			payment.setApplicationId(application.getId());
		}
		payment.setAmount(new BigDecimal(amountField.getText()));
		payment.setPaymentDate(toDate(paymentDateField.getValue()));
		payment.setPaymentForm(paymentFormComboBox.getValue());
		payment.setPaymentType(paymentTypeComboBox.getValue());
		//payment.setType(typeField.getText());
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
		if (payment!=null){
			paymentDateField.setValue(toLocalDate(payment.getPaymentDate()));
			paymentFormComboBox.setValue(payment.getPaymentForm());
			paymentTypeComboBox.setValue(payment.getPaymentType());
			amountField.setText(payment.getAmount().toString());
			//typeField.setText(payment.getType());
		} else {
			clearForm();
		}
	}


	private void clearForm() {
		amountField.setText("");
		paymentDateField.setValue(LocalDate.now());
		//typeField.setText("");
		paymentFormComboBox.getSelectionModel().clearSelection();
		paymentTypeComboBox.getSelectionModel().clearSelection();
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (StringUtils.isEmpty(amountField.getText())) {
			errorMessage += "Zadejte částku!\n";
		} else {
			try {
				new BigDecimal(amountField.getText());
			} catch(NumberFormatException e) {
				errorMessage += "Zadejte částku ve správném tvaru!\n";
			}
		}
		if (paymentDateField.getValue() == null) {
			errorMessage += "Zadejte datum platby!\n";
		}
		if (paymentFormComboBox.getValue() == null) {
			errorMessage += "Zadejte způsob úhrady!\n";
		}
		if (paymentTypeComboBox.getValue() == null) {
			errorMessage += "Zadejte druh platby!\n";
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
			populatePaymentByForm();
			applicationManager.storePayment(payment);
			dialogStage.close();
			this.setDialogStage(null);
			this.setApplication(null);
			this.setPayment(null);
		}
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/applicationPayment.fxml";
	}


	public void handleCancel(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setApplication(null);
	}
}
