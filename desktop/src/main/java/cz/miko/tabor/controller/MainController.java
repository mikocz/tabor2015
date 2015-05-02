package cz.miko.tabor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.stereotype.Service;

@Service
public class MainController extends AbstractController {

	@FXML
	private Text actiontarget;

	@FXML
	public void handleSubmitButtonAction(ActionEvent actionEvent) {
		System.out.println("handleSubmitButtonAction");

		actiontarget.setText("Sign in button pressed");
	}
}
