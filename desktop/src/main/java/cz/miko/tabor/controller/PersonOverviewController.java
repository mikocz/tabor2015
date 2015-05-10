package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.User;
import cz.miko.tabor.core.service.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cz.miko.tabor.core.service.Java8Utils.toLocalDate;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class PersonOverviewController extends AbstractController {

	@Autowired
	private UserManager userManager;

	@Autowired
	private MainController mainController;

	@FXML
	private TableView<User> personTable = new TableView<>();

	@FXML
	private TableColumn<User, Date> birthdayColumn;
	@FXML
	private TableColumn<User, String> firstNameColumn;

	@FXML
	private TextField filterField;

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


	private ObservableList<User> allUsersData = FXCollections.observableArrayList();
	private ObservableList<User> filteredUserData = FXCollections.observableArrayList();

	private DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");

	@FXML
	private void initialize() {


		// Custom rendering of the table cell.
		birthdayColumn.setCellFactory(column -> new TableCell<User, Date>() {
			@Override
			protected void updateItem(Date item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null || empty) {
					setText(null);
					setStyle("");
				}
				else {
					setText(myDateFormatter.format(toLocalDate(item)));
				}
			}
		});

		this.allUsersData = FXCollections.observableArrayList(userManager.getUsers());
		this.filteredUserData.addAll(allUsersData);
		personTable.setItems(allUsersData);

		// Listen for text changes in the filter text field
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			updateFilteredData();
		});

		allUsersData.addListener((ListChangeListener.Change<? extends User> change) -> {
			updateFilteredData();
		});

		personTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showPersonDetails(newValue));

		showPersonDetails(null);
	}

	private void showPersonDetails(User person) {
		if (person != null) {
			// Fill the labels with info from the person object.
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getAddress());
			postalCodeLabel.setText(person.getPostCode());
			cityLabel.setText(person.getCity());
			birthdayLabel.setText(myDateFormatter.format(toLocalDate(person.getBirthday())));
			phoneLabel.setText(person.getPhone());
			emailLabel.setText(person.getEmail());
		} else {
			// Person is null, remove all the text.
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			cityLabel.setText("");
			birthdayLabel.setText("");
			phoneLabel.setText("");
			emailLabel.setText("");
		}
	}


	/**
	 * Updates the filteredData to contain all data from the masterData that
	 * matches the current filter.
	 */
	private void updateFilteredData() {

		filteredUserData.clear();

		List<User> users = this.allUsersData;

		for (User user : users) {
			if (matchesFilter(user)) {
				filteredUserData.add(user);
			}
		}
		
		this.personTable.setItems(filteredUserData);

		// Must re-sort table after items changed
		reapplyTableSortOrder();
	}

	private boolean matchesFilter(User user) {
		String filterString = filterField.getText();
		if (filterString == null || filterString.isEmpty()) {
			// No filter --> Add all.
			return true;
		}

		String lowerCaseFilterString = filterString.toLowerCase();

		if (!StringUtils.isEmpty(user.getFirstName()) && user.getFirstName().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		} else if (!StringUtils.isEmpty(user.getLastName()) && user.getLastName().toLowerCase().contains(lowerCaseFilterString)) {
			return true;
		}

		return false; // Does not match
	}

	private void reapplyTableSortOrder() {
		ArrayList<TableColumn<User, ?>> sortOrder = new ArrayList<>(personTable.getSortOrder());
		personTable.getSortOrder().clear();
		personTable.getSortOrder().addAll(sortOrder);
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/personOverview.fxml";
	}

	public void handleNewPerson(ActionEvent actionEvent) {

		User tempPerson = new User();

		boolean okClicked = mainController.showPersonEditorDialog(tempPerson);
		if(okClicked) {
			allUsersData.add(tempPerson);
		}
	}

	public void handleEditPerson(ActionEvent actionEvent) {

	}

	public void handleDeletePerson(ActionEvent actionEvent) {

	}
}
