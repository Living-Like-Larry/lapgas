package livinglikelarry.lapgas.state;

import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public class LabAsstLapgasState implements LapgasState {

	@Override
	public void setPaymentTabState(Tab paymentTab) {
		paymentTab.setDisable(true);
	}

	@Override
	public void setLabAsstTabState(Tab labAsstTab) {
		labAsstTab.setDisable(true);
	}

	@Override
	public void setUpdatingStudentPaymentStateMI(MenuItem updatingStudentPaymentMenuItem) {
		updatingStudentPaymentMenuItem.setDisable(true);
	}

	@Override
	public void setSettingMenuItemState(MenuItem settingMenuItem) {
		settingMenuItem.setDisable(true);
	}

	@Override
	public void setPasswordUpdaterAction() {
		//
	}

}
