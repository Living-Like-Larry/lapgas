package livinglikelarry.lapgas.state;

import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
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

	@Override
	public void setLabAsstComboBoxMode(ComboBox<String> modeComboBox) {
		modeComboBox.setValue("filter");
		modeComboBox.setVisible(false);
	}

	@Override
	public void setLabAsstActionMenuButton(MenuButton labAsstActionMenuButton) {
		// TODO Auto-generated method stub
		
	}

}
