package com.taskFlow.demo.UserManager;

import com.taskFlow.demo.Exceptions.TaskValidationException;
import com.taskFlow.demo.Model.Entities.User;

import java.util.HashMap;
import java.util.Map;

public class TokenManager {
    private static final int MAX_REASSIGNMENTS_PER_DAY = 2;
    private static final int MAX_DELETIONS_PER_MONTH = 1;

    private Map<Long, Integer> reassignmentTokens = new HashMap<>();
    private Map<Long, Integer> deletionTokens = new HashMap<>();

    public void validateReassignments(User manager) {
        long managerId = manager.getId();
        validateTokenLimit(reassignmentTokens, managerId, MAX_REASSIGNMENTS_PER_DAY,
                "Manager has reached the maximum daily reassignment limit");
    }

    public void validateDeletions(User manager) {
        long managerId = manager.getId();
        validateTokenLimit(deletionTokens, managerId, MAX_DELETIONS_PER_MONTH,
                "Manager has reached the maximum monthly deletion limit");
    }

    private void validateTokenLimit(Map<Long, Integer> tokens, long managerId, int maxLimit, String errorMessage) {
        int count = tokens.getOrDefault(managerId, 0);
        if (count >= maxLimit) {
            throw new TaskValidationException(errorMessage);
        }
        tokens.put(managerId, count + 1);
    }
}
