
package com.rts.tap.daoimplementation;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rts.tap.constants.MessageConstants;
import com.rts.tap.dao.MRFDao;
import com.rts.tap.dto.MRFRecruiterDto;
import com.rts.tap.dto.MRFVendorDto;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFAgreement;
import com.rts.tap.model.MRFCriteria;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFStatus;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.OfferApproval;
import com.rts.tap.model.RecruiterOverallPerformance;
import com.rts.tap.model.RecruiterPerformance;
import com.rts.tap.model.Requirement;
import com.rts.tap.model.Vendor;
import com.rts.tap.model.VendorPerformance;
import com.rts.tap.model.VendorPerformanceHistory;
import com.rts.tap.utils.DateUtils;


import org.slf4j.Logger;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MRFDaoImpl implements MRFDao {

	private EntityManager entityManager;
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MRFDaoImpl.class);

	public MRFDaoImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public MRF mrfSave(MRF mrf) {

		if (mrf.getRequirement().getRequirementId() == null) {
			throw new IllegalArgumentException("Requirement ID cannot be null.");
		}

		if (mrf.getBusinessUnitHead().getEmployeeId() == null) {
			throw new IllegalArgumentException("BusinessUnitHead Employee ID cannot be null.");
		}

		if (mrf.getClientPartner().getEmployeeId() == null) {
			throw new IllegalArgumentException("ClientPartner Employee ID cannot be null.");
		}

		Requirement requirement = entityManager.find(Requirement.class, mrf.getRequirement().getRequirementId());
		if (requirement == null)
			throw new IllegalArgumentException(
					"Requirement not found with ID: " + mrf.getRequirement().getRequirementId());

		Employee businessUnitHead = entityManager.find(Employee.class, mrf.getBusinessUnitHead().getEmployeeId());
		if (businessUnitHead == null)
			throw new IllegalArgumentException(
					"BusinessUnitHead not found with ID: " + mrf.getBusinessUnitHead().getEmployeeId());

		Employee clientPartner = entityManager.find(Employee.class, mrf.getClientPartner().getEmployeeId());
		if (clientPartner == null)
			throw new IllegalArgumentException(
					"ClientPartner not found with ID: " + mrf.getClientPartner().getEmployeeId());

		mrf.setRequirement(requirement);
		mrf.setBusinessUnitHead(businessUnitHead);
		mrf.setClientPartner(clientPartner);
		mrf.setCreatedAt(DateUtils.getCurrentDate());
		mrf.setUpdatedAt(DateUtils.getCurrentDate());

		entityManager.persist(mrf);
		return mrf;
	}

	@Override
	public MRF mrfUpdate(Long mrfId, MRF updatedMRF) {
		MRF existingMRF = entityManager.find(MRF.class, mrfId);
		if (existingMRF == null) {
			throw new IllegalArgumentException("MRF not found with ID: " + mrfId);
		}

		existingMRF.setMrfDepartmentName(updatedMRF.getMrfDepartmentName());

		if (updatedMRF.getRequirement() != null) {
			Requirement requirement = entityManager.find(Requirement.class,
					updatedMRF.getRequirement().getRequirementId());
			if (requirement != null) {
				existingMRF.setRequirement(requirement);
			}
		}

		if (updatedMRF.getMrfRequiredTechnology() != null) {
			existingMRF.setMrfRequiredTechnology(updatedMRF.getMrfRequiredTechnology());
		}

		if (updatedMRF.getProbableDesignation() != null) {
			existingMRF.setProbableDesignation(updatedMRF.getProbableDesignation());
		}

		if (updatedMRF.getRequiredResourceCount() >= 0) {
			existingMRF.setRequiredResourceCount(updatedMRF.getRequiredResourceCount());
		}

		if (updatedMRF.getRequiredSkills() != null) {
			existingMRF.setRequiredSkills(updatedMRF.getRequiredSkills());
		}

		MRFCriteria existingCriteria = existingMRF.getMrfCriteria();
		if (existingCriteria != null) {
			existingCriteria.setEmploymentMode(updatedMRF.getMrfCriteria().getEmploymentMode());
			existingCriteria.setEducationalQualification(updatedMRF.getMrfCriteria().getEducationalQualification());
			existingCriteria.setYearsOfExperience(updatedMRF.getMrfCriteria().getYearsOfExperience());
			existingCriteria.setMinimumCTC(updatedMRF.getMrfCriteria().getMinimumCTC());
			existingCriteria.setMaximumCTC(updatedMRF.getMrfCriteria().getMaximumCTC());
			existingCriteria.setContractStartDate(updatedMRF.getMrfCriteria().getContractStartDate());
			existingCriteria.setClosureDate(updatedMRF.getMrfCriteria().getClosureDate());
			existingCriteria.setJobLocation(updatedMRF.getMrfCriteria().getJobLocation());

			entityManager.merge(existingCriteria);
		}

		MRFStatus existingStatus = existingMRF.getMrfStatus();
		if (existingStatus != null) {
			existingStatus.setMrfApprovalStatus(updatedMRF.getMrfStatus().getMrfApprovalStatus());
			existingStatus.setDescriptionForChanges(updatedMRF.getMrfStatus().getDescriptionForChanges());
			existingStatus.setRequirementFilled(updatedMRF.getMrfStatus().getRequirementFilled());
			existingStatus.setMrfStage(updatedMRF.getMrfStatus().getMrfStage());
			existingStatus.setMrfType(updatedMRF.getMrfStatus().getMrfType());

			entityManager.merge(existingStatus);
		}

		MRFAgreement existingAgreement = existingMRF.getMrfAgreement();
		if (existingAgreement != null) {
			existingAgreement.setServiceLevelAgreement(updatedMRF.getMrfAgreement().getServiceLevelAgreement());
			existingAgreement.setBillingCycle(updatedMRF.getMrfAgreement().getBillingCycle());
			existingAgreement.setProposedBudget(updatedMRF.getMrfAgreement().getProposedBudget());
			existingAgreement.setNegotiatedPricePoint(updatedMRF.getMrfAgreement().getNegotiatedPricePoint());

			entityManager.merge(existingAgreement);
		}

		existingMRF.setUpdatedAt(DateUtils.getCurrentDate());

		entityManager.merge(existingMRF);

		return existingMRF;
	}

	@Override
	public String mrfDelete(Long mrfId) {
		MRF mrf = entityManager.find(MRF.class, mrfId);
		if (mrf != null) {
			Long criteriaId = mrf.getMrfCriteria() != null ? mrf.getMrfCriteria().getMrfCriteriaId() : null;
			Long statusId = mrf.getMrfStatus() != null ? mrf.getMrfStatus().getMrfStatusId() : null;
			Long agreementId = mrf.getMrfAgreement() != null ? mrf.getMrfAgreement().getMrfAgreementId() : null;

			if (criteriaId != null) {
				MRFCriteria criteria = entityManager.find(MRFCriteria.class, criteriaId);
				if (criteria != null) {
					entityManager.remove(criteria);
				}
			}
			if (statusId != null) {
				MRFStatus status = entityManager.find(MRFStatus.class, statusId);
				if (status != null) {
					entityManager.remove(status);
				}
			}
			if (agreementId != null) {
				MRFAgreement agreement = entityManager.find(MRFAgreement.class, agreementId);
				if (agreement != null) {
					entityManager.remove(agreement);
				}
			}

			entityManager.remove(mrf);

			return "MRF Record and Related Entities Deleted";
		}
		return "Record not Found";
	}

	@Override
	public int getRequirementFilledCount(Long requirementId) {
		String hql = "SELECT SUM(ms.requirementFilled) " + "FROM MRF m " + "JOIN m.mrfStatus ms "
				+ "WHERE m.requirement.requirementId = :requirementId";

		Query query = entityManager.createQuery(hql);
		query.setParameter("requirementId", requirementId);

		try {
			Long result = (Long) query.getSingleResult();
			return (result != null) ? result.intValue() : 0;
		} catch (NoResultException e) {
			return 0;
		}
	}

	@Override
	public MRF getMrf(long mrfId) {
		return entityManager.find(MRF.class, mrfId);
	}

	@Override
	public List<MRF> getAllMRF() {
		TypedQuery<MRF> query = entityManager.createQuery("SELECT m FROM MRF m", MRF.class);
		return query.getResultList();
	}

	@Override
	public MRF mrfCriteriaSave(MRFCriteria mrfCriteria) {
		entityManager.persist(mrfCriteria);
		return null;
	}

	@Override
	public MRF mrfAgreementSave(MRFAgreement mrfAgreement) {
		entityManager.persist(mrfAgreement);
		return null;
	}

	@Override
	public MRF mrfStatusSave(MRFStatus mrfStatus) {
		entityManager.persist(mrfStatus);
		return null;
	}

	@Override
	public MRF findById(Long mrfId) {
		return entityManager.find(MRF.class, mrfId);
	}

	@Override
	public MRFRecruitingManager saveAssignedMRFToRecruitingManager(MRFRecruitingManager mrfRecruitingManager) {
		entityManager.persist(mrfRecruitingManager);
		return mrfRecruitingManager;

	}

	@Override
	public List<Employee> getAllRecruitingManager() {
		TypedQuery<Employee> query = entityManager.createQuery(
				"SELECT e FROM Employee e JOIN e.role r WHERE r.roleName = 'Recruiting Manager'", Employee.class);
		return query.getResultList();
	}

	@Override
	public List<MRFRecruitingManager> findAssignedMRFs() {

		String jpql = "SELECT rm FROM MRFRecruitingManager rm " + "JOIN FETCH rm.mrf m "
				+ "JOIN FETCH rm.recruitingManager r " + "WHERE rm.recruitingManagerAssignedStatus = 'Assigned'";

		TypedQuery<MRFRecruitingManager> query = entityManager.createQuery(jpql, MRFRecruitingManager.class);
		return query.getResultList();
	}

	@Override
	public List<OfferApproval> findOfferApprovalsByEmployeeId(Long employeeId) {
		TypedQuery<OfferApproval> query = entityManager.createQuery(
				"SELECT oa FROM OfferApproval oa JOIN oa.approverLevel al WHERE al.employee.employeeId = :employeeId",
				OfferApproval.class);
		query.setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRF> getAllMrfsBasedOnClientPartnerId(Long employeeId) {
		String str = "SELECT m FROM MRF m WHERE m.clientPartner.employeeId = :employeeId";
		Query query = entityManager.createQuery(str);
		query.setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	@Override
	public MRFAgreement updateSOW(MRFAgreement mrfAgreement) {

		return entityManager.merge(mrfAgreement);

	}

	@Override
	public MRFRecruitingManager findAssignedMRFsToRecruitingManager(Long mrfId) {
		String jpql = "SELECT m FROM MRFRecruitingManager m WHERE m.mrf.mrfId = :mrfId";
		TypedQuery<MRFRecruitingManager> query = entityManager.createQuery(jpql, MRFRecruitingManager.class);
		query.setParameter("mrfId", mrfId);
		return query.getSingleResult();
	}

	@Override
	public List<MRFRecruiters> getRecruitersByMrfId(Long mrfId) {
		String jpql = "SELECT r FROM MRFRecruiters r WHERE r.mrfRecruitingManager.mrf.mrfId = :mrfId";
		return entityManager.createQuery(jpql, MRFRecruiters.class).setParameter("mrfId", mrfId).getResultList();
	}

	@Override
	public List<MRFVendor> getAssignedVendorsByMrfId(Long mrfId) {
		String jpql = "SELECT v FROM MRFVendor v WHERE v.mrf.mrfId = :mrfId";
		return entityManager.createQuery(jpql, MRFVendor.class).setParameter("mrfId", mrfId).getResultList();
	}

	@Override
	public MRFStatus MrfBuStatus(MRFStatus mrfstatus) {
		return entityManager.merge(mrfstatus);
	}

	@Override
	public MRFStatus findStatusById(Long mrfId) {
		return entityManager.find(MRFStatus.class, mrfId);
	}

	@Override
	public List<MRF> findByEmployeeId(String employeeId) {
		String jpql = "SELECT m FROM MRF m WHERE m.businessUnitHead.employeeId = :employeeId";
		TypedQuery<MRF> query = entityManager.createQuery(jpql, MRF.class);
		query.setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	public MRFCriteria getMrfByMrfJdId(Long mrfJdId) {
		System.out.println("Fetching MRF for mrfJdId: " + mrfJdId);
		String hql = "SELECT m.mrfId FROM MRF m JOIN m.mrfJd jd WHERE jd.mrfJdId = :mrfJdId";
		Long mrfId = (Long) entityManager.createQuery(hql).setParameter("mrfJdId", mrfJdId).getSingleResult();

		String hq = "SELECT mc FROM MRF mrf JOIN mrf.mrfCriteria mc WHERE mrf.mrfId =: mrfId";

		MRFCriteria criteria = (MRFCriteria) entityManager.createQuery(hq).setParameter("mrfId", mrfId)
				.getSingleResult();

		return criteria;

	}

	@Override
	public List<Employee> getRecruitingManagerByClientPartnerId(Long clientPartnerId) {
		String jpql = "SELECT e FROM Employee e WHERE e.managerId = :clientPartnerId AND e.role.roleName = 'Recruiting Manager'";

		TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
		query.setParameter("clientPartnerId", clientPartnerId);
		return query.getResultList();
	}

	@Override
	public List<Vendor> getVendorsByRecruitingManagerId(Long recruitingManagerId) {
		String jpql = "SELECT mv.vendor FROM MRFVendor mv WHERE mv.recruitingManager.id = :recruitingManagerId";
		TypedQuery<Vendor> query = entityManager.createQuery(jpql, Vendor.class);
		query.setParameter("recruitingManagerId", recruitingManagerId);
		return query.getResultList();
	}

	@Override
	public List<Employee> getRecruitersByRecruitingManagerId(Long recruitingManagerId) {
		String jpql = "SELECT e FROM Employee e WHERE e.managerId = :recruitingManagerId AND e.role.roleName = 'Recruiter'";

		TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
		query.setParameter("recruitingManagerId", recruitingManagerId);
		return query.getResultList();
	}

	@Override
	public String assignMrfToVendor(MRFVendorDto mrfVendorDto) {
		String mrfStatus = "";
		try {
			MRF mrf = getMrfById(mrfVendorDto.getMrfId());
			Vendor vendor = findByIdForVendor(mrfVendorDto.getVendorId());
			Employee rm = getEmployeeById(mrfVendorDto.getRecrutingManagerId());
			MRFVendor mrfVendor = new MRFVendor();
			String status = mrf.getMrfStatus().getMrfStage();
			if (status.equals("Pending")) {
				mrf.getMrfStatus().setMrfStage("Assigned");
			}
			mrfVendor.setMrf(mrf);
			mrfVendor.setRecruitingManager(rm);
			mrfVendor.setVendor(vendor);
			mrfVendor.setAssignedCount(mrfVendorDto.getAssignedCount());
			mrfVendor.setAssignedDate(new Date());
			mrfVendor.setVendorAssignedStatus(MessageConstants.RECRUITING_MANAGER_MRF_STATUS_ASSIGNED);
			entityManager.persist(mrfVendor);

			// create new record for vendor performance as history
			VendorPerformanceHistory vendorPerformanceHistory = new VendorPerformanceHistory();
			vendorPerformanceHistory.setVendor(vendor);
			vendorPerformanceHistory.setMrf(mrf);

			VendorPerformance vp = getVendorPerformanceRecord(vendor.getVendorId());
			if (vp == null) {
				// create new Vendor performance for overall performance
				VendorPerformance vendorPerformance = new VendorPerformance();
				vendorPerformance.setVendor(vendor);
				entityManager.persist(vendorPerformance);
			}
			entityManager.persist(vendorPerformanceHistory);
			mrfStatus = MessageConstants.RECRUITING_MANAGER_ASSIGNED_MRF_SUCCESS;
		} catch (Exception e) {
			System.err.println(e);
			logger.error(e.getMessage());
			mrfStatus = MessageConstants.RECRUITING_MANAGER_ASSIGNED_MRF_FAILED;
		}
		return mrfStatus;
	}
	
	@Override
	public Vendor findByIdForVendor(Long id) {
		return entityManager.find(Vendor.class, id);
	}

	VendorPerformance getVendorPerformanceRecord(long id) {
		try {
			String getVendorPerformanceHql = "SELECT vp FROM VendorPerformance vp WHERE vp.vendor.vendorId = :id";
			Query query = entityManager.createQuery(getVendorPerformanceHql).setParameter("id", id);
			VendorPerformance vp = (VendorPerformance) query.getSingleResult();
			return vp;
		} catch (Exception e) {
			System.out.println(e);
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public MRF getMrfById(long id) {
		return entityManager.find(MRF.class, id);
	}

	@Override
	public Employee getEmployeeById(long id) {
		return entityManager.find(Employee.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfVendors() {
		String getAllMrfVendor = "SELECT mrfvendors FROM MRFVendor mrfvendors ";
		Query query = entityManager.createQuery(getAllMrfVendor);
		List<MRFVendor> rp = query.getResultList();
		return rp != null ? rp : Collections.emptyList();
	}

	@Override
	public String assignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto) {
		String mrfStatus = "";
		try {
			// assigning MRF's to Recruiter
			MRFRecruitingManager mrfrm = getMrfRecruitingManagerById(mrfRecruiterDto.getMrfRecruitingManagerId());
			Employee recruiter = getEmployeeById(mrfRecruiterDto.getRecruiterId());
			mrfrm.getMrf().getMrfStatus().setMrfStage("Assigned");
			MRFRecruiters mrfrecruiter = new MRFRecruiters();
			mrfrecruiter.setRecruiterId(recruiter);
			mrfrecruiter.setMrfRecruitingManager(mrfrm);
			mrfrecruiter.setCreatedAt(mrfRecruiterDto.getCreatedAt());
			mrfrecruiter.setUpdatedAt(mrfRecruiterDto.getUpdatedAt());
			mrfrecruiter.setClosedDate(mrfRecruiterDto.getClosedDate());
			mrfrecruiter.setAssignedCount(mrfRecruiterDto.getAssignedCount());
			mrfrecruiter.setRecruiterAssignedStatus(MessageConstants.RECRUITER_MRF_STATUS_ASSIGNED);
			entityManager.persist(mrfrecruiter);

			// create recruiter performance history
			RecruiterPerformance rp = new RecruiterPerformance();
			rp.setAssignedDate(LocalDateTime.now());
			rp.setUpdatedDate(LocalDateTime.now());
			rp.setRecruiter(recruiter);
			rp.setMrf(mrfrm.getMrf());
			entityManager.persist(rp);

			RecruiterOverallPerformance rop = getRecruiterOverallPerformance(recruiter.getEmployeeId());
			if (rop == null) {
				rop = new RecruiterOverallPerformance();
				rop.setRecruiter(recruiter);
				entityManager.persist(rop);
			}

			mrfStatus = MessageConstants.RECRUITER_MRF_STATUS_ASSIGNED;
		} catch (Exception e) {
			logger.error(e.getMessage());
			mrfStatus = MessageConstants.RECRUITER_ASSIGNED_MRF_FAILED;
		}
		return mrfStatus;
	}

	RecruiterOverallPerformance getRecruiterOverallPerformance(long recId) {
		try {
			String getRecruiterPerformanceHql = "SELECT rop FROM RecruiterOverallPerformance rop WHERE rop.recruiter.employeeId = :id";
			Query query = entityManager.createQuery(getRecruiterPerformanceHql).setParameter("id", recId);
			RecruiterOverallPerformance rop = (RecruiterOverallPerformance) query.getSingleResult();
			return rop;
		} catch (Exception e) {
			System.out.println(e);
			logger.error(e.getMessage());
			return null;
		}
	}

	public MRFRecruitingManager getMrfRecruitingManagerById(long id) {
		return entityManager.find(MRFRecruitingManager.class, id);
	}

	@Override
	public String reassignMrfToRecruiter(MRFRecruiterDto mrfRecruiterDto) {
		String mrfStatus = "";
		try {
			Employee newRecruiter = getEmployeeById(mrfRecruiterDto.getRecruiterId());

			MRFRecruiters existingMrfrecruiter = findMrfRecruiterByMrfId(mrfRecruiterDto.getMrfRecruitingManagerId());

			if (existingMrfrecruiter != null) {
				existingMrfrecruiter.setRecruiterId(newRecruiter);
				existingMrfrecruiter.setUpdatedAt(mrfRecruiterDto.getUpdatedAt());
				existingMrfrecruiter.setClosedDate(mrfRecruiterDto.getClosedDate());
				existingMrfrecruiter.setRecruiterAssignedStatus(MessageConstants.RECRUITER_MRF_STATUS_REASSIGNED);

				entityManager.merge(existingMrfrecruiter);
				mrfStatus = MessageConstants.RECRUITER_MRF_STATUS_REASSIGNED;
			} else {
				mrfStatus = MessageConstants.RECRUITER_ASSIGNED_MRF_FAILED;
			}
		} catch (Exception e) {
			System.err.println(e);
			logger.error(e.getMessage());
			mrfStatus = MessageConstants.RECRUITER_ASSIGNED_MRF_FAILED;
		}
		return mrfStatus;
	}

	@Override
	public MRFRecruiters findMrfRecruiterByMrfId(long mrfRecruitingManagerId) {
		try {
			return entityManager.createQuery(
					"SELECT mr FROM MRFRecruiters mr WHERE mr.mrfRecruitingManager.id = :mrfRecruitingManagerId",
					MRFRecruiters.class).setParameter("mrfRecruitingManagerId", mrfRecruitingManagerId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			System.err.println(e);
			logger.error(e.getMessage());
			return null; 
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruiters> getAllMrfsForRecruiterId(long id) {
		String hql = "SELECT mrfs FROM MRFRecruiters mrfs WHERE mrfs.recruiterId.employeeId = :id";
		Query query = entityManager.createQuery(hql).setParameter("id", id);
		List<MRFRecruiters> results = query.getResultList();
		return results != null ? results : Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfsForVendor(long vendorId) {
		String hql = "SELECT mrfs FROM MRFVendor mrfs WHERE mrfs.vendor.vendorId = :vendorId";
		Query query = entityManager.createQuery(hql).setParameter("vendorId", vendorId);
		List<MRFVendor> results = query.getResultList();
		return results != null ? results : Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfsVendorsAssignedByRM(long employeeId) {
		String hql = "SELECT mrfVen FROM MRFVendor mrfVen where mrfVen.recruitingManager.employeeId = :employeeId";
		Query query = entityManager.createQuery(hql).setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruiters> getAllMrfsRecruitersAssignedByRM(long employeeId) {
		String hql = "SELECT mrfRec FROM MRFRecruiters mrfRec where mrfRec.mrfRecruitingManager.recruitingManager.employeeId = :employeeId";
		Query query = entityManager.createQuery(hql).setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruiters> getAllAssignedMrfsForAllRecruiter(long id) {
		String getAllAssignedMrfsHql = "SELECT mrfRec FROM MRFRecruiters mrfRec WHERE mrfRec.mrfRecruitingManager.recruitingManager.employeeId = :rmId";
		Query query = entityManager.createQuery(getAllAssignedMrfsHql).setParameter("rmId", id);
		List<MRFRecruiters> mrfRecs = query.getResultList();
		return mrfRecs != null ? mrfRecs : Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllAssignedMrfsForAllVendors() {
		String getAllAssignedMrfsHql = "SELECT mrfVen FROM MRFVendor mrfVen";
		Query query = entityManager.createQuery(getAllAssignedMrfsHql);
		List<MRFVendor> mrfVen = query.getResultList();
		return mrfVen != null ? mrfVen : Collections.emptyList();
	}

	@Override
	public MRFRecruitingManager fetchMrfRecruitingManagerByMrfIdAndmManagerId(long mrfId, long rmId) {
		try {
			String getMrfRecruitingManagerHql = "SELECT mrfRm FROM MRFRecruitingManager mrfRm WHERE mrfRm.mrf.mrfId = :mrfId AND mrfRm.recruitingManager.employeeId = :rmId";
			Query query = entityManager.createQuery(getMrfRecruitingManagerHql).setParameter("mrfId", mrfId)
					.setParameter("rmId", rmId);
			return (MRFRecruitingManager) query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfsVendors() {
		String hql = "SELECT mrfVen FROM MRFVendor mrfVen";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

}
