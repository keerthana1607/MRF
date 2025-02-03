package com.rts.tap.controller;
 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dto.WorkFlowDto;
import com.rts.tap.model.WorkFlow;
import com.rts.tap.service.WorkFlowService;
 
@RestController
@RequestMapping(APIConstants.BASE_URL)
@CrossOrigin(origins = APIConstants.FRONT_END_URL)
public class WorkFlowController {
 
    private static final Logger logger = LoggerFactory.getLogger(WorkFlowController.class);
 
    private final WorkFlowService workFlowService;
 
    public WorkFlowController(WorkFlowService workFlowService) {
        this.workFlowService = workFlowService;
    }
 
    @GetMapping(APIConstants.GET_WORKFLOW_FOR_RECRUITMENT_PROCESS)
    public ResponseEntity<?> getWorkflowForRecruitmentProcess(@PathVariable Long mrfId) {
        logger.info("Fetching workflow for recruitment process with MRF ID: " + mrfId);
        WorkFlow workFlow = workFlowService.getWorkflowByMrfIdForRecruitmentProcess(mrfId);
        logger.info("Workflow fetched successfully for MRF ID: " + mrfId);
        return ResponseEntity.ok(workFlow);
    }
 
    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_WORKFLOW_BY_EMPLOYEEID)
    public ResponseEntity<?> getWorkflowByEmployeeId(@PathVariable Long employeeId) {
        logger.info("Fetching workflows for employee ID: " + employeeId);
        List<WorkFlow> workFlows = workFlowService.getWorkFlowByEmployeeId(employeeId);
        logger.info("Workflows fetched successfully for employee ID: " + employeeId);
        return ResponseEntity.ok(workFlows);
    }
 
    @PatchMapping(APIConstants.RECRUITING_MANAGER_UPDATE_WORKFLOW)
    public ResponseEntity<?> updateWorkFlow(@PathVariable Long workflowId, @RequestBody WorkFlowDto workFlowDto) {
        logger.info("Updating workflow with ID: " + workflowId);
        WorkFlow workflow = workFlowService.getWorkFlowById(workflowId);
        workflow.setStatus(workFlowDto.getStatus());
        if (workFlowDto.getReason() != null) {
            workflow.setReason(workFlowDto.getReason());
        }
        workFlowService.updateWorkFlow(workflow);
        logger.info("Workflow updated successfully for ID: " + workflowId);
        return ResponseEntity.ok(MessageConstants.WORKFLOW_APPROVAL_STATUS_SUCCESS);
    }
 
    @PatchMapping(APIConstants.UPDATE_WORKFLOW_STATUS_AS_PENDING_FOR_RECRUITMENT_PROCESS)
    public ResponseEntity<?> updateWorkFlowAsPendingForRecruitmentProcess(@PathVariable Long mrfId) {
        logger.info("Updating workflow status as pending for recruitment process with MRF ID: " + mrfId);
        workFlowService.updateWorkFlowAsPendingForRecruitmentProcess(mrfId);
        logger.info("Workflow status updated successfully as pending for recruitment process with MRF ID: " + mrfId);
        return ResponseEntity.ok(MessageConstants.WORKFLOW_STATUS_UPDATED);
    }
 
    @PatchMapping(APIConstants.UPDATE_WORKFLOW_STATUS_AS_PENDING_FOR_APPROVAL_PROCESS)
    public ResponseEntity<?> updateWorkFlowAsPendingForApprovalProcess(@PathVariable Long mrfId) {
        logger.info("Updating workflow status as pending for approval process with MRF ID: " + mrfId);
        workFlowService.updateWorkFlowAsPendingForApprovalProcess(mrfId);
        logger.info("Workflow status updated successfully as pending for approval process with MRF ID: " + mrfId);
        return ResponseEntity.ok(MessageConstants.WORKFLOW_STATUS_UPDATED);
    }
}