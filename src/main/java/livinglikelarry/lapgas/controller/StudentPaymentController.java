package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class StudentPaymentController {

	@FXML
	private ImageView paymentReceiptImageView;

	@FXML
	private Text paymentValueText;

	public void setPaymentReceipt(String paymentReceiptFilePath) {
		try {
			this.paymentReceiptImageView.setImage(new Image(Files.newInputStream(Paths.get(paymentReceiptFilePath))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPaymentValue(BigDecimal paymentValue) {
		this.paymentValueText.setText(this.paymentValueText.getText() + " " + paymentValue.toString() + ",00");
	}

}
