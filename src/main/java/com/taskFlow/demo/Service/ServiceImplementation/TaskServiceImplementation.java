package com.taskFlow.demo.Service.ServiceImplementation;
import com.taskFlow.demo.Exceptions.DateException;
import com.taskFlow.demo.Exceptions.TaskNotFoundException;
import com.taskFlow.demo.Exceptions.TaskValidationException;
import com.taskFlow.demo.Exceptions.UserNotFoundException;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Model.Entities.User;
import com.taskFlow.demo.Repository.TaskRepo;
import com.taskFlow.demo.Repository.UserRepo;
import com.taskFlow.demo.Service.TaskService;
import com.taskFlow.demo.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
    public class TaskServiceImplementation implements TaskService {
    private final  TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    private  final UserRepo userRepo;
    @Autowired
    public TaskServiceImplementation(UserRepo userRepo,TaskRepo taskRepo,TaskMapper taskMapper){
    this.taskRepo =taskRepo;
    this.taskMapper=taskMapper;
    }
    @Override
    public TaskDTO createTask(TaskDTO taskDto) {
        validateTaskConstraints(taskDto);
        ValidateTagNumber(taskDto);
        validateDeadline(taskDto);
        Task task = taskMapper.toEntity(taskDto);
        task = taskRepo.save(task);
        return taskMapper.toDto(task);
    }
    @Override
    public TaskDTO updateTask(TaskDTO taskDto) {
       validateTaskConstraints(taskDto);

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
    if(taskDto.getStart_time().isAfter(LocalTime.now()) ){
        throw new DateException("Tas connot be created in the time previous");
    }   else if(taskDto.getStart_time().isBefore(LocalTime.now())) {
            throw new DateException("Task cannot be created with a start time in the past");
        }
    }
    private void ValidateTagNumber(TaskDTO taskDto) {

        List<Tag> tags = taskDto.getTags();
        if (tags == null || tags.size() < 2) {
            throw new TaskValidationException("A task must have at least two tags");
        }
    }

    private void validateDeadline(TaskDTO taskDto) {
        LocalDate assignmentDate = taskDto.getAssignement_day().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate deadlineDate = taskDto.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysDifference = ChronoUnit.DAYS.between(assignmentDate, deadlineDate);
        if (daysDifference < 3) {
            throw new TaskValidationException("Deadline must be at least 3 days from the assignment day.");
        }
    }

    private  void assignTaskToUser(Long taskId,Long userId)
    {
    Task task = taskRepo.findById(taskId).orElseThrow(()->new TaskNotFoundException("task not found with this is "));
    User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundException("user not found with this id "));
    task.getAssignedUsers()
    }

}
