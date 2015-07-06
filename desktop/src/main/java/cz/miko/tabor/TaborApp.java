package cz.miko.tabor;

import cz.miko.tabor.config.MainAppHolder;
import cz.miko.tabor.config.TaborAppConfig;
import cz.miko.tabor.controller.MainController;
import cz.miko.tabor.core.config.TaborCoreConfig;
import cz.miko.tabor.core.model.Camp;
import cz.miko.tabor.core.service.CampManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TaborApp extends Application  {

    public static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(TaborAppConfig.class);

    @Getter
    private Stage primaryStage;
    @Getter
    private BorderPane rootLayout;
    @Setter
    @Getter
    private ToolBar toolBar;
    @Setter
    @Getter
    private Camp activeCamp;
    @Getter
    private String applicationTitlePrefix = "Táborová evidence";

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        MainAppHolder mainAppHolder = APPLICATION_CONTEXT.getBean(MainAppHolder.class);
        mainAppHolder.setTaborApp(this);

        initRootLayout(primaryStage);

//        scene.getStylesheets().add
//                (Main.class.getResource("main.css").toExternalForm());
    }

    private Node initRootLayout(Stage primaryStage) {
        TaborCoreConfig taborCoreConfig = APPLICATION_CONTEXT.getBean(TaborCoreConfig.class);
        taborCoreConfig.init(APPLICATION_CONTEXT);

        MainController controller = APPLICATION_CONTEXT.getBean(MainController.class);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        CampManager campManager = APPLICATION_CONTEXT.getBean(CampManager.class);
        setActiveCamp(campManager.getActiveCamp());
        setApplicationTitle(campManager.getActiveCamp());
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        Node view = controller.getView();
        rootLayout = (BorderPane)view;
        Scene scene = new Scene((Parent)view, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        toolBar = (ToolBar)getPrimaryStage().getScene().lookup("#mainToolBar");
        controller.showApplicationOverview(null);

        return view;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setApplicationTitle(Camp camp) {
        if (camp !=null) {
            getPrimaryStage().setTitle(getApplicationTitlePrefix() + " - " + camp.getName());
        } else {
            getPrimaryStage().setTitle(getApplicationTitlePrefix());
        }
    }
}
