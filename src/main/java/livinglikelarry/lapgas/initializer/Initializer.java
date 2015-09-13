package livinglikelarry.lapgas.initializer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.javalite.activejdbc.Base;

import livinglikelarry.lapgas.Configurator;

public class Initializer {

	public static void main(String[] args) {
		try {
			makeLapgasDB();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static void makeLapgasDB() throws SQLException, IOException {
		Base.open(Configurator.properties("main.driver"), Configurator.properties("main.url"),
				Configurator.properties("main.username"), Configurator.properties("main.password"));

		ResultSet resultSet = Base.connection().createStatement().executeQuery("SHOW DATABASES");
		boolean isFound = false;
		while (resultSet.next()) {
			if (resultSet.getString("Database").equalsIgnoreCase("lapgas")) {
				isFound = true;
				break;
			}
		}
		if (!isFound) {
			Base.exec("CREATE DATABASE lapgas");
			Base.exec("USE lapgas");
			Base.exec(Configurator.table("courses"));
			Base.exec(Configurator.table("student_payments"));
			Base.exec(Configurator.table("lab_assistant_attendances"));
		}

		Base.close();

	}

}
