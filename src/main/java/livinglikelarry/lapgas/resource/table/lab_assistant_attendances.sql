CREATE TABLE lab_assistant_attendances (
    id BIGINT AUTO_INCREMENT,
    student_number CHAR(14),
    created_at TIMESTAMP,
    is_attended BOOLEAN,
    PRIMARY KEY(id)
);
