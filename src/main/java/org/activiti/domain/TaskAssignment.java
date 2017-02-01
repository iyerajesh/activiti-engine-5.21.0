package org.activiti.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Rajesh Iyer
 */

public class TaskAssignment {

	private static final Logger logger = LoggerFactory.getLogger(TaskAssignment.class);

	private String claimNumber;
	private String taskId;

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
