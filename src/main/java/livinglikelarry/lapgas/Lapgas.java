package livinglikelarry.lapgas;

import java.io.IOException;

import org.javalite.activejdbc.DB;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.controller.LoginController;
import livinglikelarry.lapgas.model.sql.AdminSql;

/**
 * Main class for lapgas
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class Lapgas extends Application {

	private static DB adminDB;

	@Override
	public void start(Stage primaryStage) {
		try {
			makeAdminDB();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(Configurator.view("Login"));
			LoginController loginController = (LoginController) fxmlLoader.getController();
			loginController.setAdminDB(Lapgas.adminDB);
			Scene scene = new Scene((AnchorPane) fxmlLoader.getRoot());
			primaryStage.setScene(scene);
			loginController.setStage(primaryStage);
			primaryStage.setTitle("Login");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private static void makeAdminDB() throws IOException {
		adminDB = new DB("admin");
		adminDB.open(Configurator.properties("admin.driver"),
				Configurator.properties("admin.url") + Configurator.properties("admin.dbname"),
				Configurator.properties("admin.username"), Configurator.properties("admin.password"));
		if (adminDB.exec("SELECT name FROM sqlite_master WHERE name='admins'") == 0) {
			adminDB.exec(Configurator.table("admins"));
			new AdminSql().set("password", "livinglikelarry").saveIt();
		}
	}
	
	
}
