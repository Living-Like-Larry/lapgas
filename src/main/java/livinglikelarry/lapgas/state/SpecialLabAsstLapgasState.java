package livinglikelarry.lapgas.state;

import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

/**
 * 
 * @author Moch Deden (https://github.com/selesdepselesnul)
 *
 */
public class SpecialLabAsstLapgasState implements LapgasState {

	@Override
	public void setPaymentTabState(Tab paymentTab) {
		paymentTab.setDisable(false);
	}

	@Override
	public void setLabAsstTabState(Tab labAsstTab) {
		labAsstTab.setDisable(true);
	}

	@Override
	public void setUpdatingStudentPaymentStateMI(MenuItem updatingStudentPayment) {
		updatingStudentPayment.setDisable(true);
	}

	@Override
	public void setLabAsstComboBoxMode(ComboBox<String> modeComboBox) {
		modeComboBox.setValue("filter");
		modeComboBox.setVisible(false);
	}

	@Override
	public void setLabAsstActionMenuButton(MenuButton labAsstActionMenuButton) {
		labAsstActionMenuButton.setVisible(true);
	}

	@Override
	public void setScannerTabSetting(Tab scannerTab) {
		scannerTab.setDisable(false);
	}

	@Override
	public void setCourseTabSetting(Tab courseTab) {
		courseTab.setDisable(true);
	}

}
