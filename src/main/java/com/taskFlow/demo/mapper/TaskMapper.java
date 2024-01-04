    package com.taskFlow.demo.mapper;

    import com.taskFlow.demo.Model.DTOs.Response.TaskResponseDTO;
    import com.taskFlow.demo.Model.DTOs.TaskDTO;
    import com.taskFlow.demo.Model.Entities.Task;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.MappingTarget;
    import org.mapstruct.Named;

    import java.time.LocalDate;
    import java.time.ZoneId;
    import java.util.Date;

    @Mapper(componentModel = "spring", uses = {UserMapper.class, TagMapper.class})
    public interface TaskMapper {
        TaskDTO toDto(Task task);
        @Mapping(target = "assignmentDay", source = "assignmentDay", qualifiedByName = "dateToLocalDate")
        @Mapping(target = "deadline", source = "deadline", qualifiedByName = "dateToLocalDate")
        Task toEntity(TaskDTO taskDto);
        @Named("dateToLocalDate")
        default LocalDate dateToLocalDate(Date date) {
            return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        }
        void updateTaskFromDto(TaskDTO taskDto, @MappingTarget Task task);
        TaskResponseDTO toResponseDto(TaskDTO taskDTO);
    }
