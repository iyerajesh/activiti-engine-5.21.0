package org.activiti;

import org.activiti.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EventSender {

	private static final Logger logger = LoggerFactory.getLogger(EventSender.class);

	final static String eventTriggerQueue = "UNUM.EVENT.QUEUE";

	@Autowired
	RabbitTemplate rabbitTemplate;

	/*
	 * Sends the "leave" trigger message to the UNUM.CLAIM.NOTIFICATION queue
	 */
	public void sendEventMessage(Event event) throws JsonProcessingException {

		logger.debug("----- Sending event message to queue: UNUM.EVENT.QUEUE...--------");

		ObjectMapper mapper = new ObjectMapper();

		// Object to JSON in String
		String jsonInString = mapper.writeValueAsString(event);

		// RabbitTemplate rabbitTemplate = new
		// RabbitMQConfig().rabbitTemplate();
		rabbitTemplate.convertAndSend(eventTriggerQueue, jsonInString);

	}

}
