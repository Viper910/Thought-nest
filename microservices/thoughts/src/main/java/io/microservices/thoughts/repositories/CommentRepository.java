package io.microservices.thoughts.repositories;

import io.microservices.thoughts.models.Comment;
import io.microservices.thoughts.models.Thought;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface CommentRepository extends MongoRepository<Comment,String> {

    Optional<List<Comment>> findAllByThoughtOrderByUpdatedAtDesc(Thought thought);
    Optional<List<Comment>> findAllByThoughtOrderByCreatedAtDesc(Thought thought);
    Optional<List<Comment>> findAllByThought(Thought thought);

}
