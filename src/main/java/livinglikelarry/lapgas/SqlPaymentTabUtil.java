package livinglikelarry.lapgas;

import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.controller.main.PaymentTabUtil;
import livinglikelarry.lapgas.model.Courses;
import livinglikelarry.lapgas.model.StudentPayment;

public class SqlPaymentTabUtil implements PaymentTabUtil {

	@Override
	public List<String> getCourses(String studentNumber) {
		return Arrays.asList("fasfasf", "fasfasfafsaf");
	}

	@Override
	public long saveToDatabase(String studentNumber, ObservableList<Courses> courses) {
		return 0;
	}

}
