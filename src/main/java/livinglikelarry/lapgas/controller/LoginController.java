package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.javalite.activejdbc.DB;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.AdminAuth;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.controller.main.MainController;
import livinglikelarry.lapgas.model.Admin;
import livinglikelarry.lapgas.model.AdminSql;

/**
 * 
 * Controller for LoginView
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class LoginController {

	@FXML
	private PasswordField passwordTextField;
	private Stage primaryStage;
	private DB adminDB;

	@FXML
	public void handleLoginButton() throws IOException, SQLException {
		Admin admin = AdminSql.findFirst("id = ?", "1");
		AdminAuth adminAuth = new AdminAuth(admin, this.passwordTextField.getText());
		adminAuth.onSuccess(x -> {
			try {
				adminDB.close();

				FXMLLoader fxmlLoader = new FXMLLoader();
				GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Main"));
				MainController mainController = (MainController) fxmlLoader.getController();
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				mainController.setStage(stage);
				primaryStage.close();
				stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).onFailed(x -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Autentikasi gagal");
			alert.setContentText("Sepertinya password yang anda masukan salah");
			alert.showAndWait();
		});
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setAdminDB(DB adminDB) {
		this.adminDB = adminDB;
	}

}
