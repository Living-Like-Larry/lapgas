package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.Course;
import livinglikelarry.lapgas.model.CoursesTableModel;

public class SettingController implements Initializable {
	@FXML
	private TableView<CoursesTableModel> courseTableView;

	@FXML
	private TableColumn<CoursesTableModel, String> courseNumberTableColumn;

	@FXML
	private TableColumn<CoursesTableModel, String> courseTableColumn;

	@FXML
	private TableColumn<CoursesTableModel, Integer> semesterTableColumn;

	@FXML
	private TextField courseNumberTextField;

	@FXML
	private TextField courseTextField;

	@FXML
	private ComboBox<Integer> semesterComboBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.semesterComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9);

		this.courseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		this.courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.semesterTableColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));

		this.courseTableView.getColumns()
				.setAll(Arrays.asList(this.courseNumberTableColumn, this.courseTableColumn, this.semesterTableColumn));
		loadAllCourses();

	}

	private void loadAllCourses() {
		Configurator
				.doDBACtion(() -> this.courseTableView.getItems()
						.setAll(Course.findAll()
								.stream().map(x -> new CoursesTableModel((String) x.get("course_number"),
										(String) x.get("name"), (int) x.get("semester")))
						.collect(Collectors.toList())));
	}

	@FXML
	public void handleAddingButton() {
		final String courseNumber = this.courseNumberTextField.getText();
		final String name = this.courseTextField.getText();
		final Integer semester = this.semesterComboBox.getValue();
		if (courseNumber != null && name != null && semester != null) {
			Configurator.doDBACtion(() -> {
				Course course = new Course();
				course.set("course_number", courseNumber).set("name", name).set("semester", semester).insert();
			});
			this.courseTableView.getItems().add(new CoursesTableModel(courseNumber, name, semester));
			this.courseNumberTextField.clear();
			this.courseTextField.clear();
			this.semesterComboBox.getSelectionModel().clearSelection();
			loadAllCourses();
		}
	}
}
