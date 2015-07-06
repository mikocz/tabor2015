package cz.miko.tabor.extension;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import cz.miko.tabor.core.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class SelectKeyComboBoxListener implements EventHandler<KeyEvent> {

	private ComboBox comboBox;
	private StringBuilder sb = new StringBuilder();

	public SelectKeyComboBoxListener(ComboBox comboBox) {
		this.comboBox = comboBox;
		this.comboBox.setOnKeyReleased(this);

		this.comboBox.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
			if(event.getCode() == KeyCode.ESCAPE && sb.length() > 0) {
				sb.delete(0, sb.length());
			}
		});

		// add a focus listener such that if not in focus, reset the filtered typed keys
		this.comboBox.focusedProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				if(newValue instanceof Boolean && !(Boolean)newValue)
					sb.delete(0, sb.length());
				else {
					ListView lv = ((ComboBoxListViewSkin)SelectKeyComboBoxListener.this.comboBox.getSkin()).getListView();
					lv.scrollTo(lv.getSelectionModel().getSelectedIndex());
				}
			}
		});

		this.comboBox.setOnMouseClicked(event -> {
			ListView lv = ((ComboBoxListViewSkin)SelectKeyComboBoxListener.this.comboBox.getSkin()).getListView();
			lv.scrollTo(lv.getSelectionModel().getSelectedIndex());
		});
	}

	@Override
	public void handle(KeyEvent event) {
		if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.TAB) {
			return;
		}
		else if(event.getCode() == KeyCode.BACK_SPACE && sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		else {
			sb.append(event.getText());
		}

		if(sb.length() == 0)
			return;

		boolean found = false;
		ObservableList<User> items = comboBox.getItems();
		for(int i = 0; i < items.size(); i++) {
			if(event.getCode() != KeyCode.BACK_SPACE && items.get(i).getLastName().toLowerCase().startsWith(sb.toString().toLowerCase())) {
				ListView lv = ((ComboBoxListViewSkin)comboBox.getSkin()).getListView();
				lv.getSelectionModel().clearAndSelect(i);
				lv.scrollTo(lv.getSelectionModel().getSelectedIndex());
				found = true;
				break;
			}
		}

		if(!found && sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
	}
}