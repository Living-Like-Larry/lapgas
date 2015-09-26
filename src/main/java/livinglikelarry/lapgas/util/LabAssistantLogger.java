package livinglikelarry.lapgas.util;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrBuilder;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.model.sql.LabAssistantLog;
import livinglikelarry.lapgas.model.table.CoursesTableModel;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class LabAssistantLogger {

	public static void logNewStudentPayment(String labAsstStudentNumber, String studentNumber,
			ObservableList<CoursesTableModel> courseTableModel, File paymentReceipt, String studentClass) {
		String courseNames = courseTableModel.stream().map(x -> x.getCourse()).collect(Collectors.joining(","));
		String paymentValue = courseTableModel.stream().map(x -> x.getPaymentValue().toString())
				.collect(Collectors.joining(","));
		Configurator.doDBACtion(() -> {
			LabAssistantLog labAssistantLog = new LabAssistantLog();
			final LocalDateTime now = LocalDateTime.now();
			labAssistantLog.set("student_number", (String) labAsstStudentNumber)
					.set("log",
							"Pada tanggal " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear()
									+ "\n" + "tepat pada pukul " + now.getHour() + ":" + now.getMinute() + ":"
									+ now.getSecond() + "\n" + "aslab dengan npm " + labAsstStudentNumber + "\n"
									+ "melakukan penambahan pembayaran mahasiswa, " + "\n"
									+ "dengan detail sebagai berikut : \n" + "npm -> " + studentNumber + "\n"
									+ "matakuliah -> " + courseNames + "\n" + "kelas -> " + studentClass + "\n"
									+ "pembayaran sejumlah -> " + paymentValue)
					.saveIt();
		});
	}

	public static String getAllLog(String studentNumber) {
		StrBuilder logsStringBuilder = new StrBuilder();
		Configurator.doDBACtion(() -> {
			logsStringBuilder.append(LabAssistantLog.find("student_number = ? ", (String) studentNumber).stream()
					.map(x -> (String) x.get("log")).collect(Collectors.joining("\n\n")));
		});

		return logsStringBuilder.toString();
	}

	public static String getLogByDate(String studentNumber, LocalDate from, LocalDate to) {
		StringBuilder stringBuilder = new StringBuilder();
		Configurator.doDBACtion(() -> {
			stringBuilder.append(LabAssistantLog.findAll().stream().filter(x -> {
				Timestamp createdAt = (Timestamp) x.get("created_at");
				LocalDate createdAtLocalDate = createdAt.toLocalDateTime().toLocalDate();
				return (createdAtLocalDate.isEqual(from) || createdAtLocalDate.isAfter(from))
						&& (createdAtLocalDate.isEqual(to) || createdAtLocalDate.isBefore(to));
			}).map(x -> (String) x.get("log")).collect(Collectors.joining("\n\n")));

		});
		return stringBuilder.toString();
	}

}
