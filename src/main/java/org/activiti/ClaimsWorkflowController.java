package org.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.domain.TaskAssignment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaimsWorkflowController {

	@Autowired
	private ClaimsWorkflowService claimsWorkflowService;

	private static final Logger logger = LoggerFactory.getLogger(ClaimsWorkflowController.class);

	@RequestMapping(value = "/claims/workflow/assignments/manager", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public TaskAssignment startProcessInstance(@RequestBody Map<String, Object> data) {

		return claimsWorkflowService.startProcess(data);
	}

	@RequestMapping(value = "/claims/workflow/assignments/adjuster", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String initiateAdjusterAssignmentTask(@RequestParam("claimNumber") String claimNumber, @RequestParam("managerAssigned") String managerAssigned,
			@RequestParam("taskId") String taskId, @RequestParam("processInstanceId") String processInstanceId) {

		return claimsWorkflowService.initiateAdjusterAssignmentTask(claimNumber, managerAssigned, taskId, processInstanceId);
	}

	@RequestMapping(value = "/claims/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public List<TaskRepresentation> getTasks(@RequestParam("manager") String manager) {

		List<Task> tasks = claimsWorkflowService.getTasks(manager);

		logger.debug("The size of the tasks for the manager:" + tasks.size());

		List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
		for (Task task : tasks) {
			dtos.add(new TaskRepresentation(task.getId(), task.getName(), task.getFormKey()));
		}
		return dtos;
	}

	static class TaskRepresentation {

		private String id;
		private String name;
		private String claimNumber;

		public TaskRepresentation(String id, String name, String claimNumber) {
			this.id = id;
			this.name = name;
			this.claimNumber = claimNumber;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClaimNumber() {
			return claimNumber;
		}

		public void setClaimNumber(String claimNumber) {
			this.claimNumber = claimNumber;
		}
	}

	static class StartProcessRepresentation {

		private String assignee;

		public String getAssignee() {
			return assignee;
		}

		public void setAssignee(String assignee) {
			this.assignee = assignee;
		}
	}

}