package io.microservices.thoughts.controller;

import io.microservices.thoughts.constants.SortField;
import io.microservices.thoughts.constants.ThoughtsCRUDConstants;
import io.microservices.thoughts.customExceptions.ThoughtNotFoundException;
import io.microservices.thoughts.kafkaEventsModel.ThoughtEvent;
import io.microservices.thoughts.models.ApiResponse;
import io.microservices.thoughts.models.Thought;
import io.microservices.thoughts.models.ThoughtResponse;
import io.microservices.thoughts.services.ThoughtServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/thought")
public class ThoughtsController {

    private final Logger LOGGER = LoggerFactory.getLogger(ThoughtsController.class);

    @Autowired
    private ThoughtServiceImpl thoughtService;

    @GetMapping("/healthCheck")
    public ResponseEntity<ApiResponse<Thought>> healthCheck() {
        ApiResponse<Thought> apiResponse = ApiResponse.success(null, "Thought-Microservice is running.");
        return ResponseEntity.status(200).body(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ThoughtResponse>> createNewThought(@RequestBody ThoughtEvent event) {
        try {
            event.setCreatedAt(System.currentTimeMillis());
            event.setUpdatedAt(System.currentTimeMillis());
            LOGGER.debug("Event Recieved:{}", event);
            if (event.getUserId() == null || event.getUserId().isEmpty()) {
                LOGGER.error("Unauthorized access need userId.");
                throw new Exception("User Id Not been provided.");
            }
            ThoughtResponse thoughtResponse = thoughtService.sendThoughtEventToTopic(event,
                    ThoughtsCRUDConstants.CREATE);
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.success(thoughtResponse,
                    "Thought creation request accepted. You will be notified when processing is complete");
            return ResponseEntity.status(202).body(apiResponse);
        } catch (Exception e) {
            LOGGER.error("creation request due to an internal error {}", e.getMessage());
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.error(
                    "Unable to process thought creation request due to an internal error. Please check your input and try again.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<ThoughtResponse>> deleteThought(@PathVariable String id) {
        try {
            ThoughtEvent event = new ThoughtEvent();
            event.setThoughtId(id);
            event.setUpdatedAt(System.currentTimeMillis());
            // if(event.getUserId() == null || event.getUserId().isEmpty()){
            // System.out.println(event.getUserId());
            // throw new Exception("User Id Not been provided.");
            // }
            ThoughtResponse thoughtResponse = thoughtService.sendThoughtEventToTopic(event,
                    ThoughtsCRUDConstants.DELETE);
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.success(thoughtResponse,
                    "Delete event for thought [" + id + "] received and is being processed.");
            return ResponseEntity.status(202).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.error(
                    "Error encountered while dispatching delete event for thought[" + id + "].Event not published.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ThoughtResponse>> updateThought(@RequestBody ThoughtEvent event) {
        String id = event.getThoughtId();
        try {
            event.setUpdatedAt(System.currentTimeMillis());
            if (event.getUserId() == null || event.getUserId().isEmpty()) {
                throw new Exception("User Id Not been provided.");
            }
            ThoughtResponse thoughtResponse = thoughtService.sendThoughtEventToTopic(event,
                    ThoughtsCRUDConstants.UPDATE);
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.success(thoughtResponse,
                    "Update event for thought [" + id + "] received and is being processed.");
            return ResponseEntity.status(202).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<ThoughtResponse> apiResponse = ApiResponse.error(
                    "Error encountered while dispatching update event for thought[" + id + "].Event not published.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Thought>> getThoughById(@PathVariable String id) {
        try {
            Optional<Thought> thought = thoughtService.getThoughtById(id);
            if (thought.isPresent()) {
                ApiResponse<Thought> apiResponse = ApiResponse.success(thought.get(),
                        "Fetched Successfully. ThoughtId:" + id);
                return ResponseEntity.status(200).body(apiResponse);
            } else {
                throw new ThoughtNotFoundException("ThoughId:+" + id + " + not present id DB.");
            }
        } catch (Exception e) {
            ApiResponse<Thought> apiResponse = ApiResponse.error("Thought ID:-[" + id + "] do not exist anymore.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

    @GetMapping("/query")
    public ResponseEntity<ApiResponse<List<Thought>>> getThoughts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizePerPage,
            @RequestParam(defaultValue = "UPDATED_TIME") List<SortField> sortField,
            @RequestParam(defaultValue = "")String sortDirection) {
        try {
            Sort sort = thoughtService.createFilterQuery(sortField, sortDirection);
            Pageable pageable = PageRequest.of(page, sizePerPage, sort);
            Optional<List<Thought>> thoughts = thoughtService.getAllThoughts(pageable);
            if (thoughts.isPresent()) {
                ApiResponse<List<Thought>> apiResponse = ApiResponse.success(thoughts.get(), "Fetched Successfully.");
                return ResponseEntity.status(200).body(apiResponse);
            } else {
                throw new ThoughtNotFoundException("Thoughts collection is empty.");
            }
        } catch (Exception e) {
            ApiResponse<List<Thought>> apiResponse = ApiResponse.error("Unable to fetch thoughts.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Thought>>> getThoughts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int sizePerPage) {
        try {
            Pageable pageable = PageRequest.of(page, sizePerPage);
            Optional<List<Thought>> thoughts = thoughtService.getAllThoughts(pageable);
            if (thoughts.isPresent()) {
                ApiResponse<List<Thought>> apiResponse = ApiResponse.success(thoughts.get(), "Fetched Successfully.");
                return ResponseEntity.status(200).body(apiResponse);
            } else {
                throw new ThoughtNotFoundException("Thoughts collection is empty.");
            }
        } catch (Exception e) {
            ApiResponse<List<Thought>> apiResponse = ApiResponse.error("Unable to fetch thoughts.",
                    Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

}
