package livinglikelarry.lapgas.sanitychecker;

import java.io.IOException;

import org.javalite.activejdbc.Base;

import livinglikelarry.lapgas.model.sql.StudentPayment;
import livinglikelarry.lapgas.util.Configurator;

public class StudentPaymentTest {
	public static void main(String[] args) throws IOException {
		Base.open(Configurator.properties("main.driver"), Configurator.properties("main.url") + "lapgas",
				Configurator.properties("main.username"), Configurator.properties("main.password"));
		System.out.println(StudentPayment.where("student_number = ? ", "41155050140062"));
		Base.close();
	}

}
