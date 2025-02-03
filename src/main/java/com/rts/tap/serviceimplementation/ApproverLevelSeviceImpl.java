package com.rts.tap.serviceimplementation;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.ApproverLevelDao;
import com.rts.tap.dao.MRFDao;
import com.rts.tap.dao.WorkFlowDao;
import com.rts.tap.dto.ApproverLevelDto;
import com.rts.tap.exception.ApproverLevelNotFoundException;
import com.rts.tap.exception.DataAccessException;
import com.rts.tap.exception.DatabaseException;
import com.rts.tap.exception.InvalidInputException;
import com.rts.tap.exception.ResourceNotFoundException;
import com.rts.tap.model.ApproverLevel;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.WorkFlow;
import com.rts.tap.service.ApproverLevelService;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
 
@Transactional
@Service
public class ApproverLevelSeviceImpl implements ApproverLevelService {
    
    private ApproverLevelDao approverLevelDao;
    private WorkFlowDao workflowDao;
    private MRFDao mrfDao;
   
    public ApproverLevelSeviceImpl(ApproverLevelDao approverLevelDao, WorkFlowDao workflowDao, MRFDao mrfDao) {
        this.approverLevelDao = approverLevelDao;
        this.workflowDao = workflowDao;
        this.mrfDao = mrfDao;
    }
    
 
    @Override
    public void saveApproverLevel(List<ApproverLevel> approverLevel) {
        // Validate input
        if (approverLevel == null || approverLevel.isEmpty()) {
            throw new InvalidInputException("Approver levels cannot be null or empty");
        }
 
        // Attempt to save approver levels
        approverLevelDao.saveApproverLevel(approverLevel);
 
        WorkFlow workFlow = new WorkFlow();
        ApproverLevelDto approverLevelDto = new ApproverLevelDto();
        approverLevelDto.setApproverLevel(approverLevel);
        workFlow.setCount(approverLevelDto.getApproverLevel().size());
        workFlow.setWorkFlowType(MessageConstants.SET_WORKFLOW_TYPE_APPROVER_LEVEL);
        workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
 
        Long mrfId = approverLevelDto.getApproverLevel().get(0).getMrf().getMrfId();
        MRF mrf = mrfDao.findById(mrfId);
        if (mrf == null) {
            throw new ResourceNotFoundException("MRF with ID " + mrfId + " not found");
        }
        workFlow.setMrf(mrf);
 
        Long buHeadId = approverLevelDto.getBuHeadId();
        if (buHeadId == null) {
            throw new InvalidInputException("BU Head ID cannot be null");
        }
        workFlow.setEmployee(mrf.getBusinessUnitHead());
 
        // Add workflow to the database
        try {
            workflowDao.addWorkFlow(workFlow);
        } catch (DataAccessException e) {
            throw new DatabaseException("Data access issue while saving workflow: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save approver levels: " + e.getMessage(), e);
        }
    }
    @Override
    public void updateApproverLevel(ApproverLevel approverLevel) {
        try {
            if (approverLevel == null) {
                throw new IllegalArgumentException("Approver level cannot be null");
            }
            approverLevelDao.updateApproverLevel(approverLevel);
            
            WorkFlow workFlow = workflowDao.getWorkFlowApproverLevelByMrfId(approverLevel.getMrf().getMrfId());
            if (workFlow == null) {
                throw new ApproverLevelNotFoundException("Workflow not found for MRF ID: " + approverLevel.getMrf().getMrfId());
            }
            workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
            workflowDao.updateWorkFlow(workFlow);
        } catch (IllegalArgumentException | ApproverLevelNotFoundException e) {
            throw e; // Re-throw for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating approver level: " + e.getMessage(), e);
        }
    }
 
    @Override
    public void deleteApproverLevel(long approverLevelId) {
        try {
            ApproverLevel approverLevel = approverLevelDao.findApproverLevelById(approverLevelId);
            if (approverLevel == null) {
                throw new ApproverLevelNotFoundException("Approver level with ID " + approverLevelId + " not found");
            }
            
            List<ApproverLevel> approverLevels = approverLevelDao.getApproverLevelByMrfId(approverLevel.getMrf().getMrfId());
            for (ApproverLevel al : approverLevels) {
                if (al.getLevel() > approverLevel.getLevel()) {
                    al.setLevel(al.getLevel() - 1);
                    approverLevelDao.updateApproverLevel(al);
                }
            }
            approverLevelDao.deleteApproverLevel(approverLevelId);
            
            WorkFlow workFlow = workflowDao.findWorkFlowByMrf(approverLevel.getMrf());
            if (workFlow == null) {
                throw new ApproverLevelNotFoundException("Workflow not found for MRF ID: " + approverLevel.getMrf().getMrfId());
            }
            workFlow.setCount(workFlow.getCount() - 1);
            workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
            workflowDao.updateWorkFlow(workFlow);
        } catch (ApproverLevelNotFoundException e) {
            throw e; // Re-throw for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting approver level: " + e.getMessage(), e);
        }
    }
 
    @Override
    public List<ApproverLevel> getApproverLevelByMrfId(Long mrfId) {
        try {
            List<ApproverLevel> approverLevels = approverLevelDao.getApproverLevelByMrfId(mrfId);
            if (approverLevels == null || approverLevels.isEmpty()) {
                throw new ApproverLevelNotFoundException("No approver levels found for MRF ID: " + mrfId);
            }
            return approverLevels;
        } catch (ApproverLevelNotFoundException e) {
            throw e; // Re-throw for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching approver levels: " + e.getMessage(), e);
        }
    }
    
 
    @Override
    public WorkFlow getWorkFlowByMrfId(Long mrfId) {
        try {
            WorkFlow workFlow = workflowDao.getWorkFlowApproverLevelByMrfId(mrfId);
            if (workFlow == null) {
                throw new ApproverLevelNotFoundException("Workflow not found for MRF ID: " + mrfId);
            }
            return workFlow;
        } catch (ApproverLevelNotFoundException e) {
            // Re-throw the custom exception
            throw e;
        } catch (Exception e) {
            // Handle other unexpected errors
            throw new ApproverLevelNotFoundException("Workflow not found for MRF ID: " + mrfId);
        }
    }
 
    @Override
    public Employee getEmployeeByEmployeeId(Long employeeId) {
        try {
            Employee employee = workflowDao.getEmployeeByEmployeeId(employeeId);
            if (employee == null) {
                throw new ApproverLevelNotFoundException("Employee not found with ID:" + employeeId );
            }
            return employee;
        } catch (NoResultException e) {
            throw new ApproverLevelNotFoundException("Employee not found with ID:" + employeeId);
        }
    }
    
    
    @Override
    public void saveSingleApproverLevel(ApproverLevel approverLevel) {
        try {
            if (approverLevel == null) {
                throw new IllegalArgumentException("Approver level cannot be null");
            }
            WorkFlow workFlow = workflowDao.findWorkFlowByMrf(approverLevel.getMrf());
            if (workFlow == null) {
                throw new ApproverLevelNotFoundException("Workflow not found for MRF ID: " + approverLevel.getMrf().getMrfId());
            }
            workFlow.setCount(workFlow.getCount() + 1);
            workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
            workflowDao.updateWorkFlow(workFlow);
            
            approverLevelDao.saveSingleApproverLevel(approverLevel);
        } catch (IllegalArgumentException | ApproverLevelNotFoundException e) {
            throw e; // Re-throw for controller to handle
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving single approver level: " + e.getMessage(), e);
        }
    }
    
   
}
 