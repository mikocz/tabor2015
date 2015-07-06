package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.Entity;
import cz.miko.tabor.core.model.User;
import cz.miko.tabor.core.service.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class PersonOverviewController extends AbstractOverviewController<User> {

	@Autowired
	private UserManager userManager;


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



	@Override
	@FXML
	protected void initialize() {
		super.initialize();

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
					setText(dateToString(item));
				}
			}
		});

		// Listen for text changes in the filter text field
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			refreshDataTable();
		});

		showPersonDetails(null);
	}

	@Override
	public List<Node> getToolBarNodes() {
		ArrayList<Node> buttons = new ArrayList<>();

		buttons.add(getNewButton("Nová kontakt", this::handleNewPerson));
		buttons.add(getNewButton("Upravit kontakt", this::handleEditPerson));
		buttons.add(getNewButton("Odstranit kontakt", this::handleDeletePerson));

		return buttons;
	}

	@Override
	protected TableView<User> getDataTable() {
		return this.personTable;
	}

	private void showPersonDetails(User person) {
		if (person != null) {
			// Fill the labels with info from the person object.
			selectedItem = person;
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getAddress());
			postalCodeLabel.setText(person.getPostCode());
			cityLabel.setText(person.getCity());
			birthdayLabel.setText(dateToString(person.getBirthday()));
			phoneLabel.setText(person.getPhone());
			emailLabel.setText(person.getEmail());
			noteLabel.setText(person.getNote());
		} else {
			// Person is null, remove all the text.
			selectedItem = null;
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

	@Override
	protected boolean matchesFilter(User user) {
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

	@Override
	protected void handleEditItem() {
		handleEditPerson(null);
	}

	@Override
	protected void showItemDetails(User item) {
		showPersonDetails(item);
	}

	@Override
	protected List<User> getData() {
		return userManager.getUsers();
	}

	@Override
	protected Class<? extends Entity> getSupportedClass() {
		return User.class;
	}


	@Override
	protected String getFxmlConfig() {
		return "/view/personOverview.fxml";
	}

	public void handleNewPerson(ActionEvent actionEvent) {

		User tempPerson = new User();

		boolean okClicked = getMainController().showPersonEditorDialog(tempPerson);
		if(okClicked) {
			allData.add(tempPerson);
			refreshDataTable();
		}
	}

	public void handleEditPerson(ActionEvent actionEvent) {
		if (selectedItem!=null) {
			getMainController().showPersonEditorDialog(selectedItem);
			showPersonDetails(selectedItem);
			final int selectedIdx = getDataTable().getSelectionModel().getSelectedIndex();
			refreshDataTable();
			getDataTable().getSelectionModel().select(selectedIdx);
		}
	}

	public void handleDeletePerson(ActionEvent actionEvent) {
		if (selectedItem!=null) {
			final int selectedIdx = getDataTable().getSelectionModel().getSelectedIndex();

			userManager.deleteUserById(selectedItem.getId());
			allData.remove(selectedItem);
			filteredData.remove(selectedItem);

			if (selectedIdx != -1) {
				final int newSelectedIdx =
						(selectedIdx > getDataTable().getItems().size() - 1)
								? selectedIdx - 1
								: selectedIdx;
				getDataTable().getSelectionModel().select(newSelectedIdx);
			}
		}
	}
}
