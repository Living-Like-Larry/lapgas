CREATE TABLE student_payments (
    npm CHAR(14),
    courses_id CHAR(7),
    payment_value CHAR(1),
    clases CHAR(2),
    payment_receipt TEXT,
    PRIMARY KEY(npm),
    FOREIGN KEY (courses_id) REFERENCES courses (id)
);
