package livinglikelarry.lapgas.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;

import javafx.beans.property.SimpleStringProperty;
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

	private Stage stage;
	private File choosenPaymentReceiptFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory("course"));
		this.coursesPaymentTabTableView.getColumns().setAll(this.coursePaymentTabTableColumn);
		this.coursesPaymentTabComboBox.getItems().setAll("test test", "fsafsaf");
	}

	@FXML
	public void handleSubmitPaymentButton() {
		try {
			// do database stuff
			long id = -1;
			savePaymentReceiptToFS(this.choosenPaymentReceiptFile, id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleChoosingImageButton() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Gambar Struk Bank", "*.png", "*.jpg", "*.gif"));
		choosenPaymentReceiptFile = fileChooser.showOpenDialog(this.stage);
	}

	private void savePaymentReceiptToFS(File choosenFile, long id) throws IOException {
		if (choosenFile != null) {
			Path choosenPath = choosenFile.toPath();
			Path newPathName = Paths.get(id + "." + FilenameUtils.getExtension(choosenPath.toString()));
			Path targetPath = Paths.get(Configurator.PIC_PATH + newPathName);
			Files.copy(choosenPath, targetPath);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Bukti Wajib Ada");
			alert.setContentText("Bukti pembayaran harus disertakan");
			alert.setHeaderText("Perhatian !");
			alert.showAndWait();
		}

	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleChoosingCoursesComboBox() {
		String selectedCourse = this.coursesPaymentTabComboBox.getSelectionModel().getSelectedItem();
		// do removing from dbase
		////// code here
		//////

		if (selectedCourse != null) {
			this.coursesPaymentTabTableView.getItems().add(new Courses(selectedCourse));
			this.coursesPaymentTabComboBox.getItems().remove(selectedCourse);
		}

	}

}
