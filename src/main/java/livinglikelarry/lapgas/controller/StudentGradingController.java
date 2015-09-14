package livinglikelarry.lapgas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.javalite.activejdbc.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.StudentPayment;
import livinglikelarry.lapgas.model.StudentPaymentTableModel;

public class StudentGradingController implements Initializable {

	@FXML
	private ComboBox<String> gradeComboBox;
	private long studentPaymentId;
	private TableView<StudentPaymentTableModel> studentPaymentTableView;
	private Consumer<TableView<StudentPaymentTableModel>> studentPaymentTableViewConsumer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.gradeComboBox.getItems().setAll("A", "B", "C", "D", "E", "T");
	}

	public void setGrade(long id, String grade) {
		this.studentPaymentId = id;
		this.gradeComboBox.setValue(grade);
	}

	@FXML
	public void handleUpdatingGrade() {
		Configurator.doDBACtion(() -> {
			Model studentPayment = StudentPayment.findById(this.studentPaymentId);
			studentPayment.set("grade", this.gradeComboBox.getValue());
			studentPayment.saveIt();
		});
		this.studentPaymentTableViewConsumer.accept(this.studentPaymentTableView);
	}

	public void setStudentPaymentTableView(TableView<StudentPaymentTableModel> studentPaymentTableView,
			Consumer<TableView<StudentPaymentTableModel>> studentPaymentTableViewConsumer) {
		this.studentPaymentTableView = studentPaymentTableView;
		this.studentPaymentTableViewConsumer = studentPaymentTableViewConsumer;
	}

}
