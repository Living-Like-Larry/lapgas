package livinglikelarry.lapgas.controller.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.javalite.activejdbc.Model;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.model.sql.Course;
import livinglikelarry.lapgas.model.sql.StudentPayment;
import livinglikelarry.lapgas.model.table.CoursesTableModel;
import livinglikelarry.lapgas.util.Configurator;

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

	private String savePaymentReceipt(File choosenPaymentReceiptFile, long id) throws IOException {
		return storePaymentReceipt(choosenPaymentReceiptFile, id, (x, y) -> {
			try {
				Files.copy(x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private String storePaymentReceipt(File choosenPaymentReceiptFile, long id,
			BiConsumer<Path, Path> receiptPaymentSaverConsumer) {
		final String fileExtension = FilenameUtils.getExtension(choosenPaymentReceiptFile.toString());
		String newPathString = Configurator.PIC_PATH + id + "." + fileExtension;
		receiptPaymentSaverConsumer.accept(choosenPaymentReceiptFile.toPath(), Paths.get(newPathString));
		return newPathString;

	}

	public String updatePaymentReceipt(String oldFile, File choosenPaymentReceiptFile, long id) {
		return storePaymentReceipt(choosenPaymentReceiptFile, id, (x, y) -> {
			try {
				Files.delete(Paths.get(oldFile));
				Files.copy(x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public String save(String studentNumber, String studentClass, ObservableList<CoursesTableModel> courseNames,
			File paymentReceiptFile) {
		Map<String, BigDecimal> coursePair = new HashMap<>();
		courseNames.forEach(x -> {
			Model course = Course.first("name = ?", (String) x.getCourse());
			// BigDecimal paymentValue = x.getPaymentValue();
			coursePair.put((String) course.get("course_number"), x.getPaymentValue());
		});
		coursePair.forEach((k, v) -> {
			try {
				final StudentPayment studentPayment = new StudentPayment();
				studentPayment.set("student_number", studentNumber).set("course_number", k).set("class", studentClass)
						.set("payment_value", (BigDecimal) v).set("grade", "T").saveIt();
				String paymentReceipt = savePaymentReceipt(paymentReceiptFile, (long) studentPayment.getId());
				studentPayment.set("payment_receipt", paymentReceipt).saveIt();
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		return studentNumber;

	}
}
