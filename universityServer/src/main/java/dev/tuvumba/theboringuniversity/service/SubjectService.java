package dev.tuvumba.theboringuniversity.service;


import dev.tuvumba.theboringuniversity.domain.Student;
import dev.tuvumba.theboringuniversity.domain.Subject;
import dev.tuvumba.theboringuniversity.domain.Teacher;
import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.domain.dto.TeacherDto;
import dev.tuvumba.theboringuniversity.repository.StudentRepository;
import dev.tuvumba.theboringuniversity.repository.SubjectRepository;
import dev.tuvumba.theboringuniversity.repository.TeacherRepository;
import dev.tuvumba.theboringuniversity.service.mapper.StudentMapper;
import dev.tuvumba.theboringuniversity.service.mapper.SubjectMapper;
import dev.tuvumba.theboringuniversity.service.mapper.TeacherMapper;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService implements CrudService<SubjectDto, Long> {


    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper, StudentRepository studentRepository, TeacherRepository teacherRepository, StudentMapper studentMapper, TeacherMapper teacherMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    public void assignTeacher(Long subjectId, String teacherUsername) {
        Teacher teacher = teacherRepository.findById(teacherUsername).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.addTeacher(teacher);
        teacherRepository.save(teacher);
        subjectRepository.save(subject);
    }

    public void unassignTeacher(Long subjectId, String teacherUsername) {
        Teacher teacher = teacherRepository.findById(teacherUsername).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.removeTeacher(teacher);
        teacherRepository.save(teacher);
        subjectRepository.save(subject);
    }


    public void assignStudent(Long subjectId, String studentUsername) {
        Student student = studentRepository.findById(studentUsername).orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.addStudent(student);
        studentRepository.save(student);
        subjectRepository.save(subject);
    }

    public void unassignStudent(Long subjectId, String studentUsername) {
        Student student = studentRepository.findById(studentUsername).orElseThrow(() -> new RuntimeException("Student not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.removeStudent(student);
        studentRepository.save(student);
        subjectRepository.save(subject);

    }

    @Override
    public SubjectDto save(SubjectDto dto) {
        if(dto.getId() != null && subjectRepository.existsById(dto.getId())) {
            throw new RuntimeException("Subject" + dto.getName() + " already exists");
        }

        Subject subject = subjectMapper.convertToEntity(dto);
        System.out.println("Subject " + subject.getName() + ", description: " + subject.getDescription());
        return subjectMapper.convertToDto(subjectRepository.save(subject));
    }

    public Iterable<StudentDto> getStudents(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(()-> new RuntimeException("Subject not found"));

        List<Student> students = new ArrayList<>( subject.getStudents());
        return studentMapper.convertToDto(students);
    }

    public Iterable<TeacherDto> getTeachers(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        List<Teacher> teachers = new ArrayList<>( subject.getTeachers());
        return teacherMapper.convertToDto(teachers);
    }


    @Override
    public Iterable<SubjectDto> findAll() {
        return subjectMapper.convertToDto((List<Subject>) subjectRepository.findAll());
    }

    @Override
    public SubjectDto findById(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(()-> new RuntimeException("Subject not found"));
        return subjectMapper.convertToDto(subject);
    }

    public SubjectDto findByName(String name) {
        return subjectMapper.convertToDto(subjectRepository.findByName(name).orElseThrow(()-> new RuntimeException("Subject not found")));
    }

    public List<SubjectDto> findByNameStart(String name, int limit) {
        if(limit <= 0) return new ArrayList<>();
        return subjectMapper.convertToDto(subjectRepository.findByNameContaining(name, Limit.of(limit)));
    }


    @Override
    public SubjectDto update(Long id, SubjectDto entity) {
        Subject existingSubject = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        existingSubject.setName(entity.getName());
        existingSubject.setDescription(entity.getDescription());
        Subject updatedSubject = subjectRepository.save(existingSubject);
        return subjectMapper.convertToDto(updatedSubject);
    }

    @Override
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
}
