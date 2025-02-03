package com.rts.tap.dao;

import java.util.List;

import com.rts.tap.model.Candidate;
import com.rts.tap.model.RecruitmentProcess;

public interface RecruitmentProcessDao {
	public void setRecruitmentLevel(RecruitmentProcess recruitmentProcess);

	public List<RecruitmentProcess> findRecruitmentProcessByMrfId(Long mrfId);

	public void updateRecruitmentProcess(RecruitmentProcess recruitmentProcess);

	public RecruitmentProcess findById(Long recruitmentProcessId);

	public void deleteRecruitmentProcessById(Long recruitmentProcessId);

	List<Candidate> findCandidateByRpId(Long rpId);

	public RecruitmentProcess findRecruitementProcessByRecruitementProcessId(Long recruitementProcessId);
}
