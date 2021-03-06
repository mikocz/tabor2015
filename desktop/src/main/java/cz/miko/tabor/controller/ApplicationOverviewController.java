package cz.miko.tabor.controller;

import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.ApplicationDetail;
import cz.miko.tabor.core.model.CampSummary;
import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.model.Gang;
import cz.miko.tabor.core.model.Payment;
import cz.miko.tabor.core.service.ApplicationManager;
import cz.miko.tabor.report.ApplicationJRDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static cz.miko.tabor.core.service.TaborUtils.dateToString;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationOverviewController extends AbstractOverviewController<ApplicationDetail> {

	@FXML
	public Label applicationCountLabel;
	@FXML
	public Label applicationPaidLabel;
	@FXML
	public Label applicationGirlCountLabel;
	@FXML
	public Label applicationBoyCountLabel;
	@FXML
	public Label paymentTotalLabel;
	@FXML
	public Label paymentCashTotalLabel;
	@FXML
	public Label paymentTransferTotalLabel;

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
	private TextField filterField;
	@FXML
	private ComboBox<String> paidFilterComboBox;
	@FXML
	private ComboBox<Gang> gangFilteredComboBox;


	@FXML
	private TableView<Payment> paymentTable;
	@FXML
	private TableView<ApplicationDetail> applicationTable;

	@FXML
	private TableColumn<ApplicationDetail, Date> birthdayColumn;
	@FXML
	private TableColumn<Payment, Date> paymentDataColumn;
	@FXML
	private TableColumn actionColumn;


	@FXML
	private ToolBar applicationOverViewToolbar;

	@Override
	protected void initialize() {
		super.initialize();

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

		actionColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				return new TableCell() {
					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							final Button btnPrint = new Button("Odstranit");
							btnPrint.setOnAction(event -> {
								param.getTableView().getSelectionModel().select(getIndex());
								Payment payment = paymentTable.getSelectionModel().getSelectedItem();
								if (payment != null) {
									deletePayment(payment);
								}
							});
							setGraphic(btnPrint);
							setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						}
					}
				};
			}
		});

		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			refreshDataTable();
		});
		paidFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> refreshDataTable());

		initGangFilteredComboBox();
  	}

	private void initGangFilteredComboBox() {
		List<Gang> gangListByCamp = new ArrayList<>();


		Gang allGang = new Gang();
		allGang.setId(-20);
		allGang.setName("Vše");
		gangListByCamp.add(allGang);
		Gang noGang = new Gang();
		noGang.setId(-10);
		noGang.setName("Bez zařazení do čety");
		gangListByCamp.add(noGang);

		gangListByCamp.addAll(applicationManager.getGangListByCamp(getTaborApp().getActiveCamp().getId()));
		ObservableList<Gang> gangs =
				FXCollections.observableArrayList(gangListByCamp);

		gangFilteredComboBox.setValue(allGang);
		gangFilteredComboBox.setItems(gangs);
		gangFilteredComboBox.valueProperty().addListener((observable, oldValue, newValue) -> refreshDataTable());
		gangFilteredComboBox.setCellFactory(new Callback<ListView<Gang>, ListCell<Gang>>() {
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
		gangFilteredComboBox.setConverter(new StringConverter<Gang>() {
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
		return matchesTextFiltered(item) && matchesPaidFilter(item) && matchesGangComboBox(item);
	}

	private boolean matchesGangComboBox(ApplicationDetail item) {
		if (gangFilteredComboBox.getValue() == null || -20 == gangFilteredComboBox.getValue().getId()) {
			return true;
		}

		if (-10 == gangFilteredComboBox.getValue().getId() && item.getGang() == null) {
			return true;
		}
		if (item.getGang()!=null && Objects.equals(gangFilteredComboBox.getValue().getId(), item.getGang().getId())) {
			return true;
		}

		return false;
	}

	private boolean matchesPaidFilter(ApplicationDetail item) {
		if (paidFilterComboBox.getValue() == null || "Vše".equalsIgnoreCase(paidFilterComboBox.getValue())) {
			return true;
		}
		if ("Uhrazeno".equalsIgnoreCase(paidFilterComboBox.getValue()) && item.isApplicationPaid()) {
			return true;
		}
		if ("Neuhrazeno".equalsIgnoreCase(paidFilterComboBox.getValue()) && !item.isApplicationPaid()) {
			return true;
		}
		return false;
	}

	private boolean matchesTextFiltered(ApplicationDetail item) {
		String filterString = filterField.getText();
		if ((filterString == null || filterString.isEmpty())) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (!StringUtils.isEmpty(item.getFirstName()) && item.getFirstName().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		} else if (!StringUtils.isEmpty(item.getLastName()) && item.getLastName().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		} else if (!StringUtils.isEmpty(item.getCode()) && item.getCode().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		} else if (!StringUtils.isEmpty(item.getPreCode()) && item.getPreCode().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		}

		return false;
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


		CampSummary campSummary = applicationManager.getCampSummary(application.getCampId());
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
		buttons.add(getNewButton("Export do CSV", this::handleExportToCsv));
		buttons.add(getNewButton("Export reportu do PDF", this::exportToJasperReport));

		return buttons;
	}

	private void handleExportToCsv(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export");
		File file = fileChooser.showSaveDialog(getPrimaryStage());
		if (file != null) {

			String COMMA_DELIMITER = ";";
			String NEW_LINE_SEPARATOR = "\n";

			FileWriter fileWriter = null;

			try {
				fileWriter = new FileWriter(file);

				//Write a new student object list to the CSV file
				ObservableList<ApplicationDetail> items = getDataTable().getItems();
				for(ApplicationDetail item : items) {
					fileWriter.append(item.getCode());
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(item.getPreCode());
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(item.getFirstName());
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(item.getLastName());
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(dateToString(item.getBirthday()));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(item.getIndividualPriceOrCampPrice().toString());
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(item.getPaid().toString());
					fileWriter.append(NEW_LINE_SEPARATOR);
				}
				System.out.println("CSV file was created successfully !!!");
			} catch (Exception e) {
				System.out.println("Error in CsvFileWriter !!!");
				e.printStackTrace();
			} finally {
				if (fileWriter!=null) {
					try {
						fileWriter.flush();
						fileWriter.close();
					} catch (IOException e) {
						System.out.println("Error while flushing/closing fileWriter !!!");
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void handleNewPayment(ActionEvent actionEvent) {
		if (selectedItem!=null){
			getMainController().showPaymentEditorDialog(selectedItem,null);
		}
	}

	public void exportToJasperReport(ActionEvent actionEvent) {
		try {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Vyberte report");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JRXML", "*.jrxml"));
			File reportFile = fileChooser.showOpenDialog(getPrimaryStage());

			JasperPrint jasperPrint = JasperFillManager.fillReport(JasperCompileManager.compileReport(new FileInputStream(reportFile)),
					new HashMap(),
					new ApplicationJRDataSource(getDataTable().getItems()));

			FileChooser saveChooser = new FileChooser();
			saveChooser.setTitle("Export");
			saveChooser.setInitialFileName(FilenameUtils.getBaseName(reportFile.getName())+".pdf");
			File file = saveChooser.showSaveDialog(getPrimaryStage());

			JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void deletePayment(Payment payment) {
		applicationManager.removePayment(payment);
	}

	@Override
	public void onApplicationEvent(EntityUpdateEvent event) {
		super.onApplicationEvent(event);
		if (event.getEntityClass().equals(Payment.class)) {
			populateDataTable();
		}
	}
}
