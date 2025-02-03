package com.rts.tap.dao;

import java.util.List;

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

public interface RecruitingManagerDao {

	public List<MRFRecruiters> getAllRecruitersAssigned();
	
	public List<Employee> getAllRecruitersByRecruitingManagerId(long id);
	
	public void updateMrfStage(MrfStatusDTO mrfStage, Long mrfId);
	
	public List<MRFRecruiters> getAssignedRecruitersByMrfRecruitingManagerId(long id);
	
	public List<MRFVendor> getAllVendorsAssignedForMrf(long mrfId);
	
	List<MRFRecruiters> getRecruitersByMrfId(Long mrfId);
	
	List<Candidate> getListOfCandidateByMrfId(Long mrfId);
	
	public List<MRFRecruiters> getAllMrfsForRecruiterId(long id);
	
	public List<Candidate> getCandidateByMrfAndIdVendorId(Long mrfId, Long vendorId);
	
	public List<MRFVendor> getAllMrfVendors();

	public List<MRFVendor> getAllMrfsVendors();

	public MRF getMrfById(long id);
	
	public List<MRFRecruitingManager> getAllMrfsAssignedForRM(long id);

}
