package com.taskFlow.demo.Service;

import com.taskFlow.demo.Model.DTOs.Response.TaskResponseDTO;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface TaskService {
    TaskDTO createTask(TaskDTO taskDto);
    TaskDTO updateTask(TaskDTO taskDto);

    void deleteTask(Long taskId, Long id);

    TaskResponseDTO getTask(Long taskId);

    List<TaskDTO> getAllTasks();

    TaskDTO replaceTask(Long initialTaskId, TaskDTO newTaskDto, User manager);

    @Transactional
    void grantDoubleModificationTokensIfNoResponse();
}
