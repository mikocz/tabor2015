package cz.miko.tabor.controller;

import cz.miko.tabor.core.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class PersonalEditorController extends AbstractController {
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private User user;

	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Stage dialogStage;

	@FXML
	@Setter
	@Getter(AccessLevel.PROTECTED)
	private Button storeButton;

	@FXML
	private void initialize() {
	}

	@Override
	protected String getFxmlConfig() {
		return "/view/personEditor.fxml";
	}

	public boolean isOkClicked() {
		return false;
	}

	public void handleOk(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setUser(null);

	}

	public void handleCancel(ActionEvent actionEvent) {
		dialogStage.close();
		this.setDialogStage(null);
		this.setUser(null);

	}
}
