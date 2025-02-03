package com.rts.tap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(MrfNotFoundException.class)
	public ResponseEntity<String> handleMrfNotFoundException(MrfNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RecruitingManagerNotFoundException.class)
	public ResponseEntity<String> handleRecruitingManagerNotFoundException(RecruitingManagerNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RecruitersNotFoundException.class)
	public ResponseEntity<String> handleRecruitersNotFoundException(RecruitersNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(VendorsNotFoundException.class)
	public ResponseEntity<String> handleVendorsNotFoundException(VendorsNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(JobDescriptionNotFoundException.class)
	public ResponseEntity<String> handleJobDescriptionNotFoundException(JobDescriptionNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(JobDescriptionTitleNotFoundException.class)
	public ResponseEntity<String> handleJobDescriptionTitleNotFoundException(JobDescriptionTitleNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RequirementCountNotFoundException.class)
	public ResponseEntity<String> handleRequirementCountNotFoundException(RequirementCountNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OfferApprovalNotFoundException.class)
	public ResponseEntity<String> handleOfferApprovalNotFoundException(OfferApprovalNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MrfAgreementException.class)
	public ResponseEntity<String> handleMrfAgreementNotFoundException(MrfAgreementException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MRFUpdateException.class)
	public ResponseEntity<String> handleMrfAgreementNotFoundException(MRFUpdateException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MrfApprovalForBuException.class)
	public ResponseEntity<String> handleMrfApprovalForBuException(MrfApprovalForBuException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AddMrfException.class)
	public ResponseEntity<String> handleAddMrfException(AddMrfException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CandidateNotFoundException.class)
	public ResponseEntity<String> handleCandidateNotFoundException(CandidateNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(CandidateSaveException.class)
	public ResponseEntity<String> handleCandidateSaveException(CandidateSaveException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
	}

	@ExceptionHandler(MRFVendorRecordsNotFoundException.class)
	public ResponseEntity<String> handleMRFVendorNotFoundException(MRFVendorRecordsNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoAssignedRecruitersFound.class)
	public ResponseEntity<String> handleAssignedRecruitersNotFoundException(NoAssignedRecruitersFound ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RecruitingManagerException.class)
	public ResponseEntity<String> handleRecruitingManagerException(RecruitingManagerException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("An internal server error occurred: " + e.getMessage());
	}

	@ExceptionHandler(MrfNotFoundException.class)
	public ResponseEntity<String> handlerMrfNotFoundException(MrfNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NoVendorsAssignedException.class)
	public ResponseEntity<String> NoVendorsAssignedException(NoVendorsAssignedException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(CandidateNotFoundException.class)
	public ResponseEntity<String> handlerCandidateNotFoundException(CandidateNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler()
	public ResponseEntity<String> handleCustomErrors(CustomException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(WorkFlowNotFoundException.class)
	public ResponseEntity<String> handleWorkFlowNotFoundException(WorkFlowNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(MRFValidationException.class)
	public ResponseEntity<String> handleMRFValidationException(MRFValidationException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(MRFAssignmentException.class)
	public ResponseEntity<String> handleMRFAssignmentException(MRFAssignmentException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(MRFBulkAssignmentException.class)
	public ResponseEntity<String> handleMRFBulkAssignmentException(MRFBulkAssignmentException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(ApproverLevelNotFoundException.class)
	public ResponseEntity<String> handleApproverLevelNotFoundException(ApproverLevelNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RecruitmentProcessNotFoundException.class)
	public ResponseEntity<String> handleRecruitmentProcessNotFoundException(RecruitmentProcessNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(CandidatesNotFoundException.class)
	public ResponseEntity<String> handleCandidatesNotFoundException(CandidatesNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler({ InvalidInputException.class, ResourceNotFoundException.class })
	public ResponseEntity<String> handleBadRequest(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler({ DatabaseException.class, UnexpectedException.class })
	public ResponseEntity<String> handleServerError(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<String> handleCustomException(CustomException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(BadInputException.class)
	public ResponseEntity<String> handleBadInputException(BadInputException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(CustomServiceException.class)
	public ResponseEntity<String> handleCustomServiceException(CustomServiceException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidArgumentException.class)
	public ResponseEntity<String> handleInvalidArgumentException(InvalidArgumentException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(DataRetrievalException.class)
	public ResponseEntity<String> handleDataRetrievalException(DataRetrievalException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
