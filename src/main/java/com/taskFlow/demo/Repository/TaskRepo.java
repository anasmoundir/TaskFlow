package com.taskFlow.demo.Repository;

import com.taskFlow.demo.Model.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface TaskRepo extends JpaRepository<Task,Long> {
    @Query("SELECT t FROM Task t WHERE t.status = 'PENDING_CHANGE' AND t.assignmentDay < :twelveHoursAgo")
    List<Task> findPendingChangeRequestsOlderThan(LocalDateTime twelveHoursAgo);

    @Query("SELECT t FROM Task t WHERE t.status <> 'DONE' AND t.deadline < :twentyFourHoursAgo")
    List<Task> findOverdueTasks(LocalDateTime twentyFourHoursAgo);
    @Query("SELECT t FROM Task t WHERE t.status = 'PENDING' AND t.createdBy.id = :userId AND t.assignmentDay < :twelveHoursAgo")
    List<Task> findPendingChangeRequestsOlderThan(@Param("twelveHoursAgo") LocalDateTime twelveHoursAgo, @Param("userId") Long userId);
}
