package com.rts.tap.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rts.tap.dao.RecruitingManagerDao;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.exception.CandidateNotFoundException;
import com.rts.tap.exception.MRFVendorRecordsNotFoundException;
import com.rts.tap.exception.NoAssignedRecruitersFound;
import com.rts.tap.exception.RecruiterNotFoundException;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFVendor;

@ExtendWith(MockitoExtension.class)

public class RecruitingManagerServiceImplementationTest {

	@Mock

	private RecruitingManagerDao recruitingManagerDao;

	@InjectMocks

	private RecruitingManagerServiceImplementation recruitingManagerService;

	private MRF mrf;

	private MRFRecruiters mrfRecruiter;

	private Employee employee;

	private MRFVendor mrfVendor;

	private Candidate candidate;

	private MrfStatusDTO mrfStatusDTO;

	private MRFVendorDto mrfVendorDto;

	@BeforeEach

	void setUp() {

		mrf = new MRF();

		mrf.setMrfId(1L);

		employee = new Employee();

		employee.setEmployeeId(1L);

		mrfRecruiter = new MRFRecruiters();

		mrfRecruiter.setMrfRecruitersId(1L);

		mrfVendor = new MRFVendor();

		mrfVendor.setMrfVendorId(1L);

		mrfVendor.setMrf(mrf);

		mrfVendor.setVendorAssignedStatus("Assigned");

		candidate = new Candidate();

		candidate.setCandidateId(1L);

		mrfStatusDTO = new MrfStatusDTO();

		mrfStatusDTO.setMrfStatusId(1L);

		mrfStatusDTO.setMrfStage("Pending");

		mrfVendorDto = new MRFVendorDto();

		mrfVendorDto.setMrfId(1L);

		mrfVendorDto.setVendorId(1L);

		mrfVendorDto.setRecrutingManagerId(1L);

	}

	@Test

	void testGetMrfById() {

		when(recruitingManagerDao.getMrfById(1L)).thenReturn(mrf);

		MRF result = recruitingManagerService.getMrfById(1L);

		assertNotNull(result);

		assertEquals(1L, result.getMrfId());

		verify(recruitingManagerDao, times(1)).getMrfById(1L);

	}

	@Test

	void testGetAllAssignedMrfRecruiterRecords() {

		when(recruitingManagerDao.getAllRecruitersAssigned()).thenReturn(Collections.singletonList(mrfRecruiter));

		List<MRFRecruiters> result = recruitingManagerService.getAllAssignedMrfRecruiterRecords();

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllRecruitersAssigned();

	}

	@Test

	void testGetAllAssignedMrfRecruiterRecords_ThrowsNoAssignedRecruitersFound() {

		when(recruitingManagerDao.getAllRecruitersAssigned())
				.thenThrow(new NoAssignedRecruitersFound("No recruiters found"));

		assertThrows(NoAssignedRecruitersFound.class,
				() -> recruitingManagerService.getAllAssignedMrfRecruiterRecords());

		verify(recruitingManagerDao, times(1)).getAllRecruitersAssigned();

	}

	@Test

	void testGetAllRecruitersByRecruitingManagerID() {

		when(recruitingManagerDao.getAllRecruitersByRecruitingManagerId(1L))
				.thenReturn(Collections.singletonList(employee));

		List<Employee> result = recruitingManagerService.getAllRecruitersByRecruitingManagerID(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllRecruitersByRecruitingManagerId(1L);

	}

	@Test

	void testGetAllRecruitersByRecruitingManagerID_ThrowsRecruiterNotFoundException() {

		when(recruitingManagerDao.getAllRecruitersByRecruitingManagerId(1L)).thenReturn(Collections.emptyList());

		assertThrows(RecruiterNotFoundException.class,
				() -> recruitingManagerService.getAllRecruitersByRecruitingManagerID(1L));

		verify(recruitingManagerDao, times(1)).getAllRecruitersByRecruitingManagerId(1L);

	}

	@Test

	void testUpdateMrfStage() {

		when(recruitingManagerDao.getMrfById(1L)).thenReturn(mrf);

		doNothing().when(recruitingManagerDao).updateMrfStage(mrfStatusDTO, 1L);

		recruitingManagerService.updateMrfStage(mrfStatusDTO, 1L);

		verify(recruitingManagerDao, times(1)).getMrfById(1L);

		verify(recruitingManagerDao, times(1)).updateMrfStage(mrfStatusDTO, 1L);

	}

	@Test

	void testGetAllRecruitersByMRFRecruitingManagerId() {

		when(recruitingManagerDao.getAssignedRecruitersByMrfRecruitingManagerId(1L))
				.thenReturn(Collections.singletonList(mrfRecruiter));

		List<MRFRecruiters> result = recruitingManagerService.getAllRecruitersByMRFRecruitingManagerId(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAssignedRecruitersByMrfRecruitingManagerId(1L);

	}

	@Test

	void testGetAllVendorsAssignedForMRFbyMrfId() {

		when(recruitingManagerDao.getAllVendorsAssignedForMrf(1L)).thenReturn(Collections.singletonList(mrfVendor));

		List<MRFVendor> result = recruitingManagerService.getAllVendorsAssignedForMRFbyMrfId(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllVendorsAssignedForMrf(1L);

	}


	@Test

	void testGetRecruitersByMrfId() {

		when(recruitingManagerDao.getRecruitersByMrfId(1L)).thenReturn(Collections.singletonList(mrfRecruiter));

		List<MRFRecruiters> result = recruitingManagerService.getRecruitersByMrfId(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getRecruitersByMrfId(1L);

	}

	@Test

	void testGetRecruitersByMrfId_ThrowsRecruiterNotFoundException() {

		when(recruitingManagerDao.getRecruitersByMrfId(1L)).thenReturn(Collections.emptyList());

		assertThrows(RecruiterNotFoundException.class, () -> recruitingManagerService.getRecruitersByMrfId(1L));

		verify(recruitingManagerDao, times(1)).getRecruitersByMrfId(1L);

	}

	@Test

	void testGetAllCandidateByMrfId() {

		when(recruitingManagerDao.getMrfById(1L)).thenReturn(mrf);

		when(recruitingManagerDao.getListOfCandidateByMrfId(1L)).thenReturn(Collections.singletonList(candidate));

		List<Candidate> result = recruitingManagerService.getAllCandidateByMrfId(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getMrfById(1L);

		verify(recruitingManagerDao, times(1)).getListOfCandidateByMrfId(1L);

	}

	@Test

	void testGetAllCandidateByMrfId_ThrowsCandidateNotFoundException() {

		when(recruitingManagerDao.getMrfById(1L)).thenReturn(mrf);

		when(recruitingManagerDao.getListOfCandidateByMrfId(1L)).thenReturn(Collections.emptyList());

		assertThrows(CandidateNotFoundException.class, () -> recruitingManagerService.getAllCandidateByMrfId(1L));

		verify(recruitingManagerDao, times(1)).getMrfById(1L);

		verify(recruitingManagerDao, times(1)).getListOfCandidateByMrfId(1L);

	}

	@Test

	void testGetAllMRFByRecruiterId() {

		when(recruitingManagerDao.getAllMrfsForRecruiterId(1L)).thenReturn(Collections.singletonList(mrfRecruiter));

		List<MRFRecruiters> result = recruitingManagerService.getAllMRFByRecruiterId(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllMrfsForRecruiterId(1L);

	}


	@Test

	void testGetAllMrfVendors() {

		when(recruitingManagerDao.getAllMrfVendors()).thenReturn(Collections.singletonList(mrfVendor));

		List<MRFVendor> result = recruitingManagerService.getAllMrfVendors();

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllMrfVendors();

	}

	@Test

	void testGetAllMrfVendorsRecords_ThrowsMRFVendorRecordsNotFoundException() {

		when(recruitingManagerDao.getAllMrfsVendors())
				.thenThrow(new MRFVendorRecordsNotFoundException("No vendors found"));

		assertThrows(MRFVendorRecordsNotFoundException.class, () -> recruitingManagerService.getAllMrfVendorsRecords());

		verify(recruitingManagerDao, times(1)).getAllMrfsVendors();

	}

	@Test

	void testGetAllMrfsAssignedForRM() {

		MRFRecruitingManager mrfRecruitingManager = new MRFRecruitingManager();

		mrfRecruitingManager.setMrfRecruitingManagerId(1L);

		when(recruitingManagerDao.getAllMrfsAssignedForRM(1L))
				.thenReturn(Collections.singletonList(mrfRecruitingManager));

		List<MRFRecruitingManager> result = recruitingManagerService.getAllMrfsAssignedForRM(1L);

		assertNotNull(result);

		assertEquals(1, result.size());

		verify(recruitingManagerDao, times(1)).getAllMrfsAssignedForRM(1L);

	}

}