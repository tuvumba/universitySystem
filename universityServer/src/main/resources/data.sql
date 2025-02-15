-- Insert Users
INSERT INTO users (username, name, surname, password)
VALUES ('teacher', 'example', 'teacher', 'teacher');

INSERT INTO users (username, name, surname, password)
VALUES ('admin', 'example', 'admin', 'admin');

INSERT INTO users (username, name, surname, password)
VALUES ('student','student', 'example',  'student');

INSERT INTO users (username, name, surname, password)
VALUES ('teacher-student', 'teacher-student', 'example', 'teacher-student');

INSERT INTO users (username, name, surname, password)
VALUES ('none','none', 'example',  'none');

INSERT INTO users (username, name, surname, password)
VALUES ('wfontenot','Whitney', 'Fontenot',  '1234');


-- Insert Teachers
INSERT INTO teacher (username, name, surname, titles)
VALUES ('admin','example', 'admin',  '[ADMIN]');

INSERT INTO teacher (username, name, surname, titles)
VALUES ('teacher','teacher', 'example',  'PhD');

INSERT INTO teacher (username, name, surname, titles)
VALUES ('alicesmith','Alice', 'Smith',  'PhD');

INSERT INTO teacher (username, name, surname, titles)
VALUES ('davidwill','David', 'Williams',  'MSc');

INSERT INTO teacher (username, name, surname, titles)
VALUES ('teacher-student', 'teacher-student', 'example', 'MSc');

-- Insert Subjects
INSERT INTO subject (name, description)
VALUES ('Mathematics', 'An introduction to calculus and algebra.');

INSERT INTO subject (name, description)
VALUES ('Physics', 'Exploring mechanics and thermodynamics.');

-- Insert Students
INSERT INTO student (username, name, surname)
VALUES ('teacher-student', 'example', 'teacher-student');
INSERT INTO student (username, name, surname)
VALUES ('student', 'example', 'student');
INSERT INTO student (username, name, surname)
VALUES ('admin', 'example', 'admin');
INSERT INTO student (username, name, surname) values ( 'wfontenot', 'Whitney', 'Fontenot');
INSERT INTO student (username, name, surname) values ('cbyrch1', 'Carey', 'Byrch');
INSERT INTO student (username, name, surname) values ('dallsworth2', 'Demetria', 'Allsworth');
INSERT INTO student (username, name, surname) values ('blane3', 'Bryon', 'Lane');
INSERT INTO student (username, name, surname) values ('hmoxted4', 'Helaine', 'Moxted');
INSERT INTO student (username, name, surname) values ('ltaysbil5', 'Leo', 'Taysbil');
INSERT INTO student (username, name, surname) values ('ddamrel6', 'Dallas', 'Damrel');
INSERT INTO student (username, name, surname) values ('kjuschka7', 'Kerby', 'Juschka');
INSERT INTO student (username, name, surname) values ('bashmole8', 'Brad', 'Ashmole');

insert into student_subject (student_username, subject_id) values ('admin', 2);
insert into student_subject (student_username, subject_id) values ('admin', 1);
insert into student_subject (student_username, subject_id) values ('dallsworth2', 2);
insert into student_subject (student_username, subject_id) values ('wfontenot', 2);
insert into student_subject (student_username, subject_id) values ('blane3', 2);
insert into student_subject (student_username, subject_id) values ('ddamrel6', 1);
insert into student_subject (student_username, subject_id) values ('cbyrch1', 2);
insert into student_subject (student_username, subject_id) values ('ltaysbil5', 1);
insert into student_subject (student_username, subject_id) values ('blane3', 1);
insert into student_subject (student_username, subject_id) values ('dallsworth2', 1);
insert into student_subject (student_username, subject_id) values ('student', 1);
insert into student_subject (student_username, subject_id) values ('teacher-student', 1);


insert into teacher_subject (teacher_username, subject_id) values ('admin', 1);
insert into teacher_subject (teacher_username, subject_id) values ('admin', 2);
insert into teacher_subject (teacher_username, subject_id) values ('teacher', 1);
insert into teacher_subject (teacher_username, subject_id) values ('teacher', 2);
insert into teacher_subject (teacher_username, subject_id) values ('teacher-student', 2);
insert into teacher_subject (teacher_username, subject_id) values ('davidwill', 1);
insert into teacher_subject (teacher_username, subject_id) values ('alicesmith', 1);
