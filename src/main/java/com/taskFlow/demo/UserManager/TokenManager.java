package com.taskFlow.demo.UserManager;

import com.taskFlow.demo.Exceptions.TaskValidationException;
import com.taskFlow.demo.Model.DTOs.UserDTO;
import com.taskFlow.demo.Model.Entities.Task;
import com.taskFlow.demo.Model.Entities.User;
import com.taskFlow.demo.Repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TokenManager {
    private final TaskRepo taskRepo;

    public TokenManager(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    private static final int MAX_REASSIGNMENTS_PER_DAY = 2;
    private static final int MAX_DELETIONS_PER_MONTH = 1;
    private static final int MAX_REPLACEMENTS_PER_DAY = 2;
    private Map<Long, Integer> reassignmentTokens = new HashMap<>();
    private Map<Long, Integer> deletionTokens = new HashMap<>();
    private Map<Long, Integer> replacementTokens = new HashMap<>();

    public void validateReassignments(UserDTO manager) {
        long managerId = manager.getId();
        validateTokenLimit(reassignmentTokens, managerId, MAX_REASSIGNMENTS_PER_DAY,
                "Manager has reached the maximum daily reassignment limit");
    }

    public void validateDeletions(User manager) {
        long managerId = manager.getId();
        validateTokenLimit(deletionTokens, managerId, MAX_DELETIONS_PER_MONTH,
                "Manager has reached the maximum monthly deletion limit");
    }

    public void validateReplacements(User manager) {
        long managerId = manager.getId();

        int replacementCount = replacementTokens.getOrDefault(managerId, 0);
        if (replacementCount >= MAX_REPLACEMENTS_PER_DAY) {
            throw new TaskValidationException("Manager has reached the maximum daily replacement limit");
        }

        replacementTokens.put(managerId, replacementCount + 1);
    }

    private void validateTokenLimit(Map<Long, Integer> tokens, long managerId, int maxLimit, String errorMessage) {
        int count = tokens.getOrDefault(managerId, 0);
        if (count >= maxLimit) {
            throw new TaskValidationException(errorMessage);
        }
        tokens.put(managerId, count + 1);
    }



    public void grantDoubleModificationTokens(User user) {
        long userId = user.getId();

        LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);
        List<Task> pendingChangeRequests = taskRepo.findPendingChangeRequestsOlderThan(twelveHoursAgo, userId);

        if (!pendingChangeRequests.isEmpty()) {
            grantModificationTokens(user, 2);
        }
    }

    private void grantModificationTokens(User user, int tokensToGrant) {
        long userId = user.getId();

        int reassignmentCount = reassignmentTokens.getOrDefault(userId, 0);
        reassignmentTokens.put(userId, reassignmentCount + tokensToGrant);

        int deletionCount = deletionTokens.getOrDefault(userId, 0);
        deletionTokens.put(userId, deletionCount + tokensToGrant);

        int replacementCount = replacementTokens.getOrDefault(userId, 0);
        replacementTokens.put(userId, replacementCount + tokensToGrant);
    }
}