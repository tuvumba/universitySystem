package dev.tuvumba.theboringuniversity.service.mapper;


import dev.tuvumba.theboringuniversity.domain.Student;
import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    private final ModelMapper modelMapper;

    public StudentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Student convertToEntity(StudentDto studentDto) {
        return modelMapper.map(studentDto, Student.class);
    }

    public StudentDto convertToDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public List<StudentDto> convertToDto(List<Student> students) {
        return students.stream().
                map(studentDto -> modelMapper.map(studentDto, StudentDto.class)).
                collect(Collectors.toList());
    }

}
