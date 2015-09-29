package livinglikelarry.lapgas;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.controller.LoginController;
import livinglikelarry.lapgas.model.sql.AdminSql;
import livinglikelarry.lapgas.model.sql.Scanner;
import livinglikelarry.lapgas.util.Configurator;

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
			initDB();
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(Configurator.view("Login"));
			LoginController loginController = (LoginController) fxmlLoader.getController();
			Scene scene = new Scene((AnchorPane) fxmlLoader.getRoot());
			primaryStage.setScene(scene);
			loginController.setStage(primaryStage);
			primaryStage.setTitle("Login");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initDB() throws IOException, SQLException {
		makeAdminDB();
		makeLapgasDB();
	}

	private static void makeAdminDB() throws IOException {
		Configurator.doRawAdminDBActionConsumer((x) -> {
			try {
				if (x.exec("SELECT name FROM sqlite_master WHERE name='admins'") == 0) {
					x.exec(Configurator.table("admins"));
					x.exec(Configurator.table("scanners"));
					new AdminSql().set("password", "livinglikelarry").saveIt();
					new Scanner().set("scanner_path", null).saveIt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				x.close();
			}

		});
	}

	private static void makeLapgasDB() throws SQLException, IOException {
		Configurator.doRawDBActionConsumer((x) -> {

			try {
				ResultSet resultSet = x.connection().createStatement().executeQuery("SHOW DATABASES");
				boolean isFound = false;
				while (resultSet.next()) {
					if (resultSet.getString("Database").equalsIgnoreCase("lapgas")) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					x.exec("CREATE DATABASE lapgas");
					x.exec("USE lapgas");
					x.exec(Configurator.table("courses"));
					x.exec(Configurator.table("student_payments"));
					x.exec(Configurator.table("lab_assistant"));
					x.exec(Configurator.table("lab_assistant_attendances"));
					x.exec(Configurator.table("lab_assistant_logs"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

}
