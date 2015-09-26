package livinglikelarry.lapgas.model.table;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class LabAssistantTableModel {
	private SimpleStringProperty studentNumber;
	private SimpleStringProperty role;

	public LabAssistantTableModel(String studentNumber, String role) {
		this.studentNumber = new SimpleStringProperty(studentNumber);
		this.role = new SimpleStringProperty(role);
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

	public final SimpleStringProperty roleProperty() {
		return this.role;
	}
	

	public final java.lang.String getRole() {
		return this.roleProperty().get();
	}
	

	public final void setRole(final java.lang.String role) {
		this.roleProperty().set(role);
	}
	

}
