package io.microservices.thoughts.kafkaServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import io.microservices.thoughts.constants.ThoughtsCRUDConstants;
import io.microservices.thoughts.dto.kafkaEventsModel.ThoughtEvent;
import io.microservices.thoughts.models.Thought;
import io.microservices.thoughts.repositories.ThoughtsRepository;
import jakarta.annotation.PreDestroy;

@Service
public class ThoughtsConsumer {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ReentrantLock lock = new ReentrantLock();
    private final List<Thought> createBatch = Collections.synchronizedList(new ArrayList<>());
    private final List<Thought> updateBatch = Collections.synchronizedList(new ArrayList<>());
    private final List<String> deleteBatch = Collections.synchronizedList(new ArrayList<>());
    private final Logger LOGGER = LoggerFactory.getLogger(ThoughtsConsumer.class);

    private long lastCreateBatchTime = System.currentTimeMillis();
    private long lastUpdateBatchTime = System.currentTimeMillis();
    private long lastDeleteBatchTime = System.currentTimeMillis();

    private final int TIME_THRESHOLD_SECONDS = 3;
    private static final int MINIMUM_BATCH_SIZE = 10;

    @Autowired
    private ThoughtsRepository thoughtsRepository;

    public ThoughtsConsumer() {
        // Schedule the batch processing task to run every 4 seconds
        LOGGER.info("Sheduler for thought consumer has been started each batch will be update in {}s and minimum batch size:{}",TIME_THRESHOLD_SECONDS,MINIMUM_BATCH_SIZE);
        scheduler.scheduleAtFixedRate(this::processBatches, 0, 4, TimeUnit.SECONDS);
    }

    @KafkaListener(topics = "THOUGHT_INCOMING_REQUESTS", groupId = "THOUGHT_CONSUMER_1")
    public void thoughtsProcessor(Message<ThoughtEvent> message, @Header("eventType") ThoughtsCRUDConstants eventType) {
        ThoughtEvent event = message.getPayload();

        switch (eventType) {
            case CREATE -> addToCreateBatch(event);
            case UPDATE -> addToUpdateBatch(event);
            case DELETE -> addToDeleteBatch(event);
            default -> LOGGER.warn("Unknown event type: {}", eventType);
        }
    }

    private void addToCreateBatch(ThoughtEvent event) {
        Thought thought = new Thought();
        thought.setThought(event.getThought());
        thought.setTags(event.getTags());
        thought.setUserId(event.getUserId());
        thought.setEventId(event.getEventId());
        createBatch.add(thought);
        LOGGER.debug("Event added to create batch: {}", event);
    }

    private void addToUpdateBatch(ThoughtEvent event) {
        Thought thought = new Thought();
        thought.setThought(event.getThought());
        thought.setTags(event.getTags());
        thought.setUserId(event.getUserId());
        thought.setId(event.getThoughtId());
        thought.setEventId(event.getEventId());
        updateBatch.add(thought);
        LOGGER.debug("Event added to update batch: {}", event);
    }

    private void addToDeleteBatch(ThoughtEvent event) {
        deleteBatch.add(event.getThoughtId());
        LOGGER.debug("Event added to delete batch: {}", event);
    }

    private void processBatches() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();

            if (createBatch.size() >= MINIMUM_BATCH_SIZE || (currentTime - lastCreateBatchTime) >= TimeUnit.SECONDS.toMillis(TIME_THRESHOLD_SECONDS)) {
                batchCreation(new ArrayList<>(createBatch));
                createBatch.clear();
                lastCreateBatchTime = currentTime;
            }
            if (updateBatch.size() >= MINIMUM_BATCH_SIZE || (currentTime - lastUpdateBatchTime) >= TimeUnit.SECONDS.toMillis(TIME_THRESHOLD_SECONDS)) {
                batchUpdation(new ArrayList<>(updateBatch));
                updateBatch.clear();
                lastUpdateBatchTime = currentTime;
            }
            if (deleteBatch.size() >= MINIMUM_BATCH_SIZE || (currentTime - lastDeleteBatchTime) >= TimeUnit.SECONDS.toMillis(TIME_THRESHOLD_SECONDS)) {
                batchDeletion(new ArrayList<>(deleteBatch));
                deleteBatch.clear();
                lastDeleteBatchTime = currentTime;
            }
        } finally {
            lock.unlock();
        }
    }

    // Ensure to shut down the scheduler gracefully
    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void batchCreation(List<Thought> batch){
        try {
            thoughtsRepository.saveAll(batch);
            LOGGER.debug("Batch of size {} got created and stored in DB.",batch.size());
        } catch (Exception e) {
            LOGGER.error("Batch of size {} was unable created and stored in DB.",batch.size());
        }
    }

    private void batchUpdation(List<Thought> batch){
        try {
            thoughtsRepository.saveAll(batch);
            LOGGER.debug("Batch of size {} got updated and stored in DB.",batch.size());
        } catch (Exception e) {
            LOGGER.error("Batch of size {} was unable to update and stored in DB.",batch.size());
        }
    }

    private void batchDeletion(List<String> batch){
        try {
            thoughtsRepository.deleteAllById(batch);
            LOGGER.debug("Batch of size {} got deleted.",batch.size());
        } catch (Exception e) {
            LOGGER.error("Batch of size {} was unable to deleted",batch.size());
        }
    }

}
