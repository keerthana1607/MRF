package com.rts.tap.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.MRFCandidate;
import com.rts.tap.service.MRFCandidateService;

@CrossOrigin(origins = APIConstants.FRONT_END_URL)
@RestController
@RequestMapping(path = APIConstants.BASE_MRFCANDIDATE_URL)
public class MRFCandidateController {

	private static final Logger logger = LoggerFactory.getLogger(MRFCandidateController.class);
	private MRFCandidateService mrfCandidateService;

	public MRFCandidateController(MRFCandidateService mrfCandidateService) {
		this.mrfCandidateService = mrfCandidateService;
	}

	// Method to retrieve all candidates
	@GetMapping(path = APIConstants.GET_ALL_MRFCANDIDATE_URL)
	public List<MRFCandidate> getAllCandidates() {

		logger.info("Received request to fetch all MRFCandidates.");
		return mrfCandidateService.getAllCandidates();

	}

	// Method to retrieve a candidate by ID
	@GetMapping(path = APIConstants.GET_BY_ID_MRFCANDIDATE_URL)
	public MRFCandidate getCandidateById(@PathVariable Long id) {
		logger.info("Received request to fetch MRFCandidate with ID: {}", id);
		return mrfCandidateService.getCandidateById(id);
	}

	// Method to save a new candidate
	@PostMapping(path = APIConstants.SAVE_MRFCANDIDATE_URL)
	public MRFCandidate saveCandidate(@RequestBody MRFCandidate mrfCandidate) {
		logger.info("Received request to save a new MRFCandidate.");
		return mrfCandidateService.saveCandidate(mrfCandidate);
	}

	// Method to update an existing candidate
	@PutMapping(path = APIConstants.UPDATE_MRFCANDIDATE_URL)
	public MRFCandidate updateCandidate(@PathVariable Long id, @RequestBody MRFCandidate mrfCandidate) {
		logger.info("Received request to update MRFCandidate with ID: {}", id);
		return mrfCandidateService.updateCandidate(id, mrfCandidate);

	}

	// Method to delete a candidate
	@DeleteMapping(path = APIConstants.DELETE_MRFCANDIDATE_URL)
	public void deleteCandidate(@PathVariable Long id) {
		logger.info("Received request to delete MRFCandidate with ID: {}", id);
		mrfCandidateService.deleteCandidate(id);
	}

	// Method to retrieve candidates by score ID
	@GetMapping(path = APIConstants.GET_REMAINING_MRFCANDIDATE_URL)
	public List<Candidate> getCandidateByScoreId(@PathVariable Long id) {
		logger.info("Received request to fetch MRFCandidates by score ID: {}", id);
		return mrfCandidateService.getCandidateByScoreId(id);
	}

	// Method to retrieve yet to offer candidates by MRf ID
	@GetMapping(path = APIConstants.GET_YET_TO_OFFER_MRFCANDIDATE_URL)
	public List<Candidate> getCandidateByMrfId(@PathVariable Long mrfId) {
		logger.info("Received request to fetch yet to offer MRFCandidates by MRf ID: {}", mrfId);
		return mrfCandidateService.getYetToOfferCandidateByMrfId(mrfId);

	}
}