package com.rts.tap.controller;
 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.model.ApproverLevel;
import com.rts.tap.model.Employee;
import com.rts.tap.model.WorkFlow;
import com.rts.tap.service.ApproverLevelService;
 
@RestController
@RequestMapping(path = APIConstants.BASE_URL)
@CrossOrigin(origins = APIConstants.FRONT_END_URL)
public class ApproverLevelController {
 
    private static final Logger logger = LoggerFactory.getLogger(ApproverLevelController.class);
 
    private final ApproverLevelService approverLevelService;
 
    public ApproverLevelController(ApproverLevelService approverLevelService) {
        this.approverLevelService = approverLevelService;
    }
 
    @PostMapping(APIConstants.ADD_APPROVERLEVEL_URL)
    public ResponseEntity<?> saveApproverLevel(@RequestBody List<ApproverLevel> approverLevel) {
        logger.info("Attempting to save approver levels.");
        approverLevelService.saveApproverLevel(approverLevel);
        logger.info("Approver levels saved successfully.");
        return ResponseEntity.ok(MessageConstants.APPROVER_LEVEL_ADDED);
    }
 
    @PutMapping(APIConstants.UPDATE_APPROVERLEVEL_URL)
    public ResponseEntity<?> updateApproverLevel(@RequestBody ApproverLevel approverLevel) {
        logger.info("Attempting to update approver level with ID: " + approverLevel.getApproverId());
        approverLevelService.updateApproverLevel(approverLevel);
        logger.info("Approver level updated successfully.");
        return ResponseEntity.ok(MessageConstants.APPROVER_LEVEL_UPDATED);
    }
 
    @DeleteMapping(APIConstants.DELETE_APPROVERLEVEL_URL)
    public ResponseEntity<?> deleteApproverLevel(@PathVariable("approverLevelId") long approverLevelId) {
        logger.info("Attempting to delete approver level with ID: " + approverLevelId);
        approverLevelService.deleteApproverLevel(approverLevelId);
        logger.info("Approver level deleted successfully.");
        return ResponseEntity.ok(MessageConstants.APPROVER_LEVEL_DELETED);
    }
 
    @GetMapping(APIConstants.GET_APPROVERLEVEL_URL)
    public ResponseEntity<?> getApproverLevelByMrfId(@PathVariable Long mrfId) {
        logger.info("Fetching approver levels for MRF ID: " + mrfId);
        List<ApproverLevel> approverLevel = approverLevelService.getApproverLevelByMrfId(mrfId);
        logger.info("Approver levels fetched successfully.");
        return ResponseEntity.ok(approverLevel);
    }
 
 
    @GetMapping(APIConstants.GET_WORKFLOW_URL)
    public ResponseEntity<?> getWorkFlowByMrfId(@PathVariable Long mrfId) {
        logger.info("Fetching workflow for MRF ID: " + mrfId);
        WorkFlow workFlow = approverLevelService.getWorkFlowByMrfId(mrfId);
        logger.info("Workflow fetched successfully.");
        return ResponseEntity.ok(workFlow);
    }
    
    @GetMapping(APIConstants.GET_EMPLOYEE_URL)
    public ResponseEntity<?> getEmployeeByEmployeeId(@PathVariable Long employeeId) {
        logger.info("Fetching employee with ID: " + employeeId);
        Employee employee = approverLevelService.getEmployeeByEmployeeId(employeeId);
        logger.info("Employee fetched successfully.");
        return ResponseEntity.ok(employee);
    }
    
 
    @PostMapping(APIConstants.ADD_SINGLE_APPROVERLEVEL_URL)
    public ResponseEntity<?> saveSingleApproverLevel(@RequestBody ApproverLevel approverLevel) {
        logger.info("Attempting to save a single approver level.");
        approverLevelService.saveSingleApproverLevel(approverLevel);
        logger.info("Single approver level saved successfully.");
        return ResponseEntity.ok(MessageConstants.APPROVER_LEVEL_ADDED);
    }
}
 