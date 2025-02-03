package com.rts.tap.constants;

public class APIConstants {
	private APIConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	public static final String BASE_URL = "/tap";
//	public static final String FRONT_END_URL = "http://192.168.8.90:3003";
	public static final String FRONT_END_URL = "http://localhost:3000";

	public static final String ADD_MRF = "/mrf/addMrf";
	public static final String UPDATE_MRF = "/mrf/updateMrf/{mrfId}";
	public static final String GET_MRF = "/mrf/getMrf/{mrfId}";
	public static final String GET_ALL_MRF = "/mrf/getAllMrf";
	public static final String GET_ALL_SUBREQUIREMENTID_MAPPED_WITH_MRF = "/mrf/getAllMrfSubRequirementId";
	public static final String DELETE_MRF = "/mrf/deleteMrf/{mrfId}";
	public static final String GET_RESOURCE_FILLED_COUNT = "/mrf/requirement-filled-count/{requirementId}";
	public static final String ASSIGN_MRF_TO_RECRUITING_MANAGER = "/mrf/assignToRecruitingManager";
	public static final String GET_ALL_RECRUITING_MANAGER = "/mrf/listOfRecruitingManager";
	public static final String GET_ALL_ASSIGNED_MRF_TO_RECRUITING_MANAGER = "/mrf/assigned";
	public static final String GET_OFFER_ASSIGNED_BY_RECRUITING_MANAGER = "/offerApproval/employee/{employeeId}";
	public static final String SEND_FOR_APPROVAL = "/mrf/sendForApprovalToBUH/{mrfId}";
	public static final String GET_MRFS_BASED_ON_CLIENTPARTNER = "/mrf/getMrfBasedOnClientPartnerId/{employeeId}";
	public static final String UPDATE_SOW_DOCUMENT = "/mrf/updateSOW/{mrfId}";
	public static final String GET_ASSIGNED_MRF_TO_RECRUITING_MANAGER = "/recruitingManager/assignedMrf/{mrfId}";
	public static final String GET_ASSIGNED_MRF_TO_RECRUITERS = "/recruiters/assignedMrf/{mrfId}";
	public static final String GET_ASSIGNED_MRF_TO_VENDOR = "/vendor/assigendMrf/{mrfId}";
	public static final String UPDATE_MRF_STATUS = "/updateMrfStatus/{mrfId}";
	public static final String GET_ALL_MRFBU = "/mrf/getAllBUMrf";
	public static final String GET_MRF_BY_MRFJDID = "/get-mrf-mrfJdId";
	public static final String GET_RECRUITING_MANAGER_BY_CLIENTPARTNERID = "/getRecruitingManagerByClientPartnerId/{clientPartnerId}";
	public static final String GET_VENDORS_BY_RECRUITING_MANAGER = "/getVendorsByRecruitingManagerId/{recruitingManagerId}";
	public static final String GET_RECRUITERS_BY_RECRUITING_MANAGER = "/getRecruitersByRecruitingManagerId/{recruitingManagerId}";

	public static final String ADD_JOB_DESCRIPTION = "/jobDescription/addJobDescription";
	public static final String EDIT_JOB_DESCRIPTION = "/jobDescription/editJobDescription/{id}";
	public static final String GET_ALL_JOB_DESCRIPTIONS = "/jobDescription/viewAllJobDescriptions";
	public static final String GET_JOB_DESCRIPTION_BY_ID = "/jobDescription/getJobDescriptionById/{id}";
	public static final String GET_JOB_DESCRIPTION_BY_JOBTITLE = "/jobDescription/getJobDescriptionByJobTitle/{jobTitle}";
	public static final String GET_ALL_JOB_TITLES = "/jobDescription/viewAllJobTitles";
	public static final String ADD_JOB_DESCRIPTION_MRFJD = "/jobDescription/addJobDescriptionAssignToMrf";
	public static final String GET_ALL_MRFJD = "/mrfjd/getall";
	public static final String EDIT_JOB_DESCRIPTION_MRFJD = "/jobDescription/editMrfJd";
	public static final String GET_MRFJD_BY_ID = "/jobDescription/getMrfJdById/{id}";

	public static final String RECRUITING_MANAGER_ASSIGN_MRF_VENDOR = "/assignMrfs/vendor";
	public static final String RECRUITING_MANAGER_GET_ALL_MRFVENDOR = "/fetch/allMrfVendors";
	public static final String RECRUITING_MANAGER_ASSIGN_MRF_RECRUITER = "/assignMrfs/recruiter";
	public static final String RECRUITING_MANAGER_BULK_ASSIGN_MRF_RECRUITER = "bulk/assignMrfs/recruiter/{recId}";
	public static final String RECRUITING_MANAGER_BULK_ASSIGN_MRF_VENDOR = "bulk/assignMrfs/vendor/{vendorId}";
	public static final String RECRUITING_MANAGER_REASSIGN_MRF_RECRUITER = "/reAssignMrfs/recruiter";
	public static final String GET_ALL_MRF_ASSIGNED_FOR_RECRUITER = "/getAllMrfsForRecruiter/{recruiterId}";
	public static final String GET_ALL_MRF_ASSIGNED_FOR_VENDOR = "/getallmrf/{vendorId}";
	public static final String RECRUITING_MANAGER_GET_ALL_MRFVENDOR_FORBU = "/fetch/allMrfVendors/{employeeId}";
	public static final String RECRUITING_MANAGER_GET_ALL_MRFRECRUITER_FORBU = "/fetch/allrecruiters/{employeeId}";
	public static final String GET_ALL_ASSIGNED_MRFS_FOR_A_RECRUITER = "/getAllAssignedMrfs/recruiter/{id}";
	public static final String GET_ALL_ASSIGNED_MRFS_FOR_A_VENDOR = "/getAllAssignedMrfs/vendor";

	public static final String GET_EMPLOYEE_BY_ID = "/getEmployeeById/{employeeId}";
	public static final String GET_CLIENT_BY_ID = "/client-profile-by-id/{clientId}";

	public static final String INSERT_RECRUITMENT_PROCESS_LEVEL = "/insertRecruitmentProcessLevel";
	public static final String GET_RECRUITMENT_PROCESS_LEVELS = "/getRecruitmentProcessLevels/{mrfId}";

	public static final String ADD_APPROVERLEVEL_URL = "/addApproverLevel";
	public static final String UPDATE_APPROVERLEVEL_URL = "/updateApproverLevel";
	public static final String DELETE_APPROVERLEVEL_URL = "/deleteApproverLevel/{approverLevelId}";
	public static final String GET_APPROVERLEVEL_URL = "/getApproverLevel/{mrfId}";
	public static final String GET_WORKFLOW_URL = "/getWorkflow/{mrfId}";
	public static final String GET_EMPLOYEE_URL = "/getEmployee/{employeeId}";
	public static final String ADD_SINGLE_APPROVERLEVEL_URL = "/addSingleApproverLevel";
	public static final String RECRUITING_MANAGER_GET_WORKFLOW_BY_EMPLOYEEID = "/getWorkflowbyEmployeeId/{employeeId}";
	public static final String GET_WORKFLOW_FOR_RECRUITMENT_PROCESS = "/getWorkflowByMrfIdForRecruitmentProcess/{mrfId}";
	public static final String RECRUITING_MANAGER_UPDATE_WORKFLOW = "/updateWorkFlow/{workflowId}";
	public static final String UPDATE_WORKFLOW_STATUS_AS_PENDING_FOR_RECRUITMENT_PROCESS = "/updateWorkFlowAsPendingForRecruitmentProcess/{mrfId}";
	public static final String UPDATE_WORKFLOW_STATUS_AS_PENDING_FOR_APPROVAL_PROCESS = "/updateWorkFlowAsPendingForApprovalProcess/{mrfId}";

	public static final String CROSS_ORIGIN_URL = "http://localhost:3000";
	public static final String RECRUITING_MANAGER_URL = BASE_URL + "/recruitingManager";
	public static final String RECRUITING_MANAGER_GET_ALL_ASSIGNED_RECRUITERS = "/fetch/allrecruiters";
	public static final String RECRUITING_MANAGER_GET_ALL_RECRUITERS = "/{id}";
	public static final String RECRUITING_MANAGER_GET_ALL_RECRUITERS_BY_MRF = "/fetchByMrfRmId/{id}";
	public static final String RECRUITING_MANAGER_UPDATE_STAGE = "/updateStage/{mrfId}";
	public static final String RECRUITING_MANAGER_FETCH_VENDORS_BY_MRF = "/fetchVendorsByMrfId/{id}";
	public static final String GET_ALL_ASSIGNED_RECRUITERS_BY_MRF_ID = "/byMrfId/{mrfId}";// rec
	public static final String GET_ALL_CANDIDATES_BY_MRF_ID = "/getCandidatesbyMrfId/{mrfId}";// rec
	public static final String GET_CANDIDATE_BY_MRFID_AND_VENDORID = "/getcandidateForMrfAndVendorId";
	public static final String RECRUITING_MANAGER_GET_ALL_MRFVENDORS = "/getAllMrfVendor";
	public static final String RECRUITING_MANAGER_GET_ALL_MRF = "/allMrfs/{id}";

	public static final String GET_ALL_MRFCANDIDATE_URL = "/list";
	public static final String GET_BY_ID_MRFCANDIDATE_URL = "/get/{id}";
	public static final String SAVE_MRFCANDIDATE_URL = "/save";
	public static final String UPDATE_MRFCANDIDATE_URL = "/update/{id}";
	public static final String DELETE_MRFCANDIDATE_URL = "/delete/{id}";
	public static final String GET_REMAINING_MRFCANDIDATE_URL = "/remainingcandidate/{id}";
	public static final String GET_YET_TO_OFFER_MRFCANDIDATE_URL = "/yetToOffercandidate/{mrfId}";
	public static final String BASE_MRFCANDIDATE_URL = BASE_URL + "/mrfCandidates";	
	public static final String GET_ASSESSED_CANDIDATE_URL = "/getcandidates/{id}";
	
	public static final String GET_RECRUITEMENT_PROCESS_BY_RECRUITEMENT_PROCESS_ID = "/getRecruitementProcessByRecruitementProcessId/{recruitementProcessId}";
	public static final String GET_CANDIDATE_ASSESSMENT_BY_RPID = "/getCandidateAssessmentByRpId/{rpId}";
	public static final String ADD_RECRUITMENT_PROCESS = "/addRecruitmentProcess";
	public static final String UPDATE_RECRUITMENT_PROCESS_LEVEL = "/updateRecruitmentProcessLevel";
	public static final String DELETE_RECRUITMENT_PROCESS_LEVEL = "/deleteRecruitmentProcessLevel/{recruitmentProcessId}";

}
