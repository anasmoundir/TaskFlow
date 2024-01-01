package com.taskFlow.demo.Service.ServiceImplementation;
import com.taskFlow.demo.Exceptions.DateException;
import com.taskFlow.demo.Exceptions.TaskNotFoundException;
import com.taskFlow.demo.Exceptions.TaskValidationException;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Repository.TagRepo;
import com.taskFlow.demo.Repository.TaskRepo;
import com.taskFlow.demo.Repository.UserRepo;
import com.taskFlow.demo.Service.TaskService;
import com.taskFlow.demo.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskServiceImplementation implements TaskService {
    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;
    private final UserRepo userRepo;
    private final TagRepo tagRepo;

    @Autowired
    public TaskServiceImplementation(UserRepo userRepo, TaskRepo taskRepo, TaskMapper taskMapper, TagRepo tagRepo) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
        this.userRepo = userRepo;
        this.tagRepo = tagRepo;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDto) {
        validateTaskConstraints(taskDto);
        Task task = taskMapper.toEntity(taskDto);


        task.setAssignementDay(taskDto.getAssignement_day());
        task.setStartTime(taskDto.getStart_time());
        task.setEndTime(taskDto.getEnd_time());
        task.setDeadline(taskDto.getDeadline());
        task.setStatus(taskDto.getStatus());
        task.setCreatedBy(taskDto.getCreatedBy());
        task.setAssignedTo(taskDto.getAssignedTo());

        processTags(task, taskDto.getTags());

        task = taskRepo.save(task);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDto) {
        validateTaskConstraints(taskDto);

        Task existingTask = taskRepo.findById(taskDto.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        validateUpdateTask(existingTask);

        Task updatedTask = taskMapper.toEntity(taskDto);
        updatedTask.setId(existingTask.getId());

        updatedTask.setAssignementDay(taskDto.getAssignement_day());
        updatedTask.setStartTime(taskDto.getStart_time());
        updatedTask.setEndTime(taskDto.getEnd_time());
        updatedTask.setDeadline(taskDto.getDeadline());
        updatedTask.setStatus(taskDto.getStatus());
        updatedTask.setCreatedBy(taskDto.getCreatedBy());
        updatedTask.setAssignedTo(taskDto.getAssignedTo());

        processTags(updatedTask, taskDto.getTags());

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
        validateStartTime(taskDto.getAssignement_day(), taskDto.getStart_time());
        ValidateTagNumber(taskDto);
        validateDeadline(taskDto);
    }



    private void ValidateTagNumber(TaskDTO taskDto) {
        List<Tag> tags = taskDto.getTags();
        if (tags == null || tags.size() < 2) {
            throw new TaskValidationException("A task must have at least two tags");
        }
    }

    private void validateStartTime(Date assignmentDay, LocalTime startTime) {
        Instant instant = assignmentDay.toInstant();
        LocalDateTime assignmentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .with(startTime);

        if (assignmentDateTime.isBefore(LocalDateTime.now())) {
            throw new DateException("Task cannot be created or updated with an assignment date in the past");
        }
    }

    private void validateDeadline(TaskDTO taskDto) {
        if (taskDto.getAssignement_day() == null || taskDto.getDeadline() == null) {
            throw new TaskValidationException("Assignment day and deadline are required");
        }

        Instant assignmentInstant = taskDto.getAssignement_day().toInstant();
        Instant deadlineInstant = taskDto.getDeadline().toInstant();

        LocalDate assignmentDate = LocalDateTime.ofInstant(assignmentInstant, ZoneId.systemDefault()).toLocalDate();
        LocalDate deadlineDate = LocalDateTime.ofInstant(deadlineInstant, ZoneId.systemDefault()).toLocalDate();

        long daysDifference = ChronoUnit.DAYS.between(assignmentDate, deadlineDate);

        if (daysDifference < 3) {
            throw new TaskValidationException("Deadline must be at least 3 days from the assignment day.");
        }
    }

    private void validateUpdateTask(Task task) {
        if (task.getStatus() == Status.Done) {
            throw new IllegalStateException("Cannot update a completed task");
        }
    }

    private void processTags(Task task, List<Tag> taskTags) {
        List<Tag> existingTags = new ArrayList<>();
        for (Tag tag : taskTags) {
            Optional<Tag> existingTag = tagRepo.findById(tag.getId());
            if (existingTag.isPresent()) {
                existingTags.add(existingTag.get());
            } else {
                Tag newTag = new Tag();
                newTag.setLe_nom(tag.getLe_nom());
                newTag.setDescription(tag.getDescription());
                newTag.setImage(tag.getImage());
                existingTags.add(tagRepo.save(newTag));
            }
        }
        task.setTags(new HashSet<>(existingTags));
    }
}