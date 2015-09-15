CREATE TABLE lab_assistant_attendances (
    id BIGINT AUTO_INCREMENT,
    student_number CHAR(14),
    created_at TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY (student_number) REFERENCES lab_assistants (student_number)
);
