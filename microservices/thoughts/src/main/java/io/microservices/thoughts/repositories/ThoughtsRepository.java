package io.microservices.thoughts.repositories;

import io.microservices.thoughts.models.Thought;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ThoughtsRepository extends MongoRepository<Thought, String> {

    Optional<List<Thought>> findAllByUserId(ObjectId userid,Pageable pageable);
    Optional<List<Thought>> findByTotalLikesGreaterThan(int minLikes,Pageable pageable);
    Optional<List<Thought>> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Optional<List<Thought>> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Query("{ 'tags': { $in: ?0, $options: 'i' } }")
    Optional<List<Thought>> findByTagsInIgnoreCase(Set<String> tags,Pageable pageable);
}
