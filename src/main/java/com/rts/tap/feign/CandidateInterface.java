package com.rts.tap.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.model.Score;

@FeignClient("ASSESSMENT-SERVICE")
public interface CandidateInterface {
	
	@GetMapping(path=APIConstants.GET_ASSESSED_CANDIDATE_URL)
    public List<Score> getScoreOfAssessedCandidate(@PathVariable Long id);

}
