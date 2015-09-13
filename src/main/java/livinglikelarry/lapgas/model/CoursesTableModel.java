package livinglikelarry.lapgas.model;

import javafx.beans.property.SimpleStringProperty;

public class CoursesTableModel {
	private SimpleStringProperty course;

	public CoursesTableModel(String course) {
		this.course = new SimpleStringProperty(course);
	}

	public final SimpleStringProperty courseProperty() {
		return this.course;
	}

	public final String getCourse() {
		return this.courseProperty().get();
	}

	public final void setCourse(final java.lang.String course) {
		this.courseProperty().set(course);
	}

}