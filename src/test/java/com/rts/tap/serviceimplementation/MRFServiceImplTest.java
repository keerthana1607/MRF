package com.rts.tap.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.rts.tap.dao.MRFDao;
import com.rts.tap.dto.BulkMrfAssignDto;
import com.rts.tap.dto.MRFCriteriaDTO;
import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.model.Client;
import com.rts.tap.model.ClientOrganization;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFAgreement;
import com.rts.tap.model.MRFCriteria;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFStatus;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.OfferApproval;
import com.rts.tap.model.Requirement;
import com.rts.tap.model.Role;
import com.rts.tap.model.Vendor;
import com.rts.tap.utils.EmailUtil;

public class MRFServiceImplTest {

    @InjectMocks
    private MRFServiceImpl mrfService;

    private Employee employee;
    private MRFVendorDto mrfVendorDto;
    private MRFRecruiterDto mrfRecruiterDto;
    private BulkMrfAssignDto bulkMrfAssignDto;
    private MRFRecruitingManager mrfRecruitingManager;

    @Mock
    private MRFDao mrfDao;

    @Mock
    private EmailUtil emailUtil;

    private MRF mrf;
    private MRFAgreement mrfAgreement;

    private Employee businessUnitHead;
    private Employee clientPartner;
    private Requirement requirement;
    private Client client;
    
    @Mock
    private MultipartFile serviceLevelAgreement;


  
    

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mrf = new MRF();
        mrf.setMrfId(1L);

        mrfAgreement = new MRFAgreement();
        mrfAgreement.setServiceLevelAgreement(null);
        mrf.setMrfAgreement(mrfAgreement);

        mrf = new MRF();
        mrf.setMrfId(1L);
        mrf.setMrfStatus(new MRFStatus());
        mrf.setMrfAgreement(new MRFAgreement());
        mrf.setMrfCriteria(new MRFCriteria());
        mrf.setCreatedAt(new Date());
        mrf.setUpdatedAt(new Date());

        mrfAgreement = new MRFAgreement();
//        mrfCriteria = new MRFCriteria();
//        mrfStatus = new MRFStatus();

        businessUnitHead = new Employee();
        businessUnitHead.setEmployeeEmail("buh@example.com");
        businessUnitHead.setEmployeeName("BU Head");

        clientPartner = new Employee();
        clientPartner.setEmployeeEmail("cp@example.com");
        clientPartner.setEmployeeName("Client Partner");
        clientPartner.setRole(new Role(null, "Client Partner Role", null, null));

        client = new Client();
        client.setClientName("Test Client");
        ClientOrganization clientOrganization = new ClientOrganization();
        clientOrganization.setOrganizationName("Test Organization");
        client.setClientOrganization(clientOrganization);

        requirement = new Requirement();
        requirement.setClient(client);

        mrf = new MRF();
        mrf.setMrfId(1L);
        mrf.setBusinessUnitHead(businessUnitHead);
        mrf.setClientPartner(clientPartner);
        mrf.setRequirement(requirement);
        mrf.setMrfStatus(new MRFStatus());
        
        
        employee = new Employee();
        employee.setEmployeeId(1L);

//        mrf = new MRF();
//        mrf.setMrfId(1L);

        mrfVendorDto = new MRFVendorDto();
        mrfRecruiterDto = new MRFRecruiterDto();

        bulkMrfAssignDto = new BulkMrfAssignDto();
        bulkMrfAssignDto.setRmId(1L);
        bulkMrfAssignDto.setMrfVendorDtos(new ArrayList<>());
        bulkMrfAssignDto.setMrfRecruiterDtos(new ArrayList<>());

        mrfRecruitingManager = new MRFRecruitingManager();
        mrfRecruitingManager.setMrfRecruitingManagerId(1L);
    
    }

    
    @Test
    public void testUpdateMrfStatus_RejectedWithDescription() {
    	
    	String expectedTimeline = "Immediate";
        mrf.setRequirement(new Requirement());
        mrf.getRequirement().setTimeline(expectedTimeline);
        MRFCriteria criteria = new MRFCriteria();
        mrf.setMrfCriteria(criteria);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        mrfService.updateMrfStatus(1L, new MrfStatusDTO()); // This will indirectly call updateContractTimeline


        assertNotNull(criteria.getContractStartDate());
        assertNotNull(criteria.getClosureDate());
        Calendar expectedClosureDate = Calendar.getInstance();
        expectedClosureDate.setTime(criteria.getContractStartDate());
        expectedClosureDate.add(Calendar.DAY_OF_MONTH, 7);
        assertEquals(expectedClosureDate.getTime(), criteria.getClosureDate());
        

        MrfStatusDTO newStatus = new MrfStatusDTO();
        newStatus.setMrfApprovalStatus("Rejected");
        newStatus.setDescriptionForChanges("Reason for rejection");
     
        MRFStatus mrfStatus = new MRFStatus();
        mrf.setMrfStatus(mrfStatus);
     
        when(mrfDao.findById(anyLong())).thenReturn(mrf);
        when(mrfDao.mrfUpdate(anyLong(), any(MRF.class))).thenReturn(mrf);
        when(mrfDao.MrfBuStatus(any(MRFStatus.class))).thenReturn(mrfStatus);
    
        MRFStatus updatedStatus = mrfService.updateMrfStatus(1L, newStatus);
     

        assertNotNull(updatedStatus);
        assertEquals("Rejected", updatedStatus.getMrfApprovalStatus());
        assertEquals("Reason for rejection", updatedStatus.getDescriptionForChanges());
     
    }
   
 
    @Test
    public void testUpdateSOW_Success1() throws IOException {
    	 // Setting up mock objects
        mrfAgreement = new MRFAgreement();
        mrf = new MRF();
        mrf.setMrfAgreement(mrfAgreement);
 
        when(mrfDao.findById(anyLong())).thenReturn(mrf); // Mock findById
        MockMultipartFile serviceLevelAgreement = new MockMultipartFile("file", "sow.txt", "text/plain", "Sample SOW content".getBytes());
 
        when(mrfDao.updateSOW(any(MRFAgreement.class))).thenReturn(mrfAgreement);
 
        MRFAgreement updatedAgreement = mrfService.updateSOW(1L, serviceLevelAgreement);
 
        assertNotNull(updatedAgreement);
        verify(mrfDao, times(1)).findById(1L);
        verify(mrfDao, times(1)).updateSOW(mrfAgreement);
    }
 
    @Test
    public void testUpdateSOW_NoMRFAgreement() {
    	 // Setting up mock objects
        mrfAgreement = new MRFAgreement();
        mrf = new MRF();
        mrf.setMrfAgreement(mrfAgreement);
 
        when(mrfDao.findById(anyLong())).thenReturn(mrf); // Mock findById
        mrf.setMrfAgreement(null);
 
        Exception exception = assertThrows(NoSuchElementException.class, () -> mrfService.updateSOW(1L, null));
 
        assertEquals("MRFAgreement with ID 1 not found.", exception.getMessage());
        verify(mrfDao, times(1)).findById(1L);

    }
 
    @Test
    public void testUpdateSOW_ExistingSLA() {
    	 // Setting up mock objects
        mrfAgreement = new MRFAgreement();
        mrf = new MRF();
        mrf.setMrfAgreement(mrfAgreement);
 
        when(mrfDao.findById(anyLong())).thenReturn(mrf); // Mock findById
        mrfAgreement.setServiceLevelAgreement(new byte[10]); // SLA already exists
 
        Exception exception = assertThrows(IllegalStateException.class, () -> mrfService.updateSOW(1L, null));
 
        assertEquals("Service Level Agreement already exists and cannot be updated.", exception.getMessage());
        verify(mrfDao, times(1)).findById(1L);
    }
 
    @Test
    public void testUpdateSOW_IOError() throws IOException {
        mrfAgreement = new MRFAgreement();
        mrf = new MRF();
        mrf.setMrfAgreement(mrfAgreement);
 
        when(mrfDao.findById(anyLong())).thenReturn(mrf); // Mock findById
        MockMultipartFile serviceLevelAgreement = mock(MockMultipartFile.class);
        when(serviceLevelAgreement.getBytes()).thenThrow(new IOException("Test IO Exception"));
 
        Exception exception = assertThrows(RuntimeException.class, () -> mrfService.updateSOW(1L, serviceLevelAgreement));
 
        assertEquals("Failed to upload service level agreement", exception.getMessage());
        verify(mrfDao, times(1)).findById(1L);
    }

    
    @Test
    public void testUpdateSOW_Success() throws IOException {
        byte[] agreementBytes = new byte[]{1, 2, 3, 4};

        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(serviceLevelAgreement.getBytes()).thenReturn(agreementBytes);
        when(mrfDao.updateSOW(any(MRFAgreement.class))).thenReturn(mrfAgreement);

    }

    @Test
    public void testUpdateSOW_MRFNotFound() {
        when(mrfDao.findById(1L)).thenReturn(null);
    }

    @Test
    public void testSendForApproval_Success() {
        // Set up a pending approval status
        mrf.getMrfStatus().setMrfApprovalStatus("Not Assigned");
        mrf.setMrfAgreement(new MRFAgreement()); // Ensure agreement exists

        when(mrfDao.findById(1L)).thenReturn(mrf);
        when(mrfDao.MrfBuStatus(any(MRFStatus.class))).thenReturn(mrf.getMrfStatus());

        MRFStatus resultStatus = mrfService.sendForApproval(1L);

        assertEquals("Pending", resultStatus.getMrfApprovalStatus());
        verify(emailUtil).sendBuApprovalRequestNotificationEmail(
                "buh@example.com", "BU Head", "Test Client", "Test Organization",
                "Client Partner", "Client Partner Role", "cp@example.com",
                null, null
        );
        verify(mrfDao).MrfBuStatus(mrf.getMrfStatus());
    }

    @Test
    public void testSendForApproval_NoMrfFound() {
        when(mrfDao.findById(1L)).thenReturn(null);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () ->
                mrfService.sendForApproval(1L));
        assertEquals("MRF not found with ID: 1", thrown.getMessage());
    }

    @Test
    public void testSendForApproval_BusinessUnitHeadNull() {
        mrf.setBusinessUnitHead(null);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                mrfService.sendForApproval(1L));
        assertEquals("Business Unit Head or Client Partner is not assigned.", thrown.getMessage());
    }

    @Test
    public void testSendForApproval_ClientPartnerNull() {
        mrf.setClientPartner(null);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                mrfService.sendForApproval(1L));
        assertEquals("Business Unit Head or Client Partner is not assigned.", thrown.getMessage());
    }

    @Test
    public void testSendForApproval_RequirementNull() {
        mrf.setRequirement(null);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                mrfService.sendForApproval(1L));
        assertEquals("Requirement is required for MRF approval.", thrown.getMessage());
    }

    @Test
    public void testSendForApproval_InvalidApprovalStatus() {
        mrf.getMrfStatus().setMrfApprovalStatus("Pending");
        when(mrfDao.findById(1L)).thenReturn(mrf);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () ->
                mrfService.sendForApproval(1L));
        assertEquals("Cannot send MRF for approval: Current status is Pending", thrown.getMessage());
    }

    @Test
    public void testAddMrf() {
        when(mrfDao.mrfSave(any(MRF.class))).thenReturn(mrf);
        MRF savedMrf = mrfService.addMrf(mrf);
        assertEquals(savedMrf.getMrfId(), mrf.getMrfId());
        
    }

    @Test
    public void testGetRequirementFilledCount() {
        when(mrfDao.getRequirementFilledCount(anyLong())).thenReturn(5);
        int count = mrfService.getRequirementFilledCount(1L);
        assertEquals(5, count);
    }

    @Test
    public void testUpdateMrf() {
        when(mrfDao.mrfUpdate(anyLong(), any(MRF.class))).thenReturn(mrf);
        MRF updatedMrf = mrfService.updateMrf(1L, mrf);
        assertEquals(updatedMrf.getMrfId(), mrf.getMrfId());
        verify(mrfDao).mrfUpdate(1L, mrf);
    }

    @Test
    public void testDeleteMrfById() {
        when(mrfDao.mrfDelete(anyLong())).thenReturn("MRF deleted successfully");
        String result = mrfService.deleteMrfById(1);
        assertEquals("MRF deleted successfully", result);
    }

    @Test
    public void testGetMrfById() {
        when(mrfDao.getMrf(anyLong())).thenReturn(mrf);
        MRF foundMrf = mrfService.getMrfById(1);
        assertEquals(foundMrf.getMrfId(), mrf.getMrfId());
    }

    @Test
    public void testGetAllMrf() {
        List<MRF> mrfList = new ArrayList<>();
        mrfList.add(mrf);
        when(mrfDao.getAllMRF()).thenReturn(mrfList);
        List<MRF> result = mrfService.getAllMrf();
        assertEquals(1, result.size());
    }

    @Test
    public void testAssignMrfToRecruitingManager() {
        MRFRecruitingManager mrfRecruitingManager = new MRFRecruitingManager();
        when(mrfDao.saveAssignedMRFToRecruitingManager(any(MRFRecruitingManager.class))).thenReturn(mrfRecruitingManager);
        MRFRecruitingManager assignedMrf = mrfService.assignMrfToRecruitingManager(mrfRecruitingManager);
        assertNotNull(assignedMrf);
    }

//    @Test
//    public void testGetRecruitingManagersByRoleName() {
//        List<Employee> employees = new ArrayList<>();
//        when(mrfDao.getAllRecruitingManager()).thenReturn(employees);
//        List<Employee> result = mrfService.getRecruitingManagersByRoleName();
//        assertEquals(employees, result);
//    }

//    @Test
//    public void testGetAssignedMRFs() {
//        List<MRFRecruitingManager> assignedMrfList = new ArrayList<>();
//        when(mrfDao.findAssignedMRFs()).thenReturn(assignedMrfList);
//        List<MRFRecruitingManager> result = mrfService.getAssignedMRFs();
//        assertEquals(assignedMrfList, result);
//    }

    @Test
    public void testGetOfferApprovalsByEmployeeId() {
        List<OfferApproval> offerApprovals = new ArrayList<>();
        when(mrfDao.findOfferApprovalsByEmployeeId(anyLong())).thenReturn(offerApprovals);
        List<OfferApproval> result = mrfService.getOfferApprovalsByEmployeeId(1L);
        assertEquals(offerApprovals, result);
    }

   
   

//    @Test
//    public void testGetAllMrfsBasedOnClientPartnerId() {
//        List<MRF> mrfList = new ArrayList<>();
//        when(mrfDao.getAllMrfsBasedOnClientPartnerId(anyLong())).thenReturn(mrfList);
//        List<MRF> result = mrfService.getAllMrfsBasedOnClientPartnerId(1L);
//        assertEquals(mrfList, result);
//    }

    @Test
    public void testGetAssignedMrf() {
        MRFRecruitingManager assignedMrf = new MRFRecruitingManager();
        when(mrfDao.findAssignedMRFsToRecruitingManager(anyLong())).thenReturn(assignedMrf);
        MRFRecruitingManager result = mrfService.getAssignedMrf(1L);
        assertEquals(assignedMrf, result);
    }

//    @Test
//    public void testGetRecruitersByMrfId() {
//        List<MRFRecruiters> recruiters = new ArrayList<>();
//        when(mrfDao.getRecruitersByMrfId(anyLong())).thenReturn(recruiters);
//        List<MRFRecruiters> result = mrfService.getRecruitersByMrfId(1L);
//        assertEquals(recruiters, result);
//    }

//    @Test
//    public void testGetAssignedVendorsByMrfId() {
//        List<MRFVendor> vendors = new ArrayList<>();
//        when(mrfDao.getAssignedVendorsByMrfId(anyLong())).thenReturn(vendors);
//        List<MRFVendor> result = mrfService.getAssignedVendorsByMrfId(1L);
//        assertEquals(vendors, result);
//    }

   

   
//    @Test
//    public void testGetAllMrfByEmployeeId() {
//        List<MRF> mrfList = new ArrayList<>();
//        when(mrfDao.findByEmployeeId(anyString())).thenReturn(mrfList);
//        List<MRF> result = mrfService.getAllMrfByEmployeeId("123");
//        assertEquals(mrfList, result);
//    }

    

    @Test
    public void testUpdateMrfStatus_MRFNotFound() {
        when(mrfDao.findById(anyLong())).thenReturn(null);
        MrfStatusDTO statusDTO = new MrfStatusDTO();
        
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            mrfService.updateMrfStatus(2L, statusDTO);
        });
        assertEquals("MRF with ID 2 not found.", thrown.getMessage());
    }

    @Test
    public void testGetMrfByMrfJdId() {
        Long mrfJdId = 1L;
        MRFCriteria criteria = new MRFCriteria();
        when(mrfDao.getMrfByMrfJdId(mrfJdId)).thenReturn(criteria);

        MRFCriteriaDTO dto = mrfService.getMrfByMrfJdId(mrfJdId);
        assertNotNull(dto);
    }

//    @Test
//    public void testViewAllRecruitingManagerByClientPartnerId() {
//        List<Employee> employees = new ArrayList<>();
//        when(mrfDao.getRecruitingManagerByClientPartnerId(anyLong())).thenReturn(employees);
//        List<Employee> result = mrfService.viewAllRecruitingManagerByClientPartnerId(1L);
//        assertEquals(employees, result);
//    }

//    @Test
//    public void testViewAllVendorsByRecruitingManagerId() {
//        List<Vendor> vendors = new ArrayList<>();
//        when(mrfDao.getVendorsByRecruitingManagerId(anyLong())).thenReturn(vendors);
//        List<Vendor> result = mrfService.viewAllVendorsByRecruitingManagerId(1L);
//        assertEquals(vendors, result);
//    }

//    @Test
//    public void testViewAllRecruitersByRecruitingManagerId() {
//        List<Employee> recruiters = new ArrayList<>();
//        when(mrfDao.getRecruitersByRecruitingManagerId(anyLong())).thenReturn(recruiters);
//        List<Employee> result = mrfService.viewAllRecruitersByRecruitingManagerId(1L);
//        assertEquals(recruiters, result);
//    }
    

    @Test
    public void testUpdateMrfStatus_MRFNotFound1() {
        // Arrange
        when(mrfDao.findById(anyLong())).thenReturn(null);
        MrfStatusDTO statusDTO = new MrfStatusDTO();

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            mrfService.updateMrfStatus(2L, statusDTO);
        });
        assertEquals("MRF with ID 2 not found.", thrown.getMessage());
    }
    
    @Test
    public void testUpdateContractTimeline_ImmediateTimeline() {
        // Arrange
        String expectedTimeline = "Immediate";
        mrf.setRequirement(new Requirement());
        mrf.getRequirement().setTimeline(expectedTimeline);
        MRFCriteria criteria = new MRFCriteria();
        mrf.setMrfCriteria(criteria);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        // Act
        mrfService.updateMrfStatus(1L, new MrfStatusDTO()); // This will indirectly call updateContractTimeline

        // Assert
        assertNotNull(criteria.getContractStartDate());
        assertNotNull(criteria.getClosureDate());
        Calendar expectedClosureDate = Calendar.getInstance();
        expectedClosureDate.setTime(criteria.getContractStartDate());
        expectedClosureDate.add(Calendar.DAY_OF_MONTH, 7);
        assertEquals(expectedClosureDate.getTime(), criteria.getClosureDate());
    }

    @Test
    public void testUpdateContractTimeline_30DaysTimeline() {
        // Arrange
        String expectedTimeline = "30Days";
        mrf.setRequirement(new Requirement());
        mrf.getRequirement().setTimeline(expectedTimeline);
        MRFCriteria criteria = new MRFCriteria();
        mrf.setMrfCriteria(criteria);
        when(mrfDao.findById(1L)).thenReturn(mrf);

        // Act
        mrfService.updateMrfStatus(1L, new MrfStatusDTO()); // This will indirectly call updateContractTimeline

        // Assert
        assertNotNull(criteria.getContractStartDate());
        assertNotNull(criteria.getClosureDate());
        Calendar expectedClosureDate = Calendar.getInstance();
        expectedClosureDate.setTime(criteria.getContractStartDate());
        expectedClosureDate.add(Calendar.DAY_OF_MONTH, 30);
        assertEquals(expectedClosureDate.getTime(), criteria.getClosureDate());
    }
    
//    @Test
//    public void testFetchRecruitingManagerById() {
//        when(mrfDao.getEmployeeById(anyLong())).thenReturn(employee);
//        Employee result = mrfService.fetchRecruitingManagerById(1L);
//        assertNotNull(result);
//        verify(mrfDao, times(1)).getEmployeeById(anyLong());
//    }

//    @Test
//    public void testGetAllMrfsAssignedForRM() {
//        when(mrfDao.getAllMrfsAssignedForRM(anyLong())).thenReturn(new ArrayList<>());
//        List<MRFRecruitingManager> results = mrfService.getAllMrfsAssignedForRM(1L);
//        assertNotNull(results);
//        verify(mrfDao, times(1)).getAllMrfsAssignedForRM(anyLong());
//    }

//    @Test
//    public void testGetMrfById1() {
//        when(mrfDao.getMrfById(anyLong())).thenReturn(mrf);
//        MRF result = mrfService.getMrfById(1L);
//        assertNull(result);
//        verify(mrfDao, times(1)).getMrfById(anyLong());
//    }

    @Test
    public void testMrfAssignToVendor() {
        when(mrfDao.assignMrfToVendor(any(MRFVendorDto.class))).thenReturn("Assigned");
        String result = mrfService.mrfAssignToVendor(mrfVendorDto);
        assertNotNull(result);
        verify(mrfDao, times(1)).assignMrfToVendor(any(MRFVendorDto.class));
    }

    @Test
    public void testMrfAssignToRecruiter() {
        when(mrfDao.assignMrfToRecruiter(any(MRFRecruiterDto.class))).thenReturn("Assigned");
        String result = mrfService.mrfAssignToRecruiter(mrfRecruiterDto);
        assertNotNull(result);
        verify(mrfDao, times(1)).assignMrfToRecruiter(any(MRFRecruiterDto.class));
    }

    @Test
    public void testMrfBulkAssignToRecruiter() {
        when(mrfDao.fetchMrfRecruitingManagerByMrfIdAndmManagerId(anyLong(), anyLong())).thenReturn(mrfRecruitingManager);
        when(mrfDao.assignMrfToRecruiter(any(MRFRecruiterDto.class))).thenReturn("Assigned");
        bulkMrfAssignDto.setMrfRecruiterDtos(List.of(new MRFRecruiterDto()));
        String result = mrfService.mrfBulkAssignToRecruiter(1L, bulkMrfAssignDto);
        assertNotNull(result);
        verify(mrfDao, times(1)).assignMrfToRecruiter(any(MRFRecruiterDto.class));
    }

    @Test
    public void testMrfBulkAssignToVendor() {
        when(mrfDao.assignMrfToVendor(any(MRFVendorDto.class))).thenReturn("Assigned");
        bulkMrfAssignDto.setMrfVendorDtos(List.of(new MRFVendorDto()));
        String result = mrfService.mrfBulkAssignToVendor(1L, bulkMrfAssignDto);
        assertNotNull(result);
        verify(mrfDao, times(1)).assignMrfToVendor(any(MRFVendorDto.class));
    }

    @Test
    public void testGetMrfRecruitingManagerByMrfIdAndManagerId() {
        when(mrfDao.fetchMrfRecruitingManagerByMrfIdAndmManagerId(anyLong(), anyLong())).thenReturn(mrfRecruitingManager);
        MRFRecruitingManager result = mrfService.getMrfRecruitingManagerByMrfIdAndManagerId(1L, 1L);
        assertNotNull(result);
    }

//    @Test
//    public void testGetAllMrfVendorsRecords() {
//        when(mrfDao.getAllMrfsVendors()).thenReturn(new ArrayList<>());
//        List<MRFVendorDto> results = mrfService.getAllMrfVendorsRecords();
//        assertNotNull(results);
//    }

//    @Test
//    public void testGetAllAssignedMrfRecruiterRecords() {
//        when(mrfDao.getAllRecruitersAssigned()).thenReturn(new ArrayList<>());
//        List<MRFRecruiters> results = mrfService.getAllAssignedMrfRecruiterRecords();
//        assertNotNull(results);
//    }

//    @Test
//    public void testUpdateMrfRecruiter() {
//        when(mrfDao.updateMrfRecruiter(any(MRFRecruiterDto.class), anyLong())).thenReturn("Updated");
//        String result = mrfService.updateMrfRecruiter(mrfRecruiterDto, 1L);
//        assertNotNull(result);
//    }

    @Test
    public void testReassignMrfToRecruiter() {
        when(mrfDao.reassignMrfToRecruiter(any(MRFRecruiterDto.class))).thenReturn("Reassigned");
        String result = mrfService.reassignMrfToRecruiter(mrfRecruiterDto);
        assertNotNull(result);
    }

//    @Test
//    public void testGetAllRecruitersByRecruitingManagerID() {
//        when(mrfDao.getAllRecruitersByRecruitingManagerId(anyLong())).thenReturn(new ArrayList<>());
//        List<Employee> results = mrfService.getAllRecruitersByRecruitingManagerID(1L);
//        assertNotNull(results);
//    }

//    @Test
//    public void testGetAllRecruitersByMRFRecruitingManagerId() {
//        when(mrfDao.getAssignedRecruitersByMrfRecruitingManagerId(anyLong())).thenReturn(new ArrayList<>());
//        List<MRFRecruiters> results = mrfService.getAllRecruitersByMRFRecruitingManagerId(1L);
//        assertNotNull(results);
//    }

//    @Test
//    public void testGetAllVendorsAssignedForMRFbyMrfId() {
//        when(mrfDao.getAllVendorsAssignedForMrf(anyLong())).thenReturn(new ArrayList<>());
//        List<MRFVendor> results = mrfService.getAllVendorsAssignedForMRFbyMrfId(1L);
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchAllVendorOverAllPerformance() {
//        when(mrfDao.getAllVendorOverAllPerformance()).thenReturn(new ArrayList<>());
//        List<VendorPerformance> results = mrfService.fetchAllVendorOverAllPerformance();
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchAllIndividualPerformanceOfVendorByMrfId() {
//        when(mrfDao.getAllIndividualPerformanceOfVendorByMrfId(anyLong())).thenReturn(new ArrayList<>());
//        List<VendorPerformanceHistory> results = mrfService.fetchAllIndividualPerformanceOfVendorByMrfId(1L);
//        assertNotNull(results);
//    }
//    
//    @Test
//    public void testFetchAllIndividualPerformanceOfVendorByVendorId() {
//        when(mrfDao.getAllIndividualPerformanceOfVendorByVendorId(anyLong())).thenReturn(new ArrayList<>());
//        List<VendorPerformanceHistory> results = mrfService.fetchAllIndividualPerformanceOfVendorByVendorId(1L);
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchAllRecruiterOverAllPerformance() {
//        when(mrfDao.getAllRecruiterOverAllPerformance(anyLong())).thenReturn(new ArrayList<>());
//        List<RecruiterOverallPerformance> results = mrfService.fetchAllRecruiterOverAllPerformance(1L);
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchAllIndividualPerformanceOfRecruiterByMrfId() {
//        when(mrfDao.getAllIndividualPerformanceOfRecruiterByMrfId(anyLong())).thenReturn(new ArrayList<>());
//        List<RecruiterPerformance> results = mrfService.fetchAllIndividualPerformanceOfRecruiterByMrfId(1L);
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchAllIndividualPerformanceOfRecruiterByRecruiterId() {
//        when(mrfDao.getAllIndividualPerformanceOfRecruiterByRecruiterId(anyLong())).thenReturn(new ArrayList<>());
//        List<RecruiterPerformance> results = mrfService.fetchAllIndividualPerformanceOfRecruiterByRecruiterId(1L);
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testGetAllMrfVendors() {
//        when(mrfDao.getAllMrfVendors()).thenReturn(new ArrayList<>());
//        List<MRFVendor> results = mrfService.getAllMrfVendors();
//        assertNotNull(results);
//    }
//
//    @Test
//    public void testFetchIndividualPerformanceOfVendorById() {
//        when(mrfDao.getIndividualPerformanceOfVendorById(anyLong())).thenReturn(new VendorPerformance());
//        VendorPerformance result = mrfService.fetchIndividualPerformanceOfVendorById(1L);
//        assertNotNull(result);
//    }

//    @Test
//    public void testFetchAllAssignedMrfsForAllRecruiter() {
//        when(mrfDao.getAllAssignedMrfsForAllRecruiter(anyLong())).thenReturn(new ArrayList<>());
//        List<MRFRecruiters> results = mrfService.fetchAllAssignedMrfsForAllRecruiter(1L);
//        assertNotNull(results);
//    }

//    @Test
//    public void testFetchAllAssignedMrfsForAllVendors() {
//        when(mrfDao.getAllAssignedMrfsForAllVendors()).thenReturn(new ArrayList<>());
//        List<MRFVendor> results = mrfService.fetchAllAssignedMrfsForAllVendors();
//        assertNotNull(results);
//    }
    
//    @Test
//    public void testGetAllMRFByRecruiterId() {
//        long recruiterId = 1L;
//        List<MRFRecruiters> expectedMrfList = new ArrayList<>();
//        when(mrfDao.getAllMrfsForRecruiterId(recruiterId)).thenReturn(expectedMrfList); // Mocking the DAO call
//
//        List<MRFRecruiters> actualMrfList = mrfService.getAllMRFByRecruiterId(recruiterId); // Calling the service method
//
//        assertEquals(expectedMrfList, actualMrfList); // Asserting that the returned list matches the expected list
//        verify(mrfDao, times(1)).getAllMrfsForRecruiterId(recruiterId); // Verifying that the DAO method was called once
//    }

//    @Test
//    public void testGetAllMRFByVendorId() {
//        long vendorId = 1L;
//        List<MRFVendor> expectedMrfList = new ArrayList<>();
//        when(mrfDao.getAllMrfsForVendor(vendorId)).thenReturn(expectedMrfList); // Mocking the DAO call
//
//        List<MRFVendor> actualMrfList = mrfService.getAllMRFByVendorId(vendorId); // Calling the service method
//
//        assertEquals(expectedMrfList, actualMrfList); // Asserting that the returned list matches the expected list
//        verify(mrfDao, times(1)).getAllMrfsForVendor(vendorId); // Verifying that the DAO method was called once
//    }

//    @Test
//    public void testGetAllMrfsVendorsAssignedByRM() {
//        long employeeId = 1L;
//        List<MRFVendor> expectedMrfVendorList = new ArrayList<>();
//        when(mrfDao.getAllMrfsVendorsAssignedByRM(employeeId)).thenReturn(expectedMrfVendorList); // Mocking the DAO call
//
//        List<MRFVendor> actualMrfVendorList = mrfService.getAllMrfsVendorsAssignedByRM(employeeId); // Calling the service method
//
//        assertEquals(expectedMrfVendorList, actualMrfVendorList); // Asserting that the returned list matches the expected list
//        verify(mrfDao, times(1)).getAllMrfsVendorsAssignedByRM(employeeId); // Verifying that the DAO method was called once
//    }
    
//    @Test
//    public void testGetAllMrfsRecruitersAssignedByRM() {
//        long employeeId = 1L; // The ID of the employee which is used as an input
//        List<MRFRecruiters> expectedMrfRecruitersList = new ArrayList<>(); // This is the expected result from the DAO
//
//        // Mocking the behavior of the DAO
//        when(mrfDao.getAllMrfsRecruitersAssignedByRM(employeeId)).thenReturn(expectedMrfRecruitersList);
//
//        // Calling the service method
//        List<MRFRecruiters> actualMrfRecruitersList = mrfService.getAllMrfsRecruitersAssignedByRM(employeeId);
//
//        // Asserting that the actual list returned by the service matches the expected list
//        assertEquals(expectedMrfRecruitersList, actualMrfRecruitersList);
//
//        // Verifying that the DAO method was called exactly once with the correct employeeId
//        verify(mrfDao, times(1)).getAllMrfsRecruitersAssignedByRM(employeeId);
//    }
    
    @Test
    public void testGetAllMrfVendorsRecords() {
        // Arrange
        List<MRFVendor> mrfVendorList = new ArrayList<>();
        
        // Create mock MRFVendor objects to return from the DAO
        MRFVendor mrfVendor = new MRFVendor();
        mrfVendor.setAssignedCount(5);
        mrfVendor.setAchievedCount(10);
        mrfVendor.setVendorAssignedStatus("Active");

        // Create mock MRF and RecruitingManager for MRFVendor
        MRF mockMrf = new MRF();
        mockMrf.setMrfId(1L);
        mrfVendor.setMrf(mockMrf);
        
        Employee mockRecruitingManager = new Employee();
        mockRecruitingManager.setEmployeeId(2L);
        mrfVendor.setRecruitingManager(mockRecruitingManager);

        Vendor mockVendor = new Vendor();
        mockVendor.setVendorId(3L);
        mrfVendor.setVendor(mockVendor);

        // Add the mock MRFVendor to the list
        mrfVendorList.add(mrfVendor);

        // Mock DAO call to return the list of MRFVendor
        when(mrfDao.getAllMrfsVendors()).thenReturn(mrfVendorList);

        // Act
        List<MRFVendorDto> actualMrfVendorDtoList = mrfService.getAllMrfVendorsRecords();

        // Assert
        assertNotNull(actualMrfVendorDtoList);
        assertEquals(1, actualMrfVendorDtoList.size()); // Ensure it contains one item

        // Validate the transformed fields
        MRFVendorDto actualMrfVendorDto = actualMrfVendorDtoList.get(0);
        assertEquals(1L, actualMrfVendorDto.getMrfId());
        assertEquals(2L, actualMrfVendorDto.getRecrutingManagerId());
        assertEquals(3L, actualMrfVendorDto.getVendorId());
        assertEquals(5, actualMrfVendorDto.getAssignedCount());
        assertEquals(10, actualMrfVendorDto.getAchievedCount());
        assertEquals("Active", actualMrfVendorDto.getVendorAssignedStatus());

        // Verify that the DAO method was called once
        verify(mrfDao, times(1)).getAllMrfsVendors();
    }
       
}