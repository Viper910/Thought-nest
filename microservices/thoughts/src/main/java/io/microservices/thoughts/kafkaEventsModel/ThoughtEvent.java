package io.microservices.thoughts.kafkaEventsModel;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Set;

@Data
@NoArgsConstructor
public class ThoughtEvent{

    @Id
    private String eventId;
    
    private String thought;
    
    private String userId;
    
    private String thoughtId;

    @CreatedDate
    private long createdAt;

    @LastModifiedDate
    private long updatedAt;

    private Set<String> tags;
}



