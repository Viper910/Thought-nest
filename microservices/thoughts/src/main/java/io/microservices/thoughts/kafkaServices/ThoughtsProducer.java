package io.microservices.thoughts.kafkaServices;


import io.microservices.thoughts.constants.ThoughtEventKafkaHeaders;
import io.microservices.thoughts.constants.ThoughtsCRUDConstants;
import io.microservices.thoughts.dto.kafkaEventsModel.ThoughtEvent;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class ThoughtsProducer {

    private final NewTopic newTopic;

    private final KafkaTemplate<String, ThoughtEvent> kafkaTemplate;

    public ThoughtsProducer(NewTopic newTopic,KafkaTemplate<String, ThoughtEvent> kafkaTemplate){
        this.newTopic = newTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ThoughtEvent thought, ThoughtsCRUDConstants eventType){
        Message<ThoughtEvent> message = MessageBuilder.
                withPayload(thought)
                .setHeader(ThoughtEventKafkaHeaders.TOPIC,newTopic.name())
                .setHeader(ThoughtEventKafkaHeaders.EVENT_TYPE,eventType.name())
                .setHeader(ThoughtEventKafkaHeaders.TIMESTAMP, System.currentTimeMillis())
                .build();

        kafkaTemplate.send(message);
    }

}
