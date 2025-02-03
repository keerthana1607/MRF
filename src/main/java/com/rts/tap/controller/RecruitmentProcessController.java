package com.rts.tap.controller;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dto.RecruitmentProcessDto;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.RecruitmentProcess;
import com.rts.tap.service.RecruitmentProcessService;
 
@RestController
@RequestMapping(path = APIConstants.BASE_URL)
@CrossOrigin(origins = APIConstants.FRONT_END_URL)
public class RecruitmentProcessController {
 
    private static final Logger logger = LoggerFactory.getLogger(RecruitmentProcessController.class);
 
    private final RecruitmentProcessService recruitmentProcessService;
 
    public RecruitmentProcessController(RecruitmentProcessService recruitmentProcessService) {
        this.recruitmentProcessService = recruitmentProcessService;
    }
 
 
    @PostMapping(APIConstants.INSERT_RECRUITMENT_PROCESS_LEVEL)
    public ResponseEntity<?> insertRecruitmentProcessLevel(@RequestBody RecruitmentProcess recruitmentProcess) {
        logger.info("Attempting to insert recruitment process level.");
        recruitmentProcessService.insertRecruitmentProcessLevel(recruitmentProcess);
        logger.info("Recruitment process level inserted successfully.");
        return ResponseEntity.ok(MessageConstants.RECRUITMENT_PROCESS_LEVEL_INSERTED);
    }
    @GetMapping(APIConstants.GET_RECRUITMENT_PROCESS_LEVELS)
    public ResponseEntity<?> getRecruitmentProcessLevel(@PathVariable Long mrfId) {
        logger.info("Fetching recruitment process levels for MRF ID: " + mrfId);
        List<RecruitmentProcess> recruitmentProcesses = recruitmentProcessService.getRecruitmentProcessLevels(mrfId);
        logger.info("Recruitment process levels fetched successfully for MRF ID: " + mrfId);
        return ResponseEntity.ok(recruitmentProcesses);
    }
 
    @PostMapping(APIConstants.ADD_RECRUITMENT_PROCESS)
    public ResponseEntity<?> addRecruitmentProcess(@RequestBody RecruitmentProcessDto recruitmentProcessDto) {
        logger.info("Attempting to add recruitment process.");
 
        recruitmentProcessService.setRecruitmentProcess(recruitmentProcessDto);
        
        logger.info("Recruitment process added successfully.");
        return ResponseEntity.ok(MessageConstants.RECRUITMENT_PROCESS_ADDED);
    }
    
    @PutMapping(APIConstants.UPDATE_RECRUITMENT_PROCESS_LEVEL)
    public ResponseEntity<?> updateRecruitmentProcessLevel(@RequestBody RecruitmentProcess recruitmentProcess) {
        logger.info("Attempting to update recruitment process level.");
        recruitmentProcessService.updateRecruitmentProcessLevel(recruitmentProcess);
        logger.info("Recruitment process level updated successfully.");
        return ResponseEntity.ok(MessageConstants.RECRUITMENT_PROCESS_LEVEL_UPDATED);
    }
 
 
    @GetMapping(APIConstants.GET_CANDIDATE_ASSESSMENT_BY_RPID)
    public ResponseEntity<?> getCandidateByRpId(@PathVariable Long rpId) {
        logger.info("Fetching candidates for recruitment process ID: " + rpId);
        List<Candidate> candidates = recruitmentProcessService.getCandidateByRpId(rpId);
        logger.info("Candidates fetched successfully for recruitment process ID: " + rpId);
        return ResponseEntity.ok(candidates);
    }
 
 
    @GetMapping(APIConstants.GET_RECRUITEMENT_PROCESS_BY_RECRUITEMENT_PROCESS_ID)
    public ResponseEntity<?> getRecruitementProcessByRecruitementProcessId(@PathVariable Long recruitementProcessId) {
        logger.info("Fetching recruitment process by ID: " + recruitementProcessId);
        RecruitmentProcess recruitmentProcess = recruitmentProcessService.getRecruitementProcessByRecruitementProcessId(recruitementProcessId);
        logger.info("Recruitment process fetched successfully for ID: " + recruitementProcessId);
        return ResponseEntity.ok(recruitmentProcess);
    }
}
 