package dev.tuvumba.theboringuniversity.service.mapper;


import dev.tuvumba.theboringuniversity.domain.Teacher;
import dev.tuvumba.theboringuniversity.domain.dto.TeacherDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {
    private final ModelMapper modelMapper;

    public TeacherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Teacher convertToEntity(TeacherDto TeacherDto) {
        return modelMapper.map(TeacherDto, Teacher.class);
    }

    public TeacherDto convertToDto(Teacher Teacher) {
        return modelMapper.map(Teacher, TeacherDto.class);
    }

    public List<TeacherDto> convertToDto(List<Teacher> Teachers) {
        return Teachers.stream().
                map(teacherDto -> modelMapper.map(teacherDto, TeacherDto.class)).
                collect(Collectors.toList());
    }
}
