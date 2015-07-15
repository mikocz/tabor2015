package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Camp;
import cz.miko.tabor.core.model.CampSummary;
import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.service.ApplicationManager;
import cz.miko.tabor.core.service.CampManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class CampOverviewController extends AbstractOverviewController<Camp> {

	@Autowired
	private CampManager campManager;

	@Autowired
	private ApplicationManager applicationManager;

	@FXML
	private TableView<Camp> campTable;

	@FXML
	private Label nameLabel;

	@FXML
	private Label priceLabel;

	@FXML
	private Label applicationCountLabel;

	@FXML
	private Label applicationPaidLabel;

	@FXML
	private Label applicationGirlCountLabel;

	@FXML
	private Label applicationBoyCountLabel;

	@FXML
	private Label paymentTotalLabel;

	@FXML
	private Label paymentCashTotalLabel;

	@FXML
	private Label paymentTransferTotalLabel;

	@FXML
	protected void initialize() {

		super.initialize();

		campTable.setPlaceholder(new Label("Žádné tábory nejsou definovány."));

		campTable.setRowFactory(tv -> new TableRow<Camp>() {
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
		handleEditCamp(null);
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
		selectedItem = camp;
		if (camp!=null) {
			nameLabel.setText(camp.getName());
			priceLabel.setText(camp.getPrice().toString());

			CampSummary campSummary = applicationManager.getCampSummary(camp.getId());
			applicationCountLabel.setText(String.valueOf(campSummary.getApplicationCount()));
			applicationBoyCountLabel.setText(String.valueOf(campSummary.getApplicationBoyCount()));
			applicationGirlCountLabel.setText(String.valueOf(campSummary.getApplicationGirlCount()));
			applicationPaidLabel.setText(String.valueOf(campSummary.getApplicationPaidCount()));
			if (campSummary.getPaymentTotal()!=null) {
				paymentTotalLabel.setText(campSummary.getPaymentTotal().toString());
			}
			if (campSummary.getPaymentCashTotal()!=null) {
				paymentCashTotalLabel.setText(campSummary.getPaymentCashTotal().toString());
			}
			if (campSummary.getPaymentTransferTotal()!=null) {
				paymentTransferTotalLabel.setText(campSummary.getPaymentTransferTotal().toString());
			}

		} else {
			nameLabel.setText("");
			priceLabel.setText("");
		}
	}

	@Override
	public List<Node> getToolBarNodes() {
		ArrayList<Node> buttons = new ArrayList<>();

		buttons.add(getNewButton("Nový tábor", this::setNewCamp));
		buttons.add(getNewButton("Upravit tábor", this::handleEditCamp));
		buttons.add(getNewButton("Nastav jako hlavní", this::setActiveCamp));

		return buttons;
	}

	private void handleEditCamp(ActionEvent actionEvent) {
		if (selectedItem !=null) {
			getMainController().showCampEditorDialog(selectedItem);
			showItemDetails(selectedItem);
			final int selectedIdx = getDataTable().getSelectionModel().getSelectedIndex();
			refreshDataTable();
			getDataTable().getSelectionModel().select(selectedIdx);
		}
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/campOverview.fxml";
	}

	public void setNewCamp(ActionEvent actionEvent) {
		getMainController().showCampEditorDialog(null);
	}

	public void setActiveCamp(ActionEvent actionEvent) {
		if (selectedItem == null) {
			return;
		}
		campManager.setActiveCamp(selectedItem);
		loadCamps();
	}
}
