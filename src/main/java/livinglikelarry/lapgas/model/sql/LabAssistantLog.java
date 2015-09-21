package livinglikelarry.lapgas.model.sql;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@DbName("lapgas")
@Table("lab_assistant_logs")
@BelongsTo(parent = LabAssistant.class, foreignKeyName = "student_number")
public class LabAssistantLog extends Model {

}
