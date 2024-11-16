package io.microservices.thoughts.constants;

import org.springframework.kafka.support.KafkaHeaders;

public class ThoughtEventKafkaHeaders extends KafkaHeaders {
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_TIME = "eventTime";
}
