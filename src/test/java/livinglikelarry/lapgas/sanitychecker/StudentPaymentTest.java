package livinglikelarry.lapgas.sanitychecker;

import java.io.IOException;

import org.javalite.activejdbc.Base;

import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.StudentPayment;

public class StudentPaymentTest {
	public static void main(String[] args) throws IOException {
		Base.open(Configurator.properties("main.driver"), Configurator.properties("main.url") + "lapgas",
				Configurator.properties("main.username"), Configurator.properties("main.password"));
		// Base.exec("CREATE DATABASE lapgas");
		// Base.exec("USE lapgas");
		// Base.exec(Configurator.table("course"));
		// Base.exec(Configurator.table("student_payment"));
		// Base.exec(Configurator.table("lab_assistant_attendance"));
		System.out.println(StudentPayment.where("student_number = ? ", "41155050140062"));
		Base.close();
	}

}
