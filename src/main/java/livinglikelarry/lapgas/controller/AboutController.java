package livinglikelarry.lapgas.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import livinglikelarry.lapgas.util.Configurator;

public class AboutController implements Initializable {
	@FXML
	private ImageView logoImageView;

	@FXML
	private TextArea aboutTextArea;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			this.logoImageView.setImage(new Image(Configurator.image("logo.jpg").toString()));
			loadLicense();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleDeveloperButton() {
		this.aboutTextArea.setText("Moch Deden (htpp://github.com/seledepselesnul)\nRabani (http://github.com/fsckgrep3)\nRiki (http://github.com/riki49)");
	}
	
	@FXML
	public void handleLicenseButton() {
		try {
			loadLicense();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private void loadLicense() throws IOException {
		this.aboutTextArea.setText(Configurator.text("license"));
	}
}
