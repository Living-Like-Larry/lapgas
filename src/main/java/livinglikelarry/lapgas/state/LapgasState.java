package livinglikelarry.lapgas.state;

import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public interface LapgasState {
	public void setPaymentTabState(Tab paymentTab);

	public void setUpdatingStudentPaymentStateMI(MenuItem updatingStudentPayment);

	public void setLabAsstTabState(Tab labAsstTab);

	public void setPasswordUpdaterAction();

	public void setLabAsstComboBoxMode(ComboBox<String> modeComboBox);

	public void setLabAsstActionMenuButton(MenuButton labAsstActionMenuButton);

	public void setScannerTabSetting(Tab scannerTab);

	public void setCourseTabSetting(Tab courseTab);

}
