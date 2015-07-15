package cz.miko.tabor.controller;

import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public abstract class AbstractOverviewController<T> extends AbstractController<T> implements ApplicationListener<EntityUpdateEvent> {

	@Autowired
	@Getter(AccessLevel.PROTECTED)
	private MainController mainController;

	protected T selectedItem;

	protected ObservableList<T> allData = FXCollections.observableArrayList();
	protected ObservableList<T> filteredData = FXCollections.observableArrayList();

	@FXML
	protected void initialize() {

		populateDataTable();

		getDataTable().getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					selectedItem = newValue;
					showItemDetails(newValue);
				});

		getDataTable().setRowFactory(tv -> {

			TableRow<T> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && (!row.isEmpty())) {
					selectedItem = row.getItem();
					handleEditItem();
				}
			});
			return row;
		});

		if (!getDataTable().getItems().isEmpty()) {
			getDataTable().getSelectionModel().select(0);
		}

		allData.addListener((ListChangeListener.Change<? extends T> change) -> {
			refreshDataTable();
		});

	}

	protected abstract TableView<T> getDataTable();

	protected void reapplyTableSortOrder() {
		List<TableColumn<T, ?>> sortOrder = new ArrayList(getDataTable().getSortOrder());
		getDataTable().getSortOrder().clear();
		getDataTable().getSortOrder().addAll(sortOrder);
	}

	protected void refreshDataTable() {

		filteredData.clear();

		List<T> list = this.allData;

		filteredData.addAll(list.stream().filter(this::matchesFilter).collect(Collectors.toList()));

		getDataTable().setItems(filteredData);

		// Must re-sort table after items changed
		reapplyTableSortOrder();
	}

	protected abstract boolean matchesFilter(T item);

	protected abstract void handleEditItem();

	protected abstract void showItemDetails(T item);


	protected void populateDataTable() {
		final int selectedIdx = getDataTable().getSelectionModel().getSelectedIndex();
		allData = FXCollections.observableArrayList(getData());
		filteredData.addAll(allData);
		refreshDataTable();
		getDataTable().getSelectionModel().select(selectedIdx);
	}

	protected abstract List<T> getData();

	@Override
	public void onApplicationEvent(EntityUpdateEvent event) {
		if (event.getEntityClass().equals(getSupportedClass())) {
			populateDataTable();
		}
	}

	protected abstract Class<? extends Entity> getSupportedClass();
}
