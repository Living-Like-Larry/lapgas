package livinglikelarry.lapgas.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@DbName("lapgas")
@Table("student_payments")
@BelongsTo(parent = Course.class, foreignKeyName = "course_number")
public class StudentPayment extends Model {
}
