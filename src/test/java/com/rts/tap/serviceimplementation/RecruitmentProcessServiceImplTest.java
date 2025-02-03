package com.rts.tap.serviceimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import com.rts.tap.dao.InterviewerDao;

import com.rts.tap.dao.MRFDao;

import com.rts.tap.dao.RecruitmentProcessDao;

import com.rts.tap.feign.ClientInterface;

import com.rts.tap.feign.EmployeeInterface;

import com.rts.tap.model.Client;

import com.rts.tap.model.Employee;

import com.rts.tap.model.Interviewer;

import com.rts.tap.model.MRF;

import com.rts.tap.model.RecruitmentProcess;

import com.rts.tap.service.ApproverLevelService;

import java.util.ArrayList;

import java.util.List;

@ExtendWith(MockitoExtension.class)

public class RecruitmentProcessServiceImplTest {

	@InjectMocks

	private RecruitmentProcessServiceImpl recruitmentProcessService;

	@Mock

	private RecruitmentProcessDao recruitmentProcessDao;

	@Mock

	private MRFDao mrfDao;

	@Mock

	private ApproverLevelService approverLevelService;

	@Mock

	private EmployeeInterface employeeInterface;

	@Mock

	private ClientInterface clientInterface;

	@Mock

	private InterviewerDao interviewerDao;

	private MRF mrf;

	private Employee employee;

	private Client client;

	private Interviewer interviewer;

	private RecruitmentProcess recruitmentProcess;

	@BeforeEach

	public void setUp() {

		mrf = new MRF();

		employee = new Employee();

		client = new Client();

		interviewer = new Interviewer();

		recruitmentProcess = new RecruitmentProcess();

		mrf.setMrfId(1L);

		employee.setEmployeeId(2L);

		client.setClientId(3L);

		interviewer.setEmployee(employee);

		interviewer.setClient(client);

	}

	@Test

	public void testInsertRecruitmentProcessLevelInterviewType() {

		recruitmentProcess.setMrf(mrf);

		recruitmentProcess.setRecruitmentProcessType("interview");

		List<Interviewer> interviewers = new ArrayList<>();

		interviewers.add(interviewer);

		recruitmentProcess.setInterviewer(interviewers);

		when(mrfDao.findById(1L)).thenReturn(mrf);

		when(employeeInterface.getEmployeeById(2L)).thenReturn(ResponseEntity.ok(employee));

//		when(clientInterface.getClientDetailsById(3L)).thenReturn(ResponseEntity.ok(client));

		recruitmentProcessService.insertRecruitmentProcessLevel(recruitmentProcess);

		verify(interviewerDao, times(1)).addInterviewer(any());

	}

	@Test

	public void testInsertRecruitmentProcessLevelAssessmentType() {

		recruitmentProcess.setMrf(mrf);

		recruitmentProcess.setRecruitmentProcessType("assessment");

		recruitmentProcessService.insertRecruitmentProcessLevel(recruitmentProcess);

		// Assert that interviewers are set to null

		assertNull(recruitmentProcess.getInterviewer());

	}

	@Test

	public void testGetRecruitmentProcessLevels() {

		Long mrfId = 1L;

		List<RecruitmentProcess> expectedProcesses = new ArrayList<>();

		expectedProcesses.add(recruitmentProcess);

		when(recruitmentProcessDao.findRecruitmentProcessByMrfId(mrfId)).thenReturn(expectedProcesses);

		List<RecruitmentProcess> actualProcesses = recruitmentProcessService.getRecruitmentProcessLevels(mrfId);

		assertEquals(expectedProcesses, actualProcesses);

	}

	@Test

	public void testGetEmployeeByIdFromEmployeeService() {

		when(employeeInterface.getEmployeeById(2L)).thenReturn(ResponseEntity.ok(employee));

		Employee result = recruitmentProcessService.getEmployeeByIdFromEmployeeService(2L);

		assertEquals(employee, result);

	}

	@Test

	public void testGetClientByIdFromClientService() {

		when(clientInterface.getClientDetailsById(3L)).thenReturn(ResponseEntity.ok(client));

		Client result = recruitmentProcessService.getClientByIdFromClientService(3L);

		assertEquals(client, result);

	}

	@Test

	public void testGetEmployeeByIdFromEmployeeServiceExceptionHandling() {

		when(employeeInterface.getEmployeeById(2L)).thenThrow(new RuntimeException("Error"));

		Employee result = recruitmentProcessService.getEmployeeByIdFromEmployeeService(2L);

		assertNull(result);

	}

	@Test

	public void testGetClientByIdFromClientServiceExceptionHandling() {

		when(clientInterface.getClientDetailsById(3L)).thenThrow(new RuntimeException("Error"));

		Client result = recruitmentProcessService.getClientByIdFromClientService(3L);

		assertNull(result);

	}

}
