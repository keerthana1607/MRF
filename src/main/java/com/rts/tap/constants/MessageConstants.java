
package com.rts.tap.constants;

public class MessageConstants {
	private MessageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	public static final String SUCCESSFULLY_SEND_FOR_APPROVAL = "Sent to BU Head Successfully";
	public static final String FAILED_SEND_FOR_APPROVAL = "Failed to Send for Approval to BU Head";

	public static final String SUCCESSFULLY_UPDATING_SOW_DOCUMENT = "SOW Document Updated Successfully";
	public static final String FAILED_UPDATING_SOW_DOCUMENT = "Failed to Update SOW Document";

	public static final String JOB_DESCRIPTION_ADDED_SUCCESS = "New Job Description Added Successfully";
	public static final String JOB_DESCRIPTION_ADDED_FAILED = "Failed to Add Job Description";
	public static final String JOB_DESCRIPTION_NOT_FOUND = "No such Job Description is Found";
	public static final String JOB_DESCRIPTION_UPDATE_SUCCESS = "Job Description Updated Successfully";
	public static final String JOB_DESCRIPTION_UPDATE_FAILED = "Failed to Update Job Description";

	public static final String RECRUITING_MANAGER_ASSIGNED_MRF_SUCCESS = "MRF assigned to vendor successful";
	public static final String RECRUITING_MANAGER_ASSIGNED_MRF_FAILED = "MRF assigned to vendor Failed";
	public static final String RECRUITING_MANAGER_MRF_STATUS_ASSIGNED = "Assigned";

	public static final String RECRUITER_ASSIGNED_MRF_SUCCESS = "MRF assigned to Recruiter successful";
	public static final String RECRUITER_ASSIGNED_MRF_FAILED = "MRF assigned to Recruiter Failed";
	public static final String RECRUITER_MRF_STATUS_ASSIGNED = "Assigned";
	public static final String RECRUITER_MRF_STATUS_REASSIGNED = null;

	public static final String RECRUITMENT_PROCESS_LEVEL_INSERTED = "Recruitment Process Level Inserted";

	public static final String APPROVER_LEVEL_ADDED = "Approver Level Added";
	public static final String APPROVER_LEVEL_UPDATED = "Approver Level Updated";
	public static final String APPROVER_LEVEL_DELETED = "Approver Level Deleted";
	public static final String SET_WORKFLOW_TYPE_APPROVER_LEVEL = "Approver Level";
	public static final String SET_WORKFLOW_STATUS_AS_PENDING = "Approved";
	public static final String SET_WORKFLOW_TYPE_RECRUITMENT_PROCESS = "Recruitment Process";
	public static final String WORKFLOW_STATUS_UPDATED = "Workflow status updated";
	public static final String WORKFLOW_APPROVAL_STATUS_SUCCESS = "Workflow Approval done";

	public static final String GET_ALL_MRFCANDIDATE_ERROR = "Error while fetching all MRFCandidates.";
	public static final String GET_BY_ID_MRFCANDIDATE_ERROR = "Error while fetching MRFCandidate by ID: ";
	public static final String SAVE_MRFCANDIDATE_ERROR = "Error while saving MRFCandidate.";
	public static final String UPDATE_MRFCANDIDATE_ERROR = "Error while updating MRFCandidate with ID: ";
	public static final String DELETE_MRFCANDIDATE_ERROR = "Error while deleting MRFCandidate with ID: ";
	public static final String GET_CANDIDATE_BY_SCOREID_ERROR = "Error while fetching candidates by score ID: ";
	public static final String GET_YET_TO_OFFER_CANDIDATEBY_MRFID_ERROR = "Error while fetching yet to offer candidates by MRF ID: ";
	public static final String MRF_STAGE_UPDATED = "Mrf Stage is Updated";
	
	public static final String RECRUITMENT_PROCESS_ADDED = "Recruitment Process Added";
	public static final String RECRUITMENT_PROCESS_LEVEL_UPDATED = "Recruitment Process Level Updated";
	public static final String RECRUITMENT_PROCESS_LEVEL_DELETED = "Recruitment Process Level Deleted";
}



