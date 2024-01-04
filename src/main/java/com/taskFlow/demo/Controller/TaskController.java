package com.taskFlow.demo.Controller;

import com.taskFlow.demo.Model.DTOs.Response.TaskResponseDTO;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Service.ServiceImplementation.TaskServiceImplementation;
import com.taskFlow.demo.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private  final TaskServiceImplementation taskService;
    private  final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskServiceImplementation taskService,TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper= taskMapper;
    }


    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto) {
        TaskDTO createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDto) {
        taskDto.setId(taskId);
        TaskDTO updatedTask = taskService.updateTask(taskDto);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
        public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @RequestBody Long ownerId) {
        taskService.deleteTask(taskId,ownerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTask(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        List<TaskResponseDTO> taskResponseDTOs = tasks.stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(taskResponseDTOs, HttpStatus.OK);
    }
    @PostMapping("/trigger-actions")
    public ResponseEntity<String> triggerActions() {
        taskService.grantDoubleModificationTokensIfNoResponse();
        taskService.markTasksAsNotDone();

        return ResponseEntity.ok("Actions triggered successfully.");
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledMarkTasksAsNotDone() {
        taskService.markTasksAsNotDone();
    }
    }
