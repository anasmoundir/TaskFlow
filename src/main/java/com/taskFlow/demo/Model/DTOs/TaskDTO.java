package com.taskFlow.demo.Model.DTOs;

import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Model.Entities.User;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @NotNull(message = "body cannot be null")
    private String body;
    @NotNull(message = "Assignment day cannot be null")
    private Date assignmentDay;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;


    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;

    @NotNull(message = "Deadline cannot be null")
    private Date deadline;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Created by user cannot be null")
    @Valid
    private UserDTO createdBy;

    @Valid
    private UserDTO assignedTo;

    @NotNull(message = "Tags list cannot be null")
    @Size(min = 2, message = "At least one tag must be specified")
    @Valid
    private List<@Valid TagDTO> tags;
}