package cz.miko.tabor.controller;

import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.ApplicationDetail;
import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.model.Payment;
import cz.miko.tabor.core.service.ApplicationManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cz.miko.tabor.core.service.TaborUtils.dateToString;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationOverviewController extends AbstractOverviewController<ApplicationDetail> {

	@Autowired
	private ApplicationManager applicationManager;

	@FXML
	private Label codeLabel;
	@FXML
	private Label preCodeLabel;
	@FXML
	private Label priceLabel;
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Label phoneLabel;
	@FXML
	private Label noteLabel;
	@FXML
	private Label sexLabel;
	@FXML
	private Label gangLabel;

	@FXML
	private TableView<Payment> paymentTable;
	@FXML
	private TableView<ApplicationDetail> applicationTable;

	@FXML
	private TableColumn<ApplicationDetail, Date> birthdayColumn;
	@FXML
	private TableColumn<Payment, Date> paymentDataColumn;


	@FXML
	private ToolBar applicationOverViewToolbar;

	@Override
	protected void initialize() {
		super.initialize();
		if (!applicationTable.getItems().isEmpty()) {
			applicationTable.getSelectionModel().select(0);
		}
		// Custom rendering of the table cell.
		birthdayColumn.setCellFactory(column -> new TableCell<ApplicationDetail, Date>() {
			@Override
			protected void updateItem(Date item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null || empty) {
					setText(null);
					setStyle("");
				}
				else {
					setText(dateToString(item));
				}
			}
		});
		paymentDataColumn.setCellFactory(column -> new TableCell<Payment, Date>() {
			@Override
			protected void updateItem(Date item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null || empty) {
					setText(null);
					setStyle("");
				}
				else {
					setText(dateToString(item));
				}
			}
		});
		applicationTable.setPlaceholder(new Label("Žádné přihlášky."));
		paymentTable.setPlaceholder(new Label("Žádné platby."));
  	}

	public void handleEditApplication(ActionEvent actionEvent) {
		if (selectedItem !=null) {
			getMainController().showApplicationEditorDialog(selectedItem);
			showItemDetails(selectedItem);
			final int selectedIdx = getDataTable().getSelectionModel().getSelectedIndex();
			refreshDataTable();
			getDataTable().getSelectionModel().select(selectedIdx);
		}
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/applicationOverview.fxml";
	}

	@Override
	protected TableView<ApplicationDetail> getDataTable() {
		return applicationTable;
	}

	@Override
	protected boolean matchesFilter(ApplicationDetail item) {
		return true;
	}

	@Override
	protected void handleEditItem() {
		handleEditApplication(null);
	}

	@Override
	protected void showItemDetails(ApplicationDetail application) {
		if(application == null){
			return;
		}
		codeLabel.setText(application.getCode());
		preCodeLabel.setText(application.getPreCode());
		priceLabel.setText(application.getIndividualPriceOrCampPrice().toString());
		firstNameLabel.setText(application.getFirstName());
		lastNameLabel.setText(application.getLastName());
		streetLabel.setText(application.getAddress());
		cityLabel.setText(application.getUser().getCity());
		postalCodeLabel.setText(application.getPostCode());
		birthdayLabel.setText(dateToString(application.getBirthday()));
		phoneLabel.setText(application.getPhone());
		emailLabel.setText(application.getEmail());
		noteLabel.setText(application.getNote());
		if (application.getGang()!=null){
			gangLabel.setText(application.getGang().getName());
		} else {
			gangLabel.setText("");
		}
		paymentTable.setItems(FXCollections.observableArrayList(application.getPayments()!=null?application.getPayments(): new ArrayList<>()));
	}

	@Override
	protected List<ApplicationDetail> getData() {
		if (getTaborApp().getActiveCamp() == null) {
			throw new RuntimeException("No active camp found!");
		}
		return applicationManager.getApplicationDetailForActualCamp(getTaborApp().getActiveCamp().getId());
	}

	@Override
	protected Class<? extends Entity> getSupportedClass() {
		return Application.class;
	}

	public void handleNewApplication(ActionEvent actionEvent) {
	 	getMainController().showApplicationEditorDialog(null);
	}

	public void handleDeleteApplication(ActionEvent actionEvent) {
		if (selectedItem !=null) {
			applicationManager.removeApplication(selectedItem);
		}
	}

	@Override
	public List<Node> getToolBarNodes() {
		ArrayList<Node> buttons = new ArrayList<>();

		buttons.add(getNewButton("Nová přihláška", this::handleNewApplication));
		buttons.add(getNewButton("Upravit přihlášku", this::handleEditApplication));
		buttons.add(getNewButton("Nová platba", this::handleNewPayment));
		buttons.add(getNewButton("Odstranit přihlášku", this::handleDeleteApplication));

		return buttons;
	}

	private void handleNewPayment(ActionEvent actionEvent) {
		if (selectedItem!=null){
			getMainController().showPaymentEditorDialog(selectedItem,null);
		}
	}

	@Override
	public void onApplicationEvent(EntityUpdateEvent event) {
		super.onApplicationEvent(event);
		if (event.getEntityClass().equals(Payment.class)) {
			populateDataTable();
		}
	}
}
