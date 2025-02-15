package dev.tuvumba.theboringuniversity.service;


import dev.tuvumba.theboringuniversity.domain.Student;
import dev.tuvumba.theboringuniversity.domain.Subject;
import dev.tuvumba.theboringuniversity.domain.Teacher;
import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.repository.StudentRepository;
import dev.tuvumba.theboringuniversity.service.mapper.StudentMapper;
import dev.tuvumba.theboringuniversity.service.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService implements CrudService<StudentDto, String> {

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, SubjectMapper subjectMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.subjectMapper = subjectMapper;
    }
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final SubjectMapper subjectMapper;


    @Override
    public StudentDto save(StudentDto entity) {
        if(entity != null && studentRepository.existsById(entity.getUsername()))
        {
            throw new RuntimeException("Student with username " + entity.getUsername() + " already exists");
        }
        return studentMapper.convertToDto(studentRepository.save(studentMapper.convertToEntity(entity)));
    }

    public List<SubjectDto> getSubjects(String username) {
        Student student = studentRepository.findById(username).orElseThrow(() -> new RuntimeException("Student not found"));
        ArrayList<Subject> subjects = new ArrayList<>(student.getEnrolledSubjects());
        return subjectMapper.convertToDto(subjects);
    }

    public List<StudentDto> findStudentsContaining(String username, int limit) {
        List<StudentDto> byName = studentMapper.convertToDto(studentRepository.findByNameContainingIgnoreCase(username, Limit.of(limit)));
        List<StudentDto> bySurname = studentMapper.convertToDto(studentRepository.findBySurnameContainingIgnoreCase(username, Limit.of(limit)));
        Set<StudentDto> resultSet = new HashSet<>(byName);
        resultSet.addAll(bySurname);
        return resultSet.stream().limit(limit).toList();
    }

    @Override
    public Iterable<StudentDto> findAll() {
        return studentMapper.convertToDto((List<Student>) studentRepository.findAll());
    }

    @Override
    public StudentDto findById(String username) {
        Student student = studentRepository.findById(username).orElseThrow(()-> new RuntimeException("Student not found"));
        return studentMapper.convertToDto(student);
    }

    @Override
    public StudentDto update(String username, StudentDto entity) {
        Student existingStudent = studentRepository.findById(username).orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingStudent.setName(entity.getName());
        existingStudent.setSurname(entity.getSurname());
        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.convertToDto(updatedStudent);
    }

    @Override
    public void delete(String username) {
       studentRepository.deleteById(username);
    }

}
