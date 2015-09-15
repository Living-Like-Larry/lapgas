package livinglikelarry.lapgas.model.table;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class StudentPaymentTableModel {
	private SimpleStringProperty studentNumber;
	private SimpleStringProperty studentClass;
	private SimpleObjectProperty<Timestamp> paymentDateTime;
	private SimpleStringProperty courseNumber;
	private BigDecimal paymentValue;
	private String paymentReceiptFilePath;
	private SimpleStringProperty studentGrade;
	private long id;

	public StudentPaymentTableModel(long id, String studentNumber, String courseNumber, String studentClass, Timestamp timestamp,
			BigDecimal paymentValue, String paymentReceiptFilePath, String grade) {

		this.setId(id);
		this.studentNumber = new SimpleStringProperty(studentNumber);
		this.courseNumber = new SimpleStringProperty(courseNumber);
		this.studentClass = new SimpleStringProperty(studentClass);
		this.paymentDateTime = new SimpleObjectProperty<>(timestamp);
		this.setPaymentValue(paymentValue);
		this.setPaymentReceiptFilePath(paymentReceiptFilePath);
		this.studentGrade = new SimpleStringProperty(grade);
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

	public final SimpleStringProperty studentClassProperty() {
		return this.studentClass;
	}

	public final String getStudentClass() {
		return this.studentClassProperty().get();
	}

	public final void setStudentClass(final String studentClass) {
		this.studentClassProperty().set(studentClass);
	}

	public final SimpleObjectProperty<Timestamp> paymentDateTimeProperty() {
		return this.paymentDateTime;
	}

	public final Timestamp getPaymentDateTime() {
		return this.paymentDateTimeProperty().get();
	}

	public final void setPaymentDateTime(final Timestamp paymentDateTime) {
		this.paymentDateTimeProperty().set(paymentDateTime);
	}

	public final SimpleStringProperty courseNumberProperty() {
		return this.courseNumber;
	}

	public final String getCourseNumber() {
		return this.courseNumberProperty().get();
	}

	public final void setCourseNumber(final String courseNumber) {
		this.courseNumberProperty().set(courseNumber);
	}

	public BigDecimal getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(BigDecimal paymentValue) {
		this.paymentValue = paymentValue;
	}

	public String getPaymentReceiptFilePath() {
		return paymentReceiptFilePath;
	}

	public void setPaymentReceiptFilePath(String paymentReceiptFilePath) {
		this.paymentReceiptFilePath = paymentReceiptFilePath;
	}

	public final SimpleStringProperty studentGradeProperty() {
		return this.studentGrade;
	}

	public final String getStudentGrade() {
		return this.studentGradeProperty().get();
	}

	public final void setStudentGrade(final String studentGrade) {
		this.studentGradeProperty().set(studentGrade);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
