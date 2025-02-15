package dev.tuvumba.theboringuniversity.service.mapper;

import dev.tuvumba.theboringuniversity.domain.Subject;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {

    private final ModelMapper modelMapper;

    public SubjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Subject convertToEntity(SubjectDto subjectDto) {
        return modelMapper.map(subjectDto, Subject.class);
    }

    public SubjectDto convertToDto(Subject subject) {
        return modelMapper.map(subject, SubjectDto.class);
    }

    public List<SubjectDto> convertToDto(List<Subject> subjects) {
        return subjects.stream().
                map(subjectDto -> modelMapper.map(subjectDto, SubjectDto.class)).
                collect(Collectors.toList());
    }


}
