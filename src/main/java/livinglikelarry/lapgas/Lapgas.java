package livinglikelarry.lapgas;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.controller.LoginController;

/**
 * Main class for lapgas
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class Lapgas extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(Configurator.view("Login"));
			LoginController loginController = (LoginController) fxmlLoader.getController();
			Scene scene = new Scene((AnchorPane) fxmlLoader.getRoot());
			primaryStage.setScene(scene);
			loginController.setStage(primaryStage);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
