package com.taskFlow.demo.Service.ServiceImplementation;

import com.taskFlow.demo.Exceptions.TaskNotFoundException;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Repository.TaskRepo;
import com.taskFlow.demo.Service.TaskService;
import com.taskFlow.demo.mapper.TaskMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
    public class TaskServiceImplementation implements TaskService {
    private final  TaskRepo taskRepo;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImplementation(TaskRepo taskRepo,TaskMapper taskMapper){
    this.taskRepo =taskRepo;
    this.taskMapper=taskMapper;
    }
    @Override
    public TaskDTO createTask(TaskDTO taskDto) {
//        validateTaskConstraints(taskDto);

        Task task = taskMapper.toEntity(taskDto);
        task = taskRepo.save(task);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDto) {
//        validateTaskConstraints(taskDto);

        Task existingTask = taskRepo.findById(taskDto.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (existingTask.getStatus() == Status.Done) {
            throw new IllegalStateException("Cannot update a completed task");
        }

        Task updatedTask = taskMapper.toEntity(taskDto);
        updatedTask.setId(existingTask.getId());
        updatedTask = taskRepo.save(updatedTask);

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));


        taskRepo.delete(task);
    }

    @Override
    public TaskDTO getTask(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepo.findAll();
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateTaskConstraints(TaskDTO taskDto) {


    }

}
