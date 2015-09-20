package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.javalite.activejdbc.DB;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.AdminAuth;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.LabAsstLapgasState;
import livinglikelarry.lapgas.LapgasState;
import livinglikelarry.lapgas.RootLapgasState;
import livinglikelarry.lapgas.SpecialLabAsstLapgasState;
import livinglikelarry.lapgas.controller.main.MainController;
import livinglikelarry.lapgas.model.Admin;
import livinglikelarry.lapgas.model.sql.AdminSql;

/**
 * 
 * Controller for LoginView
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class LoginController implements Initializable {

	@FXML
	private PasswordField passwordTextField;

	@FXML
	private ImageView loginImageView;

	@FXML
	private TextField studentNumberTextField;

	@FXML
	private ComboBox<String> roleComboBox;

	private LapgasState lapgasState;

	private Stage primaryStage;
	private DB adminDB;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.loginImageView.setImage(new Image(Configurator.image("if_unla.png").toString()));
		this.roleComboBox.getItems().setAll("Root", "Aslab Khusus", "Aslab");
	}

	@FXML
	public void handlePressingEnter(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			Admin admin = AdminSql.findFirst("id = ?", "1");
			AdminAuth adminAuth = new AdminAuth(admin, this.passwordTextField.getText());
			adminAuth.onSuccess(x -> {
				try {
					adminDB.close();
					makeLapgasDB();

					FXMLLoader fxmlLoader = new FXMLLoader();
					GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Main"));
					MainController mainController = (MainController) fxmlLoader.getController();
					mainController.setLapgasState(lapgasState);
					Stage stage = new Stage();
					stage.setTitle("Lapgas");
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
	}

	@FXML
	public void handleChoosingRole() {
		final String selectedRole = this.roleComboBox.getSelectionModel().getSelectedItem();
		Configurator.setRole(selectedRole);
		if (!selectedRole.equalsIgnoreCase("root")) {
			this.studentNumberTextField.setVisible(true);
			System.out.println("my role is not root");
			if (selectedRole.equalsIgnoreCase("aslab")) {
				System.out.println("i'm labasst");
				this.lapgasState = new LabAsstLapgasState();
			} else {
				System.out.println("i'm special labasst");
				this.lapgasState = new SpecialLabAsstLapgasState();
			}
		} else {
			System.out.println("my role is root");
			this.studentNumberTextField.setVisible(false);
			this.lapgasState = new RootLapgasState();

		}
	}

	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setAdminDB(DB adminDB) {
		this.adminDB = adminDB;
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

}
