package com.rts.tap.dao;

import java.util.List;

import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFAgreement;
import com.rts.tap.model.MRFCriteria;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFStatus;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.OfferApproval;
import com.rts.tap.model.Vendor;

public interface MRFDao {

	MRF mrfSave(MRF mrf);

	MRF mrfCriteriaSave(MRFCriteria mrfCriteria);

	MRF mrfAgreementSave(MRFAgreement mrfAgreement);

	MRF mrfStatusSave(MRFStatus mrfStatus);

	MRF mrfUpdate(Long mrfId, MRF mrf);

	String mrfDelete(Long mrfId);

	MRF getMrf(long mrfId);

	List<MRF> getAllMRF();

	public MRF findById(Long mrfId);

	int getRequirementFilledCount(Long requirementId);

	MRFRecruitingManager saveAssignedMRFToRecruitingManager(MRFRecruitingManager mrfRecruitingManager);

	List<Employee> getAllRecruitingManager();

	List<MRFRecruitingManager> findAssignedMRFs();

	List<OfferApproval> findOfferApprovalsByEmployeeId(Long employeeId);

	List<MRF> getAllMrfsBasedOnClientPartnerId(Long employeeId);

	MRFAgreement updateSOW(MRFAgreement mrfAgreement);

	MRFRecruitingManager findAssignedMRFsToRecruitingManager(Long mrfId);

	List<MRFRecruiters> getRecruitersByMrfId(Long mrfId);

	List<MRFVendor> getAssignedVendorsByMrfId(Long mrfId);

	MRFStatus MrfBuStatus(MRFStatus mrfstatus);

	MRFStatus findStatusById(Long mrfId);

	List<MRF> findByEmployeeId(String employeeId);

	MRFCriteria getMrfByMrfJdId(Long mrfjdId);

	List<Employee> getRecruitingManagerByClientPartnerId(Long clientPartnerId);

	List<Vendor> getVendorsByRecruitingManagerId(Long recruitingManagerId);

	List<Employee> getRecruitersByRecruitingManagerId(Long recruitingManagerId);

	public String assignMrfToVendor(MRFVendorDto mrfVendorDto);

	public List<MRFVendor> getAllMrfVendors();

	public String assignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto);

	public String reassignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto);

	public List<MRFRecruiters> getAllMrfsForRecruiterId(long id);

	public List<MRFVendor> getAllMrfsForVendor(long vendorId);

	public List<MRFVendor> getAllMrfsVendorsAssignedByRM(long employeeId);

	public List<MRFRecruiters> getAllMrfsRecruitersAssignedByRM(long employeeId);

	public List<MRFVendor> getAllAssignedMrfsForAllVendors();

	public List<MRFRecruiters> getAllAssignedMrfsForAllRecruiter(long id);

	MRFRecruitingManager fetchMrfRecruitingManagerByMrfIdAndmManagerId(long mrfId, long rmId);

	public List<MRFVendor> getAllMrfsVendors();
	
	public MRF getMrfById(long id);
	
	public Employee getEmployeeById(long id);
	
	public MRFRecruiters findMrfRecruiterByMrfId(long mrfRecruitingManagerId);
	
	public Vendor findByIdForVendor(Long id);

}
