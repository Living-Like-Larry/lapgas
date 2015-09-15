package livinglikelarry.lapgas.model.sql;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

import livinglikelarry.lapgas.model.Admin;

/**
 * 
 * Admin implementation specific for sql
 * 
 * @author Moch Deden (http://gihub.com/selesdepselesnul)
 *
 */
@DbName("admin")
@Table("admins")
public class AdminSql extends Model implements Admin {

	@Override
	public void setPassword(String password) {
		this.set("password", password);
		this.saveIt();
	}

	@Override
	public String getPassword() {
		return (String) this.get("password");
	}
}
