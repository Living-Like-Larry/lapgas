package livinglikelarry.lapgas.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReceiptPaymentReviewerController {

	@FXML
	private ImageView choosenPaymentReceiptImageView;


	public void setReceiptPaymentFile(File choosenPaymentReceiptFile) throws IOException {
		this.choosenPaymentReceiptImageView
				.setImage(new Image(Files.newInputStream(choosenPaymentReceiptFile.toPath())));
	}

}
