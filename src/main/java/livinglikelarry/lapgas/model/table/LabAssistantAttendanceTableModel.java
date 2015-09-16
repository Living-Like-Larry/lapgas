package livinglikelarry.lapgas.model.table;

import java.sql.Date;
import java.sql.Timestamp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class LabAssistantAttendanceTableModel {

	private SimpleStringProperty studentNumber;
	private SimpleObjectProperty<Timestamp> studentAttendance;

	public LabAssistantAttendanceTableModel(String studentNumber, Timestamp studentAttendance) {
		this.studentNumber = new SimpleStringProperty(studentNumber);
		this.studentAttendance = new SimpleObjectProperty<>(studentAttendance);
	}

	public Date getStudentAttendanceDate() {
		return Date.valueOf(this.studentAttendance.get().toLocalDateTime().toLocalDate());
	}

	public final SimpleObjectProperty<Timestamp> studentAttendanceProperty() {
		return this.studentAttendance;
	}

	public final Timestamp getStudentAttendance() {
		return this.studentAttendanceProperty().get();
	}

	public final void setStudentAttendance(final Timestamp studentAttendance) {
		this.studentAttendanceProperty().set(studentAttendance);
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
