package com.taskFlow.demo.Model.DTOs;

import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Model.Entities.User;
import jakarta.persistence.*;
import lombok.*;

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
    private Date Assignement_day;
    private LocalTime start_time;
    private LocalTime end_time;
    private Date deadline;
    private Status status;
    private User createdBy;
    private User assignedTo;
    private List<Tag> tags;





}