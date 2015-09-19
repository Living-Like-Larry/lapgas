package livinglikelarry.lapgas.controller.main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import livinglikelarry.lapgas.model.table.StudentPaymentTableModel;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
