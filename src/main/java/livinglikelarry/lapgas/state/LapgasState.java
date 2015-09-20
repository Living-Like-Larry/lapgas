package livinglikelarry.lapgas.state;

import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

public interface LapgasState {
	public void setPaymentTabState(Tab paymentTab);
	
	public void setUpdatingStudentPaymentStateMI(MenuItem updatingStudentPayment);
	
	public void setSettingMenuItemState(MenuItem settingMenuItem);
	
	public void setLabAsstTabState(Tab labAsstTab);
	
	public void setPasswordUpdaterAction();

}
