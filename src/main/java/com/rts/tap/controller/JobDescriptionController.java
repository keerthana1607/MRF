
/**

* Author: Team-B

*

* JobDescriptionController handles job description-related operations,

* including creating, updating, retrieving, and deleting job descriptions

* and MRF assignments.

*/

package com.rts.tap.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.constants.MessageConstants;
import com.rts.tap.model.JobDescription;
import com.rts.tap.model.MrfJd;
import com.rts.tap.service.JobDescriptionService;

@RestController

@RequestMapping(APIConstants.BASE_URL)

@CrossOrigin(APIConstants.FRONT_END_URL)

public class JobDescriptionController {

	private static final Logger logger = LoggerFactory.getLogger(JobDescriptionController.class);

	private final JobDescriptionService jobDescriptionService;

	public JobDescriptionController(JobDescriptionService jobDescriptionService) {

		this.jobDescriptionService = jobDescriptionService;

	}

	/**
	 * 
	 * Adds a new job description.
	 *
	 * 
	 * 
	 * @param jobTitle                 The title of the job.
	 * 
	 * @param jobParameter             The job parameters.
	 * 
	 * @param rolesAndResponsibilities The file associated with roles and
	 * 
	 *                                 responsibilities.
	 * 
	 * @return ResponseEntity with a success message.
	 * 
	 */

	@PostMapping(APIConstants.ADD_JOB_DESCRIPTION)

	public ResponseEntity<String> addJobDescription(@RequestParam("jobTitle") String jobTitle,

			@RequestParam("jobParameter") String jobParameter,

			@RequestParam("rolesAndResponsibilities") MultipartFile rolesAndResponsibilities) throws IOException {

		logger.info("Request to add a job description received with title: {}", jobTitle);

		@SuppressWarnings("unused")

		JobDescription savedJobDescription = jobDescriptionService.addJobDescription(jobTitle, jobParameter,

				rolesAndResponsibilities);

		logger.info("Job description added successfully.");

		return ResponseEntity.ok().body(MessageConstants.JOB_DESCRIPTION_ADDED_SUCCESS);

	}

	/**
	 * 
	 * Assigns a new job description to a Manpower Requisition Form (MRF).
	 *
	 * 
	 * 
	 * @param jobTitle                 The title of the job.
	 * 
	 * @param jobParameter             The job parameters.
	 * 
	 * @param rolesAndResponsibilities The file associated with roles and
	 * 
	 *                                 responsibilities.
	 * 
	 * @return ResponseEntity with the assigned MrfJd object.
	 * 
	 */

	@PostMapping(APIConstants.ADD_JOB_DESCRIPTION_MRFJD)

	public ResponseEntity<MrfJd> addJobDescriptionAssignToMrf(@RequestParam("jobTitle") String jobTitle,

			@RequestParam("jobParameter") String jobParameter,

			@RequestParam("rolesAndResponsibilities") MultipartFile rolesAndResponsibilities) throws IOException {

		logger.info("Request to assign job description to MRF received for title: {}", jobTitle);

		MrfJd mrfJd = jobDescriptionService.addJobDescriptionAssignToMrf(jobTitle, jobParameter,

				rolesAndResponsibilities);

		logger.info("Job description assigned to MRF successfully.");

		return ResponseEntity.ok().body(mrfJd);

	}

	/**
	 * 
	 * Edits an existing job description identified by its ID.
	 *
	 * 
	 * 
	 * @param jobDescriptionId         The ID of the job description to update.
	 * 
	 * @param jobTitle                 The new job title.
	 * 
	 * @param jobParameter             The new job parameters.
	 * 
	 * @param rolesAndResponsibilities The updated file for roles and
	 * 
	 *                                 responsibilities.
	 * 
	 * @return ResponseEntity with the updated JobDescription.
	 * 
	 */

	@PutMapping(APIConstants.EDIT_JOB_DESCRIPTION)

	public ResponseEntity<?> editJobDescription(@PathVariable("id") Long jobDescriptionId,

			@RequestParam("jobTitle") String jobTitle, @RequestParam("jobParameter") String jobParameter,

			@RequestParam(value = "rolesAndResponsibilities") MultipartFile rolesAndResponsibilities) {

		logger.info("Request to edit job description with ID: {}", jobDescriptionId);

		JobDescription updatedJobDescription = jobDescriptionService.editJobDescription(jobDescriptionId, jobTitle,

				jobParameter, rolesAndResponsibilities);

		logger.info("Job description updated successfully with ID: {}", jobDescriptionId);

		return ResponseEntity.ok().body(updatedJobDescription);

	}

	/**
	 * 
	 * Edits an existing MRF job description.
	 *
	 * 
	 * 
	 * @param mrfJdId                  The ID of the MRF job description to update.
	 * 
	 * @param jobTitle                 The new job title.
	 * 
	 * @param jobParameter             The new job parameters.
	 * 
	 * @param rolesAndResponsibilities The updated file for roles and
	 * 
	 *                                 responsibilities.
	 * 
	 * @return ResponseEntity with the updated MrfJd object.
	 * 
	 */

	@PutMapping(APIConstants.EDIT_JOB_DESCRIPTION_MRFJD)

	public ResponseEntity<?> editMrfJd(@RequestParam("mrfJdId") Long mrfJdId, @RequestParam("jobTitle") String jobTitle,

			@RequestParam("jobParameter") String jobParameter,

			@RequestParam(value = "rolesAndResponsibilities") MultipartFile rolesAndResponsibilities) {

		logger.info("Request to edit MRF JD with ID: {}", mrfJdId);

		MrfJd updatedMrfJd = jobDescriptionService.editMrfJd(mrfJdId, jobTitle, jobParameter, rolesAndResponsibilities);

		logger.info("MRF JD updated successfully with ID: {}", mrfJdId);

		return ResponseEntity.ok().body(updatedMrfJd);

	}

	/**
	 * 
	 * Retrieves all job descriptions.
	 *
	 * 
	 * 
	 * @return ResponseEntity with a list of JobDescription objects.
	 * 
	 */

	@GetMapping(APIConstants.GET_ALL_JOB_DESCRIPTIONS)

	public ResponseEntity<List<JobDescription>> getAllJobDescriptions() {

		logger.info("Request to fetch all job descriptions.");

		List<JobDescription> jobDescriptions = jobDescriptionService.getAllJobDescriptions();

		logger.info("Total job descriptions retrieved: {}", jobDescriptions.size());

		return ResponseEntity.ok(jobDescriptions);

	}

	/**
	 * Retrieves a job description by its ID.
	 *
	 * @param jobDescriptionId The ID of the job description.
	 * @return ResponseEntity with the JobDescription or a not found message.
	 */
	@GetMapping(APIConstants.GET_JOB_DESCRIPTION_BY_ID)
	public ResponseEntity<?> getByJobDescriptionId(@PathVariable("id") Long jobDescriptionId) {
		logger.info("Request to fetch job description with ID: {}", jobDescriptionId);
		JobDescription jobDescription = jobDescriptionService.getByJobDescriptionId(jobDescriptionId);
		if (jobDescription != null) {
			logger.info("Job description retrieved successfully with ID: {}", jobDescriptionId);
			return ResponseEntity.ok(jobDescription);
		} else {
			logger.warn("Job description with ID: {} not found.", jobDescriptionId);
			return ResponseEntity.ok(MessageConstants.JOB_DESCRIPTION_NOT_FOUND);
		}
	}
	/**
	 * Retrieves an MRF job description by its ID.
	 *
	 * @param jobDescriptionId The ID of the MRF job description.
	 * @return ResponseEntity with the MrfJd or a not found message.
	 */

	@GetMapping(APIConstants.GET_MRFJD_BY_ID)
	public ResponseEntity<?> getMrfJdById(@PathVariable("id") Long jobDescriptionId) {
	    logger.info("Request to fetch MRF JD with ID: {}", jobDescriptionId);
	    MrfJd mrfJd = jobDescriptionService.getByMrfJdId(jobDescriptionId);
	    logger.info("MRF JD retrieved successfully with ID: {}", jobDescriptionId);
	    return ResponseEntity.ok(mrfJd); // Assuming MRF JD exists
	}

	/**
	 * Retrieves a job description by job title.
	 *
	 * @param jobTitle The title of the job.
	 * @return ResponseEntity with the JobDescription or a not found message.
	 */

	@GetMapping(APIConstants.GET_JOB_DESCRIPTION_BY_JOBTITLE)
	public ResponseEntity<?> getByJobTitle(@PathVariable("jobTitle") String jobTitle) {
	    logger.info("Request to fetch job description by title: {}", jobTitle);
	    
	    JobDescription jobDescription = jobDescriptionService.getByJobTitle(jobTitle);
	    logger.info("Job description retrieval attempt for title: {}", jobTitle);
	    
	    return ResponseEntity.ok(jobDescription);
	}

	/**
	 * Retrieves all job titles.
	 *
	 * @return ResponseEntity with a list of job titles.
	 */
	@GetMapping(APIConstants.GET_ALL_JOB_TITLES)
	public ResponseEntity<List<String>> getAllJobTitles() {
		logger.info("Request to fetch all job titles.");
		List<String> jobTitles = jobDescriptionService.getAllJobTitles();
		logger.info("Total job titles retrieved: {}", jobTitles.size());
		return ResponseEntity.ok(jobTitles);
	}

	/**
	 * Retrieves all MRF job descriptions.
	 *
	 * @return ResponseEntity with a list of MrfJd objects.
	 */
	@GetMapping(APIConstants.GET_ALL_MRFJD)
	public ResponseEntity<List<MrfJd>> getAllMrfJd() {
		logger.info("Request to fetch all MRF JDs.");
		List<MrfJd> mrfJds = jobDescriptionService.getAllMrfJd();
		logger.info("Total MRF JDs retrieved: {}", mrfJds.size());
		return ResponseEntity.ok(mrfJds);
	}
}
