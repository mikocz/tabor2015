package cz.miko.tabor.extension;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class RequiredTextField extends TextField {

	@Setter
	@Getter
	private boolean valid = true;

	public RequiredTextField() {
		super();
		init();
	}

	public RequiredTextField(String text) {
		super(text);
		init();
	}

	private void init() {
		this.textProperty().addListener((ov, t, t1) -> {
			validate();
		});
	}

	public boolean validate() {
		ObservableList<String> styleClass = this.getStyleClass();
		if (textProperty().get()!=null) {
			if (textProperty().get().length()<1) {
				if (!styleClass.contains("error")) {
					styleClass.add("error");
					valid = false;
				}
			} else {
				styleClass.removeAll(Collections.singleton("error"));
				valid = true;
			}
		}
		return valid;
	}
}
