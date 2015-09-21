package livinglikelarry.lapgas.util;

import java.io.File;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrBuilder;

import javafx.collections.ObservableList;
import livinglikelarry.lapgas.model.sql.LabAssistantLog;
import livinglikelarry.lapgas.model.table.CoursesTableModel;

public class LabAssistantLogger {

	public static void logNewStudentPayment(String labAsstStudentNumber, String studentNumber,
			ObservableList<CoursesTableModel> courseTableModel, File paymentReceipt, String studentClass,
			String paymentValue) {
		String courseNames = courseTableModel.stream().map(x -> x.getCourse()).collect(Collectors.joining(","));
		Configurator.doDBACtion(() -> {
			LabAssistantLog labAssistantLog = new LabAssistantLog();
			labAssistantLog.set("student_number", (String) labAsstStudentNumber)
					.set("log",
							"melakukan penambahan pembayaran mahasiswa : \n" + "npm                 : " + studentNumber
									+ "\n" + "matakuliah          : " + courseNames + "\n" + "bukti pembayaran    : "
									+ paymentReceipt.toString() + "\n" + "kelas               : " + studentClass + "\n"
									+ "pembayaran sejumlah : " + paymentValue)
					.saveIt();
		});
	}

	public static String getAllLog(String studentNumber) {
		StrBuilder logsStringBuilder = new StrBuilder();
		Configurator.doDBACtion(() -> {
			logsStringBuilder.append(LabAssistantLog.find("student_number = ? ", (String) studentNumber).stream()
					.map(x -> (String)x.get("log")).collect(Collectors.joining("\n\n")));
		});
		return logsStringBuilder.toString();
	}

}
