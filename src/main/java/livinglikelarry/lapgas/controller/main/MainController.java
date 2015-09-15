package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.controller.SettingController;
import livinglikelarry.lapgas.controller.StudentGradingController;
import livinglikelarry.lapgas.controller.StudentPaymentController;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.StudentPayment;
import livinglikelarry.lapgas.model.table.CoursesTableModel;
import livinglikelarry.lapgas.model.table.StudentPaymentTableModel;
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
	private TextField classTabPaymentTextField;

	@FXML
	private TextField paymentValueTabPaymentTextField;

	@FXML
	private TextField studentPatternTabStudentTextField;

	@FXML
	private TableView<StudentPaymentTableModel> studentPaymentTableView;

	@FXML
	private TableColumn<StudentPaymentTableModel, String> courseNumberTableColumn;

	@FXML
	private TableColumn<StudentPaymentTableModel, String> studentNumberTableColumn;

	@FXML
	private TableColumn<StudentPaymentTableModel, Timestamp> paymentDateTimeTableColumn;

	@FXML
	private TableColumn<StudentPaymentTableModel, String> studentClassTableColumn;

	@FXML
	private TableColumn<StudentPaymentTableModel, String> studentGradeTableColumn;

	private Stage stage;
	private File choosenPaymentReceiptFile;
	private PaymentTabUtil paymentTabUtil;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.coursesPaymentTabTableView.getColumns().setAll(Arrays.asList(this.coursePaymentTabTableColumn));
		loadAllCourseNames(this.coursesPaymentTabComboBox);

		this.studentNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
		this.studentClassTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
		this.paymentDateTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDateTime"));
		this.courseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		this.studentGradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentGrade"));
		this.studentPaymentTableView.getColumns()
				.setAll(Arrays.asList(this.studentNumberTableColumn, this.studentClassTableColumn,
						this.paymentDateTimeTableColumn, this.courseNumberTableColumn, this.studentGradeTableColumn));

		loadAllStudentPayment(this.studentPaymentTableView);

		paymentTabUtil = new PaymentTabUtil();
	}

	private void loadAllStudentPayment(TableView<StudentPaymentTableModel> studentPaymentTableView) {
		Configurator.doDBACtion(() -> {
			studentPaymentTableView.getItems().setAll(StudentPayment.findAll().stream()
					.map(x -> new StudentPaymentTableModel((long) x.getId(), (String) x.get("student_number"),
							(String) x.get("course_number"), (String) x.get("class"), (Timestamp) x.get("created_at"),
							new BigDecimal((long) x.get("payment_value")), (String) x.get("payment_receipt"),
							(String) x.get("grade")))
					.collect(Collectors.toList()));
		});
	}

	private void loadAllCourseNames(ComboBox<String> coursesPaymentComboBox) {
		Configurator.doDBACtion(() -> {
			coursesPaymentComboBox.getItems()
					.setAll(Course.findAll().stream().map(x -> (String) x.get("name")).collect(Collectors.toList()));
		});
	}

	@FXML
	public void handleDisplayingPaymentReceipt() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			GridPane root = (GridPane) fxmlLoader.load(Configurator.view("StudentPayment"));
			StudentPaymentController studentPaymentController = (StudentPaymentController) fxmlLoader.getController();
			StudentPaymentTableModel studentPayment = this.studentPaymentTableView.getSelectionModel()
					.getSelectedItem();
			studentPaymentController.setPaymentReceipt(studentPayment.getPaymentReceiptFilePath());
			studentPaymentController.setPaymentValue(studentPayment.getPaymentValue());
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSettingMenu() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Setting"));
			SettingController settingController = (SettingController) fxmlLoader.getController();
			settingController.setCoursesComboBox(this.coursesPaymentTabComboBox, this::loadAllCourseNames);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleTypingPatternStudentTab() {
		String pattern = this.studentPatternTabStudentTextField.getText();
		if (pattern != null) {
			Configurator.doDBACtion(() -> {
				StudentPayment.findAll().stream();
			});
		}
	}

	@FXML
	public void handleTypingNpm() {

		final String UNLA_IF_STUD_NUM_PATTERN = "4115505\\d{7}";
		String studentNumber = this.npmPaymentTabTextField.getText();
		if (studentNumber.matches(UNLA_IF_STUD_NUM_PATTERN)) {
			Configurator.doDBACtion(() -> {
				List<String> courses = paymentTabUtil.getCourses(studentNumber);
				if (courses != null) {
					coursesPaymentTabComboBox.getItems().clear();
					coursesPaymentTabComboBox.getItems().setAll(courses);
				}
			});
		}
	}

	@FXML
	public void handleSubmitPaymentButton() {
		final String studentNumber = this.npmPaymentTabTextField.getText();
		final ObservableList<CoursesTableModel> courseNames = this.coursesPaymentTabTableView.getItems();
		final File paymentReceipt = this.choosenPaymentReceiptFile;
		final String studentClass = this.classTabPaymentTextField.getText();
		final String paymentValue = this.paymentValueTabPaymentTextField.getText();
		if (studentNumber != null && courseNames.size() != 0 && paymentReceipt != null && studentClass != null
				&& paymentValue != null) {
			if (paymentValue.matches("\\d+")) {
				Configurator.doDBACtion(() -> {
					try {
						this.paymentTabUtil.save(studentNumber, studentClass, new BigDecimal(paymentValue), courseNames,
								paymentReceipt);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Disimpan");
				alert.setHeaderText("Data di simpan");
				alert.setContentText("Data pembayaran berhasil di simpan");
				alert.showAndWait();
				this.classTabPaymentTextField.clear();
				this.coursesPaymentTabTableView.getItems().clear();
				this.paymentReceiptPathTextField.clear();
				this.paymentValueTabPaymentTextField.clear();
				this.npmPaymentTabTextField.clear();
				this.coursesPaymentTabComboBox.getSelectionModel().clearSelection();
				this.loadAllCourseNames(this.coursesPaymentTabComboBox);
				this.loadAllStudentPayment(this.studentPaymentTableView);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Kesalahan !");
				alert.setHeaderText("Nilai pembayaran tidak valid!");
				alert.setContentText("Perhatian ! Harap isikan kolom pembayaran dengan data yang valid !");
				alert.showAndWait();
			}
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
		String selectedCourse = this.coursesPaymentTabComboBox.getValue();
		if (selectedCourse != null) {
			this.coursesPaymentTabTableView.getItems().add(new CoursesTableModel(selectedCourse));
			this.coursesPaymentTabComboBox.getItems().remove(selectedCourse);
		}
	}

	@FXML
	public void handleUpdatingGrade() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(Configurator.view("StudentGrading"));
			StudentGradingController studentGradingController = (StudentGradingController) fxmlLoader.getController();
			final StudentPaymentTableModel selectedStudentPayment = this.studentPaymentTableView.getSelectionModel()
					.getSelectedItem();
			studentGradingController.setGrade(selectedStudentPayment.getId(), selectedStudentPayment.getStudentGrade());
			studentGradingController.setStudentPaymentTableView(this.studentPaymentTableView,
					this::loadAllStudentPayment);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
