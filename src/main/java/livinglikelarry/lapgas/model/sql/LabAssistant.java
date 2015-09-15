package livinglikelarry.lapgas.model.sql;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;


@DbName("lapgas")
@Table("lab_assistants")
@IdName("student_number")
public class LabAssistant extends Model {

}
