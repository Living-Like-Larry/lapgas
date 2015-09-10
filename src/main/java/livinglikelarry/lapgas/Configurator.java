package livinglikelarry.lapgas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.javalite.activejdbc.Base;

import livinglikelarry.lapgas.model.AdminSql;

/**
 * Class that is represented as a wide configurator for lapgas system
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public final class Configurator {

	private static final String VIEW_BASE_PATH = "livinglikelarry/lapgas/resource/view/";
	private static final String VIEW_EXT = ".fxml";
	private static final String TABLE_BASE_PATH = "livinglikelarry/lapgas/resource/table/";


	/**
	 * 
	 * load the view
	 * @param view name of view 
	 * @return stream of view
	 */
	public static InputStream view(String view) {
		return ClassLoader.getSystemResourceAsStream(VIEW_BASE_PATH + view + VIEW_EXT);
	}

	/**
	 * configure admin auth
	 */
	public static void initAdminAuth() {
		Base.open("org.sqlite.JDBC", "jdbc:sqlite:admin.db", "", "");
		if (Base.exec("SELECT name FROM sqlite_master WHERE name='admins'") == 0) {
			Base.exec(table("admins"));
			new AdminSql().set("password", "livinglikelarry").saveIt();
		}
	}

	/**
	 * 
	 * load a table
	 * @param table name of the table
	 * @return sql schema for that table
	 */
	public static String table(String table) {
		BufferedReader buff = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResourceAsStream(TABLE_BASE_PATH + table + ".sql")));
		return buff.lines().collect(Collectors.joining());
	}

}
