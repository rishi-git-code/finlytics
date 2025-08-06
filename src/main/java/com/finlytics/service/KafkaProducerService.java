package com.finlytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlytics.dto.UserReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String topic = "user-events";

    public void sendUserEvent(UserReqDTO userReqDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(userReqDTO);
            System.out.println("User event published to Kafka: {}"+ message);
            kafkaTemplate.send(topic,userReqDTO.getUserId(),message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
