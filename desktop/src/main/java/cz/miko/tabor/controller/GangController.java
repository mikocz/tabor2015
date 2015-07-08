package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.model.Gang;
import cz.miko.tabor.core.service.GangManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class GangController extends AbstractOverviewController<Gang> {

	private Gang selectedGang;

	@Autowired
	private GangManager gangManager;

	@FXML
	private TableView<Gang> gangTable;

	@FXML
	private TextField nameTextField;


	@Override
	protected String getFxmlConfig() {
		return "/view/gangOverview.fxml";
	}

	@Override
	protected TableView<Gang> getDataTable() {
		return gangTable;
	}

	@Override
	protected boolean matchesFilter(Gang item) {
		return true;
	}

	@Override
	protected void handleEditItem() {
		// TODO MKO

	}

	@Override
	protected void showItemDetails(Gang item) {
		showGangDetails(item);
	}

	@Override
	protected List<Gang> getData() {
		return gangManager.getGangListCamp(getActiveCampIdId());
	}

	@Override
	protected Class<? extends Entity> getSupportedClass() {
		// TODO MKO
		return null;
	}

	private void showGangDetails(Gang gang) {
		System.out.println(gang);
	}


	public void storeGang(ActionEvent actionEvent) {
		if (validData()) {
			if (selectedGang == null) {
				selectedGang = new Gang();
				selectedGang.setCampId(getActiveCampIdId());
			}
			Gang storeGang = selectedGang;
			storeGang.setName(nameTextField.getText());
			gangManager.storeGang(storeGang);
			loadGangs();
		}
	}

	private void loadGangs() {

		final int selectedIdx = gangTable.getSelectionModel().getSelectedIndex();

		ObservableList<Gang> allCampsData = FXCollections.observableArrayList(gangManager.getGangListCamp(getActiveCampIdId()));

		gangTable.setItems(allCampsData);

		gangTable.getSelectionModel().select(selectedIdx);
	}

	private boolean validData() {
		String errorMessage = "";

		if (StringUtils.isEmpty(nameTextField.getText())) {
			errorMessage += "Zadejte název čety!\n";
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

	public void setNewGang(ActionEvent actionEvent) {
		selectedGang = null;
		nameTextField.setText("");
	}

	@Override
	public List<Node> getToolBarNodes() {
		ArrayList<Node> buttons = new ArrayList<>();

		buttons.add(getNewButton("Nová četa", this::setNewGang));
		buttons.add(getNewButton("Uložit četu", this::storeGang));

		return buttons;
	}
}
