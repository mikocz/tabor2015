package cz.miko.tabor.extension;

import cz.miko.tabor.core.model.Camp;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class CampActiveCell extends TableCell<Camp,Boolean> {

	private CheckBox checkBox;

	public CampActiveCell() {
		checkBox = new CheckBox();
		checkBox.setDisable(true);
		checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if(isEditing())
				commitEdit(newValue == null ? false : newValue);
		});
		this.setGraphic(checkBox);
		this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		this.setEditable(true);
	}
	@Override
	public void startEdit() {
		super.startEdit();
		if (isEmpty()) {
			return;
		}
		checkBox.setDisable(false);
		checkBox.requestFocus();
	}
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		checkBox.setDisable(true);
	}
	public void commitEdit(Boolean value) {
		super.commitEdit(value);
		checkBox.setDisable(true);
	}
	@Override
	public void updateItem(Boolean item, boolean empty) {
		super.updateItem(item, empty);
		if (!isEmpty()) {
			checkBox.setSelected(item);
		}
	}
}
