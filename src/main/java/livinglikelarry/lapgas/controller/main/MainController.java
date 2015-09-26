package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.controlsfx.control.textfield.TextFields;
import org.javalite.activejdbc.Model;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import livinglikelarry.lapgas.controller.SettingController;
import livinglikelarry.lapgas.controller.StudentPaymentController;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.LabAssistant;
import livinglikelarry.lapgas.model.sql.LabAssistantAttendance;
import livinglikelarry.lapgas.model.sql.StudentPayment;
import livinglikelarry.lapgas.model.table.CoursesTableModel;
import livinglikelarry.lapgas.model.table.LabAssistantAttendanceTableModel;
import livinglikelarry.lapgas.model.table.StudentPaymentTableModel;
import livinglikelarry.lapgas.resource.view.Templates;
import livinglikelarry.lapgas.state.LapgasState;
import livinglikelarry.lapgas.util.Configurator;
import livinglikelarry.lapgas.util.LabAssistantLogger;
import livinglikelarry.lapgas.util.GuiUtil;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import javafx.stage.Stage;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class MainController implements Initializable {

	@FXML
	private TableView<CoursesTableModel> coursesPaymentTabTableView;

	@FXML
	private TableColumn<CoursesTableModel, String> coursePaymentTabTableColumn;

	@FXML
	private TableColumn<CoursesTableModel, String> paymentValueTableColumn;

	@FXML
	private TextField studentNumberPaymentTabTextField;

	@FXML
	private ComboBox<String> coursesPaymentTabComboBox;

	@FXML
	private TextField paymentReceiptPathTextField;

	@FXML
	private Button reportModeButton;

	@FXML
	private TextField classTabPaymentTextField;

	@FXML
	private TextField coursePaymentValue;

	@FXML
	private TextField paymentValueTabPaymentTextField;

	@FXML
	private TextField studentNumberFilteredTabStudent;

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

	@FXML
	private DatePicker filteredStudentPaymentDatePicker;

	@FXML
	private TextField studentNumberAsstTabTextField;

	@FXML
	private ComboBox<Integer> filteredStudentPaymentBySemesterComboBox;

	@FXML
	private ComboBox<String> studentClassTabStudentComboBox;

	@FXML
	private ComboBox<String> filteredCourseNameComboBox;

	@FXML
	private ComboBox<String> filteredAndAddedComboBox;

	@FXML
	private TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView;

	@FXML
	private TableColumn<LabAssistantAttendanceTableModel, String> studentNumberLabAssistantTableColumn;

	@FXML
	private TableColumn<LabAssistantAttendanceTableModel, Timestamp> studentAttendanceLabAsstTableColumn;

	@FXML
	private DatePicker labAsstAttendanceDatePicker;

	@FXML
	private Tab paymentTab;

	@FXML
	private MenuItem settingMenuItem;

	@FXML
	private MenuItem studentPaymentUpdatingMenuItem;

	@FXML
	private MenuButton labAsstActionMenuButton;

	@FXML
	private Button studentPaymentDatePickerModeButton;

	@FXML
	private Text studentPaymentUntilText;

	@FXML
	private DatePicker studentPaymentUntilDatePicker;

	@FXML
	private DatePicker labAsstUntilPartDatePicker;

	@FXML
	MenuItem seeingPaymentReceipt;

	private Stage stage;
	private File choosenPaymentReceiptFile;
	private PaymentTabUtil paymentTabUtil;

	private ArrayList<StudentPaymentTableModel> noFilteredStudentPaymentList;

	private ArrayList<LabAssistantAttendanceTableModel> noFilteredLabAsstAttendance;

	private LinkedList<List<StudentPaymentTableModel>> filteredStudentPaymentHistoryList;

	private LapgasState lapgasState;

	private String labAsstStudentNumber;

	private static final String UNLA_IF_STUD_NUM_PATTERN = "4115505\\d{7}";

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initCourseNameTableViewPaymentTab();
		loadAllCourseNames(this.coursesPaymentTabComboBox);

		this.paymentTabUtil = new PaymentTabUtil();
		this.filteredStudentPaymentHistoryList = new LinkedList<>();
		initStudentPaymentTableView();
		loadAllStudentPayment(this.studentPaymentTableView);

		this.filteredAndAddedComboBox.getItems().setAll("absen!", "filter");

		this.filteredStudentPaymentBySemesterComboBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8);

		initLabAsstAttendanceTableView();
		loadAllLabAsstAttendances(this.labAssistantAttendanceTableView);
	}

	private void initLabAsstAttendanceTableView() {
		this.studentNumberLabAssistantTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
		this.studentAttendanceLabAsstTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentAttendance"));

		this.labAssistantAttendanceTableView.getColumns().setAll(
				Arrays.asList(this.studentNumberLabAssistantTableColumn, this.studentAttendanceLabAsstTableColumn));
	}

	private void initStudentPaymentTableView() {
		this.studentNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
		this.studentClassTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
		this.paymentDateTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDateTime"));
		this.courseNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		this.studentGradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentGrade"));
		this.studentPaymentTableView.getColumns()
				.setAll(Arrays.asList(this.studentNumberTableColumn, this.studentClassTableColumn,
						this.paymentDateTimeTableColumn, this.courseNumberTableColumn, this.studentGradeTableColumn));
	}

	private void initCourseNameTableViewPaymentTab() {
		this.coursePaymentTabTableColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
		this.paymentValueTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentValue"));
		this.coursesPaymentTabTableView.getColumns()
				.setAll(Arrays.asList(this.coursePaymentTabTableColumn, this.paymentValueTableColumn));
	}

	private void implementAutoCompleteOnPaymentFeature() {
		final ObservableList<StudentPaymentTableModel> studentPayment = this.studentPaymentTableView.getItems();
		final Function<Function<StudentPaymentTableModel, ?>, List<?>> studentPaymentsMapper = x -> studentPayment
				.stream().map(x).distinct().collect(Collectors.toList());
		final List<?> mapToStudentNumber = studentPaymentsMapper.apply(x -> x.getStudentNumber());
		TextFields.bindAutoCompletion(studentNumberPaymentTabTextField, mapToStudentNumber);
		TextFields.bindAutoCompletion(this.studentNumberFilteredTabStudent, mapToStudentNumber);
		TextFields.bindAutoCompletion(this.classTabPaymentTextField,
				studentPaymentsMapper.apply(x -> x.getStudentClass()));
		TextFields.bindAutoCompletion(this.coursePaymentValue,
				studentPaymentsMapper.apply(x -> x.getPaymentValue().toString()));
	}

	private void loadAllStudentPayment(TableView<StudentPaymentTableModel> studentPaymentTableView) {
		Configurator.doDBACtion(() -> {
			studentPaymentTableView.getItems().setAll(StudentPayment.findAll().stream().map(x -> {
				Model course = Course.findById((String) x.get("course_number"));
				return new StudentPaymentTableModel((long) x.getId(), (String) x.get("student_number"),
						(String) x.get("course_number"), (String) x.get("class"), (Timestamp) x.get("created_at"),
						new BigDecimal((long) x.get("payment_value")), (String) x.get("payment_receipt"),
						(String) x.get("grade"), (String) course.get("name"));
			}).collect(Collectors.toList()));
			this.studentClassTabStudentComboBox.getItems().setAll(this.studentPaymentTableView.getItems().stream()
					.map(x -> x.getStudentClass()).distinct().collect(Collectors.toList()));
			this.filteredCourseNameComboBox.getItems().setAll(this.studentPaymentTableView.getItems().stream()
					.map(x -> x.getCourseName()).distinct().collect(Collectors.toList()));
		});
		this.noFilteredStudentPaymentList = new ArrayList<>(this.studentPaymentTableView.getItems());
		implementAutoCompleteOnPaymentFeature();
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
			stage.setTitle("Struk");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleStudentPaymentClearModeButton() {
		this.studentPaymentTableView.getItems().setAll(new ArrayList<>(this.noFilteredStudentPaymentList));
		this.studentNumberFilteredTabStudent.clear();
		this.filteredCourseNameComboBox.getSelectionModel().clearSelection();
		this.filteredStudentPaymentBySemesterComboBox.getSelectionModel().clearSelection();
		this.filteredStudentPaymentDatePicker.setValue(null);
		this.studentPaymentUntilDatePicker.setValue(null);
		this.studentClassTabStudentComboBox.getSelectionModel().clearSelection();
	}

	@FXML
	public void handleChoosingUntilPartStudentPaymentDP() {
		if (this.studentPaymentUntilDatePicker.getValue() != null
				&& this.filteredStudentPaymentDatePicker.getValue() != null) {
			this.studentPaymentTableView.getItems()
					.setAll(this.studentPaymentTableView.getItems().stream().filter(x -> {
						LocalDate studentPaymentLocalDate = x.getPaymentDate().toLocalDate();
						LocalDate fromLocalDate = this.filteredStudentPaymentDatePicker.getValue();
						LocalDate toLocalDate = this.studentPaymentUntilDatePicker.getValue();
						return ((studentPaymentLocalDate.isAfter(fromLocalDate)
								|| studentPaymentLocalDate.isEqual(fromLocalDate))
								&& (studentPaymentLocalDate.isBefore(toLocalDate)
										|| studentPaymentLocalDate.isEqual(toLocalDate)));
					}).collect(Collectors.toList()));
		}
	}

	@FXML
	public void handleFilteringByStudentNumber() {
		final String filteredStudentNumber = this.studentNumberFilteredTabStudent.getText();

		this.studentPaymentTableView.getItems().setAll(new ArrayList<>(this.noFilteredStudentPaymentList));
		this.studentPaymentTableView.getItems()
				.setAll(this.noFilteredStudentPaymentList.stream()
						.filter(x -> x.getStudentNumber().matches(filteredStudentNumber + "\\d*"))
						.collect(Collectors.toList()));
		this.filteredStudentPaymentHistoryList.push(new ArrayList<>(this.studentPaymentTableView.getItems()));
	}

	@FXML
	public void handlePopFilteredHistory() {
		if (!this.filteredStudentPaymentHistoryList.isEmpty()) {
			this.studentPaymentTableView.getItems()
					.setAll(new ArrayList<>(this.filteredStudentPaymentHistoryList.pop()));
		}
	}

	private void filterBasedOn(Predicate<StudentPaymentTableModel> predicate) {
		this.studentPaymentTableView.getItems().setAll(
				this.studentPaymentTableView.getItems().stream().filter(predicate).collect(Collectors.toList()));
	}

	private void doFiltering(Runnable filteringRunnable) {
		this.filteredStudentPaymentHistoryList.push(new ArrayList<>(this.studentPaymentTableView.getItems()));
		filteringRunnable.run();
		System.out.println("last filtered item : " + this.filteredStudentPaymentHistoryList.peekLast().stream()
				.map(x -> String.valueOf(x.getId())).collect(Collectors.joining(" ")));
	}

	@FXML
	public void handleFilteringByClass() {
		doFiltering(
				() -> filterBasedOn(x -> x.getStudentClass().equals(this.studentClassTabStudentComboBox.getValue())));
	}

	@FXML
	public void handleFilteringBySemester() {
		doFiltering(() -> {
			Configurator.doDBACtion(() -> {
				Stream<StudentPaymentTableModel> stream = studentPaymentTableView.getItems().stream().filter(p -> {
					Model course = Course.findById(p.getCourseNumber());
					return this.filteredStudentPaymentBySemesterComboBox.getValue() == ((int) course.get("semester"));
				});
				this.studentPaymentTableView.getItems().setAll(stream.collect(Collectors.toList()));
			});
		});
	}

	@FXML
	public void handleSettingMenu() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			GridPane root = (GridPane) fxmlLoader.load(Configurator.view("Setting"));
			SettingController settingController = (SettingController) fxmlLoader.getController();
			settingController.setLabAsstAttendanceTableView(this.labAssistantAttendanceTableView,
					this::loadAllLabAsstAttendances);
			settingController.setCoursesComboBox(this.coursesPaymentTabComboBox, this::loadAllCourseNames);
			settingController.setLapgasState(this.lapgasState);
			settingController.setLabAsstStudentNumber(this.labAsstStudentNumber);
			Stage stage = new Stage();
			stage.setTitle("Setting");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleTypingStudentNumberOnPaymentTab() {
		implementAutoCompleteOnPaymentFeature();
		String studentNumber = this.studentNumberPaymentTabTextField.getText();
		if (studentNumber.matches(MainController.UNLA_IF_STUD_NUM_PATTERN)) {
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
		final String studentNumber = this.studentNumberPaymentTabTextField.getText();
		final ObservableList<CoursesTableModel> courseNames = this.coursesPaymentTabTableView.getItems();
		final File paymentReceipt = this.choosenPaymentReceiptFile;
		final String studentClass = this.classTabPaymentTextField.getText().toUpperCase();
		if (studentNumber != null && courseNames.size() != 0 && paymentReceipt != null && studentClass != null) {
			Configurator.doDBACtion(() -> {
				try {
					this.paymentTabUtil.save(studentNumber, studentClass, courseNames, paymentReceipt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if (this.labAsstStudentNumber != null) {
				LabAssistantLogger.logNewStudentPayment(this.labAsstStudentNumber, studentNumber, courseNames,
						paymentReceipt, studentClass);
			}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Disimpan");
			alert.setHeaderText("Data di simpan");
			alert.setContentText("Data pembayaran berhasil di simpan");
			alert.showAndWait();
			this.classTabPaymentTextField.clear();
			this.coursesPaymentTabTableView.getItems().clear();
			this.paymentReceiptPathTextField.clear();
			this.coursePaymentValue.clear();
			this.studentNumberPaymentTabTextField.clear();
			this.coursesPaymentTabComboBox.getSelectionModel().clearSelection();
			this.loadAllCourseNames(this.coursesPaymentTabComboBox);
			this.loadAllStudentPayment(this.studentPaymentTableView);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Kesalahan !");
			alert.setHeaderText("Ada Kolom Kosong !");
			alert.setContentText("Perhatian ! Tidak boleh ada satupun kolom yang di kosongkan !");
			alert.showAndWait();
		}
	}

	@FXML
	public void handleFilteringByCourseName() {
		doFiltering(() -> filterBasedOn(x -> x.getCourseName().equals(filteredCourseNameComboBox.getValue())));
	}

	@FXML
	public void handleFilteringLabAsstByAttendance() {
		loadAllLabAsstAttendances(this.labAssistantAttendanceTableView);
		if (this.labAsstAttendanceDatePicker.getValue() != null && this.labAsstUntilPartDatePicker.getValue() != null) {
			System.out.println("filtering attendance");
			final List<LabAssistantAttendanceTableModel> filteredAttendance = this.labAssistantAttendanceTableView
					.getItems().stream().filter(x -> {
						final LocalDate labAssistantAttendance = x.getStudentAttendanceDate().toLocalDate();
						final LocalDate fromLabAsstAttendanceFiltering = this.labAsstAttendanceDatePicker.getValue();
						final LocalDate toLabAsstAttendanceFiltering = this.labAsstUntilPartDatePicker.getValue();
						return (labAssistantAttendance.isEqual(fromLabAsstAttendanceFiltering)
								|| labAssistantAttendance.isAfter(fromLabAsstAttendanceFiltering))
								&& (labAssistantAttendance.isEqual(toLabAsstAttendanceFiltering)
										|| labAssistantAttendance.isBefore(toLabAsstAttendanceFiltering));

					}).collect(Collectors.toList());
			this.labAssistantAttendanceTableView.getItems().setAll(filteredAttendance);

		}
	}

	@FXML
	public void handleClearingFilteringLabAsstAtt() {
		this.labAssistantAttendanceTableView.getItems().setAll(this.noFilteredLabAsstAttendance);
		this.labAsstAttendanceDatePicker.setValue(null);
		this.labAsstUntilPartDatePicker.setValue(null);
		this.studentNumberAsstTabTextField.clear();
		System.out.println("clearing");
	}

	@FXML
	public void handleTypingStudentNumberOnLabAsstTab() {
		Configurator.doDBACtion(() -> {
			TextFields.bindAutoCompletion(this.studentNumberAsstTabTextField,
					LabAssistant.findAll().stream().map(x -> x.get("student_number")).collect(Collectors.toList()));
		});
		final String selectedMode = this.filteredAndAddedComboBox.getSelectionModel().getSelectedItem();
		final String studentNumber = this.studentNumberAsstTabTextField.getText();
		if (selectedMode != null) {
			if (selectedMode.equals("filter")) {

				this.labAssistantAttendanceTableView.getItems()
						.setAll(new ArrayList<>(this.noFilteredLabAsstAttendance));
				this.labAssistantAttendanceTableView.getItems()
						.setAll(this.noFilteredLabAsstAttendance.stream()
								.filter(x -> x.getStudentNumber().matches(studentNumber + "\\d*"))
								.collect(Collectors.toList()));
			} else
				if (studentNumber.matches(MainController.UNLA_IF_STUD_NUM_PATTERN) && selectedMode.equals("absen!")) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Anda yakin npm ini yang dimaksud ? npm -> " + studentNumber);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().getText().equalsIgnoreCase("ok")) {
					this.studentNumberAsstTabTextField.clear();
					if (studentNumber.matches(MainController.UNLA_IF_STUD_NUM_PATTERN)) {
						Configurator.doRawDBActionWithDBConsumer((x) -> {
							Model labAssistant = LabAssistant.findById((String) studentNumber);
							if (labAssistant != null) {
								x.close();
								makeLabAsstAttendance(studentNumber);
							} else {
								Alert alertNotLabAsst = new Alert(AlertType.WARNING);
								alertNotLabAsst.setContentText("npm ini tidak terdaftar sebagai ASLAB!");
								alertNotLabAsst.showAndWait();
							}
						});
						loadAllLabAsstAttendances(this.labAssistantAttendanceTableView);
						this.studentNumberAsstTabTextField.clear();
					}
				}

			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("pilih salah satu Mode!");
			alert.showAndWait();
		}
	}

	private void makeLabAsstAttendance(final String studentNumber) {
		Configurator.doDBACtion(() -> {
			LabAssistantAttendance labAssistantAttendance = new LabAssistantAttendance();
			labAssistantAttendance.set("student_number", studentNumber).saveIt();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("berhasil melakukan absensi!");
			alert.showAndWait();
		});
	}

	private void loadAllLabAsstAttendances(
			TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView) {
		Configurator.doDBACtion(() -> {
			labAssistantAttendanceTableView.getItems()
					.setAll(LabAssistantAttendance.findAll().stream()
							.map(x -> new LabAssistantAttendanceTableModel((String) x.get("student_number"),
									(Timestamp) x.get("created_at")))
							.collect(Collectors.toList()));
		});
		this.noFilteredLabAsstAttendance = new ArrayList<>(this.labAssistantAttendanceTableView.getItems());

	}

	@FXML
	public void handleReportingButton() {
		GuiUtil.showProgress(() -> {
			Configurator.doDBACtion(() -> {
				try {
					Collection<?> studentPayment = studentPaymentTableView.getItems().stream().map(x -> {
						Model course = Course.findById(x.getCourseNumber());
						int studentSemester = (int) course.get("semester");
						String courseName = (String) course.get("name");
						return new StudentPaymentTableModel(x.getId(), x.getStudentNumber(), x.getCourseNumber(),
								x.getStudentClass(), x.getPaymentDateTime(), x.getPaymentValue(),
								x.getPaymentReceiptFilePath(), x.getStudentGrade(), studentSemester, courseName);
					}).collect(Collectors.toList());

					final TextColumnBuilder<BigDecimal> paymentAmountColumn = DynamicReports.col
							.column("jumlah bayar", "paymentValue", DynamicReports.type.bigDecimalType())
							.setValueFormatter(Templates.createCurrencyValueFormatter("")).setWidth(20);
					AggregationSubtotalBuilder<BigDecimal> paymentAmountSum = DynamicReports.sbt
							.sum(paymentAmountColumn).setLabel("total ")
							.setValueFormatter(Templates.createCurrencyValueFormatter(""));

					GuiUtil.showReport(
							DynamicReports.report().setTemplate(Templates.reportTemplate)
									.title(Templates.createTitleComponent("Praktek Mahasiswa"))
									.pageFooter(Templates.footerComponent)
									.columns(DynamicReports.col.reportRowNumberColumn("No.").setWidth(12),
											DynamicReports.col
													.column("npm", "studentNumber", DynamicReports.type.stringType())
													.setWidth(20),
											DynamicReports.col
													.column("matakuliah", "courseName",
															DynamicReports.type.stringType())
													.setWidth(
															15),
											DynamicReports.col
													.column("nilai", "studentGrade", DynamicReports.type.stringType())
													.setWidth(
															8),
											paymentAmountColumn,
											DynamicReports.col.column("waktu membayar", "paymentDate",
													DynamicReports.type.dateType()).setPattern("dd/MM/yyyy").setWidth(
															15),
											DynamicReports.col
													.column("Kel", "studentClass", DynamicReports.type.stringType())
													.setWidth(5),
									DynamicReports.col
											.column("sem", "studentSemester", DynamicReports.type.integerType())
											.setWidth(5))
									.subtotalsAtSummary(paymentAmountSum).setDataSource(studentPayment).toJasperPrint(),
							"Laporan Pembayaran Praktek");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});

	}

	@FXML
	public void handleDeletingChoosenCourseName() {
		int selectedIndex = this.coursesPaymentTabTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			this.coursesPaymentTabTableView.getItems().remove(selectedIndex);
		}
	}

	@FXML
	public void handleAboutButton() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(Configurator.view("About"));
			Stage stage = new Stage();
			stage.setTitle("About");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.showAndWait();
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
	public void handleReportingLabAsstAttendance() {
		GuiUtil.showProgress(() -> {
			try {
				GuiUtil.showReport(DynamicReports.report().setTemplate(Templates.reportTemplate)
						.title(Templates.createTitleComponent("Absensi Asisten Lab"))
						.pageFooter(Templates.footerComponent)
						.columns(DynamicReports.col.reportRowNumberColumn("No."),
								DynamicReports.col.column("NPM", "studentNumber", DynamicReports.type.stringType()),
								DynamicReports.col
										.column("tgl hadir", "studentAttendanceDate", DynamicReports.type.dateType())
										.setPattern("dd/MM/yyyy"))
						.setDataSource(
								this.labAssistantAttendanceTableView.getItems().stream().collect(Collectors.toList()))
						.toJasperPrint(), "Absensi Aslab");
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	@FXML
	public void handleChoosingCourses() {
		String selectedCourse = this.coursesPaymentTabComboBox.getSelectionModel().getSelectedItem();
		String coursePayment = this.coursePaymentValue.getText();
		if (selectedCourse != null && coursePayment != null) {
			if (coursePayment.matches("\\d+")) {
				this.coursesPaymentTabTableView.getItems()
						.add(new CoursesTableModel(selectedCourse, new BigDecimal(coursePayment)));
				this.coursesPaymentTabComboBox.getItems().remove(selectedCourse);
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Bukan angka !");
				alert.setContentText("Yang anda masukan bukan angka !");
				alert.showAndWait();
				this.coursePaymentValue.clear();
			}
		}
	}

	
	@FXML
	public void handleUpdatingStudentPayment() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			AnchorPane root = (AnchorPane) fxmlLoader.load(Configurator.view("StudentPaymentUpdater"));
			StudentPaymentUpdaterController studentPaymentUpdaterController = (StudentPaymentUpdaterController) fxmlLoader
					.getController();
			studentPaymentUpdaterController
					.setStudentPayment(this.studentPaymentTableView.getSelectionModel().getSelectedItem());
			studentPaymentUpdaterController
					.setStudentPaymentList(new ArrayList<>(this.studentPaymentTableView.getItems()));
			Stage stage = new Stage();
			studentPaymentUpdaterController.setStage(stage);
			stage.setScene(new Scene(root));
			stage.setTitle("Perbaharui Pembayaran Mahasiswa");
			stage.setResizable(false);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleAddLabAsstAttendance() {
		makeLabAsstAttendance(this.labAsstStudentNumber);
		loadAllLabAsstAttendances(this.labAssistantAttendanceTableView);
	}

	public void setLapgasState(LapgasState lapgasState) {
		this.lapgasState = lapgasState;
		this.lapgasState.setPaymentTabState(this.paymentTab);
		this.lapgasState.setUpdatingStudentPaymentStateMI(this.studentPaymentUpdatingMenuItem);
		this.lapgasState.setLabAsstComboBoxMode(this.filteredAndAddedComboBox);
		this.lapgasState.setLabAsstActionMenuButton(this.labAsstActionMenuButton);
	}

	public void setLabAsstStudentNumber(String labAsstStudentNumber) {
		this.labAsstStudentNumber = labAsstStudentNumber;
	}

	@FXML
	public void handleStudentPaymentTViewContextMenuReq() {
		if (this.studentPaymentTableView.getItems().size() != 0) {
			this.lapgasState.setUpdatingStudentPaymentStateMI(this.studentPaymentUpdatingMenuItem);
			this.seeingPaymentReceipt.setDisable(false);
		} else {
			this.studentPaymentUpdatingMenuItem.setDisable(true);
			this.seeingPaymentReceipt.setDisable(true);
		}
	}
}
