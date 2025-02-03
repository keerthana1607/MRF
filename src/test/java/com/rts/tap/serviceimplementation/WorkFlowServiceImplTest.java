package com.rts.tap.serviceimplementation;
 
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
 
import java.util.Collections;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
public class WorkFlowServiceImplTest {
 
    @Mock
    private WorkFlowDao workFlowDao;
 
    @Mock
    private MRFDao mrfDao;
 
    @Mock
    private EmployeeInterface employeeInterface;
 
    @InjectMocks
    private WorkFlowServiceImpl workFlowService;
 
    private WorkFlow workFlow;
    private MRF mrf;
    private Employee employee;
 
    @BeforeEach
    void setUp() {
        mrf = new MRF();
        mrf.setMrfId(1L);
 
        employee = new Employee();
        employee.setEmployeeId(1L);
 
        workFlow = new WorkFlow();
        workFlow.setWorkflowId(1L);
        workFlow.setMrf(mrf);
        workFlow.setEmployee(employee);
        workFlow.setStatus(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING);
    }
 
    @Test
    void testGetWorkflowByMrfIdForRecruitmentProcess() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf)).thenReturn(workFlow);
 
        WorkFlow result = workFlowService.getWorkflowByMrfIdForRecruitmentProcess(1L);
        assertNotNull(result);
        assertEquals(1L, result.getWorkflowId());
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByMrf(mrf);
    }
 
    @Test
    void testGetWorkflowByMrfIdForRecruitmentProcess_ThrowsMrfNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(null);
 
        assertThrows(MrfNotFoundException.class, () -> workFlowService.getWorkflowByMrfIdForRecruitmentProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
    }
 
    @Test
    void testGetWorkflowByMrfIdForRecruitmentProcess_ThrowsWorkFlowNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf)).thenReturn(null);
 
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.getWorkflowByMrfIdForRecruitmentProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByMrf(mrf);
    }
 
    @Test
    void testGetWorkFlowByEmployeeId() {
        when(employeeInterface.getEmployeeById(1L)).thenReturn(ResponseEntity.ok(employee));
        when(workFlowDao.findWorkFlowForRecruitmentProcessByEmployee(employee)).thenReturn(Collections.singletonList(workFlow));
 
        List<WorkFlow> result = workFlowService.getWorkFlowByEmployeeId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeInterface, times(1)).getEmployeeById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByEmployee(employee);
    }
 
    @Test
    void testGetWorkFlowByEmployeeId_ThrowsEmployeeNotFoundException() {
        when(employeeInterface.getEmployeeById(1L)).thenThrow(new EmployeeNotFoundException("Employee not found with ID: 1"));
 
        assertThrows(EmployeeNotFoundException.class, () -> workFlowService.getWorkFlowByEmployeeId(1L));
        verify(employeeInterface, times(1)).getEmployeeById(1L);
    }
 
    @Test
    void testGetWorkFlowByEmployeeId_ThrowsWorkFlowNotFoundException() {
        when(employeeInterface.getEmployeeById(1L)).thenReturn(ResponseEntity.ok(employee));
        when(workFlowDao.findWorkFlowForRecruitmentProcessByEmployee(employee)).thenReturn(Collections.emptyList());
 
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.getWorkFlowByEmployeeId(1L));
        verify(employeeInterface, times(1)).getEmployeeById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByEmployee(employee);
    }
 
    @Test
    void testUpdateWorkFlow() {
        doNothing().when(workFlowDao).updateWorkFlow(workFlow);
 
        workFlowService.updateWorkFlow(workFlow);
        verify(workFlowDao, times(1)).updateWorkFlow(workFlow);
    }
 
    @Test
    void testUpdateWorkFlow_ThrowsWorkFlowNotFoundException() {
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.updateWorkFlow(null));
    }
 
    @Test
    void testGetWorkFlowById() {
        when(workFlowDao.getWorkFlowById(1L)).thenReturn(workFlow);
 
        WorkFlow result = workFlowService.getWorkFlowById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getWorkflowId());
        verify(workFlowDao, times(1)).getWorkFlowById(1L);
    }
 
    @Test
    void testGetWorkFlowById_ThrowsWorkFlowNotFoundException() {
        when(workFlowDao.getWorkFlowById(1L)).thenReturn(null);
 
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.getWorkFlowById(1L));
        verify(workFlowDao, times(1)).getWorkFlowById(1L);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForRecruitmentProcess() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf)).thenReturn(workFlow);
        doNothing().when(workFlowDao).updateWorkFlow(workFlow);
 
        workFlowService.updateWorkFlowAsPendingForRecruitmentProcess(1L);
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByMrf(mrf);
        verify(workFlowDao, times(1)).updateWorkFlow(workFlow);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForRecruitmentProcess_ThrowsMrfNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(null);
 
        assertThrows(MrfNotFoundException.class, () -> workFlowService.updateWorkFlowAsPendingForRecruitmentProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForRecruitmentProcess_ThrowsWorkFlowNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.findWorkFlowForRecruitmentProcessByMrf(mrf)).thenReturn(null);
 
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.updateWorkFlowAsPendingForRecruitmentProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).findWorkFlowForRecruitmentProcessByMrf(mrf);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForApprovalProcess() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.getWorkFlowApproverLevelByMrfId(1L)).thenReturn(workFlow);
        doNothing().when(workFlowDao).updateWorkFlow(workFlow);
 
        workFlowService.updateWorkFlowAsPendingForApprovalProcess(1L);
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).getWorkFlowApproverLevelByMrfId(1L);
        verify(workFlowDao, times(1)).updateWorkFlow(workFlow);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForApprovalProcess_ThrowsMrfNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(null);
 
        assertThrows(MrfNotFoundException.class, () -> workFlowService.updateWorkFlowAsPendingForApprovalProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
    }
 
    @Test
    void testUpdateWorkFlowAsPendingForApprovalProcess_ThrowsWorkFlowNotFoundException() {
        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(workFlowDao.getWorkFlowApproverLevelByMrfId(1L)).thenReturn(null);
 
        assertThrows(WorkFlowNotFoundException.class, () -> workFlowService.updateWorkFlowAsPendingForApprovalProcess(1L));
        verify(mrfDao, times(1)).findById(1L);
        verify(workFlowDao, times(1)).getWorkFlowApproverLevelByMrfId(1L);
    }
 
    @Test
    void testGetEmployeeByIdFromEmployeeService() {
        when(employeeInterface.getEmployeeById(1L)).thenReturn(ResponseEntity.ok(employee));
 
        Employee result = workFlowService.getEmployeeByIdFromEmployeeService(1L);
        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        verify(employeeInterface, times(1)).getEmployeeById(1L);
    }
 
    @Test
    void testGetEmployeeByIdFromEmployeeService_ThrowsEmployeeNotFoundException() {
        when(employeeInterface.getEmployeeById(1L)).thenThrow(new EmployeeNotFoundException("Employee not found with ID: 1"));
 
        assertThrows(EmployeeNotFoundException.class, () -> workFlowService.getEmployeeByIdFromEmployeeService(1L));
        verify(employeeInterface, times(1)).getEmployeeById(1L);
    }
}
 