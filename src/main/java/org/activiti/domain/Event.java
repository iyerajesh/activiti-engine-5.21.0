package org.activiti.domain;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Event {

	/*
	 * @author Rajesh Iyer
	 */

	private static final Logger logger = LoggerFactory.getLogger(Event.class);

	private String name;;
	private String source;
	private DateTime timestamp;
	private Map<String, Object> eventVariables = new HashMap<String, Object>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, Object> getEventVariables() {
		return eventVariables;
	}

	public void setEventVariables(Map<String, Object> eventVariables) {
		this.eventVariables = eventVariables;
	}

}
