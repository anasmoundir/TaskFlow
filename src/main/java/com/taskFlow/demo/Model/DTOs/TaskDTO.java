package com.taskFlow.demo.Model.DTOs;

import com.taskFlow.demo.Model.Entities.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;


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
    private List<Tag> tags;
}
