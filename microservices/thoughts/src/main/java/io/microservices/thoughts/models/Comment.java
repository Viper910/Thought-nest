package io.microservices.thoughts.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    @Field(targetType= FieldType.OBJECT_ID)
    private String id;

    private String text;

    private String userId;

    @DBRef(lazy = true)
    private Thought thought;

    private long likes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
