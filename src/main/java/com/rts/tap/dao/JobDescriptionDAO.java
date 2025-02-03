package com.rts.tap.dao;

import java.util.List;

import com.rts.tap.model.JobDescription;
import com.rts.tap.model.MrfJd;

public interface JobDescriptionDAO {

	JobDescription saveJobDescription(JobDescription jobDescription);

	MrfJd saveJobDescription(MrfJd mrfJd);

	JobDescription updateJobDescription(JobDescription jobDescription);

	MrfJd updateMrfJd(MrfJd mrfJd);

	List<JobDescription> viewAllJobDescriptions();

	JobDescription viewByJobDescriptionId(Long jobDescriptionId);

	MrfJd viewByMrfJdId(Long mrfJdId);

	JobDescription viewByJobTitle(String JobTitle);

	List<String> viewAllJobTitles();

	List<MrfJd> findByIds(Long id);

	public List<MrfJd> getAllMrfJd();

}
