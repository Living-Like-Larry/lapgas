package livinglikelarry.lapgas;

import java.util.List;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.controller.main.PaymentTabUtil;
import livinglikelarry.lapgas.model.CoursesTableModel;
import livinglikelarry.lapgas.model.StudentPayment;

public class SqlPaymentTabUtil implements PaymentTabUtil {

	@Override
	public List<String> getCourses(String studentNumber) {
		System.out.println(StudentPayment.where("student_number = ? ", studentNumber));
		return null;
	}

	@Override
	public long saveToDatabase(String studentNumber, ObservableList<CoursesTableModel> courses) {
		return 0;
	}

}
