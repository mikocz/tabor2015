package cz.miko.tabor.config;

import cz.miko.tabor.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class SpringFXMLLoader {

	private static final Log log = LogFactory.getLog(SpringFXMLLoader.class);

	private static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(TaborAppConfig.class);

	public static Controller load(String url) {
		InputStream fxmlStream = null;
		try {
			fxmlStream = SpringFXMLLoader.class.getResourceAsStream(url);
			FXMLLoader loader = new FXMLLoader();
			loader.setControllerFactory(aClass -> APPLICATION_CONTEXT.getBean(aClass));

			Node view = loader.load(fxmlStream);
			Controller controller = loader.getController();
			controller.setView(view);

			return controller;
		} catch (IOException e) {
			log.error("Can't load resource", e);
			throw new RuntimeException(e);
		} finally {
			if (fxmlStream != null) {
				try {
					fxmlStream.close();
				} catch (IOException e) {
					log.error("Can't close stream", e);
				}
			}
		}
	}

}
