package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.Courses;

/**
 * 
 * Utility class for helping PaymentTab part of {@link MainController}}
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public interface PaymentTabUtil {

	/**
	 * 
	 * @param studentNumber
	 * @return List of String with value of courses taken by particular
	 *         studentNumber
	 */
	public List<String> getCourses(String studentNumber);

	/**
	 * 
	 * @param choosenPaymentReceiptFile
	 * @param studentNumber
	 * @throws IOException
	 */
	public default String savePaymentReceiptToFS(File choosenPaymentReceiptFile, String studentNumber,
			List<String> courses) throws IOException {

		String newPathNameStr = choosenPaymentReceiptFile.toString();
		if (choosenPaymentReceiptFile != null) {
			String coursesJoined = courses.stream().collect(Collectors.joining("#"));
			Path choosenPath = choosenPaymentReceiptFile.toPath();
			Path newPathName = Paths.get(
					studentNumber + "#" + coursesJoined + "." + FilenameUtils.getExtension(choosenPath.toString()));
			Path targetPath = Paths.get(Configurator.PIC_PATH + newPathName);
			Files.copy(choosenPath, targetPath);
			newPathNameStr = targetPath.toString();
		}
		return newPathNameStr;
	}

	public long saveToDatabase(String studentNumber, ObservableList<Courses> courses);

}
