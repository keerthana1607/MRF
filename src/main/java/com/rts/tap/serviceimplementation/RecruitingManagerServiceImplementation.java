

package com.rts.tap.serviceimplementation;
 
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rts.tap.dao.RecruitingManagerDao;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.exception.CandidateNotFoundException;
import com.rts.tap.exception.CustomException;
import com.rts.tap.exception.DataAccessException;
import com.rts.tap.exception.DataRetrievalException;
import com.rts.tap.exception.MRFVendorRecordsNotFoundException;
import com.rts.tap.exception.MrfNotFoundException;
import com.rts.tap.exception.NoAssignedRecruitersFound;
import com.rts.tap.exception.NoVendorsAssignedException;
import com.rts.tap.exception.RecruiterNotFoundException;
import com.rts.tap.exception.ResourceNotFoundException;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.service.RecruitingManagerService;

import jakarta.transaction.Transactional;
 
/**

* author: Jeevarajan Rajarajacholan, Vashanth 

* version: v6.0 

* updated at: 29-11-2024

**/
 
@Service

@Transactional

public class RecruitingManagerServiceImplementation implements RecruitingManagerService {
 
	RecruitingManagerDao recruitingManagerDao;
 
	private static final Logger logger = LoggerFactory.getLogger(RecruitingManagerServiceImplementation.class);
 
	public RecruitingManagerServiceImplementation(RecruitingManagerDao recruitingManagerDao

			) {
 
		super();

		this.recruitingManagerDao = recruitingManagerDao;

	}

 
 
	@Override

	public MRF getMrfById(long id) {

		return recruitingManagerDao.getMrfById(id);

	}
 
	@Override

    public List<MRFRecruiters> getAllAssignedMrfRecruiterRecords() {

        try {

            return recruitingManagerDao.getAllRecruitersAssigned();

        } catch (Exception e) {

            logger.error("An error occurred while fetching assigned recruiters: " + e.getMessage(), e);

            throw new NoAssignedRecruitersFound("An error occurred while fetching assigned recruiters: " + e.getMessage());

        }

    }
 
	 @Override

	    public List<Employee> getAllRecruitersByRecruitingManagerID(long id) {

		 if((Long)id == null) {

			 throw new IllegalArgumentException("Null value or Invalid Data Type Passed As Id");

		 }

		 try {

			 List<Employee> recruiters = recruitingManagerDao.getAllRecruitersByRecruitingManagerId(id);

			 if (recruiters.isEmpty()) {

				 throw new RecruiterNotFoundException("No recruiters found for recruiting manager ID: " + id);

			 }

			 return recruiters;

		 }

		 catch (Exception e) {

			 throw new RecruiterNotFoundException("Error Fetching Recruiters "+e.toString());

		}

	    }
 
	 @Override

	    public void updateMrfStage(MrfStatusDTO mrfStage, Long mrfId) {

		 try {

			 if (mrfId == null) {

		            throw new MrfNotFoundException("MRF ID cannot be null");

		        }

		        if(recruitingManagerDao.getMrfById(mrfId) == null) {

		        	throw new MrfNotFoundException("No Mrf Found for the ID "+mrfId);

		        }

		        recruitingManagerDao.updateMrfStage(mrfStage, mrfId);

		 }

		 catch (Exception e) {

			 throw new CustomException("Mrf status Updation Failed "+e.toString());

		}

	    }
 
 
	 @Override

	    public List<MRFRecruiters> getAllRecruitersByMRFRecruitingManagerId(long id) {

		 try {

			 List<MRFRecruiters> recruiters = recruitingManagerDao.getAssignedRecruitersByMrfRecruitingManagerId(id);

		        if (recruiters.isEmpty()) {

		            throw new RecruiterNotFoundException("No recruiters found for MRF recruiting manager ID: " + id);

		        }

		        return recruiters; 

		 }

		 catch(Exception e){

			 throw new CustomException("Error fetching Recruiters "+e.toString());

		 }

	    }
 
	@Override

	public List<MRFVendor> getAllVendorsAssignedForMRFbyMrfId(long mrfId) {

		if((Long)mrfId==null) {

			throw new MrfNotFoundException("No Mrf Found for Id"+mrfId);

		}

		try {

			List<MRFVendor> mrfvendors=recruitingManagerDao.getAllVendorsAssignedForMrf(mrfId);

			if(mrfvendors.isEmpty()) {

				throw new NoVendorsAssignedException("No vendors Assigned for the Mrf Id "+ mrfId);

			}
 
		   return mrfvendors;

		}

		catch (Exception e) {

         throw new MRFVendorRecordsNotFoundException("Error Fetching MRf Vendors"+e.toString());

		}

	}
 
	 @Override

	    public List<MRFRecruiters> getRecruitersByMrfId(Long mrfId) {

		 if(mrfId == null) {

			 throw new IllegalArgumentException("Null value passed  for mrfId");

		 }

		 try {

			 List<MRFRecruiters> recruiters = recruitingManagerDao.getRecruitersByMrfId(mrfId);

		        if (recruiters.isEmpty()) {

		            throw new RecruiterNotFoundException("No recruiters found for MRF ID: " + mrfId);

		        }

		        return recruiters;
 
		 }

		 catch (Exception e) {
 
			 throw new RecruiterNotFoundException("Error Fetching Recruiters"+e.toString());

		 }

	       	    }
 
 
	 @Override

	    public List<Candidate> getAllCandidateByMrfId(Long mrfId) {

		 if(mrfId == null) {

			 throw new IllegalArgumentException("Null value passed  for mrfId");

		 }

		 if(recruitingManagerDao.getMrfById(mrfId)==null) {

			 throw new MrfNotFoundException("No MRFs Found for Id :"+mrfId);

		 }

		 try {
 
		        List<Candidate> candidates = recruitingManagerDao.getListOfCandidateByMrfId(mrfId);

		        if (candidates.isEmpty()) {

		            throw new CandidateNotFoundException("No candidates found for MRF ID: " + mrfId);

		        }

		        return candidates;

		 }

		 catch (Exception e) {

               throw new CandidateNotFoundException("Error Fetching Candidates :"+e.toString());

		 }

	    }
 
 
	 @Override

	    public List<MRFRecruiters> getAllMRFByRecruiterId(long id) {

		 try {

			 if((Long)id == null) {

				 throw new IllegalArgumentException("Null value passed  as Recruiter Id");

			 }

		        List<MRFRecruiters> mrfs = recruitingManagerDao.getAllMrfsForRecruiterId(id);

		        if (mrfs.isEmpty()) {

		            throw new MrfNotFoundException("No MRFs found for recruiter ID: " + id);

		        }

		        return mrfs;

		 }

		 catch (Exception e) {
 
		      throw new CustomException("Error fetching MRF");

		 }

	    }
 
	 @Override

	    public List<Candidate> getCandidateByMrfIdVendorId(Long mrfId, Long vendorId) {

		 try {

			 MRF mrf = recruitingManagerDao.getMrfById(mrfId);

		        if (mrf == null) {

		            throw new MrfNotFoundException("MRF not found with ID: " + mrfId);

		        }

		        long mrfJdId = mrf.getMrfJd().getMrfJdId();

		        List<Candidate> candidates = recruitingManagerDao.getCandidateByMrfAndIdVendorId(mrfJdId, vendorId);

		        if (candidates.isEmpty()) {

		            throw new CandidateNotFoundException("No candidates found for MRF ID: " + mrfId + " and vendor ID: " + vendorId);

		        }

		        return candidates; 

		 }

		 catch (Exception e) {

			 throw new CustomException("Error Fetching Candidates");

		}


	    }
 
 
	
 
	@Override

	public List<MRFVendor> getAllMrfVendors() {

		List<MRFVendor>  mrfvendors = recruitingManagerDao.getAllMrfVendors();
 
		 try {

			 if(mrfvendors==null) {

				 throw new  MRFVendorRecordsNotFoundException("No MRfVendors Found ");

			 }

		        return mrfvendors;
 
		 }

		 catch (Exception e) {

			 	throw new CustomException("No Vendors Found !!Failed To fetch Data");


			 }


	}
 
	
 
 
	 @Override

	    public List<MRFVendorDto> getAllMrfVendorsRecords() {

	        try {

	            return recruitingManagerDao.getAllMrfsVendors().stream().map(mrfVendor -> {

	                MRFVendorDto mrfVendorDto = new MRFVendorDto();

	                mrfVendorDto.setMrfId(mrfVendor.getMrf().getMrfId());

	                mrfVendorDto.setRecrutingManagerId(mrfVendor.getRecruitingManager().getEmployeeId());

	                mrfVendorDto.setVendorId(mrfVendor.getVendor().getVendorId());

	                mrfVendorDto.setAssignedCount(mrfVendor.getAssignedCount());

	                mrfVendorDto.setAchievedCount(mrfVendor.getAchievedCount());

	                mrfVendorDto.setVendorAssignedStatus(mrfVendor.getVendorAssignedStatus());

	                return mrfVendorDto;

	            }).collect(Collectors.toList());

	        } catch (Exception e) {

	            logger.error("An error occurred while fetching MRF vendors: " + e.getMessage(), e);

	            throw new MRFVendorRecordsNotFoundException("An error occurred while fetching MRF vendors: " + e.getMessage());

	        }

	    }
	 
	 @Override
	    public List<MRFRecruitingManager> getAllMrfsAssignedForRM(long id) {
	        logger.info("Fetching all MRFs assigned for Recruiting Manager with ID: {}", id);
	        try {
	            List<MRFRecruitingManager> mrfList = recruitingManagerDao.getAllMrfsAssignedForRM(id);
	            if (mrfList.isEmpty()) {
	                logger.warn("No MRFs found for Recruiting Manager with ID: {}", id);
	                throw new ResourceNotFoundException("No MRFs found for Recruiting Manager with ID: " + id);
	            }
	            logger.info("Successfully fetched MRFs for Recruiting Manager with ID: {}", id);
	            return mrfList;
	        } catch (DataAccessException e) {
	            logger.error("Database error while fetching MRFs for Recruiting Manager with ID: {}", id, e);
	            throw new DataRetrievalException("Failed to fetch MRFs due to a database error.");
	        } catch (Exception e) {
	            logger.error("Unexpected error while fetching MRFs for Recruiting Manager with ID: {}", id, e);
	            throw new RuntimeException("An unexpected error occurred while fetching MRFs.");
	        }
	    }


}

 