package livinglikelarry.lapgas.model.sql;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;


/**
 * 
 * @author Moch Deden (https://gihtub.com/selesdepselesnul)
 *
 */
@DbName("lapgas")
@Table("courses")
@IdName("course_number")
public class Course extends Model {}
