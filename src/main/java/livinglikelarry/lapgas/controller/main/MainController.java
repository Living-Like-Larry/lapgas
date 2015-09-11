package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import livinglikelarry.lapgas.MockingPaymentTabUtil;
import livinglikelarry.lapgas.model.Courses;
import javafx.stage.Stage;

/**
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class MainController implements Initializable {

	@FXML
	private TableView<Courses> coursesPaymentTabTableView;

	@FXML
	private TableColumn<Courses, String> coursePaymentTabTableColumn;

	@FXML
	private TextField npmPaymentTabTextField;

	@FXML
	private ComboBox<String> coursesPaymentTabComboBox;

	@FXML
	private TextField paymentReceiptPathTextField;
	
	@FXML
	private Button reportModeButton;
	
	@FXML
	private ComboBox<String> classReportTabComboBox;
	
	@FXML
	private ComboBox<String> coursesReportTabComboBox;
	
	@FXML
	private ComboBox<Integer> semesterReportTabComboBox;
	
	private Stage stage;
	private File choosenPaymentReceiptFile;
	private PaymentTabUtil paymentTabUtil;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.coursesPaymentTabTableView.getColumns().setAll(this.coursePaymentTabTableColumn);
		this.coursesPaymentTabComboBox.getItems().setAll("test test", "fsafsaf");
		
		this.semesterReportTabComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

		paymentTabUtil = new MockingPaymentTabUtil();

	}

	@FXML
	public void handleNpmTyping() {
		final String UNLA_IF_STUD_NUM_PATTERN = "4115505\\d{7}";
		String studentNumber = this.npmPaymentTabTextField.getText();
		if (studentNumber.matches(UNLA_IF_STUD_NUM_PATTERN)) {
			List<String> courses = paymentTabUtil.getCourses(studentNumber);
			if (courses != null) {
				this.coursesPaymentTabComboBox.getItems().clear();
				this.coursesPaymentTabComboBox.getItems().setAll(courses);
			}
		}
	}

	@FXML
	public void handleSubmitPaymentButton() {

		try {
			if (this.npmPaymentTabTextField.getText() != null && this.coursesPaymentTabTableView.getItems().size() != 0
					&& this.choosenPaymentReceiptFile != null) {
				this.paymentTabUtil.saveToDatabase(this.npmPaymentTabTextField.getText(),
						this.coursesPaymentTabTableView.getItems());
				this.paymentTabUtil.savePaymentReceiptToFS(this.choosenPaymentReceiptFile,
						this.npmPaymentTabTextField.getText(),
						this.paymentTabUtil.getCourses(this.npmPaymentTabTextField.getText()));
				this.coursesPaymentTabTableView.getItems().clear();
				this.npmPaymentTabTextField.clear();
				this.paymentReceiptPathTextField.clear();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Kesalahan !");
				alert.setHeaderText("Ada Kolom Kosong !");
				alert.setContentText("Perhatian ! Tidak boleh ada satupun kolom yang di kosongkan !");
				alert.showAndWait();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void handleChoosingImageButton() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters()
				.add(new ExtensionFilter("Gambar Struk Bank(.png, .jpg, .gif)", "*.png", "*.jpg", "*.gif"));
		choosenPaymentReceiptFile = fileChooser.showOpenDialog(this.stage);
		if (choosenPaymentReceiptFile != null) {
			this.paymentReceiptPathTextField.setText(choosenPaymentReceiptFile.toString());
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleChoosingCoursesComboBox() {
		String selectedCourse = this.coursesPaymentTabComboBox.getSelectionModel().getSelectedItem();
		if (selectedCourse != null) {
			this.coursesPaymentTabTableView.getItems().add(new Courses(selectedCourse));
			this.coursesPaymentTabComboBox.getItems().remove(selectedCourse);
		}

	}
	
	@FXML
	public void handleReportButton() {
	}

}
