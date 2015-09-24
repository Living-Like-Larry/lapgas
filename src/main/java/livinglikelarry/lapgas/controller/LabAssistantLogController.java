package livinglikelarry.lapgas.controller;

import java.util.stream.Collectors;

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
		if (this.fromDatePicker.getValue() != null && this.toDatePicker.getValue() != null) {
			this.labAsstLogTextArea.setText(LabAssistantLogger.getLogByDate(this.studentNumber,
					this.fromDatePicker.getValue(), this.toDatePicker.getValue()));
		}
	}

	@FXML
	public void handleClearingFiltering() {
		this.fromDatePicker.setValue(null);
		this.toDatePicker.setValue(null);
		Configurator.doDBACtion(() -> labAsstLogTextArea.setText(LabAssistantLog.findAll().stream()
				.map(x -> (String) x.get("log")).collect(Collectors.joining("\n\n"))));
	}
}
