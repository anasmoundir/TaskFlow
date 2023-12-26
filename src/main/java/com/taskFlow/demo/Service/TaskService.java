package com.taskFlow.demo.Service;

import com.taskFlow.demo.Model.DTOs.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    TaskDTO createTask(TaskDTO taskDto);
    TaskDTO updateTask(TaskDTO taskDto);
    void deleteTask(Long taskId);
    TaskDTO getTask(Long taskId);
    List<TaskDTO> getAllTasks();
}
