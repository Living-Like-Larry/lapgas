package livinglikelarry.lapgas.sanitychecker;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.javalite.activejdbc.Base;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import livinglikelarry.lapgas.Configurator;

/**
 * 
 * sanity checker class to make sure you can go further with this project
 * make sure you make it green before you go along with us :)
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class JavaLiteTest {

	@BeforeClass
	public static void init() throws IOException {
		Properties info = new Properties();
		/**
		 * you need to make sql.properties file in this current package and make
		 * sure you make value pair of jdbc.url for url of your database,
		 * jdbc.username for your username and jdbc.password for your password,
		 * ex :
		 * jdbc.url = jdbc:mysql://localhost/
		 * jdbc.username = larry
		 * jdbc.password = livinlikelarry
		 */
		String propertyUri = "livinglikelarry/lapgas/sanitychecker/sql.properties";
		info.load(ClassLoader.getSystemResourceAsStream(propertyUri));
		Base.open(Configurator.properties("main.driver"), Configurator.properties("main.url") + Configurator.properties("main.dbname"), 
				Configurator.properties("main.username"), Configurator.properties("main.password"));
		Base.exec("CREATE DATABASE IF NOT EXISTS javalite");
		Base.exec("USE javalite");
		Base.exec("CREATE TABLE employees (id INT PRIMARY KEY)");

	}

	@Test
	public void testTableName() throws IOException {
		assertEquals("employees", Employee.getTableName());
	}

	@AfterClass
	public static void destroy() {
		Base.exec("DROP DATABASE IF EXISTS javalite");
		Base.close();
	}

}
