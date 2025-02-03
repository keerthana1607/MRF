package com.rts.tap.serviceimplementation;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.JobDescriptionDAO;
import com.rts.tap.exception.JobDescriptionNotFoundException;
import com.rts.tap.exception.JobDescriptionTitleNotFoundException;
import com.rts.tap.model.JobDescription;
import com.rts.tap.model.MrfJd;
import com.rts.tap.service.JobDescriptionService;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService {
	private final JobDescriptionDAO jobDescriptionDao;
	private static final Logger logger = LoggerFactory.getLogger(JobDescriptionServiceImpl.class);

	public JobDescriptionServiceImpl(JobDescriptionDAO jobDescriptionDao) {
		this.jobDescriptionDao = jobDescriptionDao;
	}

	/**
	 * Adds a new job description.
	 *
	 * @param jobTitle                 the title of the job
	 * @param jobParameter             the parameters of the job
	 * @param rolesAndResponsibilities the roles and responsibilities file
	 * @return the added JobDescription object
	 */
	@Override
	public JobDescription addJobDescription(String jobTitle, String jobParameter,
			MultipartFile rolesAndResponsibilities) {
		JobDescription jobDescription = new JobDescription();
		jobDescription.setJobTitle(jobTitle);
		jobDescription.setJobParameter(jobParameter);

		try {
			byte[] rolesAndResponsibilitiesBytes = rolesAndResponsibilities.getBytes();
			jobDescription.setRolesAndResponsibilities(rolesAndResponsibilitiesBytes);
			jobDescriptionDao.saveJobDescription(jobDescription);
			logger.info("Successfully added Job Description: ", jobTitle);
			return jobDescription;
		} catch (IOException e) {
			logger.error("Failed to add job description due to IO exception: {}", e.getMessage());
			throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED);
		} catch (Exception e) {
			logger.error("Failed to add job description: {}", e.getMessage());
			throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED);
		}
	}

	/**
	 * Edits an existing job description.
	 *
	 * @param jobDescriptionId         the ID of the job description
	 * @param jobTitle                 the new title of the job
	 * @param jobParameter             the new parameters of the job
	 * @param rolesAndResponsibilities the roles and responsibilities file
	 * @return the updated JobDescription object
	 */
	@Override
	public JobDescription editJobDescription(Long jobDescriptionId, String jobTitle, String jobParameter,
			MultipartFile rolesAndResponsibilities) {
		JobDescription existingJobDescription = jobDescriptionDao.viewByJobDescriptionId(jobDescriptionId);

		if (existingJobDescription == null) {
			logger.warn("Job Description not found for ID: {}", jobDescriptionId);
			throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_NOT_FOUND);
		}

		existingJobDescription.setJobTitle(jobTitle);
		existingJobDescription.setJobParameter(jobParameter);

		if (rolesAndResponsibilities != null && !rolesAndResponsibilities.isEmpty()) {
			try {
				byte[] rolesAndResponsibilitiesBytes = rolesAndResponsibilities.getBytes();
				existingJobDescription.setRolesAndResponsibilities(rolesAndResponsibilitiesBytes);
			} catch (IOException e) {
				logger.error("Failed to update job description roles and responsibilities: {}", e.getMessage());
				throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED);
			}
		}

		jobDescriptionDao.saveJobDescription(existingJobDescription);
		logger.info("Successfully updated Job Description ID: {}", jobDescriptionId);
		return existingJobDescription;
	}

	/**
	 * Retrieves all job descriptions.
	 *
	 * @return a list of JobDescription objects
	 */

	@Override
	public List<JobDescription> getAllJobDescriptions() {
	    logger.info("Fetching all job descriptions");
	    List<JobDescription> jobDescriptions = jobDescriptionDao.viewAllJobDescriptions();
	    
	    if (jobDescriptions.isEmpty()) {
	        throw new JobDescriptionNotFoundException("No job descriptions found.");
	    }
	    
	    return jobDescriptions;
	}

	/**
	 * Retrieves a job description by its ID.
	 *
	 * @param jobDescriptionId the ID of the job description
	 * @return the JobDescription object, or null if not found
	 */

	@Override
	public JobDescription getByJobDescriptionId(Long jobDescriptionId) {
	    logger.info("Fetching Job Description by ID: {}", jobDescriptionId);
	    JobDescription jobDescription = jobDescriptionDao.viewByJobDescriptionId(jobDescriptionId);
	    
	    logger.info("Retrieved Job Description: {}", jobDescription);    
	    if (jobDescription == null) {
	    	
	        throw new JobDescriptionNotFoundException("MRF Job Description not found for ID: " + jobDescriptionId);
	    }
	    
	    return jobDescription;
	}

	/**
	 * Retrieves a job description by its title.
	 *
	 * @param jobTitle the title of the job
	 * @return the JobDescription object, or null if not found
	 */

	@Override
	public JobDescription getByJobTitle(String jobTitle) {
	    logger.info("Fetching Job Description by title: {}", jobTitle);
	    List<String> job = jobDescriptionDao.viewAllJobTitles();
	    
	    try {
	        if (!job.contains(jobTitle)) {
	            throw new JobDescriptionNotFoundException("No job description found for the given title: " + jobTitle);
	        }

	        JobDescription jobDescription = jobDescriptionDao.viewByJobTitle(jobTitle);

	        return jobDescription;

	    }catch (JobDescriptionNotFoundException error) {
	        throw error;
	    }
	}

	/**
	 * Retrieves all job titles from the system.
	 *
	 * @return a list of all job titles
	 */

	@Override
	public List<String> getAllJobTitles() {
	    logger.info("Fetching all job titles");
	    
	    List<String> jobTitles = jobDescriptionDao.viewAllJobTitles();
	    
	    logger.info("Retrieved job titles: {}", jobTitles);
	    
	    if (jobTitles == null || jobTitles.isEmpty()) {
	        logger.warn("No job titles found, throwing exception");
	        throw new JobDescriptionTitleNotFoundException("No job titles found.");
	    }
	    return jobTitles;
	}

	/**
	 * Adds a job description and assigns it to an MRD.
	 *
	 * @param jobTitle                 the title of the job
	 * @param jobParameter             the parameters of the job
	 * @param rolesAndResponsibilities the roles and responsibilities file
	 * @return the added MrfJd object
	 * @throws IOException if an IO error occurs
	 */
	@Override
	public MrfJd addJobDescriptionAssignToMrf(String jobTitle, String jobParameter,
			MultipartFile rolesAndResponsibilities) throws IOException {
		MrfJd mrfJd = new MrfJd();
		mrfJd.setJobTitle(jobTitle);
		mrfJd.setJobParameter(jobParameter);

		try {
			byte[] rolesAndResponsibilitiesBytes = rolesAndResponsibilities.getBytes();
			mrfJd.setRolesAndResponsibilities(rolesAndResponsibilitiesBytes);
			jobDescriptionDao.saveJobDescription(mrfJd);
			logger.info("Successfully added MRF Job Description: {}", jobTitle);
			return mrfJd;
		} catch (Exception e) {
			logger.error("Failed to add job description for MRF: {}", e.getMessage());
			throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_ADDED_FAILED);
		}
	}

	/**
	 * Retrieves all MRD Job Descriptions.
	 *
	 * @return a list of MrfJd objects
	 */

	@Override
	public List<MrfJd> getAllMrfJd() {
	    logger.info("Fetching all MRD Job Descriptions");
	    List<MrfJd> mrfJds = jobDescriptionDao.getAllMrfJd();

	    if (mrfJds == null || mrfJds.isEmpty()) {
	        throw new JobDescriptionNotFoundException("No MRF Job Descriptions found.");
	    }

	    return mrfJds;
	}

	/**
	 * Retrieves an MRD Job Description by its ID.
	 *
	 * @param mrfJdId the ID of the MRD Job Description
	 * @return the MrfJd object, or null if not found
	 */

	@Override
	public MrfJd getByMrfJdId(Long mrfJdId) {
	    logger.info("Fetching MRD Job Description by ID: {}", mrfJdId);
	    MrfJd mrfJd = jobDescriptionDao.viewByMrfJdId(mrfJdId);
	    
	    if (mrfJd == null) {
	        throw new JobDescriptionNotFoundException("MRD Job Description not found for ID: " + mrfJdId);
	    }
	    
	    return mrfJd;
	}

	/**
	 * Edits an existing MRD Job Description.
	 *
	 * @param mrfJdId                  the ID of the MRD Job Description to edit
	 * @param jobTitle                 the new title of the job
	 * @param jobParameter             the new parameters of the job
	 * @param rolesAndResponsibilities the roles and responsibilities file
	 * @return the updated MrfJd object
	 */
	@Override
	public MrfJd editMrfJd(Long mrfJdId, String jobTitle, String jobParameter, MultipartFile rolesAndResponsibilities) {
		MrfJd existingMrfJd = jobDescriptionDao.viewByMrfJdId(mrfJdId);

		if (existingMrfJd == null) {
			logger.warn("MRD Job Description not found for ID: {}", mrfJdId);
			throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_NOT_FOUND);
		}

		existingMrfJd.setJobTitle(jobTitle);
		existingMrfJd.setJobParameter(jobParameter);

		if (rolesAndResponsibilities != null && !rolesAndResponsibilities.isEmpty()) {
			try {
				byte[] rolesAndResponsibilitiesBytes = rolesAndResponsibilities.getBytes();
				existingMrfJd.setRolesAndResponsibilities(rolesAndResponsibilitiesBytes);
			} catch (IOException e) {
				logger.error("Failed to update MRD Job Description roles and responsibilities: {}", e.getMessage());
				throw new RuntimeException(MessageConstants.JOB_DESCRIPTION_UPDATE_FAILED);
			}
		}

		jobDescriptionDao.updateMrfJd(existingMrfJd);
		logger.info("Successfully updated MRD Job Description ID: {}", mrfJdId);
		return existingMrfJd;
	}
}