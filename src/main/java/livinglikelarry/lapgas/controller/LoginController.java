package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.AdminAuth;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.Admin;
import livinglikelarry.lapgas.model.AdminSql;

/**
 * 
 * Controller for LoginView
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class LoginController implements Initializable {

	@FXML
	private PasswordField passwordTextField;
	private Stage primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Configurator.initAdminAuth();
	}

	@FXML
	public void handleLoginButton() {
		Admin admin = AdminSql.findFirst("id = ?", "1");
		AdminAuth adminAuth = new AdminAuth(admin, this.passwordTextField.getText());
		adminAuth.onSuccess(x -> {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Main"));
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
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

}
