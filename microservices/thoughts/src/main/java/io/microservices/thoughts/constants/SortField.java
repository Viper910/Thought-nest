package io.microservices.thoughts.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    TOTAL_LIKES("totalLikes"),
    TOTAL_COMMENTS("totalComments"),
    CREATED_TIME("createdAt"),
    UPDATED_TIME("updatedAt");

    private final String databaseFieldName;
}