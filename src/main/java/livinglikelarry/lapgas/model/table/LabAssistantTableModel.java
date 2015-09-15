package livinglikelarry.lapgas.model.table;

import javafx.beans.property.SimpleStringProperty;

public class LabAssistantTableModel {
	private SimpleStringProperty studentNumber;

	public LabAssistantTableModel(String studentNumber) {
		this.studentNumber = new SimpleStringProperty(studentNumber);
	}

	public final SimpleStringProperty studentNumberProperty() {
		return this.studentNumber;
	}

	public final String getStudentNumber() {
		return this.studentNumberProperty().get();
	}

	public final void setStudentNumber(final String studentNumber) {
		this.studentNumberProperty().set(studentNumber);
	}

}
