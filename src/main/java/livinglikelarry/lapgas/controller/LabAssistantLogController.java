package livinglikelarry.lapgas.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.javalite.activejdbc.Model;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import livinglikelarry.lapgas.model.sql.LabAssistantLog;
import livinglikelarry.lapgas.util.Configurator;
import livinglikelarry.lapgas.util.LabAssistantLogger;

public class LabAssistantLogController {

	@FXML
	private TextArea labAsstLogTextArea;

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private DatePicker toDatePicker;

	private String studentNumber;

	public void setLabAsstStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
		this.labAsstLogTextArea.setText(LabAssistantLogger.getAllLog(this.studentNumber));
	}

	@FXML
	public void handleFilteringLogByDate() {
		Configurator.doDBACtion(() -> {
			if (this.fromDatePicker.getValue() != null && this.toDatePicker.getValue() != null) {
				final List<Model> filteredLog = LabAssistantLog.findAll().stream().filter(x -> {
					Timestamp createdAt = (Timestamp) x.get("created_at");
					LocalDate createdAtLocalDate = createdAt.toLocalDateTime().toLocalDate();
					LocalDate fromLocalDate = fromDatePicker.getValue();
					LocalDate toLocalDate = toDatePicker.getValue();
					return (createdAtLocalDate.isEqual(fromLocalDate) || createdAtLocalDate.isAfter(fromLocalDate))
							&& (createdAtLocalDate.isEqual(toLocalDate) || createdAtLocalDate.isBefore(toLocalDate));
				}).collect(Collectors.toList());
				this.labAsstLogTextArea.setText(
						filteredLog.stream().map(x -> (String) x.get("log")).collect(Collectors.joining("\n\n")));
			}
		});
	}

	@FXML
	public void handleClearingFiltering() {
		this.fromDatePicker.setValue(null);
		this.toDatePicker.setValue(null);
		Configurator.doDBACtion(() -> labAsstLogTextArea.setText(LabAssistantLog.findAll().stream()
				.map(x -> (String) x.get("log")).collect(Collectors.joining("\n\n"))));
	}
}
