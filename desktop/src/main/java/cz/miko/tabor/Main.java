package cz.miko.tabor;

import cz.miko.tabor.config.SpringFXMLLoader;
import cz.miko.tabor.controller.MainController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        MainController controller = (MainController) SpringFXMLLoader.load("/sample.fxml");
        Scene scene = new Scene((Parent) controller.getView(), 300, 275);
        primaryStage.setTitle("Tabor");
        primaryStage.setScene(scene);
        primaryStage.show();

//        scene.getStylesheets().add
//                (Main.class.getResource("main.css").toExternalForm());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
