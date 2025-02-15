package dev.tuvumba.theboringuniversity.service;

import dev.tuvumba.theboringuniversity.domain.Subject;
import dev.tuvumba.theboringuniversity.domain.Teacher;
import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.domain.dto.TeacherDto;
import dev.tuvumba.theboringuniversity.repository.TeacherRepository;
import dev.tuvumba.theboringuniversity.service.mapper.SubjectMapper;
import dev.tuvumba.theboringuniversity.service.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class TeacherService implements CrudService<TeacherDto, String> {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository TeacherRepository, TeacherMapper TeacherMapper, dev.tuvumba.theboringuniversity.service.mapper.SubjectMapper subjectMapper, TeacherMapper teacherMapper, TeacherRepository teacherRepository) {
        this.TeacherRepository = TeacherRepository;
        this.TeacherMapper = TeacherMapper;
        SubjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
    }
    private final TeacherMapper TeacherMapper;
    private final TeacherRepository TeacherRepository;
    private final SubjectMapper SubjectMapper;


    @Override
    public TeacherDto save(TeacherDto entity) {
        if(TeacherRepository.existsById(entity.getUsername()))
            throw new RuntimeException("Teacher with username " + entity.getUsername() + " already exists");
        return TeacherMapper.convertToDto(TeacherRepository.save(TeacherMapper.convertToEntity(entity)));
    }

    public List<SubjectDto> getSubjects(String username) {
        Teacher teacher = TeacherRepository.findById(username).orElseThrow(() -> new RuntimeException("Teacher not found"));
        ArrayList<Subject> subjects = new ArrayList<>(teacher.getTaughtSubjects());
        return SubjectMapper.convertToDto(subjects);
    }


    @Override
    public Iterable<TeacherDto> findAll() {
        return TeacherMapper.convertToDto((List<Teacher>) TeacherRepository.findAll());
    }

    @Override
    public TeacherDto findById(String username) {
        Teacher teacher = TeacherRepository.findById(username).orElseThrow(()-> new RuntimeException("Teacher not found"));
        return TeacherMapper.convertToDto(teacher);
    }

    public TeacherDto findByUsername(String username) {
        return teacherMapper.convertToDto(teacherRepository.findById(username).orElseThrow(() -> new RuntimeException("Teacher not found")));
    }

    public List<TeacherDto> findTeachersContaining(String query, int limit) {
        List<TeacherDto> byName = TeacherMapper.convertToDto(teacherRepository.findByNameContainingIgnoreCase(query, Limit.of(limit)));
        List<TeacherDto> bySurname = TeacherMapper.convertToDto(teacherRepository.findBySurnameContainingIgnoreCase(query, Limit.of(limit)));
        Set<TeacherDto> resultSet = new HashSet<>(byName);
        resultSet.addAll(bySurname);
        return resultSet.stream().limit(limit).toList();
    }

    @Override
    public TeacherDto update(String username, TeacherDto entity) {
        Teacher existingTeacher = TeacherRepository.findById(username).orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingTeacher.setName(entity.getName());
        existingTeacher.setSurname(entity.getSurname());
        existingTeacher.setTitles(entity.getTitles());
        Teacher updatedTeacher = TeacherRepository.save(existingTeacher);
        return TeacherMapper.convertToDto(updatedTeacher);
    }

    @Override
    public void delete(String id) {
        TeacherRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String getTeacherWithMostStudents() {
        Teacher teacher = TeacherRepository.findTeacherWithMostStudents();
        if(teacher == null)
            return null;
        else
        {
            TeacherDto dto = TeacherMapper.convertToDto(teacher);

            int count = 0;
            for(Subject s : teacher.getTaughtSubjects())
            {
                count += s.getStudents().size();
            }

            return dto.getTitles() + " " + dto.getName() + " " + dto.getSurname() + " with " + count + " students.";
        }

    }
}

