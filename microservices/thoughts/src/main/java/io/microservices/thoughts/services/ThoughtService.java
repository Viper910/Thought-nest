package io.microservices.thoughts.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import io.microservices.thoughts.constants.ThoughtsCRUDConstants;
import io.microservices.thoughts.dto.ThoughtResponse;
import io.microservices.thoughts.dto.kafkaEventsModel.ThoughtEvent;
import io.microservices.thoughts.models.Thought;

public interface ThoughtService {
    ThoughtResponse sendThoughtEventToTopic(ThoughtEvent thought, ThoughtsCRUDConstants eventType);
    Optional<Thought> getThoughtById(String thoughId);
    Optional<List<Thought>> getThoughtsByUserId(Pageable pageable,String userId);
    Optional<List<Thought>> getAllThoughts(Pageable pageable);
}
