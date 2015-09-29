package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.javalite.activejdbc.Model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import livinglikelarry.lapgas.model.sql.AdminSql;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.LabAssistant;
import livinglikelarry.lapgas.model.table.CoursesTableModel;
import livinglikelarry.lapgas.model.table.LabAssistantAttendanceTableModel;
import livinglikelarry.lapgas.model.table.LabAssistantTableModel;
import livinglikelarry.lapgas.state.LapgasState;
import livinglikelarry.lapgas.util.Configurator;
import livinglikelarry.lapgas.util.GuiUtil;
import livinglikelarry.lapgas.util.LapgasPattern;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class SettingController implements Initializable {
	@FXML
	private TableView<CoursesTableModel> courseTableView;

	@FXML
	private Button scannerPathButton;

	@FXML
	private TextField scannerPathTextField;

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
	private TableColumn<LabAssistantTableModel, String> labAsstRoleTableColumn;

	@FXML
	private TextField labAsstStudentNumberTextField;

	@FXML
	private PasswordField adminPasswdField;

	@FXML
	private Button adminPasswdButton;

	@FXML
	private ComboBox<String> labAssistantRoleComboBox;

	@FXML
	private ComboBox<String> coursesPaymentComboBox;

	@FXML
	private Tab labAsstTab;

	@FXML
	private Tab scannerTab;

	@FXML
	private Tab courseTab;

	private Consumer<ComboBox<String>> coursePaymentComboBoxConsumer;

	private TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView;

	private Consumer<TableView<LabAssistantAttendanceTableModel>> labAsstAttendanceTableViewConsumer;

	private LapgasState lapgasState;

	private String labAsstStudentNumber;

	@FXML
	MenuItem addingAttendanceMenuItem;

	@FXML
	MenuItem seeingLogMenuItem;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			this.labAssistantRoleComboBox.getItems().setAll("Aslab", "Aslab Khusus");
			this.semesterComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);

			this.courseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
			this.courseTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
			this.semesterTableColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
			this.courseTableView.getColumns().setAll(
					Arrays.asList(this.courseNumberTableColumn, this.courseTableColumn, this.semesterTableColumn));

			this.labAsstStudentNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
			this.labAsstRoleTableColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
			this.labAssistantTableView.getColumns()
					.setAll(Arrays.asList(this.labAsstStudentNumberTableColumn, this.labAsstRoleTableColumn));

			loadAllCourses();
			loadAllLabAssistant();
			if (Configurator.getScannerPath() != null) {
				this.scannerPathTextField.setText(Configurator.getScannerPath());
			}

			Configurator.doAdminDBAction(() -> {
				Model admin = AdminSql.findById(1L);
				this.adminPasswdField.setText((String) admin.get("password"));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		final String courseNumber = this.courseNumberTextField.getText().toUpperCase();
		final String name = this.courseTextField.getText();
		final Integer semester = this.semesterComboBox.getValue();
		if (courseNumber != null && name != null && semester != null) {
			if (!courseNumber.equals("") && !name.equals("") && courseNumber.matches(LapgasPattern.COURSE_NUMBER)) {
				Configurator.doRawDBActionWithDBConsumer((x) -> {
					try {
						Course course = new Course();
						course.set("course_number", courseNumber).set("name", name).set("semester", semester).insert();
					} catch (Exception e) {
						GuiUtil.showException(e);
						x.close();
					}
				});
				this.courseTableView.getItems().add(new CoursesTableModel(courseNumber, name, semester));
				this.courseNumberTextField.clear();
				this.courseTextField.clear();
				this.semesterComboBox.getSelectionModel().clearSelection();
				loadAllCourses();
				this.coursePaymentComboBoxConsumer.accept(this.coursesPaymentComboBox);
			} else {
				GuiUtil.alertPattern("kode matakuliah", LapgasPattern.COURSE_NUMBER);
			}
		}
	}

	public void setCoursesComboBox(ComboBox<String> coursesPaymentComboBox,
			Consumer<ComboBox<String>> coursePaymentComboBoxConsumer) {
		this.coursesPaymentComboBox = coursesPaymentComboBox;
		this.coursePaymentComboBoxConsumer = coursePaymentComboBoxConsumer;
	}

	private void loadAllLabAssistant() {
		Configurator.doDBACtion(() -> {
			this.labAssistantTableView.getItems()
					.setAll(LabAssistant.findAll().stream()
							.map(x -> new LabAssistantTableModel((String) x.getId(), (String) x.get("role")))
							.collect(Collectors.toList()));
		});
	}

	@FXML
	public void handleAddingNewLabAssistant() {
		final String selectedItem = this.labAssistantRoleComboBox.getSelectionModel().getSelectedItem();
		final String studentNumber = this.labAsstStudentNumberTextField.getText();
		if (studentNumber != null && selectedItem != null) {
			if (!studentNumber.equals("") && studentNumber.matches(LapgasPattern.STUDENT_NUMBER)) {
				Configurator.doRawDBActionWithDBConsumer(x -> {
					try {
						new LabAssistant().set("student_number", (String) studentNumber)
								.set("password", (String) "livinglikelarry").set("role", (String) selectedItem)
								.insert();
					} catch (Exception e) {
						GuiUtil.showException(e);
						x.close();
					}
				});
				this.labAssistantRoleComboBox.getSelectionModel().clearSelection();
				this.labAsstStudentNumberTextField.clear();
				loadAllLabAssistant();
			} else {
				GuiUtil.alertPattern("npm", LapgasPattern.STUDENT_NUMBER);
			}
		}
	}

	@FXML
	public void handleEditingScannerPath() {

		try {
			if (this.scannerPathButton.getText().equals("Ganti")) {
				this.scannerPathTextField.setDisable(false);
				this.scannerPathButton.setText("Simpan");
			} else {
				String scannerPath = this.scannerPathTextField.getText();
				if (scannerPath != null) {
					if (!scannerPath.equals("")) {
						Configurator.setScannerPath(scannerPath);
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Lakukan Restart!");
						alert.setContentText(
								"Harap lakukan restart pada program,\nuntuk merasakan efek perubahan path");
						alert.showAndWait();
					}
				}
				this.scannerPathButton.setText("Ganti");
				this.scannerPathTextField.setDisable(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void editAdminPassword(Runnable runnable) {
		if (!this.adminPasswdField.getText().equals("")) {
			runnable.run();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Password baru kosong !");
			alert.showAndWait();
		}
	}

	private void setAdminField() {
		this.adminPasswdField.setText(this.adminPasswdField.getText());
		this.adminPasswdButton.setText("Ganti");
		this.adminPasswdField.setDisable(true);
	}

	@FXML
	public void handleEditingAdminPasswd() {
		if (this.adminPasswdButton.getText().equals("Ganti")) {
			this.adminPasswdField.setDisable(false);
			this.adminPasswdButton.setText("Simpan");
			this.adminPasswdField.clear();
		} else if (this.labAsstStudentNumber == null) {
			editAdminPassword(() -> {
				try {
					Configurator.doAdminDBAction(() -> {
						Model admin = AdminSql.findById(1l);
						admin.set("password", (String) this.adminPasswdField.getText()).saveIt();
						setAdminField();
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} else {
			editAdminPassword(() -> {
				Configurator.doDBACtion(() -> {
					Model model = LabAssistant.findById((String) labAsstStudentNumber);
					model.set("password", (String) adminPasswdField.getText()).saveIt();
					setAdminField();
				});
			});
		}
	}

	@FXML
	public void handleAddingNewAttendance() {
		try {
			String studentNumber = this.labAssistantTableView.getSelectionModel().getSelectedItem().getStudentNumber();
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(Configurator.view("AddingNewAttendance"));
			AddingNewAttendanceController addingNewAttendanceController = (AddingNewAttendanceController) fxmlLoader
					.getController();
			addingNewAttendanceController.setStudentNumber(studentNumber);
			addingNewAttendanceController.setLabAsstAttendanceTableView(this.labAssistantAttendanceTableView,
					this.labAsstAttendanceTableViewConsumer);
			Stage stage = new Stage();
			stage.setTitle("Tambah Kehadiran");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSeeingLabAsstLogs() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(Configurator.view("LabAsstLog"));
			LabAssistantLogController labAssistantLogController = (LabAssistantLogController) fxmlLoader
					.getController();
			labAssistantLogController.setLabAsstStudentNumber(
					this.labAssistantTableView.getSelectionModel().getSelectedItem().getStudentNumber());
			Stage stage = new Stage();
			stage.setTitle("Log Aslab");
			stage.setResizable(false);
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLabAsstAttendanceTableView(
			TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView,
			Consumer<TableView<LabAssistantAttendanceTableModel>> labAsstAttendanceTableViewConsumer) {
		this.labAssistantAttendanceTableView = labAssistantAttendanceTableView;
		this.labAsstAttendanceTableViewConsumer = labAsstAttendanceTableViewConsumer;
	}

	public void setLapgasState(LapgasState lapgasState) {
		this.lapgasState = lapgasState;
		this.lapgasState.setLabAsstTabState(this.labAsstTab);
		this.lapgasState.setCourseTabSetting(courseTab);
		this.lapgasState.setScannerTabSetting(scannerTab);
	}

	public void setLabAsstStudentNumber(String labAsstStudentNumber) {
		this.labAsstStudentNumber = labAsstStudentNumber;
	}

	@FXML
	public void handleLabAssistantTViewContextMenuReq() {
		if (this.labAssistantTableView.getItems().size() != 0) {
			if (this.labAssistantTableView.getSelectionModel().getSelectedItem().getRole()
					.equalsIgnoreCase("aslab khusus")) {
				this.seeingLogMenuItem.setVisible(true);
			} else {
				this.seeingLogMenuItem.setVisible(false);
			}
			this.addingAttendanceMenuItem.setVisible(true);

		} else {
			this.addingAttendanceMenuItem.setVisible(false);
			this.seeingLogMenuItem.setVisible(false);
		}
	}

}
