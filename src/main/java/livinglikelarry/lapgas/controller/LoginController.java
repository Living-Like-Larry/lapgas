package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.javalite.activejdbc.Model;

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
import livinglikelarry.lapgas.controller.main.MainController;
import livinglikelarry.lapgas.model.Admin;
import livinglikelarry.lapgas.model.sql.AdminSql;
import livinglikelarry.lapgas.model.sql.LabAssistant;
import livinglikelarry.lapgas.state.LabAsstLapgasState;
import livinglikelarry.lapgas.state.LapgasState;
import livinglikelarry.lapgas.state.RootLapgasState;
import livinglikelarry.lapgas.state.SpecialLabAsstLapgasState;
import livinglikelarry.lapgas.util.AdminAuth;
import livinglikelarry.lapgas.util.Configurator;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.loginImageView.setImage(new Image(Configurator.image("if_unla.png").toString()));
		this.roleComboBox.getItems().setAll("Root", "Aslab Khusus", "Aslab");
	}

	@FXML
	public void handlePressingEnter(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			String role = this.roleComboBox.getSelectionModel().getSelectedItem();
			if (role != null) {
				if (role.equalsIgnoreCase("root")) {
					try {
						Configurator.doRawAdminDBActionConsumer((adminDB) -> {
							Admin admin = AdminSql.findFirst("id = ?", "1");
							AdminAuth adminAuth = new AdminAuth(admin, this.passwordTextField.getText());
							adminAuth.onSuccess(x -> {
								try {
									adminDB.close();
									showMain();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}).onFailed(x -> {
								authFailed();
							});

						});
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Configurator.doRawDBActionWithDBConsumer((x) -> {
						try {
							Model labAssistant = LabAssistant.findFirst(
									"student_number = ? AND password = ? AND role = ? ",
									(String) studentNumberTextField.getText(),
									(String) this.passwordTextField.getText(), (String) role);
							if (labAssistant != null) {
								x.close();
								showMain((String) labAssistant.get("student_number"));
							} else {
								authFailed();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					});
				}

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Harap pilih salah satu role");
				alert.showAndWait();
			}
		}
	}

	private void showMain(String labAsstStudentNumber) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Main"));
		MainController mainController = (MainController) fxmlLoader.getController();
		mainController.setLapgasState(lapgasState);
		mainController.setLabAsstStudentNumber(labAsstStudentNumber);
		Stage stage = new Stage();
		stage.setTitle("Lapgas");
		stage.setScene(new Scene(root));
		mainController.setStage(stage);
		primaryStage.close();
		stage.show();
	}

	private void authFailed() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Autentikasi gagal");
		alert.setContentText("Data yang dimasukan tidak cocok !");
		alert.showAndWait();
	}

	private void showMain() throws IOException {
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

}
