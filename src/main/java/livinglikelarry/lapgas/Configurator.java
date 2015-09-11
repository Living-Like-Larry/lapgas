package livinglikelarry.lapgas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Class that is represented as a wide configurator for lapgas system
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public final class Configurator {

	private static final String VIEW_BASE_PATH = "livinglikelarry/lapgas/resource/view/";
	private static final String VIEW_EXT = ".fxml";
	private static final String TABLE_BASE_PATH = "livinglikelarry/lapgas/resource/table/";
	private static final String CONFIG_DIR = ".lapgas-config/";
	private static final String SQL_CONFIG = CONFIG_DIR + "sql.config";
	public static final String PIC_PATH = CONFIG_DIR + "pic/";
	private static Properties properties;

	static {
		try {
			initMainAppConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * load the view `
	 * 
	 * @param view
	 *            name of view
	 * @return stream of view
	 */
	public static InputStream view(String view) {
		return ClassLoader.getSystemResourceAsStream(VIEW_BASE_PATH + view + VIEW_EXT);
	}


	/**
	 * 
	 * load a table
	 * 
	 * @param table
	 *            name of the table
	 * @return sql schema for that table
	 */
	public static String table(String table) {
		BufferedReader buff = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResourceAsStream(TABLE_BASE_PATH + table + ".sql")));
		return buff.lines().collect(Collectors.joining());
	}

	/**
	 * loading whole system properties
	 * @param key
	 * @return value of properties
	 */
	public static String properties(String key) {
		return properties.getProperty(key);
	}

	
	private static void initMainAppConfig() throws IOException {
		makePropertiesConfig();
		makePictureConfig();
	}


	private static void makePictureConfig() throws IOException {
		if (Files.notExists(Paths.get(PIC_PATH))) {
			Files.createDirectory(Paths.get(PIC_PATH));
		}
	}


	private static void makePropertiesConfig() throws IOException {
		properties = new Properties();
		if (Files.notExists(Paths.get(CONFIG_DIR))) {
			Files.createDirectories(Paths.get(CONFIG_DIR));
			Files.createFile(Paths.get(SQL_CONFIG));
		}
		properties.load(Files.newInputStream(Paths.get(SQL_CONFIG)));
	}

}
