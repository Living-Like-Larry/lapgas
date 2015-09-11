package livinglikelarry.lapgas.model;

import javafx.beans.property.SimpleStringProperty;

// model inner class for courses tableview
public class Courses {
	private SimpleStringProperty course;

	public Courses(String course) {
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