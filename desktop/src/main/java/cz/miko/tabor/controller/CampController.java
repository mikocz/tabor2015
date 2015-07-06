package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Camp;
import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.service.CampManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class CampController extends AbstractOverviewController<Camp> {

	@Autowired
	private CampManager campManager;

	@FXML
	private TableView<Camp> campTable;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField priceTextField;

	private Camp selectedCamp;


	@FXML
	protected void initialize() {

		super.initialize();

		campTable.setRowFactory(tv -> {
			TableRow<Camp> row = new TableRow<Camp>() {
				@Override
				protected void updateItem(Camp camp, boolean empty){
					super.updateItem(camp, empty);
					if (camp!=null && camp.isActive()) {
						if (! getStyleClass().contains("activeCamp")) {
							getStyleClass().add("activeCamp");
						}
					} else {
						getStyleClass().removeAll(Collections.singleton("activeCamp"));
					}
				}
			};
			return row;
		});

	}

	@Override
	protected TableView getDataTable() {
		return campTable;
	}

	@Override
	protected boolean matchesFilter(Camp item) {
		return true;
	}

	@Override
	protected void handleEditItem() {
		// do nothing
	}

	@Override
	protected void showItemDetails(Camp item) {
		showCampDetails(item);
	}

	@Override
	protected List getData() {
		return campManager.getCamps();
	}

	@Override
	protected Class<? extends Entity> getSupportedClass() {
		return Camp.class;
	}

	private void loadCamps() {

		final int selectedIdx = campTable.getSelectionModel().getSelectedIndex();

		ObservableList<Camp> allCampsData = FXCollections.observableArrayList(campManager.getCamps());

		campTable.setItems(allCampsData);

		campTable.getSelectionModel().select(selectedIdx);
	}

	private void showCampDetails(Camp camp) {
		selectedCamp = camp;
		if (camp!=null) {
			nameTextField.setText(camp.getName());
			priceTextField.setText(camp.getPrice().toString());
		} else {
			nameTextField.setText("");
			priceTextField.setText("");
		}
	}


	@Override
	protected String getFxmlConfig() {
		return "/view/campOverview.fxml";
	}

	public void setNewCamp(ActionEvent actionEvent) {
		selectedCamp = null;
		nameTextField.setText("");
		priceTextField.setText("");
	}

	public void storeCamp(ActionEvent actionEvent) {
		if (validData()) {
			if (selectedCamp == null) {
				selectedCamp = new Camp();
			}
			Camp storeCamp = selectedCamp;
			storeCamp.setName(nameTextField.getText());
			storeCamp.setPrice(new BigDecimal(priceTextField.getText()));
			campManager.storeCamp(storeCamp);
			loadCamps();
		}
	}

	private boolean validData() {
		String errorMessage = "";

		if (StringUtils.isEmpty(nameTextField.getText())) {
			errorMessage += "Zadejte název táboru!\n";
		}

		String priceTextFieldText = priceTextField.getText();
		if (StringUtils.isEmpty(priceTextFieldText)) {
			errorMessage += "Zadejte cenu tábora!\n";
		} else {
			try {
				new BigDecimal(priceTextFieldText);
			} catch(NumberFormatException e) {
				errorMessage += "Nesprávný formát ceny tábora!\n";
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

	public void setActiveCamp(ActionEvent actionEvent) {
		if (selectedCamp == null) {
			return;
		}
		campManager.setActiveCamp(selectedCamp);
		loadCamps();
	}
}
