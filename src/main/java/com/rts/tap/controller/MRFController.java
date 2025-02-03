/**

* Author: Team-B

*

* MRFController handles requests related to Manpower Requisition Form (MRF) operations,

* including creating, updating, retrieving, and deleting MRFs.

*

* It interacts with MRFService to perform business logic and return relevant responses.

* Key functionalities include:

* - Adding, updating, and deleting MRFs

* - Fetching MRFs by ID and retrieving all MRFs

* - Assigning MRFs to recruiting managers and related personnel

*

* The controller also includes error handling to ensure robust responses.

*/

package com.rts.tap.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dto.BulkMrfAssignDto;
import com.rts.tap.dto.MRFCriteriaDTO;
import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFSubRequirementDTO;
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
import com.rts.tap.service.MRFService;

@RestController

@RequestMapping(APIConstants.BASE_URL)

@CrossOrigin(APIConstants.FRONT_END_URL)

public class MRFController {

	private static final Logger logger = LoggerFactory.getLogger(MRFController.class);

	private MRFService mrfService;

	public MRFController(MRFService mrfService) {

		super();

		this.mrfService = mrfService;

	}

	/**
	 * 
	 * Saves a new MRF (Manpower Requisition Form) and uploads the associated
	 * 
	 * template file.
	 *
	 * 
	 * 
	 * @param mrfJson         JSON representation of the MRF to be saved.
	 * 
	 * @param mrfTemplateFile The MRF template file to be uploaded.
	 * 
	 * @return ResponseEntity containing the saved MRF or an error message.
	 * 
	 */
	@PostMapping(APIConstants.ADD_MRF)
	public ResponseEntity<?> saveMRF(@RequestParam("mrf") String mrfJson,

			@RequestParam("mrfTemplate") MultipartFile mrfTemplateFile) {

		logger.info("Attempting to save a new MRF.");

		try {

			ObjectMapper objectMapper = new ObjectMapper();

			MRF mrf = objectMapper.readValue(mrfJson, MRF.class);

			byte[] mrfTemplateBytes = mrfTemplateFile.getBytes();

			if (mrf.getMrfAgreement() != null) {

				mrf.getMrfAgreement().setMrfTemplate(mrfTemplateBytes);

			} else {

				logger.warn("MRF Agreement details are missing.");

				return ResponseEntity.badRequest().body("MRF Agreement details are missing.");

			}

			MRF savedMRF = mrfService.addMrf(mrf);

			logger.info("MRF saved successfully with ID: " + savedMRF.getMrfId());

			return ResponseEntity.ok(savedMRF);

		} catch (JsonProcessingException e) {

			logger.error("Invalid JSON format: " + e.getMessage());

			return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());

		} catch (IOException e) {

			logger.error("File processing error: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

					.body("File processing error: " + e.getMessage());

		} catch (Exception e) {

			logger.error("An unexpected error occurred: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

					.body("An unexpected error occurred: " + e.getMessage());

		}

	}

	/**
	 * 
	 * Updates an existing MRF identified by mrfId.
	 *
	 * 
	 * 
	 * @param mrfId   The ID of the MRF to be updated.
	 * 
	 * @param mrfJson The updated MRF data in JSON format.
	 * 
	 * @return ResponseEntity containing the updated MRF or an error message.
	 * 
	 */
	@PutMapping(APIConstants.UPDATE_MRF)
	public ResponseEntity<MRF> updateMRF(@PathVariable Long mrfId, @RequestParam("mrf") String mrfJson) {

		logger.info("Updating MRF with ID: " + mrfId);

		try {

			ObjectMapper objectMapper = new ObjectMapper();

			MRF mrf = objectMapper.readValue(mrfJson, MRF.class);

			MRF MRFToUpdate = mrfService.getMrfById(mrfId);

			mrf.setMrfAgreement(MRFToUpdate.getMrfAgreement());

			MRF updatedMRF = mrfService.updateMrf(mrfId, mrf);

			logger.info("MRF with ID: " + mrfId + " updated successfully.");

			return ResponseEntity.ok(updatedMRF);

		} catch (JsonProcessingException e) {

			logger.error("Invalid JSON format during update: " + e.getMessage());

			return ResponseEntity.badRequest().body(null);

		} catch (@SuppressWarnings("hiding") IOException e) {

			logger.error("File processing error during update: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		} catch (Exception e) {

			logger.error("An unexpected error occurred during update: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}

	}

	/**
	 * 
	 * Retrieves an MRF by its ID.
	 *
	 * 
	 * 
	 * @param mrfId The ID of the MRF to be retrieved.
	 * 
	 * @return ResponseEntity containing the MRF or a not found response.
	 * 
	 */
	@GetMapping(APIConstants.GET_MRF)
	public ResponseEntity<MRF> getMRFById(@PathVariable Long mrfId) {
		logger.info("Retrieving MRF with ID: " + mrfId);
		return ResponseEntity.ok(mrfService.getMrfById(mrfId));
	}

	/**
	 * 
	 * Retrieves all MRFs.
	 *
	 * 
	 * 
	 * @return ResponseEntity containing a list of all MRFs.
	 * 
	 */

	@GetMapping(APIConstants.GET_ALL_MRF)

	public ResponseEntity<List<MRF>> getAllMRFs() {

		logger.info("Retrieving all MRFs.");

		List<MRF> mrfs = mrfService.getAllMrf();

		logger.info("Total MRFs retrieved: " + mrfs.size());

		return ResponseEntity.ok(mrfs);

	}

	/**
	 * 
	 * Retrieves all sub-requirement IDs mapped with MRFs.
	 *
	 * 
	 * 
	 * @return ResponseEntity containing a list of MRFSubRequirementDTOs.
	 * 
	 */

	@GetMapping(APIConstants.GET_ALL_SUBREQUIREMENTID_MAPPED_WITH_MRF)

	public ResponseEntity<List<MRFSubRequirementDTO>> getAllMRFsSubRequirementId() {

		logger.info("Retrieving all sub-requirement IDs mapped with MRFs.");

		List<MRF> mrfs = mrfService.getAllMrf();

		List<MRFSubRequirementDTO> dtoList = mrfs.stream()

				.map(mrf -> new MRFSubRequirementDTO(List

						.of(mrf.getSubRequirements() != null ? mrf.getSubRequirements().getSubRequirementId() : null)))

				.collect(Collectors.toList());

		logger.info("Total sub-requirements mapped with MRFs: " + dtoList.size());

		return ResponseEntity.ok(dtoList);

	}

	/**
	 * 
	 * Deletes an MRF by its ID.
	 *
	 * 
	 * 
	 * @param mrfId The ID of the MRF to be deleted.
	 * 
	 * @return ResponseEntity containing a status message.
	 * 
	 */
	@DeleteMapping(APIConstants.DELETE_MRF)
	public ResponseEntity<String> deleteMRF(@PathVariable Long mrfId) {

		logger.info("Attempting to delete MRF with ID: " + mrfId);

		String msg = mrfService.deleteMrfById(mrfId);

		logger.info("MRF deleted successfully with ID: " + mrfId);

		return ResponseEntity.ok(msg);

	}

	/**
	 * Retrieves filled count for a specific requirement.
	 *
	 * @param requirementId The ID of the requirement.
	 * @return ResponseEntity containing the filled count or an error message.
	 */
	@GetMapping(APIConstants.GET_RESOURCE_FILLED_COUNT)
	public ResponseEntity<Integer> getRequirementFilledCount(@PathVariable Long requirementId) {
	    logger.info("Retrieving filled count for requirement with ID: " + requirementId);
	    int filledCount = mrfService.getRequirementFilledCount(requirementId);
	    logger.info("Filled count retrieved: " + filledCount);
	    return ResponseEntity.ok(filledCount);
	}

	/**
	 * Assigns an MRF to a recruiting manager.
	 *
	 * @param mrfRecruitingManager The MRFRecruitingManager object containing
	 *                             assignment details.
	 * @return ResponseEntity containing the assigned MRFRecruitingManager or an
	 *         error message.
	 */
	@PostMapping(APIConstants.ASSIGN_MRF_TO_RECRUITING_MANAGER)
    public ResponseEntity<MRFRecruitingManager> assignMrfToRecruitingManager(@RequestBody MRFRecruitingManager mrfRecruitingManager) {
        logger.info("Assigning MRF to recruiting manager.");
        MRFRecruitingManager assignedMrfToRecruitingManager = mrfService.assignMrfToRecruitingManager(mrfRecruitingManager);
        logger.info("MRF assigned to recruiting manager successfully.");
        return ResponseEntity.ok(assignedMrfToRecruitingManager);
    }

	/**
	 * Retrieves all recruiting managers.
	 *
	 * @return ResponseEntity containing a list of recruiting managers.
	 */
	@GetMapping(APIConstants.GET_ALL_RECRUITING_MANAGER)
	public ResponseEntity<List<Employee>> getAllRecruitingManager() {
		logger.info("Retrieving all recruiting managers.");
		List<Employee> listOfRecruitingManager = mrfService.getRecruitingManagersByRoleName();
		logger.info("Total recruiting managers retrieved: " + listOfRecruitingManager.size());
		return ResponseEntity.ok(listOfRecruitingManager);
	}

	/**
	 * Retrieves all MRFs assigned to recruiting managers.
	 *
	 * @return A list of MRFRecruitingManagers containing assigned MRFs.
	 */
	@GetMapping(APIConstants.GET_ALL_ASSIGNED_MRF_TO_RECRUITING_MANAGER)
	public ResponseEntity<List<MRFRecruitingManager>> getAssignedMRFs() {
	    logger.info("Retrieving all assigned MRFs to recruiting managers.");
	    List<MRFRecruitingManager> mrfrecruitingmanager = mrfService.getAssignedMRFs(); 
	    return ResponseEntity.ok(mrfrecruitingmanager);
	}

	/**
	 * Retrieves offer approvals based on the recruiting manager's employee ID.
	 *
	 * @param employeeId ID of the recruiting manager.
	 * @return ResponseEntity containing a list of offer approvals.
	 */
	@GetMapping(APIConstants.GET_OFFER_ASSIGNED_BY_RECRUITING_MANAGER)
	public ResponseEntity<List<OfferApproval>> getOfferApprovalsByEmployeeId(@PathVariable Long employeeId) {
		logger.info("Retrieving offer approvals for recruiting manager with ID: " + employeeId);
		List<OfferApproval> approvals = mrfService.getOfferApprovalsByEmployeeId(employeeId);
		logger.info("Total offer approvals retrieved: " + approvals.size());
		return ResponseEntity.ok(approvals);
	}

	/**
	 * Sends an MRF for approval to the Business Unit Head.
	 *
	 * @param mrfId The ID of the MRF to send for approval.
	 * @return ResponseEntity containing success or error message.
	 */
	@PutMapping(APIConstants.SEND_FOR_APPROVAL)
	public ResponseEntity<String> sendForApprovalToBUH(@PathVariable Long mrfId) {
		logger.info("Sending MRF for approval to Business Unit Head with ID: " + mrfId);
		try {
			MRFStatus mrfApprovalStatus = mrfService.sendForApproval(mrfId);

			// Check the approval status.
			if ("Pending".equals(mrfApprovalStatus.getMrfApprovalStatus())) {
				logger.info("MRF sent for approval successfully.");
				return ResponseEntity.ok().body(MessageConstants.SUCCESSFULLY_SEND_FOR_APPROVAL);
			} else {
				logger.warn("Failed to send MRF for approval.");
				return ResponseEntity.badRequest().body(MessageConstants.FAILED_SEND_FOR_APPROVAL);
			}
		} catch (Exception e) {
			logger.error("Failed to send MRF for approval: " + e.getMessage());
			return ResponseEntity.ok(MessageConstants.FAILED_SEND_FOR_APPROVAL);
		}
	}

	/**
	 * Retrieves all MRFs based on client partner ID.
	 *
	 * @param employeeId The ID of the employee (client partner).
	 * @return ResponseEntity containing the list of MRFs.
	 */
	@GetMapping(APIConstants.GET_MRFS_BASED_ON_CLIENTPARTNER)
	public ResponseEntity<List<MRF>> getAllMrfsByClientPartnerId(@PathVariable Long employeeId) {
		logger.info("Retrieving all MRFs based on client partner ID: " + employeeId);
		List<MRF> mrfs = mrfService.getAllMrfsBasedOnClientPartnerId(employeeId);
		logger.info("Total MRFs retrieved for client partner: " + mrfs.size());
		return ResponseEntity.ok(mrfs);
	}

	/**
	 * Updates an SOW (Statement of Work) document associated with an MRF.
	 *
	 * @param mrfId                 The ID of the MRF to update.
	 * @param serviceLevelAgreement The updated SOW document file.
	 * @return ResponseEntity containing a status message.
	 */
	@PutMapping(APIConstants.UPDATE_SOW_DOCUMENT)
	public ResponseEntity<String> updateSOWDocument(@PathVariable Long mrfId,
			@RequestParam("serviceLevelAgreement") MultipartFile serviceLevelAgreement) {
		logger.info("Updating SOW document for MRF ID: " + mrfId);
		try {
			@SuppressWarnings("unused")
			MRFAgreement updateSOW = mrfService.updateSOW(mrfId, serviceLevelAgreement);
			logger.info("SOW document updated successfully for MRF ID: " + mrfId);
			return ResponseEntity.ok().body(MessageConstants.SUCCESSFULLY_UPDATING_SOW_DOCUMENT);

		} catch (Exception e) {
			logger.error("Failed updating SOW document: " + e.getMessage());
			return ResponseEntity.ok().body(MessageConstants.FAILED_UPDATING_SOW_DOCUMENT);
		}
	}

	/**
	 * Retrieves an assigned MRF to a recruiting manager by MRF ID.
	 *
	 * @param mrfId The ID of the MRF.
	 * @return ResponseEntity containing the MRFRecruitingManager details.
	 */
	@GetMapping(APIConstants.GET_ASSIGNED_MRF_TO_RECRUITING_MANAGER)
	public ResponseEntity<MRFRecruitingManager> getAssignedMRFById(@PathVariable Long mrfId) {
		logger.info("Retrieving assigned MRF for recruiting manager with MRF ID: " + mrfId);
		MRFRecruitingManager mrfRecruitingManager = mrfService.getAssignedMrf(mrfId);
		logger.info("Assigned MRF retrieved successfully.");
		return ResponseEntity.ok(mrfRecruitingManager);
	}

	/**
	 * Retrieves a list of recruiters assigned to a specific MRF by MRF ID.
	 *
	 * @param mrfId The ID of the MRF.
	 * @return ResponseEntity containing a list of MRFRecruiters.
	 */
	@GetMapping(APIConstants.GET_ASSIGNED_MRF_TO_RECRUITERS)
	public ResponseEntity<List<MRFRecruiters>> getRecruitersByMrfId(@PathVariable Long mrfId) {
		logger.info("Retrieving recruiters for MRF ID: " + mrfId);
		List<MRFRecruiters> mrfRecruitersList = mrfService.getRecruitersByMrfId(mrfId);
		logger.info("Total recruiters retrieved for MRF ID: " + mrfId + ": " + mrfRecruitersList.size());
		return ResponseEntity.ok(mrfRecruitersList);
	}

	/**
	 * Retrieves a list of vendors assigned to a specific MRF by MRF ID.
	 *
	 * @param mrfId The ID of the MRF.
	 * @return ResponseEntity containing a list of MRFVendors.
	 */
	@GetMapping(APIConstants.GET_ASSIGNED_MRF_TO_VENDOR)
	public ResponseEntity<List<MRFVendor>> getAssignedVendorsByMrfId(@PathVariable Long mrfId) {
		logger.info("Retrieving vendors for MRF ID: " + mrfId);
		List<MRFVendor> mrfVendorList = mrfService.getAssignedVendorsByMrfId(mrfId);
		logger.info("Total vendors retrieved for MRF ID: " + mrfId + ": " + mrfVendorList.size());
		return ResponseEntity.ok(mrfVendorList);
	}

	/**
	 * Updates the status of an MRF.
	 *
	 * @param mrfId     The ID of the MRF to update.
	 * @param newStatus The new status data.
	 * @return ResponseEntity containing the updated MRFStatus.
	 */
	@PutMapping(APIConstants.UPDATE_MRF_STATUS)
	public ResponseEntity<MRFStatus> updateMRFStatus(@PathVariable Long mrfId, @RequestBody MrfStatusDTO newStatus) {
		logger.info("Updating status of MRF with ID: " + mrfId);
		try {
			MRFStatus updatedMrf = mrfService.updateMrfStatus(mrfId, newStatus);
			logger.info("MRF status updated successfully.");
			return ResponseEntity.ok(updatedMrf);
		} catch (Exception e) {
			logger.error("Error updating MRF status: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Retrieves all MRFs based on Business Unit (BU) ID for a specific employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @return ResponseEntity containing a list of MRFs.
	 */
	@GetMapping(APIConstants.GET_ALL_MRFBU)
	public ResponseEntity<List<MRF>> getAllMRFsByBUId(@RequestParam("employeeId") String employeeId) {
		logger.info("Retrieving all MRFs based on Business Unit ID for employee: " + employeeId);
		List<MRF> mrfs = mrfService.getAllMrfByEmployeeId(employeeId);
		logger.info("Total MRFs retrieved for Business Unit: " + mrfs.size());
		return ResponseEntity.ok(mrfs);
	}

	/**
	 * Retrieves an MRF based on the associated Job Description ID.
	 *
	 * @param mrfJdId The ID of the Job Description.
	 * @return ResponseEntity containing the corresponding MRFCriteriaDTO.
	 */
	@GetMapping(APIConstants.GET_MRF_BY_MRFJDID)
	public ResponseEntity<MRFCriteriaDTO> getMrfByMrjJd(@RequestParam("mrfJdId") Long mrfJdId) {
		logger.info("Retrieving MRF by Job Description ID: " + mrfJdId);
		MRFCriteriaDTO mrf = mrfService.getMrfByMrfJdId(mrfJdId);
		logger.info("MRF retrieved successfully.");
		return ResponseEntity.ok(mrf);
	}

	/**
	 * Retrieves a list of recruiting managers associated with a specific client
	 * partner ID.
	 *
	 * @param clientPartnerId The client partner's ID.
	 * @return ResponseEntity containing a list of recruiting managers.
	 */
	@GetMapping(APIConstants.GET_RECRUITING_MANAGER_BY_CLIENTPARTNERID)
	public ResponseEntity<List<Employee>> viewAllRecruitingManagerByClientPartnerId(
			@PathVariable("clientPartnerId") Long clientPartnerId) {
		logger.info("Retrieving recruiting managers for client partner ID: " + clientPartnerId);
		List<Employee> RecruitingManager = mrfService.viewAllRecruitingManagerByClientPartnerId(clientPartnerId);
		logger.info("Total recruiting managers retrieved for client partner ID: " + clientPartnerId + ": "
				+ RecruitingManager.size());
		return ResponseEntity.ok(RecruitingManager);
	}

	/**
	 * Retrieves a list of vendors associated with a specific recruiting manager ID.
	 *
	 * @param recruitingManagerId The ID of the recruiting manager.
	 * @return ResponseEntity containing a list of vendors.
	 */
	@GetMapping(APIConstants.GET_VENDORS_BY_RECRUITING_MANAGER)
	public ResponseEntity<List<Vendor>> viewAllVendorByRecruitingManagerId(
			@PathVariable("recruitingManagerId") Long recruitingManagerId) {
		logger.info("Retrieving vendors for recruiting manager ID: " + recruitingManagerId);
		List<Vendor> Vendors = mrfService.viewAllVendorsByRecruitingManagerId(recruitingManagerId);
		logger.info(
				"Total vendors retrieved for recruiting manager ID: " + recruitingManagerId + ": " + Vendors.size());
		return ResponseEntity.ok(Vendors);
	}

	/**
	 * Retrieves a list of recruiters associated with a specific recruiting manager
	 * ID.
	 *
	 * @param recruitingManagerId The ID of the recruiting manager.
	 * @return ResponseEntity containing a list of recruiters.
	 */
	@GetMapping(APIConstants.GET_RECRUITERS_BY_RECRUITING_MANAGER)
	public ResponseEntity<List<Employee>> viewAllRecruitersByRecruitingManagerId(
			@PathVariable("recruitingManagerId") Long recruitingManagerId) {
		logger.info("Retrieving recruiters for recruiting manager ID: " + recruitingManagerId);
		List<Employee> recruiters = mrfService.viewAllRecruitersByRecruitingManagerId(recruitingManagerId);
		logger.info("Total recruiters retrieved for recruiting manager ID: " + recruitingManagerId + ": "
				+ recruiters.size());
		return ResponseEntity.ok(recruiters);
	}

	/*
	 * this api will accept MrfVendor dto and perform Assign operation and return
	 * message for output success or failed
	 *
	 * @param(vendor id) - long
	 * 
	 * @param(Mrf's Id) - long
	 * 
	 * @param(Recruiting Manager's Id) - long
	 * 
	 * @return String - successfully saved or not
	 **/

	@PostMapping(APIConstants.RECRUITING_MANAGER_ASSIGN_MRF_VENDOR)
    public ResponseEntity<String> doAssignMrfToVendor(@RequestBody MRFVendorDto mrfVendorDto) {
        String response = mrfService.mrfAssignToVendor(mrfVendorDto);
        if(response!=null) {
	        logger.info("MRF assigned to vendor successfully: {}", mrfVendorDto);

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_MRFVENDOR)
	public ResponseEntity<List<MRFVendorDto>> doGetAllRecruitersAssigned() {
		List<MRFVendorDto> mrfVendors = mrfService.getAllMrfVendorsRecords();
		logger.info("Retrieved all MRF vendors successfully");
		return ResponseEntity.ok(mrfVendors);
	}

	@PostMapping(APIConstants.RECRUITING_MANAGER_ASSIGN_MRF_RECRUITER)
    public ResponseEntity<String> doAssignMrfToRecruiter(@RequestBody MRFRecruiterDto mrfRecruiterDto) {
        String response = mrfService.mrfAssignToRecruiter(mrfRecruiterDto);
        logger.info("MRF assigned to recruiter successfully: {}", mrfRecruiterDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@PostMapping(APIConstants.RECRUITING_MANAGER_BULK_ASSIGN_MRF_RECRUITER)
    public ResponseEntity<String> doBulkAssignMrfToRecruiter(@PathVariable("recId") long recId, @RequestBody BulkMrfAssignDto bulkMrfAssignDto) {
        String response = mrfService.mrfBulkAssignToRecruiter(recId, bulkMrfAssignDto);
        logger.info("Bulk MRF assigned to recruiter successfully: {}", bulkMrfAssignDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@PostMapping(APIConstants.RECRUITING_MANAGER_BULK_ASSIGN_MRF_VENDOR)
    public ResponseEntity<String> doBulkAssignMrfToVendor(@PathVariable("vendorId") long vendorId, @RequestBody BulkMrfAssignDto bulkMrfAssignDto) {
        String response = mrfService.mrfBulkAssignToVendor(vendorId, bulkMrfAssignDto);
        logger.info("Bulk MRF assigned to vendor successfully: {}", bulkMrfAssignDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@PostMapping(APIConstants.RECRUITING_MANAGER_REASSIGN_MRF_RECRUITER)
	public ResponseEntity<String> doReassignMrfToRecruiter(@RequestBody MRFRecruiterDto mrfRecruiterDto) {
		try {
			String response = mrfService.reassignMrfToRecruiter(mrfRecruiterDto);
			logger.info("MRF reassigned to recruiter successfully: {}", mrfRecruiterDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error reassigning MRF to recruiter: {}", e.getMessage(), e);
			return new ResponseEntity<>("Error reassigning MRF to recruiter", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(APIConstants.GET_ALL_MRF_ASSIGNED_FOR_RECRUITER)
	public ResponseEntity<List<MRFRecruiters>> doGetAllMrfsByRecruiterId(@PathVariable long recruiterId) {
	    List<MRFRecruiters> mrfRecruiters = mrfService.getAllMRFByRecruiterId(recruiterId);
	    logger.info("Retrieved all MRFs for recruiter ID {} successfully", recruiterId);
	    return ResponseEntity.ok(mrfRecruiters);
	}

	@GetMapping(APIConstants.GET_ALL_MRF_ASSIGNED_FOR_VENDOR)
	public ResponseEntity<List<MRFVendor>> doGetAllMrfsByVendorId(@PathVariable long vendorId) {
	    List<MRFVendor> mrfVendors = mrfService.getAllMRFByVendorId(vendorId);
	    logger.info("Retrieved all MRFs for vendor ID {} successfully", vendorId);
	    return ResponseEntity.ok(mrfVendors);
	}

	@GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_MRFVENDOR_FORBU)
	public ResponseEntity<List<MRFVendor>> doGetAllVendorsAssigned(@PathVariable long employeeId) {
	    List<MRFVendor> mrfVendors = mrfService.getAllMrfsVendorsAssignedByRM(employeeId);
	    logger.info("Retrieved all MRF vendors assigned by RM ID {} successfully", employeeId);
	    return ResponseEntity.ok(mrfVendors);
	}

	@GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_MRFRECRUITER_FORBU)
	public ResponseEntity<List<MRFRecruiters>> doGetAllRecruitersAssigned(@PathVariable long employeeId) {
	    logger.info("Retrieving all MRF recruiters assigned by RM ID: " + employeeId);
	    List<MRFRecruiters> mrfRecruiters = mrfService.getAllMrfsRecruitersAssignedByRM(employeeId);    
	    logger.info("Total MRF recruiters retrieved for RM ID {}: {}", employeeId, mrfRecruiters.size());
	    return ResponseEntity.ok(mrfRecruiters);
	}

	@GetMapping(APIConstants.GET_ALL_ASSIGNED_MRFS_FOR_A_RECRUITER)
	public ResponseEntity<List<MRFRecruiters>> doGetAllAssignedMrfsForAllRecruiters(@PathVariable("id") long id) {
	    List<MRFRecruiters> mrfRecruiters = mrfService.fetchAllAssignedMrfsForAllRecruiter(id);
	    logger.info("Retrieved all assigned MRFs for all recruiters successfully");
	    logger.info("Total MRFs retrieved for the recruiter: {}", mrfRecruiters.size());
	    return ResponseEntity.ok(mrfRecruiters);
	}

	@GetMapping(APIConstants.GET_ALL_ASSIGNED_MRFS_FOR_A_VENDOR)
	public ResponseEntity<List<MRFVendor>> doGetAllAssignedMrfsForAllVendor() {
	    List<MRFVendor> mrfVendors = mrfService.fetchAllAssignedMrfsForAllVendors();
	    logger.info("Retrieved all assigned MRFs for all vendors successfully");
	    logger.info("Total MRFs retrieved for vendors: {}", mrfVendors.size());
	    return ResponseEntity.ok(mrfVendors);
	}
}
