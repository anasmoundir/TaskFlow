package com.taskFlow.demo.Model.DTOs.Response;

import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long id;

    @NotNull(message = "assignment day cannot be null")
    private Date assignmentDay;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    private Date deadline;

    private Status status;

    @NotNull(message = "Tags cannot be null")
    private List<Tag> tags;


}
