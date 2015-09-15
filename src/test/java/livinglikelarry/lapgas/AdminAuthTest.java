package livinglikelarry.lapgas;

import org.javalite.activejdbc.DB;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import livinglikelarry.lapgas.AdminAuth;
import livinglikelarry.lapgas.model.Admin;
import livinglikelarry.lapgas.model.sql.AdminSql;

public class AdminAuthTest {

	private static Admin admin;

	private static AdminAuth adminAuth;

	private static DB adminDB;

	@BeforeClass
	public static void setUpBeforeClass() {
		adminDB = new DB("admin");
		adminDB.open(Configurator.properties("admin.driver"),
				Configurator.properties("admin.url") + Configurator.properties("admin.dbname"),
				Configurator.properties("admin.username"), Configurator.properties("admin.password"));
		adminDB.exec("CREATE TABLE IF NOT EXISTS admins (id INTEGER PRIMARY KEY AUTOINCREMENT, password TEXT)");
		admin = new AdminSql();
		admin.setPassword("livinglikelarry");
		adminAuth = new AdminAuth(admin, "livinglikelarry");
	}

	@Test
	public void testLoginSucces() {
		assertEquals(adminAuth, adminAuth.onSuccess(x -> assertEquals("livinglikelarry", x.getPassword())));
	}

	@Test
	public void testLoginFail() {
		assertEquals(adminAuth, adminAuth.onFailed(x -> assertEquals(admin, x)));
	}

	@AfterClass
	public static void setUpAfterClass() {
		adminDB.exec("DROP TABLE admins");
		adminDB.close();
	}

}