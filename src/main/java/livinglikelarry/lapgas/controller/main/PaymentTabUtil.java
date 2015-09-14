package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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

	private String saveToFS(File choosenPaymentReceiptFile, long id) throws IOException {
		final String fileExtension = FilenameUtils.getExtension(choosenPaymentReceiptFile.toString());
		String newPathString = Configurator.PIC_PATH + id + "." + fileExtension;
		Files.copy(choosenPaymentReceiptFile.toPath(), Paths.get(newPathString));
		return newPathString;
	}

	public String save(String studentNumber, String studentClass, BigDecimal paymentValue,
			ObservableList<CoursesTableModel> courseNames, File paymentReceiptFile) {
		List<String> courseNumberList = new ArrayList<>();
		courseNames.forEach(x -> {
			String course = (String) x.getCourse();
			courseNumberList.add((String) Course.findFirst("name = ? ", course).get("course_number"));
		});
		courseNumberList.forEach(x -> {

			try {
				final StudentPayment studentPayment = new StudentPayment();
				studentPayment.set("student_number", studentNumber).set("course_number", x).set("class", studentClass)
						.set("payment_value", paymentValue).saveIt();
				String paymentReceipt = saveToFS(paymentReceiptFile, (long) studentPayment.getId());
				System.out.println(paymentReceipt);
				studentPayment.set("payment_receipt", paymentReceipt).saveIt();
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		return studentNumber;

	}
}
