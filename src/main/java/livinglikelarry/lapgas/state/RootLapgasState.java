package livinglikelarry.lapgas.state;

import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public class RootLapgasState implements LapgasState {

	@Override
	public void setPaymentTabState(Tab paymentTab) {
		paymentTab.setDisable(false);
	}

	@Override
	public void setLabAsstTabState(Tab labAsstTab) {
		labAsstTab.setDisable(false);
	}

	@Override
	public void setUpdatingStudentPaymentStateMI(MenuItem updatingStudentPayment) {
		updatingStudentPayment.setDisable(false);
	}

	@Override
	public void setSettingMenuItemState(MenuItem settingMenuItem) {
		settingMenuItem.setDisable(false);
	}

	@Override
	public void setPasswordUpdaterAction() {
	}

	@Override
	public void setLabAsstComboBoxMode(ComboBox<String> modeComboBox) {
	}

}
