package com.taskFlow.demo.Service.ServiceImplementation;
import com.taskFlow.demo.Exceptions.DateException;
import com.taskFlow.demo.Exceptions.TaskNotFoundException;
import com.taskFlow.demo.Exceptions.TaskValidationException;
import com.taskFlow.demo.Exceptions.UserNotFoundException;
import com.taskFlow.demo.Model.DTOs.Response.TaskResponseDTO;
import com.taskFlow.demo.Model.DTOs.TagDTO;
import com.taskFlow.demo.Model.DTOs.TaskDTO;
import com.taskFlow.demo.Model.Entities.Status;
import com.taskFlow.demo.Model.Entities.Tag;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Model.Entities.User;
import com.taskFlow.demo.Repository.TagRepo;
import com.taskFlow.demo.Repository.TaskRepo;
import com.taskFlow.demo.Repository.UserRepo;
import com.taskFlow.demo.Service.TaskService;
import com.taskFlow.demo.UserManager.TokenManager;
import com.taskFlow.demo.mapper.TaskMapper;
import com.taskFlow.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.constraints.NotNull;
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
    private  final TokenManager tokenManager;
    private final UserMapper userMapper;

    @Autowired
    public TaskServiceImplementation(UserRepo userRepo, TaskRepo taskRepo, TaskMapper taskMapper, TagRepo tagRepo,TokenManager tokenManager,
                                     UserMapper userMapper) {
        this.taskRepo = taskRepo;
        this.taskMapper = taskMapper;
        this.userRepo = userRepo;
        this.tagRepo = tagRepo;
        this.tokenManager=tokenManager;
        this.userMapper = userMapper;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDto) {
        validateTaskConstraints(taskDto);
        tokenManager.validateReassignments(taskDto.getCreatedBy());
        System.out.println(taskDto.getAssignmentDay());
        Task task =taskMapper.toEntity(taskDto);
        System.out.println(task.getAssignmentDay());
        task = taskRepo.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDto) {
        validateTaskConstraints(taskDto);
        tokenManager.validateReassignments(taskDto.getCreatedBy());
        tokenManager.validateDeletions(userMapper.toEntity(taskDto.getCreatedBy()));

        Task existingTask = taskRepo.findById(taskDto.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        validateUpdateTask(existingTask);

        taskMapper.updateTaskFromDto(taskDto, existingTask);

        existingTask = taskRepo.save(existingTask);

        return taskMapper.toDto(existingTask);
    }


    @Override
    public void deleteTask(Long taskId, Long id) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        User loggedInUser =userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("the user not found"));
        tokenManager.validateDeletions(loggedInUser);

        taskRepo.delete(task);
    }

    @Override
    public TaskResponseDTO getTask(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.toResponseDto(taskMapper.toDto(task));
    }
    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepo.findAll();
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public TaskDTO replaceTask(Long initialTaskId, TaskDTO newTaskDto, User manager) {
        Task initialTask = taskRepo.findById(initialTaskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        validateTaskReplacement(manager, initialTask);
        validateTaskConstraints(newTaskDto);
        Task newTask = taskMapper.toEntity(newTaskDto);
        newTask.setCreatedBy(manager);
        processTags(newTask, newTaskDto.getTags());
        newTask = taskRepo.save(newTask);
        initialTask.setReplacedBy(newTask);
        taskRepo.save(initialTask);
        tokenManager.validateReplacements(manager);
        return taskMapper.toDto(newTask);
    }

    private void validateTaskReplacement(User manager, Task initialTask) {
        if (!initialTask.getStatus().equals(Status.Doing)) {
            throw new TaskValidationException("Only open tasks can be replaced");
        }

        if (!initialTask.getCreatedBy().equals(manager)) {
            throw new TaskValidationException("Only the creator of the task can replace it");
        }
    }


    private void validateTaskConstraints(TaskDTO taskDto) {
        validateStartTime(taskDto.getAssignmentDay(), taskDto.getStartTime());
        ValidateTagNumber(taskDto);
        validateDeadline(taskDto);
        if (taskDto.getAssignedTo() != null && !taskDto.getAssignedTo().equals(taskDto.getCreatedBy())) {
            throw new TaskValidationException("A user can assign tasks only to themselves");
        }
    }


    private void ValidateTagNumber(TaskDTO taskDto) {
        List<TagDTO> tags = taskDto.getTags();
        if (tags == null || tags.size() < 2) {
            throw new TaskValidationException("A task must have at least two tags");
        }
    }

    private void validateStartTime(@NotNull(message = "Assignment day cannot be null") Date assignmentDay, LocalTime startTime) {
        Instant assignmentInstant = assignmentDay.toInstant();
        LocalDateTime assignmentDateTime = LocalDateTime.ofInstant(assignmentInstant, ZoneId.systemDefault())
                .with(startTime);

        if (assignmentDateTime.isBefore(LocalDateTime.now())) {
            throw new DateException("Task cannot be created or updated with an assignment date in the past");
        }
    }

    private void validateDeadline(TaskDTO taskDto) {
        if (taskDto.getAssignmentDay() == null || taskDto.getDeadline() == null) {
            throw new TaskValidationException("Assignment day and deadline are required");
        }

        LocalDate assignmentDate = taskDto.getAssignmentDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate deadlineDate = taskDto.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysDifference = ChronoUnit.DAYS.between(assignmentDate, deadlineDate);

        if (daysDifference < 3) {
            throw new TaskValidationException("Deadline must be at least 3 days from the assignment day.");
        }
    }

    private void validateUpdateTask(Task task) {
        if (task.getStatus() == Status.Done) {
            if (task.getDeadline() != null) {
                LocalDateTime deadlineDateTime = LocalDateTime.ofInstant(
                        task.getDeadline().toInstant(),
                        ZoneId.systemDefault()
                );
                if (LocalDateTime.now().isAfter(deadlineDateTime)) {
                    throw new TaskValidationException("Task cannot be marked as completed after the deadline");
                }
            } else {
                throw new TaskValidationException("Task deadline is missing");
            }
        } else {
            throw new IllegalStateException("Cannot update a task that is not marked as completed");
        }
    }

    private void processTags(Task task, List<TagDTO> taskTags) {
        List<Tag> existingTags = new ArrayList<>();
        for (TagDTO tag : taskTags) {
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
//    @Override
//    public List<TaskDTO> getManagerOverview(Long managerId, String tag, String timeframe) {
//        List<Task> tasks = taskRepo.getTasksForManagerOverview(managerId, tag, timeframe, LocalDate.now(), LocalDate.now());
//
//        // Convert Task entities to TaskDTOs using your mapper
//        List<TaskDTO> taskDTOs = tasks.stream()
//                .map(taskMapper::toDto)
//                .collect(Collectors.toList());
//
//        return taskDTOs;
//    }
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processTasks() {

        grantDoubleModificationTokensIfNoResponse();
        markTasksAsNotDone();
    }


    @Override
    @Transactional
    public void grantDoubleModificationTokensIfNoResponse() {
        LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);
        List<Task> pendingChangeRequests = taskRepo.findPendingChangeRequestsOlderThan(twelveHoursAgo);

        for (Task task : pendingChangeRequests) {
            User user = task.getCreatedBy();
            tokenManager.grantDoubleModificationTokens(user);
        }
    }

    @Transactional
    public void markTasksAsNotDone() {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<Task> overdueTasks = taskRepo.findOverdueTasks(twentyFourHoursAgo);

        for (Task task : overdueTasks) {
            task.setStatus(Status.Expired);
            taskRepo.save(task);
        }
}



}