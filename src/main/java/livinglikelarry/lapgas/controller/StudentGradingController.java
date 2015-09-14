package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class StudentGradingController implements Initializable {

	@FXML
	private ComboBox<String> gradeComboBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.gradeComboBox.getItems().setAll("A", "B", "C", "D", "E", "T");
	}

	public void setGrade(String grade) {
		this.gradeComboBox.setValue(grade);
	}

}
