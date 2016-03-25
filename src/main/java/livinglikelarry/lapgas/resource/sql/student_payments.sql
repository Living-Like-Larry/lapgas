/* author : Rabani (https://github.com/fsckgrep3) */
CREATE TABLE student_payments (
    id BIGINT AUTO_INCREMENT,
    student_number CHAR(14),    
    course_number CHAR(7),
    payment_value BIGINT,
    class CHAR(2),
    grade TEXT,
    payment_receipt TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    PRIMARY KEY(id),
    FOREIGN KEY (course_number) REFERENCES courses (course_number)
);
