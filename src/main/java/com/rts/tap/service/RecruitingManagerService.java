package com.rts.tap.service;

import java.util.List;

import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFVendor;

/**
 * author: Jeevarajan, Vashanth version: v5.0 updated at: 29-11-2024
 **/

public interface RecruitingManagerService {

	public List<MRFRecruitingManager> getAllMrfsAssignedForRM(long id);

	public List<MRFVendorDto> getAllMrfVendorsRecords();

	public List<MRFRecruiters> getAllAssignedMrfRecruiterRecords();
	
	public List<Employee> getAllRecruitersByRecruitingManagerID(long id);
	
	public List<MRFRecruiters> getAllRecruitersByMRFRecruitingManagerId(long id);
	
	public void updateMrfStage(MrfStatusDTO mrfStage, Long mrfId);
	
	public List<MRFVendor> getAllVendorsAssignedForMRFbyMrfId(long mrfId);
	
	List<Candidate> getAllCandidateByMrfId(Long mrfId);
	
	public List<MRFRecruiters> getAllMRFByRecruiterId(long id);
	
	List<Candidate> getCandidateByMrfIdVendorId(Long mrfId, Long vendorId);
	
	public List<MRFVendor> getAllMrfVendors();
		
	List<MRFRecruiters> getRecruitersByMrfId(Long mrfId);

	MRF getMrfById(long id);
}
