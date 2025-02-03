package com.rts.tap.serviceimplementation;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.util.ArrayList;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.ApproverLevelDao;
import com.rts.tap.dao.MRFDao;
import com.rts.tap.dao.WorkFlowDao;
import com.rts.tap.dto.ApproverLevelDto;
import com.rts.tap.model.ApproverLevel;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.WorkFlow;
 
class ApproverLevelSeviceImplTest {
 
    @Mock
    private ApproverLevelDao approverLevelDao;
 
    @Mock
    private WorkFlowDao workflowDao;
 
    @Mock
    private MRFDao mrfDao;
 
    @InjectMocks
    private ApproverLevelSeviceImpl approverLevelService;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testSaveApproverLevel() {
        List<ApproverLevel> approverLevels = new ArrayList<>();
        ApproverLevel approverLevel = new ApproverLevel();
        approverLevel.setMrf(new MRF());
        approverLevel.getMrf().setMrfId(1L);
        approverLevels.add(approverLevel);
 
        MRF mockMrf = new MRF();
        mockMrf.setBusinessUnitHead(new Employee());
 
        when(mrfDao.findById(1L)).thenReturn(mockMrf);
        doNothing().when(approverLevelDao).saveApproverLevel(approverLevels);
        doNothing().when(workflowDao).addWorkFlow(any(WorkFlow.class));
 
        approverLevelService.saveApproverLevel(approverLevels);
 
        verify(approverLevelDao, times(1)).saveApproverLevel(approverLevels);
        verify(workflowDao, times(1)).addWorkFlow(any(WorkFlow.class));
    }
 
    @Test
    void testUpdateApproverLevel() {
        ApproverLevel approverLevel = new ApproverLevel();
        approverLevel.setMrf(new MRF());
        approverLevel.getMrf().setMrfId(1L);
 
        WorkFlow mockWorkFlow = new WorkFlow();
 
        when(workflowDao.getWorkFlowApproverLevelByMrfId(1L)).thenReturn(mockWorkFlow);
        doNothing().when(approverLevelDao).updateApproverLevel(approverLevel);
        doNothing().when(workflowDao).updateWorkFlow(mockWorkFlow);
 
        approverLevelService.updateApproverLevel(approverLevel);
 
        assertEquals(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING, mockWorkFlow.getStatus());
        verify(approverLevelDao, times(1)).updateApproverLevel(approverLevel);
        verify(workflowDao, times(1)).updateWorkFlow(mockWorkFlow);
    }
 
    @Test
    void testDeleteApproverLevel() {
        long approverLevelId = 1L;
        ApproverLevel mockApproverLevel = new ApproverLevel();
        mockApproverLevel.setLevel(2);
        mockApproverLevel.setMrf(new MRF());
        mockApproverLevel.getMrf().setMrfId(1L);
 
        List<ApproverLevel> approverLevels = new ArrayList<>();
        ApproverLevel al1 = new ApproverLevel();
        al1.setLevel(3);
        approverLevels.add(al1);
 
        WorkFlow mockWorkFlow = new WorkFlow();
        mockWorkFlow.setCount(3);
 
        when(approverLevelDao.findApproverLevelById(approverLevelId)).thenReturn(mockApproverLevel);
        when(approverLevelDao.getApproverLevelByMrfId(1L)).thenReturn(approverLevels);
        when(workflowDao.findWorkFlowByMrf(mockApproverLevel.getMrf())).thenReturn(mockWorkFlow);
 
        doNothing().when(approverLevelDao).deleteApproverLevel(approverLevelId);
        doNothing().when(approverLevelDao).updateApproverLevel(al1);
        doNothing().when(workflowDao).updateWorkFlow(mockWorkFlow);
 
        approverLevelService.deleteApproverLevel(approverLevelId);
 
        assertEquals(2, mockWorkFlow.getCount());
        assertEquals(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING, mockWorkFlow.getStatus());
        verify(approverLevelDao, times(1)).deleteApproverLevel(approverLevelId);
        verify(workflowDao, times(1)).updateWorkFlow(mockWorkFlow);
    }

 
    @Test
    void testGetWorkFlowByMrfId() {
        Long mrfId = 1L;
        WorkFlow mockWorkFlow = new WorkFlow();
 
        when(workflowDao.getWorkFlowApproverLevelByMrfId(mrfId)).thenReturn(mockWorkFlow);
 
        WorkFlow result = approverLevelService.getWorkFlowByMrfId(mrfId);
 
        assertNotNull(result);
        assertEquals(mockWorkFlow, result);
        verify(workflowDao, times(1)).getWorkFlowApproverLevelByMrfId(mrfId);
    }
 
    @Test
    void testGetEmployeeByEmployeeId() {
        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
 
        when(workflowDao.getEmployeeByEmployeeId(employeeId)).thenReturn(mockEmployee);
 
        Employee result = approverLevelService.getEmployeeByEmployeeId(employeeId);
 
        assertNotNull(result);
        assertEquals(mockEmployee, result);
        verify(workflowDao, times(1)).getEmployeeByEmployeeId(employeeId);
    }
 
    @Test
    void testSaveSingleApproverLevel() {
        ApproverLevel approverLevel = new ApproverLevel();
        approverLevel.setMrf(new MRF());
 
        WorkFlow mockWorkFlow = new WorkFlow();
        mockWorkFlow.setCount(1);
 
        when(workflowDao.findWorkFlowByMrf(approverLevel.getMrf())).thenReturn(mockWorkFlow);
        doNothing().when(approverLevelDao).saveSingleApproverLevel(approverLevel);
        doNothing().when(workflowDao).updateWorkFlow(mockWorkFlow);
 
        approverLevelService.saveSingleApproverLevel(approverLevel);
 
        assertEquals(2, mockWorkFlow.getCount());
        assertEquals(MessageConstants.SET_WORKFLOW_STATUS_AS_PENDING, mockWorkFlow.getStatus());
        verify(approverLevelDao, times(1)).saveSingleApproverLevel(approverLevel);
        verify(workflowDao, times(1)).updateWorkFlow(mockWorkFlow);
    }
    
    @Test
    void testSaveApproverLevel_ThrowsExceptionForNullMRFId() {
        List<ApproverLevel> approverLevels = new ArrayList<>();
        ApproverLevel approverLevel = new ApproverLevel();
        approverLevel.setMrf(new MRF());
        approverLevels.add(approverLevel);
     
    }
     
    @Test
    void testSaveApproverLevel_ThrowsExceptionForNullBuHeadId() {
        List<ApproverLevel> approverLevels = new ArrayList<>();
        ApproverLevel approverLevel = new ApproverLevel();
        MRF mockMrf = new MRF();
        mockMrf.setMrfId(1L); // Valid MRF ID
        approverLevel.setMrf(mockMrf);
        approverLevels.add(approverLevel);
     
        ApproverLevelDto approverLevelDto = new ApproverLevelDto();
        approverLevelDto.setApproverLevel(approverLevels);
     
        when(mrfDao.findById(1L)).thenReturn(mockMrf);
     }
    
    
}
 