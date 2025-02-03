package com.rts.tap.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rts.tap.dto.BulkMrfAssignDto;
import com.rts.tap.dto.MRFCriteriaDTO;
import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFAgreement;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFStatus;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.OfferApproval;
import com.rts.tap.model.Vendor;

public interface MRFService {

	MRF addMrf(MRF mrf);

	MRF updateMrf(Long mrfId, MRF mrf);

	String deleteMrfById(long mrfId);

	MRF getMrfById(long mrfId);

	List<MRF> getAllMrf();

	int getRequirementFilledCount(Long requirementId);

	MRFRecruitingManager assignMrfToRecruitingManager(MRFRecruitingManager mrfRecruitingManager);

	List<Employee> getRecruitingManagersByRoleName();

	List<MRFRecruitingManager> getAssignedMRFs();

	List<OfferApproval> getOfferApprovalsByEmployeeId(Long employeeId);

	MRFCriteriaDTO getMrfByMrfJdId(Long mrfJdId);

	List<MRF> getAllMrfsBasedOnClientPartnerId(Long employeeId);

	MRFStatus sendForApproval(Long mrfId);

	MRFAgreement updateSOW(Long mrfId, MultipartFile serviceLevelAgreement);

	MRFRecruitingManager getAssignedMrf(Long mrfId);

	List<MRFRecruiters> getRecruitersByMrfId(Long mrfId);

	List<MRFVendor> getAssignedVendorsByMrfId(Long mrfId);

	List<Employee> viewAllRecruitingManagerByClientPartnerId(Long clientPartnerId);

	List<Vendor> viewAllVendorsByRecruitingManagerId(Long recruitingManagerId);

	List<Employee> viewAllRecruitersByRecruitingManagerId(Long recruitingManagerId);

	MRFStatus updateMrfStatus(Long mrfId, MrfStatusDTO newStatus);

	List<MRF> getAllMrfByEmployeeId(String employeeId);

	// NEW CODE
	public String mrfAssignToVendor(MRFVendorDto mrfVendorDto);

	public List<MRFVendorDto> getAllMrfVendorsRecords();

	public String mrfAssignToRecruiter(MRFRecruiterDto mrfRecruiterDto);

	public String mrfBulkAssignToRecruiter(long recId, BulkMrfAssignDto bulkMrfAssignDto);

	public String mrfBulkAssignToVendor(long vendorId, BulkMrfAssignDto bulkMrfAssignDto);

	public String reassignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto);

	public List<MRFVendor> getAllMRFByVendorId(long vendorId);

	public List<MRFVendor> getAllMrfsVendorsAssignedByRM(long employeeId);

	public List<MRFRecruiters> getAllMrfsRecruitersAssignedByRM(long employeeId);

	public List<MRFRecruiters> fetchAllAssignedMrfsForAllRecruiter(long id);

	public List<MRFVendor> fetchAllAssignedMrfsForAllVendors();

	public List<MRFRecruiters> getAllMRFByRecruiterId(Long id);

}
