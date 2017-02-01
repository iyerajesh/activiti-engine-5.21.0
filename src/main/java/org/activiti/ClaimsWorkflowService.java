package org.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.domain.TaskAssignment;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClaimsWorkflowService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	private static final Logger logger = LoggerFactory.getLogger(ClaimsWorkflowService.class);

	public TaskAssignment startProcess(Map<String, Object> variables) {

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("colonialClaimsWorkflow", variables);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		
		TaskAssignment taskAssignment = new TaskAssignment();
		taskAssignment.setClaimNumber(task.getFormKey());
		taskAssignment.setTaskId(task.getId());

		return taskAssignment;

	}

	public String initiateAdjusterAssignmentTask(String claimNumber, String managerAssigned, String taskId,
			String processInstanceId) {

		// Completing the manager claim task, this should trigger a new adjuster
		// task

		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("adjusterAssignmentOutcome", true);
		taskVariables.put("managerAssigned", managerAssigned);
		taskVariables.put("claimNumber", claimNumber);

		// completing the manager task, and creating a new task for the adjuster
		// assignment.
		taskService.complete(taskId, taskVariables);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskName().asc()
				.list();
		
		logger.debug("The manager assigned in the workflow-:" + managerAssigned);
		
		Task task = tasks.get(0);
		task.setFormKey(claimNumber);
		taskService.saveTask(task);

		return task.getId();
	}

	public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}

	public void assignClaimsBackToManager() {
		// TODO: Complete implementation, empty for now

	}

}
