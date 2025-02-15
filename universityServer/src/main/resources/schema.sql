DROP TABLE IF EXISTS student_subject CASCADE;
DROP TABLE IF EXISTS teacher_subject CASCADE;
DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS users;

-- Users Table
CREATE TABLE users (
                       username VARCHAR(100) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       surname VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Teachers Table
CREATE TABLE teacher (
                         username VARCHAR(100) PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         surname VARCHAR(100) NOT NULL,
                         titles VARCHAR(255)
);

-- Subjects Table
CREATE TABLE subject (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) UNIQUE NOT NULL,
                         description TEXT
);

-- Students Table
CREATE TABLE student (
                         username VARCHAR(100) PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         surname VARCHAR(100) NOT NULL
);

-- Student-Subject Many-to-Many Relationship
CREATE TABLE student_subject (
                                 student_username VARCHAR(100) NOT NULL,
                                 subject_id INT NOT NULL,
                                 PRIMARY KEY (student_username, subject_id),
                                 FOREIGN KEY (student_username) REFERENCES student(username) ON DELETE CASCADE,
                                 FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE
);

-- Teacher-Subject Many-to-Many Relationship
CREATE TABLE teacher_subject (
                                 teacher_username VARCHAR(100) NOT NULL,
                                 subject_id INT NOT NULL,
                                 PRIMARY KEY (teacher_username, subject_id),
                                 FOREIGN KEY (teacher_username) REFERENCES teacher(username) ON DELETE CASCADE,
                                 FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE CASCADE
);
