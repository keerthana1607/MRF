package com.rts.tap.serviceimplementation;
 
import java.util.List;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.rts.tap.dao.MRFCandidateDao;
import com.rts.tap.exception.CandidateNotFoundException;
import com.rts.tap.exception.CandidateSaveException;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.MRFCandidate;
import com.rts.tap.service.MRFCandidateService;
 
@Service
@Transactional
public class MRFCandidateServiceImpl implements MRFCandidateService {
 
	private final MRFCandidateDao mrfCandidateRepository;
 
	public MRFCandidateServiceImpl(MRFCandidateDao mrfCandidateRepository) {
		this.mrfCandidateRepository = mrfCandidateRepository;
	}
 
	@Override
	public List<MRFCandidate> getAllCandidates() {
		List<MRFCandidate> candidates = mrfCandidateRepository.getAllCandidates();
		if (candidates.isEmpty()) {
			throw new CandidateNotFoundException("There are no candidates available.");
		}
		return candidates;
	}
 
	@Override
	public MRFCandidate getCandidateById(Long id) {
		MRFCandidate candidate = mrfCandidateRepository.getCandidateById(id);
		if (candidate == null) {
			throw new CandidateNotFoundException("Candidate not found with ID: " + id);
		}
		return candidate;
	}
 
	@Override
	public MRFCandidate saveCandidate(MRFCandidate mrfCandidate) {
 
		try {
			return mrfCandidateRepository.saveCandidate(mrfCandidate);
		} catch (Exception e) {
			throw new CandidateSaveException("Failed to save candidate due to: " + e.getMessage(), e);
		}
	}
 
	@Override
	public MRFCandidate updateCandidate(Long id, MRFCandidate mrfCandidate) {
		MRFCandidate existingCandidate = mrfCandidateRepository.getCandidateById(id);
		if (existingCandidate == null) {
			throw new CandidateNotFoundException("Candidate not found with ID: " + id);
		}
		existingCandidate.setCandidate(mrfCandidate.getCandidate());
		existingCandidate.setMrfRecruiter(mrfCandidate.getMrfRecruiter());
		existingCandidate.setStatus(mrfCandidate.getStatus());
		return mrfCandidateRepository.saveCandidate(existingCandidate);
	}
 
	@Override
	public void deleteCandidate(Long id) {
		MRFCandidate candidate = mrfCandidateRepository.getCandidateById(id);
		if (candidate == null) {
			throw new CandidateNotFoundException("Candidate not found with ID: " + id);
		}
		mrfCandidateRepository.deleteCandidate(id);
	}
 
	@Override
	public List<Candidate> getCandidateByScoreId(Long id) {
		List<Candidate> candidates = mrfCandidateRepository.getRemainingCandidates(id);
		if (candidates.isEmpty()) {
			throw new CandidateNotFoundException("No candidates found for score ID: " + id);
		}
		return candidates;
	}
 
	@Override
	public List<Candidate> getYetToOfferCandidateByMrfId(Long id) {
		List<Candidate> candidates = mrfCandidateRepository.getYetToOfferCandidateByMrfId(id);
		if (candidates.isEmpty()) {
			throw new CandidateNotFoundException("No candidates found for MRF ID: " + id);
		}
		return candidates;
	}
}