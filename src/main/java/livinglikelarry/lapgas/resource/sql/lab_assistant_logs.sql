/* author : Moch Deden (http://github.com/selesdepselesnul) */
CREATE TABLE lab_assistant_logs (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	log LONGTEXT,
	student_number VARCHAR(14),
	created_at TIMESTAMP,
	FOREIGN KEY (student_number) REFERENCES lab_assistants (student_number)
);