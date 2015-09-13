package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.Configurator;
import livinglikelarry.lapgas.model.Course;
import livinglikelarry.lapgas.model.CoursesTableModel;
import livinglikelarry.lapgas.model.StudentPayment;

/**
 * 
 * Utility class for helping PaymentTab part of {@link MainController}}
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class PaymentTabUtil {

	/**
	 * 
	 * @param studentNumber
	 * @return List of String with value of courses taken by particular
	 *         studentNumber
	 */
	public List<String> getCourses(String studentNumber) {
		List<StudentPayment> studentPaymentList = StudentPayment.where("student_number = ? ", studentNumber);
		List<String> takenCourseNumberList = studentPaymentList.stream().map(x -> (String) x.get("course_number"))
				.collect(Collectors.toList());
		List<String> takenCourseList = new ArrayList<>();
		takenCourseNumberList.forEach(x -> {
			takenCourseList.add((String) Course.findById(x).get("name"));
		});
		List<String> courseList = Course.findAll().stream().map(x -> (String) x.get("name"))
				.collect(Collectors.toList());
		courseList.removeAll(takenCourseList);
		return courseList;
	}

	/**
	 * 
	 * @param studentNumber
	 * @param observableList
	 * @throws IOException
	 */
	public String save(String studentNumber, ObservableList<CoursesTableModel> courseList,
			File choosenPaymentReceiptFile) throws IOException {
		List<String> courseNumber = new ArrayList<>();
		courseList.forEach(x -> {
			String course = (String) x.getCourse();
			courseNumber.add((String) Course.findFirst("name = ? ", course).get("course_number"));
		});
		courseNumber.forEach(x -> {

			try {
				final StudentPayment studentPayment = new StudentPayment();
				studentPayment.set("student_number", studentNumber).set("course_number", x).saveIt();
				String paymentReceipt;
				paymentReceipt = saveToFS(choosenPaymentReceiptFile, (long) studentPayment.getId());
				studentPayment.set("payment_receipt", paymentReceipt).saveIt();
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		return studentNumber;
	}

	private String saveToFS(File choosenPaymentReceiptFile, long id) throws IOException {
		final String fileExtension = FilenameUtils.getExtension(choosenPaymentReceiptFile.toString());
		String newPathString = Configurator.PIC_PATH + id + "." + fileExtension;
		Files.copy(choosenPaymentReceiptFile.toPath(), Paths.get(newPathString));
		return newPathString;
	}
}
