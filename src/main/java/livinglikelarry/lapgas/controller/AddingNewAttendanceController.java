package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import livinglikelarry.lapgas.model.sql.LabAssistantAttendance;
import livinglikelarry.lapgas.model.table.LabAssistantAttendanceTableModel;
import livinglikelarry.lapgas.util.Configurator;

public class AddingNewAttendanceController implements Initializable {

	@FXML
	private DatePicker newAttendanceDatePicker;
	@FXML
	private TableView<LabAssistantAttendanceTableModel> labAssistantNewAttendanceTableView;
	@FXML
	private TableColumn<LabAssistantAttendanceTableModel, Timestamp> studentAttendanceTableColumn;
	@FXML
	private Text studentNumberText;

	private String studentNumber;
	private TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView;
	private Consumer<TableView<LabAssistantAttendanceTableModel>> labAsstAttendanceTableViewConsumer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.studentAttendanceTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentAttendance"));
		this.labAssistantNewAttendanceTableView.getColumns().setAll(Arrays.asList(this.studentAttendanceTableColumn));
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
		this.studentNumberText.setText(this.studentNumber);
	}

	@FXML
	public void handleAddingNewAttendance() {
		LocalDate localDate = this.newAttendanceDatePicker.getValue();
		this.labAssistantNewAttendanceTableView.getItems().add(new LabAssistantAttendanceTableModel(this.studentNumber,
				Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIN))));
	}
	
	@FXML
	public void handleSubmitButton() {
		Configurator.doDBACtion(() -> {
			this.labAssistantNewAttendanceTableView.getItems().forEach(x -> {
				LabAssistantAttendance labAssistantAttendance = new LabAssistantAttendance();
				labAssistantAttendance.manageTime(false);
				labAssistantAttendance.set("student_number", (String)x.getStudentNumber());
				labAssistantAttendance.set("created_at", (Timestamp)x.getStudentAttendance());
				labAssistantAttendance.saveIt();
			});
		});
		this.labAssistantNewAttendanceTableView.getItems().clear();
		this.labAsstAttendanceTableViewConsumer.accept(this.labAssistantAttendanceTableView);
	}

	public void setLabAsstAttendanceTableView(
			TableView<LabAssistantAttendanceTableModel> labAssistantAttendanceTableView,
			Consumer<TableView<LabAssistantAttendanceTableModel>> labAsstAttendanceTableViewConsumer) {
		this.labAssistantAttendanceTableView = labAssistantAttendanceTableView;
		this.labAsstAttendanceTableViewConsumer = labAsstAttendanceTableViewConsumer;
	}

}
