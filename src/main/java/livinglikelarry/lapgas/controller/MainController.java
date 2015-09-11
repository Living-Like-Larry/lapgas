package livinglikelarry.lapgas.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import livinglikelarry.lapgas.Configurator;
import javafx.stage.Stage;

public class MainController implements Initializable {

	// model inner class for courses tableview
	public class Courses {
		private SimpleStringProperty course;

		public Courses(String course) {
			this.course = new SimpleStringProperty(course);
		}

		public final SimpleStringProperty courseProperty() {
			return this.course;
		}

		public final String getCourse() {
			return this.courseProperty().get();
		}

		public final void setCourse(final java.lang.String course) {
			this.courseProperty().set(course);
		}

	}

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

	private Stage stage;
	private File choosenPaymentReceiptFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.coursesPaymentTabTableView.getColumns().add(this.coursePaymentTabTableColumn);
		this.coursesPaymentTabComboBox.getItems().setAll("test test", "fsafsaf");
	}

	@FXML
	public void handleNpmTyping() {
		final String UNLA_IF_STUD_NUM_PATTERN = "4115505\\d{7}";
		String studentNumber = this.npmPaymentTabTextField.getText();
		if (studentNumber.matches(UNLA_IF_STUD_NUM_PATTERN)) {
			List<String> courses = PaymentTabUtil.getCourses(studentNumber);
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
				long id = saveToDatabase(this.npmPaymentTabTextField.getText(),
						this.coursesPaymentTabTableView.getItems());
				savePaymentReceiptToFS(this.choosenPaymentReceiptFile, id);
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

	private long saveToDatabase(String npm, ObservableList<Courses> courses) {
		return 0;
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

	private void savePaymentReceiptToFS(File choosenFile, long id) throws IOException {
		if (choosenFile != null) {
			Path choosenPath = choosenFile.toPath();
			Path newPathName = Paths.get(id + "." + FilenameUtils.getExtension(choosenPath.toString()));
			Path targetPath = Paths.get(Configurator.PIC_PATH + newPathName);
			Files.copy(choosenPath, targetPath);
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

}
