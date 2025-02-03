package com.rts.tap.controller;
 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.service.RecruitingManagerService;
 
@RestController
@CrossOrigin(APIConstants.CROSS_ORIGIN_URL)
@RequestMapping(APIConstants.RECRUITING_MANAGER_URL)
public class RecruitingManagerController {
 
    private static final Logger logger = LoggerFactory.getLogger(RecruitingManagerController.class);
 
    private final RecruitingManagerService recruitingManagerService;
 
    
    public RecruitingManagerController(RecruitingManagerService recruitingManagerService) {
        this.recruitingManagerService = recruitingManagerService;
    }
 
    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_MRFVENDOR)
    public ResponseEntity<List<MRFVendorDto>> doGetAllVendorsAssigned() {
        logger.info("Fetching all MRF vendors.");
        List<MRFVendorDto> mrfVendors = recruitingManagerService.getAllMrfVendorsRecords();
        logger.info("MRF vendors fetched successfully.");
        return ResponseEntity.ok(mrfVendors);
    }
 
    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_ASSIGNED_RECRUITERS)
    public ResponseEntity<List<MRFRecruiters>> doGetAllRecruiters() {
        logger.info("Fetching all assigned recruiters.");
        List<MRFRecruiters> recruiters = recruitingManagerService.getAllAssignedMrfRecruiterRecords();
        logger.info("Assigned recruiters fetched successfully.");
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_RECRUITERS)
    public ResponseEntity<?> doGetAllRecruitersForRecruitingManagerId(@PathVariable long id) {
        logger.info("Fetching all recruiters for recruiting manager ID: " + id);
        List<Employee> recruiters = recruitingManagerService.getAllRecruitersByRecruitingManagerID(id);
        logger.info("Recruiters fetched successfully for recruiting manager ID: " + id);
        return ResponseEntity.ok(recruiters);
    }
    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_RECRUITERS_BY_MRF)
    public ResponseEntity<?> doGetAllRecruitersByMrfRecruitingManagerId(@PathVariable long id) {
        logger.info("Fetching all recruiters by MRF recruiting manager ID: " + id);
        List<MRFRecruiters> recruiters = recruitingManagerService.getAllRecruitersByMRFRecruitingManagerId(id);
        logger.info("Recruiters fetched successfully by MRF recruiting manager ID: " + id);
        return ResponseEntity.ok(recruiters);
    }
 
    
    @PutMapping(APIConstants.RECRUITING_MANAGER_UPDATE_STAGE)
    public ResponseEntity<?> updateStage(@RequestBody MrfStatusDTO mrfstatus, @PathVariable Long mrfId) {
        logger.info("Updating MRF stage for MRF ID: " + mrfId);
        recruitingManagerService.updateMrfStage(mrfstatus, mrfId);
        logger.info("MRF stage updated successfully for MRF ID: " + mrfId);
        return ResponseEntity.ok(MessageConstants.MRF_STAGE_UPDATED);
    }
 
    @GetMapping(APIConstants.RECRUITING_MANAGER_FETCH_VENDORS_BY_MRF)
    public ResponseEntity<?> doGetAllVendorsByMrfId(@PathVariable long id) {
        logger.info("Fetching all vendors by MRF ID: " + id);
        List<MRFVendor> vendors = recruitingManagerService.getAllVendorsAssignedForMRFbyMrfId(id);
        logger.info("Vendors fetched successfully by MRF ID: " + id);
        return ResponseEntity.ok(vendors);
    }
 
 
    @GetMapping(APIConstants.GET_ALL_ASSIGNED_RECRUITERS_BY_MRF_ID)
    public ResponseEntity<?> getRecruitersByMrfId(@PathVariable Long mrfId) {
        logger.info("Fetching all assigned recruiters by MRF ID: " + mrfId);
        List<MRFRecruiters> recruiters = recruitingManagerService.getRecruitersByMrfId(mrfId);
        logger.info("Assigned recruiters fetched successfully by MRF ID: " + mrfId);
        return ResponseEntity.ok(recruiters);
    }
 
    @GetMapping(APIConstants.GET_ALL_CANDIDATES_BY_MRF_ID)
    public ResponseEntity<?> getCandidatesByMrfId(@PathVariable Long mrfId) {
        logger.info("Fetching all candidates by MRF ID: " + mrfId);
        List<Candidate> candidates = recruitingManagerService.getAllCandidateByMrfId(mrfId);
        logger.info("Candidates fetched successfully by MRF ID: " + mrfId);
        return ResponseEntity.ok(candidates);
    }
 
 
    @GetMapping(APIConstants.GET_ALL_MRF_ASSIGNED_FOR_RECRUITER)
    public ResponseEntity<?> doGetAllMrfsByRecruiterId(@PathVariable long recruiterId) {
        logger.info("Fetching all MRFs by recruiter ID: " + recruiterId);
        List<MRFRecruiters> mrfs = recruitingManagerService.getAllMRFByRecruiterId(recruiterId);
        logger.info("MRFs fetched successfully by recruiter ID: " + recruiterId);
        return ResponseEntity.ok(mrfs);
    }
    @GetMapping(APIConstants.GET_CANDIDATE_BY_MRFID_AND_VENDORID)
    public ResponseEntity<?> getCandidateByMrfIdVendor(@RequestParam("mrfId") Long mrfId,
                                                       @RequestParam("vendorId") Long vendorId) {
        logger.info("Fetching candidates by MRF ID: " + mrfId + " and vendor ID: " + vendorId);
        List<Candidate> candidates = recruitingManagerService.getCandidateByMrfIdVendorId(mrfId, vendorId);
        logger.info("Candidates fetched successfully by MRF ID: " + mrfId + " and vendor ID: " + vendorId);
        return ResponseEntity.ok(candidates);
    }
  
    @GetMapping(APIConstants.RECRUITING_MANAGER_GET_ALL_MRFVENDORS)
    public ResponseEntity<?> doGetAllRecruitersAssignedMrfVendor() {
 
    	    logger.info("Fetching all MRF vendors.");
    	    List<MRFVendor> mrfVendors = recruitingManagerService.getAllMrfVendors();
    	    logger.info("MRF vendors fetched successfully.");
    	    return ResponseEntity.ok(mrfVendors);
    }
}