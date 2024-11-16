package io.microservices.thoughts.services;

import io.microservices.thoughts.constants.SortField;
import io.microservices.thoughts.constants.ThoughtsCRUDConstants;
import io.microservices.thoughts.dto.ThoughtResponse;
import io.microservices.thoughts.dto.kafkaEventsModel.ThoughtEvent;
import io.microservices.thoughts.exception.ThoughtNotFoundException;
import io.microservices.thoughts.kafkaServices.ThoughtsProducer;
import io.microservices.thoughts.models.Thought;
import io.microservices.thoughts.repositories.ThoughtsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ThoughtServiceImpl implements ThoughtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThoughtServiceImpl.class);


    @Autowired
    private ThoughtsProducer thoughtsProducer;

    @Autowired
    private ThoughtsRepository thoughtsRepository;

    @Override
    public ThoughtResponse sendThoughtEventToTopic(ThoughtEvent thought, ThoughtsCRUDConstants eventType) {
        try{
            LOGGER.debug("THOUGHT EVENTS SENT:{}",thought.toString());
            String eventId = UUID.randomUUID().toString();
            thought.setEventId(eventId);
            thoughtsProducer.send(thought,eventType);
            return new ThoughtResponse(eventId);            
        }
        catch (Exception e){
            LOGGER.error("Unable to send data to topic: {}",e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Thought> getThoughtById(String thoughId) {
        try {
            LOGGER.debug("Fetching thought for thoughtId:{} from Db",thoughId);
            return thoughtsRepository.findById(thoughId);
        } catch (Exception e) {
            LOGGER.error("Not Able to fetch thought for thoughtID:{} from DB",thoughId);
            throw new ThoughtNotFoundException("ThoughtId not exist. ThoughtId:-" + thoughId,e);
        }
    }

    @Override
    public Optional<List<Thought>> getThoughtsByUserId(Pageable pageable, String userId) {
        try {
            LOGGER.debug("Fetching thoughts for userId:{} from Db",userId);
            return thoughtsRepository.findAllByUserId(new ObjectId(userId),pageable);
        } catch (Exception e) {
            LOGGER.error("Not Able to fetch thoughts for userID:{} from DB");
            throw new ThoughtNotFoundException("Thoughts for userId not exists. UserId:-" + userId,e);
        }
    }

    @Override
    public Optional<List<Thought>> getAllThoughts(Pageable pageable) {
        try {
            LOGGER.debug("Fetching thoughts all thoughts");
            Page<Thought> thoughts = thoughtsRepository.findAll(pageable);
            return Optional.of(thoughts.getContent());
        } catch (Exception e) {
            LOGGER.error("Not Able to fetch thoughts.{}",e.getMessage());
            throw new ThoughtNotFoundException("Thoughts Not present",e);
        }
    }

    public Sort createFilterQuery(List<SortField> sortFields,String ordersDirections) throws BadRequestException{
        //ASC:DESC:AESC 
        final String defaultOrder = "DESC";
        
        boolean foundCreateAtAndUpdateAt = sortFields.contains(SortField.CREATED_TIME) && sortFields.contains(SortField.UPDATED_TIME);
        if(foundCreateAtAndUpdateAt){
            throw new BadRequestException("Bad Query: There should be either filter by createAt or UpdateAt not both.");
        }
        String[] orders = ordersDirections.split(":");

        if(orders.length > sortFields.size()){
            throw new BadRequestException("Bad Query: There should be one ordersDirection for each sortField.");
        }
        else if(ordersDirections.length() == 0){
            orders = new String[sortFields.size()];
            Arrays.setAll(orders , (_) -> defaultOrder);
        }

        int i=0;
        List<Sort.Order> queryOrders = new ArrayList<>();
        for(SortField sortField:sortFields){
            switch (orders[i]) {
                case "DESC":
                    queryOrders.add(Sort.Order.desc(sortField.getDatabaseFieldName()));
                    break;
                case "ASC":
                    queryOrders.add(Sort.Order.asc(sortField.getDatabaseFieldName()));
                    break;
                default:
                    throw new BadRequestException("Bad Query: There should be one ordersDirection for each sortField either DESC or ASC.");
            }
            i++;
        }
        return Sort.by(queryOrders);
    }

    
}
