package com.rts.tap.serviceimplementation;
 
import java.util.List;
 
import org.springframework.stereotype.Service;
 
import com.rts.tap.constants.MessageConstants;

import com.rts.tap.dao.MRFDao;

import com.rts.tap.dao.WorkFlowDao;

import com.rts.tap.exception.EmployeeNotFoundException;

import com.rts.tap.exception.MrfNotFoundException;

import com.rts.tap.exception.WorkFlowNotFoundException;

import com.rts.tap.feign.EmployeeInterface;

import com.rts.tap.model.Employee;

import com.rts.tap.model.MRF;

import com.rts.tap.model.WorkFlow;

import com.rts.tap.service.WorkFlowService;
 
import jakarta.transaction.Transactional;
 
@Service

@Transactional

public class WorkFlowServiceImpl implements WorkFlowService {

	WorkFlowDao workFlowDao;

	MRFDao mrfDao;

	EmployeeInterface employeeInterface;
 
	public WorkFlowServiceImpl(WorkFlowDao workFlowDao, MRFDao mrfDao, EmployeeInterface employeeInterface) {

		super();

		this.workFlowDao = workFlowDao;

		this.mrfDao = mrfDao;

		this.employeeInterface = employeeInterface;

	}
 
	@Override

	public WorkFlow getWorkflowByMrfIdForRecruitmentProcess(Long mrfId) {

		MRF mrf = mrfDao.findById(mrfId);

		if (mrf == null) {

			throw new MrfNotFoundException("MRF not found with ID: " + mrfId);

		}

		WorkFlow workFlow = workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf);

		if (workFlow == null) {

			throw new WorkFlowNotFoundException("Workflow not found for MRF ID: " + mrfId);

		}

		return workFlow;

	}

	@Override

	public List<WorkFlow> getWorkFlowByEmployeeId(Long employeeId) {

		Employee employee = getEmployeeByIdFromEmployeeService(employeeId);

		if (employee == null) {

			throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);

		}

		List<WorkFlow> workFlows = workFlowDao.findWorkFlowForRecruitmentProcessByEmployee(employee);

		if (workFlows.isEmpty()) {

			throw new WorkFlowNotFoundException("No workflows found for employee ID: " + employeeId);

		}

		return workFlows;

	}

	@Override

	public void updateWorkFlow(WorkFlow workflow) {

		if (workflow == null) {

			throw new WorkFlowNotFoundException("Workflow cannot be null");

		}

		workFlowDao.updateWorkFlow(workflow);

	}
 
	@Override

	public WorkFlow getWorkFlowById(Long workFlowId) {

		WorkFlow workFlow = workFlowDao.getWorkFlowById(workFlowId);

		if (workFlow == null) {

			throw new WorkFlowNotFoundException("Workflow not found with ID: " + workFlowId);

		}

		return workFlow;

	}
 
	@Override

	public void updateWorkFlowAsPendingForRecruitmentProcess(Long mrfId) {

		MRF mrf = mrfDao.findById(mrfId);

		if (mrf == null) {

			throw new MrfNotFoundException("MRF not found with ID: " + mrfId);

		}

		WorkFlow workFlow = workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf);

		if (workFlow == null) {

			throw new WorkFlowNotFoundException("Workflow not found for MRF ID: " + mrfId);

		}

		workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);

		workFlowDao.updateWorkFlow(workFlow);

	}
 
	@Override

	public void updateWorkFlowAsPendingForApprovalProcess(Long mrfId) {

		MRF mrf = mrfDao.findById(mrfId);

		if (mrf == null) {

			throw new MrfNotFoundException("MRF not found with ID: " + mrfId);

		}

		WorkFlow workFlow = workFlowDao.getWorkFlowApproverLevelByMrfId(mrfId);

		if (workFlow == null) {

			throw new WorkFlowNotFoundException("Workflow not found for MRF ID: " + mrfId);

		}

		workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);

		workFlowDao.updateWorkFlow(workFlow);

	}

	public Employee getEmployeeByIdFromEmployeeService(Long employeeId) {

		try {

			return employeeInterface.getEmployeeById(employeeId).getBody();

		} catch (Exception e) {

			throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);

		}

	}
 
}

 