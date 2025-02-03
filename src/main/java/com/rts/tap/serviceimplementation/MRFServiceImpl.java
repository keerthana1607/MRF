package com.rts.tap.serviceimplementation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rts.tap.dao.MRFDao;
import com.rts.tap.dto.BulkMrfAssignDto;
import com.rts.tap.dto.MRFCriteriaDTO;
import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.exception.MrfNotFoundException;
import com.rts.tap.exception.RecruitersNotFoundException;
import com.rts.tap.exception.RecruitingManagerNotFoundException;
import com.rts.tap.exception.VendorsNotFoundException;
import com.rts.tap.model.Client;
import com.rts.tap.model.ClientOrganization;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFAgreement;
import com.rts.tap.model.MRFCriteria;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFStatus;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.OfferApproval;
import com.rts.tap.model.Requirement;
import com.rts.tap.model.Role;
import com.rts.tap.model.Vendor;
import com.rts.tap.service.MRFService;
import com.rts.tap.utils.DateUtils;
import com.rts.tap.utils.EmailUtil;

@Service
public class MRFServiceImpl implements MRFService {

	private static final Logger logger = LoggerFactory.getLogger(MRFServiceImpl.class);

	MRFDao mrfDao;
	EmailUtil emailUtil;

	public MRFServiceImpl(MRFDao mrfDao, EmailUtil emailUtil) {
		super();
		this.mrfDao = mrfDao;
		this.emailUtil = emailUtil;
	}

	/**
	 * Adds a new MRF (Manpower Request Form) to the system.
	 *
	 * @param mrf the MRF object to be added.
	 * @return the saved MRF object.
	 */
	@Override
	public MRF addMrf(MRF mrf) {
		logger.info("Adding new MRF with criteria: {}", mrf.getMrfCriteria());

		MRFAgreement mrfAgreement = mrf.getMrfAgreement();
		logger.info("Saving MRFAgreement: {}", mrfAgreement);

		mrfDao.mrfStatusSave(mrf.getMrfStatus());
		mrfDao.mrfAgreementSave(mrfAgreement);
		mrfDao.mrfCriteriaSave(mrf.getMrfCriteria());

		mrf.setMrfAgreement(mrfAgreement);

		MRF savedMrf = mrfDao.mrfSave(mrf);
		logger.info("Successfully added MRF with ID: {}", savedMrf.getMrfId());
		return savedMrf;
	}

	/**
	 * Gets the count of filled requirements for a specific requirement ID.
	 *
	 * @param requirementId the ID of the requirement.
	 * @return the count of filled requirements.
	 */
	@Override
	public int getRequirementFilledCount(Long requirementId) {
		return mrfDao.getRequirementFilledCount(requirementId);
	}

	/**
	 * Updates an existing MRF based on the provided ID.
	 *
	 * @param mrfId the ID of the MRF to be updated.
	 * @param mrf   the MRF object containing updated information.
	 * @return the updated MRF object.
	 */
	@Override
	public MRF updateMrf(Long mrfId, MRF mrf) {
		logger.info("Updating MRF with ID: {}", mrfId);
		return mrfDao.mrfUpdate(mrfId, mrf);
	}

	/**
	 * Deletes an MRF by its ID.
	 *
	 * @param mrfId the ID of the MRF to be deleted.
	 * @return a message indicating the result of the deletion.
	 */
	@Override
	public String deleteMrfById(long mrfId) {
		logger.info("Deleting MRF with ID: {}", mrfId);
		return mrfDao.mrfDelete(mrfId);
	}

	/**
	 * Retrieves an MRF by its ID.
	 *
	 * @param mrfId the ID of the MRF to be retrieved.
	 * @return the MRF object.
	 */
	@Override
	public MRF getMrfById(long mrfId) {
	    logger.info("Fetching MRF with ID: {}", mrfId);
	    MRF mrf = mrfDao.getMrf(mrfId);
	    if (mrf == null) {
	        throw new MrfNotFoundException("MRF with Id: " + mrfId + " is not found.");
	    }
	    return mrf;
	}

	/**
	 * Retrieves all MRFs in the system.
	 *
	 * @return a list of all MRFs.
	 */
	@Override
	public List<MRF> getAllMrf() {
	    logger.info("Fetching all MRFs");
	    List<MRF> mrfList = mrfDao.getAllMRF();
	    if (mrfList.isEmpty()) {
	        throw new MrfNotFoundException("No MRFs found.");
	    }
	    return mrfList;
	}

	/**
	 * Assigns an MRF to a recruiting manager.
	 *
	 * @param mrfRecruitingManager the MRFRecruitingManager object containing the
	 *                             assignment details.
	 * @return the saved MRFRecruitingManager object.
	 */
	@Override
	public MRFRecruitingManager assignMrfToRecruitingManager(MRFRecruitingManager mrfRecruitingManager) {
		logger.info("Assigning MRF to Recruiting Manager");
		mrfRecruitingManager.setRecruitingManagerAssignedStatus("Assigned");
		return mrfDao.saveAssignedMRFToRecruitingManager(mrfRecruitingManager);
	}

	/**
	 * Retrieves all recruiting managers by their role name.
	 *
	 * @return a list of all recruiting managers.
	 */
	@Override
	public List<Employee> getRecruitingManagersByRoleName() {
	    logger.info("Fetching all recruiting managers");
	    List<Employee> recruitingManagers = mrfDao.getAllRecruitingManager();
	    	    if (recruitingManagers.isEmpty()) {
	        throw new RecruitingManagerNotFoundException("No recruiting Managers are there");
	    }	    
	    logger.info("Retrieved recruiting managers: {}", recruitingManagers);
	    return recruitingManagers;
	}

	/**
	 * Retrieves a list of assigned MRFs.
	 *
	 * @return a list of MRFRecruitingManager objects that are assigned.
	 */
	@Override
	public List<MRFRecruitingManager> getAssignedMRFs() {
	    List<MRFRecruitingManager> assignedMRFs = mrfDao.findAssignedMRFs();
	    
	    if (assignedMRFs.isEmpty()) {
	        throw new MrfNotFoundException("No assigned MRFs found.");
	    }
	    
	    return assignedMRFs;
	}

	/**
	 * Retrieves offer approvals based on the employee ID.
	 *
	 * @param employeeId the ID of the employee.
	 * @return a list of OfferApproval objects.
	 */
	@Override
	public List<OfferApproval> getOfferApprovalsByEmployeeId(Long employeeId) {
		return mrfDao.findOfferApprovalsByEmployeeId(employeeId);
	}

	/**
	 * Sends an MRF for approval based on its ID.
	 *
	 * @param mrfId the ID of the MRF to be sent for approval.
	 * @return the updated MRFStatus object.
	 * @throws IllegalStateException if the current status does not allow sending
	 *                               for approval.
	 */
	@Override
	public MRFStatus sendForApproval(Long mrfId) {
		logger.info("Sending MRF for approval with ID: {}", mrfId);

		MRF findMrfForBUApproval = mrfDao.findById(mrfId);

		if (findMrfForBUApproval == null) {
			logger.error("MRF with ID {} not found.", mrfId);
			throw new NoSuchElementException("MRF not found with ID: " + mrfId);
		}

		Employee mrfBuhEmail = findMrfForBUApproval.getBusinessUnitHead();
		Employee mrfClientPartner = findMrfForBUApproval.getClientPartner();

		if (mrfBuhEmail == null || mrfClientPartner == null) {
			logger.error("Business Unit Head or Client Partner is null for MRF ID: {}", mrfId);
			throw new IllegalStateException("Business Unit Head or Client Partner is not assigned.");
		}

		Role clientPartnerRole = mrfClientPartner.getRole();
		Requirement mrfRequirement = findMrfForBUApproval.getRequirement();

		if (mrfRequirement == null) {
			logger.error("Requirement is null for MRF ID: {}", mrfId);
			throw new IllegalStateException("Requirement is required for MRF approval.");
		}

		Client mrfClientName = mrfRequirement.getClient();
		ClientOrganization mrfClientOrganizationName = mrfClientName.getClientOrganization();
		MRFAgreement mrfAgreement = findMrfForBUApproval.getMrfAgreement();

		byte[] mrfTemplateData = null;
		if (mrfAgreement != null) {
			mrfTemplateData = mrfAgreement.getMrfTemplate();
			logger.info("MRF template data retrieved for MRF ID: {}", mrfId);
		} else {
			logger.warn("No agreement found for MRF ID: {}, no template data available.", mrfId);
		}

		if ("Not Assigned".equals(findMrfForBUApproval.getMrfStatus().getMrfApprovalStatus())) {
			MRFStatus mrfApprovalStatus = findMrfForBUApproval.getMrfStatus();
			mrfApprovalStatus.setMrfApprovalStatus("Pending");

			String buhEmail = mrfBuhEmail.getEmployeeEmail();
			String buhName = mrfBuhEmail.getEmployeeName();
			String clientName = mrfClientName.getClientName();
			String organizationName = mrfClientOrganizationName.getOrganizationName();
			String mrfClientPartnerName = mrfClientPartner.getEmployeeName();
			String mrfClientPartnerRole = clientPartnerRole.getRoleName();
			String mrfClientPartnerEmail = mrfClientPartner.getEmployeeEmail();
			String mrfClientPartnerMobile = mrfClientPartner.getWorkLocation();

			logger.info("Sending approval request email to BU Head: {}", buhEmail);
			emailUtil.sendBuApprovalRequestNotificationEmail(buhEmail, buhName, clientName, organizationName,
					mrfClientPartnerName, mrfClientPartnerRole, mrfClientPartnerEmail, mrfClientPartnerMobile,
					mrfTemplateData);

			logger.info("MRF approval status updated to 'Pending' for MRF ID: {}", mrfId);
			return mrfDao.MrfBuStatus(mrfApprovalStatus);
		} else {
			logger.error("Cannot send MRF for approval: Current status is {} for MRF ID: {}",
					findMrfForBUApproval.getMrfStatus().getMrfApprovalStatus(), mrfId);
			throw new IllegalStateException("Cannot send MRF for approval: Current status is "
					+ findMrfForBUApproval.getMrfStatus().getMrfApprovalStatus());
		}
	}

	/**
	 * Retrieves all MRFs associated with a specific client partner ID.
	 *
	 * @param employeeId the ID of the client partner.
	 * @return a list of MRFs associated with the specified client partner.
	 */
	@Override
	public List<MRF> getAllMrfsBasedOnClientPartnerId(Long employeeId) {
	    List<MRF> mrfs = mrfDao.getAllMrfsBasedOnClientPartnerId(employeeId);

	    if (mrfs.isEmpty()) {
	        throw new MrfNotFoundException("No MRFs found for the provided Client Partner ID: " + employeeId);
	    }

	    return mrfs;
	}

	/**
	 * Retrieves the assigned MRF for a specific MRF ID.
	 *
	 * @param mrfId the ID of the MRF.
	 * @return the MRFRecruitingManager object assigned to the specified MRF ID.
	 */
	@Override
	public MRFRecruitingManager getAssignedMrf(Long mrfId) {
	    MRFRecruitingManager mrfRecruitingManager = mrfDao.findAssignedMRFsToRecruitingManager(mrfId);

	    if (mrfRecruitingManager == null) {
	        throw new MrfNotFoundException("No MRF RecruitingManager found for the provided MRF ID: " + mrfId);
	    }

	    return mrfRecruitingManager;
	}

	/**
	 * Retrieves recruiters associated with a specific MRF.
	 *
	 * @param mrfId the ID of the MRF.
	 * @return a list of MRFRecruiters associated with the specified MRF.
	 */
	@Override
	public List<MRFRecruiters> getRecruitersByMrfId(Long mrfId) {
	    List<MRFRecruiters> mrfRecruiters = mrfDao.getRecruitersByMrfId(mrfId);

	    if (mrfRecruiters.isEmpty()) {
	        throw new RecruitersNotFoundException("No Recruiters found for the provided MRF ID: " + mrfId);
	    }

	    return mrfRecruiters;
	}

	/**
	 * Retrieves vendors associated with a specific MRF.
	 *
	 * @param mrfId the ID of the MRF.
	 * @return a list of MRFVendor objects associated with the specified MRF.
	 */
	@Override
	public List<MRFVendor> getAssignedVendorsByMrfId(Long mrfId) {
	    List<MRFVendor> mrfVendors = mrfDao.getAssignedVendorsByMrfId(mrfId);
	    
	    if (mrfVendors.isEmpty()) {
	        throw new VendorsNotFoundException("No vendors found for the provided MRF ID: " + mrfId);
	    }
	    
	    return mrfVendors;
	}

	/**
	 * Updates the Statement of Work (SOW) for a specified MRF with a new service
	 * level agreement.
	 *
	 * @param mrfId                 the ID of the MRF.
	 * @param serviceLevelAgreement the file containing the new service level
	 *                              agreement.
	 * @return the updated MRFAgreement object.
	 * @throws NoSuchElementException if the MRFAgreement does not exist for the
	 *                                specified MRF.
	 * @throws IllegalStateException  if a service level agreement already exists.
	 */
	@Override
	public MRFAgreement updateSOW(Long mrfId, MultipartFile serviceLevelAgreement) {
		logger.info("Updating SOW for MRF with ID: {}", mrfId);
		MRF mrf = mrfDao.findById(mrfId);
		MRFAgreement mrfAgreement = mrf.getMrfAgreement();

		if (mrfAgreement == null) {
			throw new NoSuchElementException("MRFAgreement with ID " + mrfId + " not found.");
		}

		if (mrfAgreement.getServiceLevelAgreement() == null) {
			try {
				byte[] serviceLevelAgreementFile = serviceLevelAgreement.getBytes();
				mrfAgreement.setServiceLevelAgreement(serviceLevelAgreementFile);
				logger.info("SOW updated successfully for MRF with ID: {}", mrfId);
				return mrfDao.updateSOW(mrfAgreement);
			} catch (IOException e) {
				logger.error("Error while reading service level agreement file: {}", e.getMessage());
				throw new RuntimeException("Failed to upload service level agreement", e);
			}
		} else {
			throw new IllegalStateException("Service Level Agreement already exists and cannot be updated.");
		}
	}

	/**
	 * Retrieves all MRFs associated with a specific employee ID.
	 *
	 * @param employeeId the ID of the employee.
	 * @return a list of MRFs associated with the specified employee.
	 */
	@Override
	public List<MRF> getAllMrfByEmployeeId(String employeeId) {
	    List<MRF> mrfList = mrfDao.findByEmployeeId(employeeId);
	    
	    if (mrfList.isEmpty()) {
	        throw new MrfNotFoundException("No MRs found for the provided Employee ID: " + employeeId);
	    }
	    
	    return mrfList;
	}

	/**
	 * Updates the status of an MRF.
	 *
	 * @param mrfId     the ID of the MRF to be updated.
	 * @param newStatus the new status information to be applied.
	 * @return the updated MRFStatus object.
	 * @throws NoSuchElementException if the MRF does not exist.
	 */
	@Override
	public MRFStatus updateMrfStatus(Long mrfId, MrfStatusDTO newStatus) {
		logger.info("Updating MRF status for ID: {}", mrfId);
		MRF mrf = mrfDao.findById(mrfId);

		if (mrf == null) {
			throw new NoSuchElementException("MRF with ID " + mrfId + " not found.");
		}

		MRFCriteria mrfCriteria = mrf.getMrfCriteria();
		updateContractTimeline(mrf, mrfCriteria);

		mrf.setMrfCriteria(mrfCriteria);
		mrfDao.mrfUpdate(mrfId, mrf);

		MRFStatus mrfStatus = mrf.getMrfStatus();
		mrfStatus.setMrfApprovalStatus(newStatus.getMrfApprovalStatus());
		if ("Rejected".equals(mrfStatus.getMrfApprovalStatus())) {
			mrfStatus.setDescriptionForChanges(newStatus.getDescriptionForChanges());
		}

		return mrfDao.MrfBuStatus(mrfStatus);
	}

	private void updateContractTimeline(MRF mrf, MRFCriteria mrfCriteria) {
		String timeline = mrf.getRequirement().getTimeline();
		Date closureDate = DateUtils.getCurrentDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(closureDate);

		if ("Immediate".equalsIgnoreCase(timeline)) {
			calendar.add(Calendar.DAY_OF_MONTH, 7);
		} else if ("30Days".equalsIgnoreCase(timeline)) {
			calendar.add(Calendar.DAY_OF_MONTH, 30);
		}

		mrfCriteria.setContractStartDate(closureDate);
		mrfCriteria.setClosureDate(calendar.getTime());
	}

	/**
	 * Retrieves criteria for a specific MRF based on its Job Description ID.
	 *
	 * @param mrfJdId the ID of the Job Description.
	 * @return the MRFCriteriaDTO object containing the criteria details.
	 */
	@Override
	public MRFCriteriaDTO getMrfByMrfJdId(Long mrfJdId) {
	    MRFCriteria criteria = mrfDao.getMrfByMrfJdId(mrfJdId);
	    
	    if (criteria == null) {
	        throw new MrfNotFoundException("No MRF found for the provided MRF JD ID: " + mrfJdId);
	    }
	    
	    MRFCriteriaDTO dto = new MRFCriteriaDTO();
	    dto.setIsCertificationNeeded(criteria.getIsCertificationNeeded());
	    dto.setIsVisaRequired(criteria.getIsVisaRequired());
	    return dto;
	}

	/**
	 * Retrieves all recruiting managers associated with a specific client partner
	 * ID.
	 *
	 * @param clientPartnerId the ID of the client partner.
	 * @return a list of Employee objects representing the recruiting managers.
	 */
	@Override
	public List<Employee> viewAllRecruitingManagerByClientPartnerId(Long clientPartnerId) {
	    List<Employee> recruitingManagers = mrfDao.getRecruitingManagerByClientPartnerId(clientPartnerId);
	    
	    if (recruitingManagers.isEmpty()) {
	        throw new RecruitingManagerNotFoundException("No Recruiting Managers found for the provided client partner Id: " + clientPartnerId);
	    }
	    
	    return recruitingManagers;
	}

	/**
	 * Retrieves all vendors associated with a specific recruiting manager ID.
	 *
	 * @param recruitingManagerId the ID of the recruiting manager.
	 * @return a list of Vendor objects associated with the specified recruiting
	 *         manager.
	 */
	@Override
	public List<Vendor> viewAllVendorsByRecruitingManagerId(Long recruitingManagerId) {
	    List<Vendor> vendors = mrfDao.getVendorsByRecruitingManagerId(recruitingManagerId);
	    
	    if (vendors.isEmpty()) {
	        throw new VendorsNotFoundException("No Vendors found for the provided Recruiting Manager Id: " + recruitingManagerId);
	    }
	    
	    return vendors;
	}

	/**
	 * Retrieves all recruiters associated with a specific recruiting manager ID.
	 *
	 * @param recruitingManagerId the ID of the recruiting manager.
	 * @return a list of Employee objects representing the recruiters.
	 */
	@Override
	public List<Employee> viewAllRecruitersByRecruitingManagerId(Long recruitingManagerId) {
	    List<Employee> recruiters = mrfDao.getRecruitersByRecruitingManagerId(recruitingManagerId);
	    
	    if (recruiters.isEmpty()) {
	        throw new RecruitersNotFoundException("No recruiters found for the provided recruiting manager ID: " + recruitingManagerId);
	    }
	    
	    return recruiters;
	}
	
	
	//NEW CODE 
	
	@Override
	public String mrfAssignToVendor(MRFVendorDto mrfVendorDto) {
		return mrfDao.assignMrfToVendor(mrfVendorDto);
	}
	
	@Override
	public List<MRFVendorDto> getAllMrfVendorsRecords() {
	    List<MRFVendor> mrfVendors = mrfDao.getAllMrfsVendors();
	    
	    if (mrfVendors.isEmpty()) {
	        throw new VendorsNotFoundException("No vendors found in the MRF records.");
	    }
	    
	    return mrfVendors.stream().map(mrfVendor -> {
	        MRFVendorDto mrfVendorDto = new MRFVendorDto();
	        mrfVendorDto.setMrfId(mrfVendor.getMrf().getMrfId());
	        mrfVendorDto.setRecrutingManagerId(mrfVendor.getRecruitingManager().getEmployeeId());
	        mrfVendorDto.setVendorId(mrfVendor.getVendor().getVendorId());
	        mrfVendorDto.setAssignedCount(mrfVendor.getAssignedCount());
	        mrfVendorDto.setAchievedCount(mrfVendor.getAchievedCount());
	        System.out.println(mrfVendor.getVendorAssignedStatus());
	        mrfVendorDto.setVendorAssignedStatus(mrfVendor.getVendorAssignedStatus());
	        return mrfVendorDto;
	    }).collect(Collectors.toList());
	}
	
	
	@Override
	public String mrfAssignToRecruiter(MRFRecruiterDto mrfRecruiterDto) {
		Date now = new Date();
		mrfRecruiterDto.setCreatedAt(now); // Set the current date for creation
		mrfRecruiterDto.setUpdatedAt(now);
 
		return mrfDao.assignMrfToRecruiter(mrfRecruiterDto);
	}
	
		@Override
		public String mrfBulkAssignToRecruiter(long recId, BulkMrfAssignDto bulkMrfAssignDto) {
			Date now = new Date();
			long rmId = bulkMrfAssignDto.getRmId();
			List<MRFRecruiterDto> mrfRecruiterDtos = bulkMrfAssignDto.getMrfRecruiterDtos();
			String msg = "";
			for (MRFRecruiterDto mrfRecruiterDto : mrfRecruiterDtos) {
				long mrfRecruitingManagerId = getMrfRecruitingManagerByMrfIdAndManagerId(mrfRecruiterDto.getMrfId(),rmId).getMrfRecruitingManagerId();
				mrfRecruiterDto.setMrfRecruitingManagerId(mrfRecruitingManagerId);
				mrfRecruiterDto.setRecruiterId(recId);
				mrfRecruiterDto.setCreatedAt(now); // Set the current date for creation
				mrfRecruiterDto.setUpdatedAt(now);
				msg = mrfDao.assignMrfToRecruiter(mrfRecruiterDto);
			}
			
			return msg;
		}
	
		@Override
		public String mrfBulkAssignToVendor(long vendorId, BulkMrfAssignDto bulkMrfAssignDto) {		
			long rmId = bulkMrfAssignDto.getRmId();
			List<MRFVendorDto> mrfVendorDtos = bulkMrfAssignDto.getMrfVendorDtos();
			String msg = ""; 
			@SuppressWarnings("unused")
			int count = 1;
			for (MRFVendorDto mrfVendorDto : mrfVendorDtos) {
				mrfVendorDto.setRecrutingManagerId(rmId);
				mrfVendorDto.setVendorId(vendorId);
				msg = mrfDao.assignMrfToVendor(mrfVendorDto);
				if(!msg.equalsIgnoreCase("MRF assigned to vendor successful")) {
					logger.warn(msg);
					break;
				}
			}
			
			return msg;
		}
		
		@Override
		public String reassignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto) {
			Date now = new Date();
			mrfRecruiterDto.setUpdatedAt(now);
			return mrfDao.reassignMrfToRecruiter(mrfRecruiterDto);
		}

		@Override
		public List<MRFRecruiters> getAllMRFByRecruiterId(Long id) {
		    List<MRFRecruiters> mrfs = mrfDao.getAllMrfsForRecruiterId(id);
		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFS found for the provided recruiter ID: " + id);
		    }	
		    return mrfs;
		}

		@Override
		public List<MRFVendor> getAllMRFByVendorId(long vendorId) {
		    List<MRFVendor> mrfs = mrfDao.getAllMrfsForVendor(vendorId);
		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFs found for the provided vendor ID: " + vendorId);
		    }
		    return mrfs;
		}
	
		@Override
		public List<MRFVendor> getAllMrfsVendorsAssignedByRM(long employeeId) {
		    List<MRFVendor> mrfs = mrfDao.getAllMrfsVendorsAssignedByRM(employeeId);
		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFs found assigned by the Recruiting Manager with Id: " + employeeId);
		    }
		    return mrfs;
		}

		@Override
		public List<MRFRecruiters> getAllMrfsRecruitersAssignedByRM(long employeeId) {
		    List<MRFRecruiters> mrfs = mrfDao.getAllMrfsRecruitersAssignedByRM(employeeId);
		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFs found assigned by the Recruiting Manager with Id: " + employeeId);
		    }
		    return mrfs;
		}

		@Override
		public List<MRFRecruiters> fetchAllAssignedMrfsForAllRecruiter(long id) {
		    List<MRFRecruiters> mrfs = mrfDao.getAllAssignedMrfsForAllRecruiter(id);
		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFs found assigned to the recruiter with Id: " + id);
		    }
		    return mrfs;
		}

		@Override
		public List<MRFVendor> fetchAllAssignedMrfsForAllVendors() {
		    List<MRFVendor> mrfs = mrfDao.getAllAssignedMrfsForAllVendors();

		    if (mrfs.isEmpty()) {
		        throw new MrfNotFoundException("No MRFs found assigned to any vendors.");
		    }

		    return mrfs;
		}
		
		public MRFRecruitingManager getMrfRecruitingManagerByMrfIdAndManagerId(long mrfId, long rmId) {
			return mrfDao.fetchMrfRecruitingManagerByMrfIdAndmManagerId(mrfId,rmId);
		}
		
}