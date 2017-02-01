package org.activiti;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.domain.Claim;
import org.activiti.domain.Event;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
@Transactional
@PropertySource({ "classpath:${envTarget:rules-service}.properties" })
@PropertySource({ "classpath:${envTarget:claims-service}.properties" })

/*
 * @author Rajesh Iyer
 */

public class WorkAssignmentService {

	private static final Logger logger = LoggerFactory.getLogger(WorkAssignmentService.class);

	@Autowired
	private Environment env;

	@Autowired
	private EventSender eventSender;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	public String findManagerForClaim(String claimNumber) throws JsonProcessingException {

		Map<String, String> data = new HashMap<String, String>();
		data.put("assignClaim", "true");

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.postForObject("http://" + env.getProperty("rules.host") + ":"
				+ env.getProperty("rules.port") + env.getProperty("rules.manager.assignment.uri"), data, String.class);

		logger.debug("The manager assigned:" + response);

		/* Emit the manager assignment event */
		Event event = new Event();
		event.setName("assignManagerToClaim");
		event.setSource("activiti");
		event.setTimestamp(new DateTime());

		Map<String, Object> eventVariables = new HashMap<String, Object>();
		eventVariables.put("claimNumber", claimNumber);
		eventVariables.put("managerAssigned", response);
		event.setEventVariables(eventVariables);

		eventSender.sendEventMessage(event);

		return response;

	}

	public String findAdjusterForClaim(String managerAssigned, String claimNumber) throws IOException {

		logger.debug("The claim number in the RulesService:" + claimNumber);
		logger.debug("The manager assigned to this claim:" + managerAssigned);

		RestTemplate restTemplate = new RestTemplate();
		String claimServiceURL = "http://" + env.getProperty("claimservice.host") + ":"
				+ env.getProperty("claimservice.port") + env.getProperty("claimservice.uri") + claimNumber;

		logger.debug("THE CLAIM SERVICE URL:" + claimServiceURL);

		String jsonStr = restTemplate.getForObject(claimServiceURL, String.class);
		LinkedHashMap<?, ?> claimMap = JsonPath.read(jsonStr, "$._embedded.claimList[0]");

		logger.debug("The claimJson string getting returned...---:" + claimMap);

		Claim claim = new Claim();
		claim.setClaimNumber(new Integer((Integer) claimMap.get("claimNumber")).intValue());
		claim.setMedCode((String) claimMap.get("medCode"));
		claim.setPriority((String) claimMap.get("priority"));
		claim.setState((String) claimMap.get("state"));
		claim.setCompany((String) claimMap.get("company"));

		String adjusterName = restTemplate.postForObject("http://" + env.getProperty("rules.host") + ":"
				+ env.getProperty("rules.port") + env.getProperty("rules.adjuster.assignment.uri"), claim,
				String.class);

		logger.debug("THE ADJUSTER ASSIGNED for CLAIM NUMBER:" + claimNumber + " IS :" + adjusterName
				+ " MANAGER ASSIGNED WAS:" + managerAssigned);

		/* Emit the manager assignment event */
		Event event = new Event();
		event.setName("assignAdjusterToClaim");
		event.setSource("activiti");
		event.setTimestamp(new DateTime());

		Map<String, Object> eventVariables = new HashMap<String, Object>();
		eventVariables.put("claimNumber", claimNumber);
		eventVariables.put("adjusterAssigned", adjusterName);
		eventVariables.put("managerAssigned", managerAssigned);
		event.setEventVariables(eventVariables);

		eventSender.sendEventMessage(event);

		return adjusterName;

	}

}
