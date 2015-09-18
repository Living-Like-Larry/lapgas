package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.LabAssistant;
import livinglikelarry.lapgas.model.table.CoursesTableModel;
import livinglikelarry.lapgas.model.table.LabAssistantTableModel;

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

	@FXML
	private TableView<LabAssistantTableModel> labAssistantTableView;

	@FXML
	private TableColumn<LabAssistantTableModel, String> labAsstStudentNumberTableColumn;

	@FXML
	private TextField labAsstStudentNumberTextField;

	private ComboBox<String> coursesPaymentComboBox;

	private Consumer<ComboBox<String>> coursePaymentComboBoxConsumer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.semesterComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

		this.courseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		this.courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.semesterTableColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
		this.courseTableView.getColumns()
				.setAll(Arrays.asList(this.courseNumberTableColumn, this.courseTableColumn, this.semesterTableColumn));

		this.labAsstStudentNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
		this.labAssistantTableView.getColumns().setAll(Arrays.asList(this.labAsstStudentNumberTableColumn));

		loadAllCourses();
		loadAllLabAssistant();

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
			this.coursePaymentComboBoxConsumer.accept(this.coursesPaymentComboBox);
		}
	}

	public void setCoursesComboBox(ComboBox<String> coursesPaymentComboBox,
			Consumer<ComboBox<String>> coursePaymentComboBoxConsumer) {
		this.coursesPaymentComboBox = coursesPaymentComboBox;
		this.coursePaymentComboBoxConsumer = coursePaymentComboBoxConsumer;
	}

	private void loadAllLabAssistant() {
		Configurator.doDBACtion(() -> {
			this.labAssistantTableView.getItems().setAll(LabAssistant.findAll().stream()
					.map(x -> new LabAssistantTableModel((String) x.getId())).collect(Collectors.toList()));
		});
	}

	@FXML
	public void handleAddingNewLabAssistant() {
		Configurator.doDBACtion(() -> {
			new LabAssistant().set("student_number", (String) this.labAsstStudentNumberTextField.getText()).insert();
		});
		loadAllLabAssistant();
	}
}
