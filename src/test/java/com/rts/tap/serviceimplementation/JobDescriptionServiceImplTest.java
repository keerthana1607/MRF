//package com.rts.tap.serviceimplementation;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.rts.tap.constants.MessageConstants;
//import com.rts.tap.dao.JobDescriptionDAO;
//import com.rts.tap.model.JobDescription;
//import com.rts.tap.model.MrfJd;


//
//class JobDescriptionServiceImplTest {
//
//	@InjectMocks
//	private JobDescriptionServiceImpl jobDescriptionService;
//
//	@Mock
//	private JobDescriptionDAO jobDescriptionDao;
//
//	@Mock
//	private MultipartFile rolesAndResponsibilities;
//
//	private JobDescription jobDescription;
//	private MrfJd mrfJd;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//		jobDescription = new JobDescription();
//		jobDescription.setJobTitle("Developer");
//		jobDescription.setJobParameter("Java, Spring");
//
//		mrfJd = new MrfJd();
//		mrfJd.setJobTitle("MRD Developer");
//		mrfJd.setJobParameter("Java, Spring");
//	}
//
//	@Test
//	void addJobDescription_Success() throws IOException {
//		when(rolesAndResponsibilities.getBytes()).thenReturn("Dummy data".getBytes());
//		when(jobDescriptionDao.saveJobDescription(any(JobDescription.class))).thenReturn(jobDescription);
//
//		JobDescription result = jobDescriptionService.addJobDescription("Developer", "Java, Spring",
//				rolesAndResponsibilities);
//
//		verify(jobDescriptionDao).saveJobDescription(any(JobDescription.class));
//		assertEquals("Developer", result.getJobTitle());
//	}
//
//	@Test
//	void addJobDescription_Failure_IOException() throws IOException {
//		when(rolesAndResponsibilities.getBytes()).thenThrow(new IOException());
//
//		RuntimeException thrown = assertThrows(RuntimeException.class,
//				() -> jobDescriptionService.addJobDescription("Developer", "Java, Spring", rolesAndResponsibilities));
//
//		assertEquals(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED, thrown.getMessage());
//	}
//
//	@Test
//	void editJobDescription_Success() throws IOException {
//		when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(jobDescription);
//		when(rolesAndResponsibilities.getBytes()).thenReturn("New Dummy Data".getBytes());
//
//		JobDescription result = jobDescriptionService.editJobDescription(1L, "Senior Developer", "Java, Spring, React",
//				rolesAndResponsibilities);
//
//		verify(jobDescriptionDao).saveJobDescription(any(JobDescription.class));
//		assertEquals("Senior Developer", result.getJobTitle());
//	}
//
//	@Test
//	void editJobDescription_Failure_NotFound() {
//		when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(null);
//
//		RuntimeException thrown = assertThrows(RuntimeException.class,
//				() -> jobDescriptionService.editJobDescription(1L, "Developer", "Java, Spring", null));
//
//		assertEquals(MessageConstants.JOB_DESCRIPTION_NOT_FOUND, thrown.getMessage());
//	}
//
//	@Test
//	void getAllJobDescriptions() {
//		when(jobDescriptionDao.viewAllJobDescriptions()).thenReturn(Collections.singletonList(jobDescription));
//
//		List<JobDescription> results = jobDescriptionService.getAllJobDescriptions();
//
//		assertEquals(1, results.size());
//	}
//
//	@Test
//	void getByJobDescriptionId_Success() {
//		when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(jobDescription);
//
//		JobDescription result = jobDescriptionService.getByJobDescriptionId(1L);
//
//		assertEquals("Developer", result.getJobTitle());
//	}
//
//	@Test
//	void getByJobDescriptionId_Failure_NotFound() {
//		when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(null);
//
//		JobDescription result = jobDescriptionService.getByJobDescriptionId(1L);
//
//		assertNull(result);
//	}
//
//	@Test
//	void getAllJobTitles_Success() {
//		when(jobDescriptionDao.viewAllJobTitles()).thenReturn(Collections.singletonList("Developer"));
//
//		List<String> jobTitles = jobDescriptionService.getAllJobTitles();
//
//		assertEquals(1, jobTitles.size());
//	}
//
//	@Test
//	void addJobDescriptionAssignToMrf_Success() throws IOException {
//		when(rolesAndResponsibilities.getBytes()).thenReturn("MRD Dummy Data".getBytes());
//		when(jobDescriptionDao.saveJobDescription(any(MrfJd.class))).thenReturn(mrfJd);
//
//		MrfJd result = jobDescriptionService.addJobDescriptionAssignToMrf("MRD Developer", "Java, Spring",
//				rolesAndResponsibilities);
//
//		verify(jobDescriptionDao).saveJobDescription(any(MrfJd.class));
//		assertEquals("MRD Developer", result.getJobTitle());
//	}
//
//	@Test
//	void editMrfJd_Success() throws IOException {
//		when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(mrfJd);
//		when(rolesAndResponsibilities.getBytes()).thenReturn("Updated MRD Data".getBytes());
//
//		MrfJd result = jobDescriptionService.editMrfJd(1L, "Updated MRD Developer", "Java, Spring, React",
//				rolesAndResponsibilities);
//
//		verify(jobDescriptionDao).updateMrfJd(any(MrfJd.class));
//		assertEquals("Updated MRD Developer", result.getJobTitle());
//	}
//
//	@Test
//	void editMrfJd_Failure_NotFound() {
//		when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(null);
//
//		RuntimeException thrown = assertThrows(RuntimeException.class,
//				() -> jobDescriptionService.editMrfJd(1L, "Developer", "Java, Spring", null));
//
//		assertEquals(MessageConstants.JOB_DESCRIPTION_NOT_FOUND, thrown.getMessage());
//	}
//
//	@Test
//	void getAllMrfJd_Success() {
//		when(jobDescriptionDao.getAllMrfJd()).thenReturn(Collections.singletonList(mrfJd));
//
//		List<MrfJd> results = jobDescriptionService.getAllMrfJd();
//
//		assertEquals(1, results.size());
//	}
//
//	@Test
//	void getByMrfJdId_Success() {
//		when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(mrfJd);
//
//		MrfJd result = jobDescriptionService.getByMrfJdId(1L);
//
//		assertEquals("MRD Developer", result.getJobTitle());
//	}
//
//	@Test
//	void getByMrfJdId_Failure_NotFound() {
//		when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(null);
//
//		MrfJd result = jobDescriptionService.getByMrfJdId(1L);
//
//		assertNull(result);
//	}
//	
//	@Test
//	void addJobDescription_Failure_GeneralException() throws IOException {
//	    // Arrange
//	    when(rolesAndResponsibilities.getBytes()).thenReturn("Dummy data".getBytes());
//	    doThrow(new RuntimeException("Some other error")).when(jobDescriptionDao).saveJobDescription(any(JobDescription.class));
//
//	    // Act & Assert
//	    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//	        jobDescriptionService.addJobDescription("Developer", "Java, Spring", rolesAndResponsibilities)
//	    );
//
//	    assertEquals(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED, thrown.getMessage());
//	}
//	
//	@Test
//	void editMrfJd_Failure_IOUpdate() throws IOException {
//	    // Arrange
//	    when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(mrfJd);
//	    when(rolesAndResponsibilities.getBytes()).thenThrow(new IOException("Update failed"));
//
//	    // Act & Assert
//	    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//	        jobDescriptionService.editMrfJd(1L, "Updated MRD Developer", "Java, Spring", rolesAndResponsibilities)
//	    );
//
//	    assertEquals(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED, thrown.getMessage());
//	}
//	
//	@Test
//	void getByJobTitle_Success() {
//	    // Arrange
//	    String jobTitle = "Developer";
//	    JobDescription expectedJobDescription = new JobDescription();
//	    expectedJobDescription.setJobTitle(jobTitle);
//
//	    when(jobDescriptionDao.viewByJobTitle(jobTitle)).thenReturn(expectedJobDescription);
//
//	    // Act
//	    JobDescription actualJobDescription = jobDescriptionService.getByJobTitle(jobTitle);
//
//	    // Assert
//	    assertNotNull(actualJobDescription);
//	    assertEquals(expectedJobDescription.getJobTitle(), actualJobDescription.getJobTitle());
//	    verify(jobDescriptionDao, times(1)).viewByJobTitle(jobTitle);
//	}
//	
//	@Test
//	void updateJobDescription_Failure_IOException() throws IOException {
//	    // Arrange
//	    JobDescription jobDescription = new JobDescription();
//	    jobDescription.setJobTitle("Updated Job Title");
//
//	    // Simulate IOException when attempting to update the job description
////	    doThrow(new IOException("IO Exception occurred")).when(jobDescriptionDao).updateJobDescription(any());
//
//	    when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(mrfJd);
//	    when(rolesAndResponsibilities.getBytes()).thenThrow(new IOException("Failed to Update Job Description"));
//
//	    // Act & Assert
//	    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//	        ((JobDescriptionDAO) jobDescriptionService).updateJobDescription(jobDescription)
//	    );
//
//	    assertNotEquals(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED, thrown.getMessage());
//	    
//	    // Verify that the IOException was logged
////	    verify(logger).error("Failed to update job description roles and responsibilities: {}", "IO Exception occurred");
//	}
//	
//	
//}


package com.rts.tap.serviceimplementation;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
 
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.JobDescriptionDAO;
import com.rts.tap.model.JobDescription;
import com.rts.tap.model.MrfJd;
 
class JobDescriptionServiceImplTest {
 
    @Mock
    private JobDescriptionDAO jobDescriptionDao;
 
    @InjectMocks
    private JobDescriptionServiceImpl jobDescriptionService;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testAddJobDescriptionSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn("test content".getBytes());
 
        JobDescription result = jobDescriptionService.addJobDescription("Software Engineer", "Java", mockFile);
 
        assertNotNull(result);
        assertEquals("Software Engineer", result.getJobTitle());
        assertEquals("Java", result.getJobParameter());
        assertArrayEquals("test content".getBytes(), result.getRolesAndResponsibilities());
 
        verify(jobDescriptionDao, times(1)).saveJobDescription(result);
    }
 
    @Test
    void testAddJobDescriptionIOException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("IO Error"));
 
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.addJobDescription("Software Engineer", "Java", mockFile)
        );
 
        assertEquals(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED, exception.getMessage());
    }
 
    @Test
    void testEditJobDescriptionSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn("updated content".getBytes());
 
        JobDescription existingJobDescription = new JobDescription();
        existingJobDescription.setJobDescriptionId(1L);
        existingJobDescription.setJobTitle("Old Title");
        existingJobDescription.setJobParameter("Old Parameter");
 
        when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(existingJobDescription);
 
        JobDescription result = jobDescriptionService.editJobDescription(1L, "New Title", "New Parameter", mockFile);
 
        assertNotNull(result);
        assertEquals("New Title", result.getJobTitle());
        assertEquals("New Parameter", result.getJobParameter());
        assertArrayEquals("updated content".getBytes(), result.getRolesAndResponsibilities());
 
        verify(jobDescriptionDao, times(1)).saveJobDescription(existingJobDescription);
    }
 
    @Test
    void testEditJobDescriptionNotFound() {
        when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(null);
 
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.editJobDescription(1L, "New Title", "New Parameter", null)
        );
 
        assertEquals(MessageConstants.JOB_DESCRIPTION_NOT_FOUND, exception.getMessage());
    }
 
    @Test
    void testGetAllJobDescriptions() {
        JobDescription job1 = new JobDescription();
        JobDescription job2 = new JobDescription();
        when(jobDescriptionDao.viewAllJobDescriptions()).thenReturn(Arrays.asList(job1, job2));
 
        List<JobDescription> result = jobDescriptionService.getAllJobDescriptions();
 
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jobDescriptionDao, times(1)).viewAllJobDescriptions();
    }
 
    @Test
    void testGetByJobDescriptionId() {
        JobDescription job = new JobDescription();
        job.setJobDescriptionId(1L);
        when(jobDescriptionDao.viewByJobDescriptionId(1L)).thenReturn(job);
 
        JobDescription result = jobDescriptionService.getByJobDescriptionId(1L);
 
        assertNotNull(result);
        assertEquals(1L, result.getJobDescriptionId());
        verify(jobDescriptionDao, times(1)).viewByJobDescriptionId(1L);
    }
 
    @Test
    void testGetAllJobTitles() {
        when(jobDescriptionDao.viewAllJobTitles()).thenReturn(Arrays.asList("Developer", "Tester"));
 
        List<String> result = jobDescriptionService.getAllJobTitles();
 
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Developer"));
        assertTrue(result.contains("Tester"));
 
        verify(jobDescriptionDao, times(1)).viewAllJobTitles();
    }
 
    @Test
    void testAddJobDescriptionAssignToMrfSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn("test content".getBytes());
 
        MrfJd result = jobDescriptionService.addJobDescriptionAssignToMrf("QA Engineer", "Automation", mockFile);
 
        assertNotNull(result);
        assertEquals("QA Engineer", result.getJobTitle());
        assertEquals("Automation", result.getJobParameter());
        assertArrayEquals("test content".getBytes(), result.getRolesAndResponsibilities());
 
        verify(jobDescriptionDao, times(1)).saveJobDescription(result);
    }
 
    @Test
    void testGetAllMrfJd() {
        MrfJd mrf1 = new MrfJd();
        MrfJd mrf2 = new MrfJd();
        when(jobDescriptionDao.getAllMrfJd()).thenReturn(Arrays.asList(mrf1, mrf2));
 
        List<MrfJd> result = jobDescriptionService.getAllMrfJd();
 
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jobDescriptionDao, times(1)).getAllMrfJd();
    }
 
    @Test
    void testGetByMrfJdId() {
        MrfJd mrf = new MrfJd();
        mrf.setMrfJdId(1L);
        when(jobDescriptionDao.viewByMrfJdId(1L)).thenReturn(mrf);
 
        MrfJd result = jobDescriptionService.getByMrfJdId(1L);
 
        assertNotNull(result);
        assertEquals(1L, result.getMrfJdId());
        verify(jobDescriptionDao, times(1)).viewByMrfJdId(1L);
    }
    
    @Test
    void testEditMrfJdSuccess() throws IOException {
        Long mrfJdId = 1L;
        String jobTitle = "Updated Title";
        String jobParameter = "Updated Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn("Updated Roles and Responsibilities".getBytes());
     
        MrfJd existingMrfJd = new MrfJd();
        existingMrfJd.setMrfJdId(mrfJdId);
        existingMrfJd.setJobTitle("Old Title");
        existingMrfJd.setJobParameter("Old Parameter");
        existingMrfJd.setRolesAndResponsibilities("Old Roles and Responsibilities".getBytes());
     
        when(jobDescriptionDao.viewByMrfJdId(mrfJdId)).thenReturn(existingMrfJd);
     
        MrfJd result = jobDescriptionService.editMrfJd(mrfJdId, jobTitle, jobParameter, mockFile);
     
        assertNotNull(result);
        assertEquals(jobTitle, result.getJobTitle());
        assertEquals(jobParameter, result.getJobParameter());
        assertArrayEquals("Updated Roles and Responsibilities".getBytes(), result.getRolesAndResponsibilities());
     
        verify(jobDescriptionDao, times(1)).viewByMrfJdId(mrfJdId);
        verify(jobDescriptionDao, times(1)).updateMrfJd(existingMrfJd);
    }
     
    @Test
    void testEditMrfJdNotFound() {
        Long mrfJdId = 1L;
        String jobTitle = "Updated Title";
        String jobParameter = "Updated Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
     
        when(jobDescriptionDao.viewByMrfJdId(mrfJdId)).thenReturn(null);
     
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.editMrfJd(mrfJdId, jobTitle, jobParameter, mockFile)
        );
     
        assertEquals(MessageConstants.JOB_DESCRIPTION_NOT_FOUND, exception.getMessage());
     
        verify(jobDescriptionDao, times(1)).viewByMrfJdId(mrfJdId);
        verify(jobDescriptionDao, never()).updateMrfJd(any());
    }
     
    @Test
    void testEditMrfJdRolesAndResponsibilitiesIOException() throws IOException {
        Long mrfJdId = 1L;
        String jobTitle = "Updated Title";
        String jobParameter = "Updated Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenThrow(new IOException("IO Exception"));
     
        MrfJd existingMrfJd = new MrfJd();
        existingMrfJd.setMrfJdId(mrfJdId);
        existingMrfJd.setJobTitle("Old Title");
        existingMrfJd.setJobParameter("Old Parameter");
     
        when(jobDescriptionDao.viewByMrfJdId(mrfJdId)).thenReturn(existingMrfJd);
     
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.editMrfJd(mrfJdId, jobTitle, jobParameter, mockFile)
        );
     
        assertEquals(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED, exception.getMessage());
     
        verify(jobDescriptionDao, times(1)).viewByMrfJdId(mrfJdId);
        verify(jobDescriptionDao, never()).updateMrfJd(any());
    }
     
    @Test
    void testEditMrfJdNoRolesAndResponsibilitiesProvided() {
        Long mrfJdId = 1L;
        String jobTitle = "Updated Title";
        String jobParameter = "Updated Parameter";
     
        MrfJd existingMrfJd = new MrfJd();
        existingMrfJd.setMrfJdId(mrfJdId);
        existingMrfJd.setJobTitle("Old Title");
        existingMrfJd.setJobParameter("Old Parameter");
        existingMrfJd.setRolesAndResponsibilities("Old Roles and Responsibilities".getBytes());
     
        when(jobDescriptionDao.viewByMrfJdId(mrfJdId)).thenReturn(existingMrfJd);
     
        MrfJd result = jobDescriptionService.editMrfJd(mrfJdId, jobTitle, jobParameter, null);
     
        assertNotNull(result);
        assertEquals(jobTitle, result.getJobTitle());
        assertEquals(jobParameter, result.getJobParameter());
        assertArrayEquals("Old Roles and Responsibilities".getBytes(), result.getRolesAndResponsibilities());
     
        verify(jobDescriptionDao, times(1)).viewByMrfJdId(mrfJdId);
        verify(jobDescriptionDao, times(1)).updateMrfJd(existingMrfJd);
    }
    @Test
    void testAddJobDescriptionGenericException() {
        String jobTitle = "Test Job Title";
        String jobParameter = "Test Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
     
        doThrow(new RuntimeException("Unexpected Error")).when(jobDescriptionDao).saveJobDescription(any(JobDescription.class));
     
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.addJobDescription(jobTitle, jobParameter, mockFile)
        );
     
        assertEquals(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED, exception.getMessage());
     
        verify(jobDescriptionDao, times(1)).saveJobDescription(any(JobDescription.class));
    }
    @Test
    void testAddJobDescriptionAssignToMrfGenericException() throws IOException {
        String jobTitle = "Test Job Title";
        String jobParameter = "Test Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn("Mock File Content".getBytes());
     
        doThrow(new RuntimeException("Unexpected Error")).when(jobDescriptionDao).saveJobDescription(any(MrfJd.class));
     
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.addJobDescriptionAssignToMrf(jobTitle, jobParameter, mockFile)
        );
     
        assertEquals(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED, exception.getMessage());
     
        verify(jobDescriptionDao, times(1)).saveJobDescription(any(MrfJd.class));
    }
    
    @Test
    void testEditJobDescriptionIOException() throws IOException {
        Long jobDescriptionId = 1L;
        String jobTitle = "Updated Job Title";
        String jobParameter = "Updated Job Parameter";
        MultipartFile mockFile = mock(MultipartFile.class);
     
        JobDescription existingJobDescription = new JobDescription();
        existingJobDescription.setJobTitle("Old Job Title");
        existingJobDescription.setJobParameter("Old Job Parameter");
        existingJobDescription.setRolesAndResponsibilities("Old Responsibilities".getBytes());
     
        when(jobDescriptionDao.viewByJobDescriptionId(jobDescriptionId)).thenReturn(existingJobDescription);
     
        when(mockFile.getBytes()).thenThrow(new IOException("File read error"));
     
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                jobDescriptionService.editJobDescription(jobDescriptionId, jobTitle, jobParameter, mockFile)
        );
     
        assertEquals(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED, exception.getMessage());
     
        verify(jobDescriptionDao, times(1)).viewByJobDescriptionId(jobDescriptionId);
        verify(jobDescriptionDao, never()).saveJobDescription(any(JobDescription.class));
    }

}