package livinglikelarry.lapgas.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class CoursesTableModel {
	private SimpleStringProperty course;
	private SimpleStringProperty courseNumber;
	private SimpleObjectProperty<Integer> semester;

	public CoursesTableModel(String course) {
		this.course = new SimpleStringProperty(course);
	}

	public CoursesTableModel(String courseNumber, String course, int semester) {
		this.course = new SimpleStringProperty(course);
		this.courseNumber = new SimpleStringProperty(courseNumber);
		this.semester = new SimpleObjectProperty<>(semester);
	}

	public final SimpleStringProperty courseProperty() {
		return this.course;
	}

	public final String getCourse() {
		return this.courseProperty().get();
	}

	public final void setCourse(final String course) {
		this.courseProperty().set(course);
	}

	public final SimpleStringProperty courseNumberProperty() {
		return this.courseNumber;
	}

	public final java.lang.String getCourseNumber() {
		return this.courseNumberProperty().get();
	}

	public final void setCourseNumber(final String courseNumber) {
		this.courseNumberProperty().set(courseNumber);
	}

	public final SimpleObjectProperty<Integer> semesterProperty() {
		return this.semester;
	}

	public final Integer getSemester() {
		return this.semesterProperty().get();
	}

	public final void setSemester(final int semester) {
		this.semesterProperty().set(semester);
	}

}