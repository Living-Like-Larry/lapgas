package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.javalite.activejdbc.DB;

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
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.CoursesTableModel;
import javafx.stage.Stage;

/**
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class MainController implements Initializable {

	@FXML
	private TableView<CoursesTableModel> coursesPaymentTabTableView;

	@FXML
	private TableColumn<CoursesTableModel, String> coursePaymentTabTableColumn;

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

	@FXML
	private TextField studentNumReportTabTextField;

	private Stage stage;
	private File choosenPaymentReceiptFile;
	private PaymentTabUtil paymentTabUtil;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.coursesPaymentTabTableView.getColumns().setAll(this.coursePaymentTabTableColumn);
		this.semesterReportTabComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

		paymentTabUtil = new PaymentTabUtil();
	}

	@FXML
	public void handleTypingNpm() {

		final String UNLA_IF_STUD_NUM_PATTERN = "4115505\\d{7}";
		String studentNumber = this.npmPaymentTabTextField.getText();
		if (studentNumber.matches(UNLA_IF_STUD_NUM_PATTERN)) {
			doDBACtion(() -> {
				List<String> courses = paymentTabUtil.getCourses(studentNumber);
				coursesPaymentTabComboBox.getItems().clear();
				coursesPaymentTabComboBox.getItems().setAll(courses);
			});
		}
	}

	private void doDBACtion(Runnable actionRunnable) {
		DB lapgasDB = new DB("lapgas");
		lapgasDB.open(Configurator.properties("main.driver"), Configurator.properties("main.url") + "lapgas",
				Configurator.properties("main.username"), Configurator.properties("main.password"));
		actionRunnable.run();
		lapgasDB.close();
	}

	@FXML
	public void handleSubmitPaymentButton() {

		if (this.npmPaymentTabTextField.getText() != null && this.coursesPaymentTabTableView.getItems().size() != 0
				&& this.choosenPaymentReceiptFile != null) {

			doDBACtion(() -> {
				try {
					this.paymentTabUtil.save(this.npmPaymentTabTextField.getText(),
							this.coursesPaymentTabTableView.getItems(), this.choosenPaymentReceiptFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
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
			this.coursesPaymentTabTableView.getItems().add(new CoursesTableModel(selectedCourse));
			this.coursesPaymentTabComboBox.getItems().remove(selectedCourse);
		}
	}

	@FXML
	public void handleReportButton() {
		ReportTabUtil reportTabUtil = new ReportTabUtil();

		String course = this.coursesReportTabComboBox.getValue();
		String studentClass = this.classReportTabComboBox.getValue();
		int semester = this.semesterReportTabComboBox.getValue();
		if (this.reportModeButton.getText().equalsIgnoreCase("semua")) {
			reportTabUtil.reportAll(course, studentClass, semester);
		} else {
			reportTabUtil.reportBasedOn(this.studentNumReportTabTextField.getText(), course, studentClass, semester);
		}
	}

}
