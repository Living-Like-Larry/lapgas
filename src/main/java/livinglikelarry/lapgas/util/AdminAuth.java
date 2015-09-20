package livinglikelarry.lapgas.util;

import java.util.function.Consumer;
import livinglikelarry.lapgas.model.Admin;

/**
 * This class is used for authentication for system {@link Admin}
 * 
 * @author Moch Deden (http://github.com/selesdepselesnul)
 *
 */
public class AdminAuth {

	private Admin admin;
	private String actualPassword;

	/**
	 * contructor for AdminAuth
	 * 
	 * @param admin
	 *            class that is implemented {@link Admin} as an admin for this
	 *            system
	 * @param actualPassword
	 *            actual password that is actually passed to the system
	 */
	public AdminAuth(Admin admin, String actualPassword) {
		this.admin = admin;
		this.actualPassword = actualPassword;
	}

	/**
	 * 
	 * method for action that will be executed when login success
	 * 
	 * @param adminAction
	 * @return itself {@link AdminAuth}
	 */
	public AdminAuth onSuccess(Consumer<Admin> adminAction) {
		if (this.admin.getPassword().equalsIgnoreCase(actualPassword)) {
			adminAction.accept(this.admin);
		}
		return this;
	}

	/**
	 * 
	 * method for action that will be executed when login failed
	 * 
	 * @param adminAction
	 * @return itself {@link AdminAuth}
	 */
	public AdminAuth onFailed(Consumer<Admin> adminAction) {
		if (!this.admin.getPassword().equalsIgnoreCase(actualPassword)) {
			adminAction.accept(this.admin);
		}
		return this;
	}

}
