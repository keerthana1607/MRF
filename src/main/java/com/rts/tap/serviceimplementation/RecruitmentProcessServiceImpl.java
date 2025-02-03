package com.rts.tap.serviceimplementation;
 
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.InterviewerDao;
import com.rts.tap.dao.MRFDao;
import com.rts.tap.dao.RecruitmentProcessDao;
import com.rts.tap.dao.WorkFlowDao;
import com.rts.tap.dto.RecruitmentProcessDto;
import com.rts.tap.exception.CandidatesNotFoundException;
import com.rts.tap.exception.CustomException;
import com.rts.tap.exception.DataAccessException;
import com.rts.tap.exception.RecruitmentProcessNotFoundException;
import com.rts.tap.feign.ClientInterface;
import com.rts.tap.feign.EmployeeInterface;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Client;
import com.rts.tap.model.Employee;
import com.rts.tap.model.Interviewer;
import com.rts.tap.model.MRF;
import com.rts.tap.model.RecruitmentProcess;
import com.rts.tap.model.WorkFlow;
import com.rts.tap.service.ApproverLevelService;
import com.rts.tap.service.RecruitmentProcessService;
 
import jakarta.transaction.Transactional;
 
@Service
@Transactional
public class RecruitmentProcessServiceImpl implements RecruitmentProcessService {
 
	RecruitmentProcessDao recruitmentProcessDao;
	MRFDao mrfDao;
	ApproverLevelService approverLevelService;
    WorkFlowDao workFlowDao;
	EmployeeInterface employeeInterface;
	ClientInterface clientInterface;
	InterviewerDao interviewerDao;
 
	public RecruitmentProcessServiceImpl(RecruitmentProcessDao recruitmentProcessDao, MRFDao mrfDao,
			ApproverLevelService approverLevelService, EmployeeInterface employeeInterface,
			ClientInterface clientInterface, InterviewerDao interviewerDao,WorkFlowDao workFlowDao) {
		super();
		this.recruitmentProcessDao = recruitmentProcessDao;
		this.mrfDao = mrfDao;
		this.approverLevelService = approverLevelService;
		this.employeeInterface = employeeInterface;
		this.clientInterface = clientInterface;
		this.interviewerDao = interviewerDao;
		this.workFlowDao= workFlowDao;
	}
 
	@Override
	public void insertRecruitmentProcessLevel(RecruitmentProcess recruitmentProcess) {
	    if (recruitmentProcess == null) {
	        throw new IllegalArgumentException("Recruitment Process cannot be null");
	    }
 
	    MRF mrf = mrfDao.findById(recruitmentProcess.getMrf().getMrfId());
	    recruitmentProcess.setMrf(mrf);
 
	    if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("interview")) {
	        List<Interviewer> interviewers = new ArrayList<>();
	        for (Interviewer interviewer : recruitmentProcess.getInterviewer()) {
	            if (interviewer.getEmployee() != null && interviewer.getEmployee().getEmployeeId() != null) {
	                Employee employee = getEmployeeByIdFromEmployeeService(interviewer.getEmployee().getEmployeeId());
	                interviewer.setEmployee(employee);
	                interviewer.setClient(null);
	            } else if (interviewer.getClient() != null && interviewer.getClient().getClientId() != null) {
	                Client client = getClientByIdFromClientService(interviewer.getClient().getClientId());
	                interviewer.setClient(client);
	                interviewer.setEmployee(null);
	            }
 
	            if (interviewer.getInterviewerId() == null) {
	                interviewerDao.addInterviewer(interviewer);
	            }
 
	            interviewer.setInterviewerId(interviewer.getInterviewerId());
	            interviewers.add(interviewer);
	        }
 
	        recruitmentProcess.setInterviewer(interviewers);
	    } else if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("assessment")) {
	        recruitmentProcess.setInterviewer(null);
	    }
 
	    recruitmentProcessDao.setRecruitmentLevel(recruitmentProcess);
	}
 
 
	@Override
	public List<RecruitmentProcess> getRecruitmentProcessLevels(Long mrfId) {
	    try {
	        List<RecruitmentProcess> recruitmentProcesses = recruitmentProcessDao.findRecruitmentProcessByMrfId(mrfId);
	        if (recruitmentProcesses == null || recruitmentProcesses.isEmpty()) {
	            throw new RecruitmentProcessNotFoundException("No recruitment process levels found for MRF ID: " + mrfId);
	        }
	        return recruitmentProcesses;
	    } catch (RuntimeException e) {
	        throw new RecruitmentProcessNotFoundException("No recruitment process levels found for MRF ID:" + mrfId);
	    }
	}
	public Employee getEmployeeByIdFromEmployeeService(Long employeeId) {
		try {
			ResponseEntity<Employee> response = employeeInterface.getEmployeeById(employeeId);
			return response.getBody();
		} catch (Exception e) {
			return null;
		}
	}
 
	public Client getClientByIdFromClientService(Long clientId) {
		try {
			ResponseEntity<Client> response = clientInterface.getClientDetailsById(clientId);
			return response.getBody();
		} catch (Exception e) {
			return null;
		}
	}
 
 
	@Override
	public void setRecruitmentProcess(RecruitmentProcessDto recruitmentProcessDto) {
		try {
			MRF mrf = mrfDao.findById(recruitmentProcessDto.getRecruitmentProcesses().get(0).getMrf().getMrfId());
 
			for (RecruitmentProcess recruitmentProcess : recruitmentProcessDto.getRecruitmentProcesses()) {
				recruitmentProcess.setMrf(mrf);
 
				if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("interview")) {
					List<Interviewer> interviewers = new ArrayList<>();
					for (Interviewer interviewer : recruitmentProcess.getInterviewer()) {
						if (interviewer.getEmployee().getEmployeeId() != null) {
							Employee employee = getEmployeeByIdFromEmployeeService(interviewer.getEmployee().getEmployeeId());
							interviewer.setEmployee(employee);
							interviewer.setClient(null);
						}
 
						else if (interviewer.getClient().getClientId() != null) {
							Client client = getClientByIdFromClientService(interviewer.getClient().getClientId());
							interviewer.setClient(client);
							interviewer.setEmployee(null);
						}
 
						interviewerDao.addInterviewer(interviewer);
 
						interviewer.setInterviewerId(interviewer.getInterviewerId());
						interviewers.add(interviewer);
					}
					recruitmentProcess.setInterviewer(interviewers);
				}
 
				else if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("assessment")) {
					recruitmentProcess.setInterviewer(null);
				}
 
				recruitmentProcessDao.setRecruitmentLevel(recruitmentProcess);
 
			}
 
 
			WorkFlow workFlow = new WorkFlow();
			workFlow.setCount(recruitmentProcessDto.getRecruitmentProcesses().size());
			workFlow.setWorkFlowType(MessageConstants.SET_WORKFLOW_TYPE_RECRUITMENT_PROCESS);
			workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
			workFlow.setMrf(mrf);
			Employee recruitmentManager = getEmployeeByIdFromEmployeeService(recruitmentProcessDto.getRecruiterManagerId());
			workFlow.setEmployee(recruitmentManager);
 
			workFlowDao.addWorkFlow(workFlow);
		}
		catch (DataAccessException e) {
			throw new CustomException("Recruitment process setting failed"+ e.getMessage());
		}
	
 
	}
 
 
 
	@Override
	public void updateRecruitmentProcessLevel(RecruitmentProcess recruitmentProcess) {
	    try {
	        if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("interview")) {
	            List<Interviewer> interviewers = new ArrayList<>();
	            for (Interviewer interviewer : recruitmentProcess.getInterviewer()) {
	                if (interviewer.getEmployee() != null && interviewer.getEmployee().getEmployeeId() != null) {
	                    Employee employee = getEmployeeByIdFromEmployeeService(interviewer.getEmployee().getEmployeeId());
	                    interviewer.setEmployee(employee);
	                    interviewer.setClient(null);
	                } else if (interviewer.getClient() != null && interviewer.getClient().getClientId() != null) {
	                    Client client = getClientByIdFromClientService(interviewer.getClient().getClientId());
	                    interviewer.setClient(client);
	                    interviewer.setEmployee(null);
	                }
 
	                // Add or update interviewer
	                if (interviewer.getInterviewerId() == null) {
	                    interviewerDao.addInterviewer(interviewer);
	                } else {
	                    interviewerDao.updateInterviewer(interviewer); // Assuming you have an update method for existing interviewers
	                }
 
	                interviewers.add(interviewer);
	            }
	            recruitmentProcess.setInterviewer(interviewers);
	        }
 
	        recruitmentProcessDao.updateRecruitmentProcess(recruitmentProcess);
 
	        WorkFlow workFlow = workFlowDao.findWorkFlowForRecruitmentProcessByMrf(recruitmentProcess.getMrf());
	        if (workFlow == null) {
	            throw new CustomException("Workflow not found for the given MRF.");
	        }
	        workFlowDao.updateWorkFlow(workFlow);
 
	    } catch (DataAccessException e) {
	        throw new CustomException("Database error during update: " + e.getMessage());    
	    } catch (Exception e) {
	        throw new CustomException("An unexpected error occurred: " + e.getMessage());
	    }
	}
 
	@Override
	public void deleteRecruitmentProcessLevel(Long recruitmentProcessId) {
		RecruitmentProcess recruitmentProcess = recruitmentProcessDao.findById(recruitmentProcessId);
 
		List<RecruitmentProcess> recruitmentProcesses = recruitmentProcessDao
				.findRecruitmentProcessByMrfId(recruitmentProcess.getMrf().getMrfId());
		for (RecruitmentProcess rp : recruitmentProcesses) {
			if (rp.getLevel() > recruitmentProcess.getLevel()) {
				rp.setLevel(rp.getLevel() - 1);
				recruitmentProcessDao.updateRecruitmentProcess(rp);
			}
		}
 
		if (recruitmentProcess.getRecruitmentProcessType().equalsIgnoreCase("interview")) {
			for (Interviewer interviewer : recruitmentProcess.getInterviewer()) {
				interviewerDao.deleteInterviewerById(interviewer.getInterviewerId());
			}
		}
 
		recruitmentProcessDao.deleteRecruitmentProcessById(recruitmentProcessId);
 
		WorkFlow workFlow = workFlowDao.findWorkFlowForRecruitmentProcessByMrf(recruitmentProcess.getMrf());
		workFlow.setCount(workFlow.getCount() - 1);
		workFlowDao.updateWorkFlow(workFlow);
	}
 
	
 
 
	@Override
	public List<Candidate> getCandidateByRpId(Long rpId) {
	    try {
	        List<Candidate> candidates = recruitmentProcessDao.findCandidateByRpId(rpId);
	        if (candidates == null || candidates.isEmpty()) {
	            throw new CandidatesNotFoundException("No candidates found for recruitment process ID: " + rpId);
	        }
	        return candidates;
	    } catch (RuntimeException e) {
	        throw new CandidatesNotFoundException("No candidates found for recruitment process ID: " + rpId);
	    }
	}
 
 
 
	@Override
	public RecruitmentProcess getRecruitementProcessByRecruitementProcessId(Long recruitementProcessId) {
	    try {
	        RecruitmentProcess recruitmentProcess = recruitmentProcessDao.findRecruitementProcessByRecruitementProcessId(recruitementProcessId);
	        if (recruitmentProcess == null) {
	            throw new RecruitmentProcessNotFoundException("Recruitment process not found for ID: " + recruitementProcessId);
	        }
	        return recruitmentProcess;
	    } catch (RuntimeException e) {
	        throw new RecruitmentProcessNotFoundException("Recruitment process not found for ID: " + recruitementProcessId);
	    }
	}
}
 
 