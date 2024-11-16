package io.microservices.thoughts.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Document(collection = "thoughts")
public class Thought {

    @Id
    @JsonProperty("id")
    private String id;

    private String thought;

    private String userId;

    private long totalLikes;
    private long totalComments;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Set<String> tags;

    private String eventId;
}



