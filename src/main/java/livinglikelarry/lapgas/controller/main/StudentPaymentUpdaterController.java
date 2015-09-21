package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.javalite.activejdbc.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.StudentPayment;
import livinglikelarry.lapgas.model.table.StudentPaymentTableModel;
import livinglikelarry.lapgas.util.Configurator;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class StudentPaymentUpdaterController implements Initializable {

	@FXML
	private ComboBox<String> courseNameComboBox;

	@FXML
	private TextField classTextField;

	@FXML
	private ComboBox<String> gradeComboBox;

	@FXML
	private TextField amountOfPaymentTextField;

	@FXML
	private Button paymentReceiptButton;

	@FXML
	private TextField paymentReceiptTextField;

	@FXML
	private ImageView paymentReceiptImageView;

	private StudentPaymentTableModel studentPaymentTableModel;

	private Stage stage;

	private File choosenPaymentReceiptFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.gradeComboBox.getItems().setAll("A", "B", "C", "D", "E", "T");
	}

	public void setStudentPayment(StudentPaymentTableModel studentPaymentTableModel) {
		this.studentPaymentTableModel = studentPaymentTableModel;
		initAllField();
	}

	private void initAllField() {
		try {
			this.amountOfPaymentTextField.setText(this.studentPaymentTableModel.getPaymentValue().toString());
			this.classTextField.setText(this.studentPaymentTableModel.getStudentClass());
			this.gradeComboBox.setValue(this.studentPaymentTableModel.getStudentGrade());
			this.paymentReceiptImageView.setImage(new Image(
					Files.newInputStream(Paths.get(this.studentPaymentTableModel.getPaymentReceiptFilePath()))));
			Configurator.doDBACtion(() -> courseNameComboBox.getItems()
					.setAll(Course.findAll().stream().map(x -> (String) x.get("name")).collect(Collectors.toList())));
			this.courseNameComboBox.setValue(this.studentPaymentTableModel.getCourseName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleChoosingNewReceiptPayment() {
		FileChooser fileChooser = new FileChooser();
		this.choosenPaymentReceiptFile = fileChooser.showOpenDialog(stage);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleUpdatingButton() {
		Configurator.doDBACtion(() -> {
			try {
				Model studentPayment = StudentPayment.findById((long) this.studentPaymentTableModel.getId());
				final String newGrade = this.gradeComboBox.getValue();
				final String newClass = (String) this.classTextField.getText().toUpperCase();
				final BigDecimal newAmountOfPayment = new BigDecimal(this.amountOfPaymentTextField.getText());
				Model course = Course.first("name = ?", (String) this.courseNameComboBox.getValue());
				final String newCourseNumber = (String) course.get("course_number");

				if (this.choosenPaymentReceiptFile == null) {
					this.choosenPaymentReceiptFile = new File(studentPaymentTableModel.getPaymentReceiptFilePath());
				}
				studentPaymentTableModel.setPaymentReceiptFilePath(new PaymentTabUtil().updatePaymentReceipt(
						this.studentPaymentTableModel.getPaymentReceiptFilePath(), this.choosenPaymentReceiptFile,
						this.studentPaymentTableModel.getId()));
				studentPaymentTableModel.setStudentGrade(newGrade);
				studentPaymentTableModel.setStudentClass(newClass);
				studentPaymentTableModel.setPaymentValue(newAmountOfPayment);
				studentPaymentTableModel.setCourseNumber(newCourseNumber);
				studentPaymentTableModel.setCourseName(this.courseNameComboBox.getValue());

				studentPayment.set("payment_value", newAmountOfPayment).set("class", newClass)
						.set("course_number", newCourseNumber).set("grade", (String) newGrade)
						.set("payment_receipt", (String) studentPaymentTableModel.getPaymentReceiptFilePath()).saveIt();
				this.stage.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
