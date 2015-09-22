package livinglikelarry.lapgas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import livinglikelarry.lapgas.util.LabAssistantLogger;

public class LabAssistantLogController {
	
	@FXML
	private TextArea labAsstLogTextArea;
	
	private String studentNumber;

	public void setLabAsstStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
		this.labAsstLogTextArea.setText(LabAssistantLogger.getAllLog(this.studentNumber));
	}

}
