package com.rts.tap.daoimplementation;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rts.tap.dao.RecruitingManagerDao;
import com.rts.tap.dto.MrfStatusDTO;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.Employee;
import com.rts.tap.model.MRF;
import com.rts.tap.model.MRFRecruiters;
import com.rts.tap.model.MRFRecruitingManager;
import com.rts.tap.model.MRFVendor;
import com.rts.tap.model.RecruiterOverallPerformance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * author: Jeevarajan, Vashanth version: v5.0 updated at: 29-11-2024
 **/

@Repository
@Transactional
public class RecruitingManagerDaoImplementation implements RecruitingManagerDao {

	EntityManager entityManager;

	private static final Logger logger = LoggerFactory.getLogger(RecruitingManagerDaoImplementation.class);

	public RecruitingManagerDaoImplementation(EntityManager entityManager
		
			) {
		super();
		this.entityManager = entityManager;
	}
	/**
	 * this method will accept recruiting manager id and perform fetch operation and
	 * return all Mrf's assigned for Recruiting manager
	 * 
	 * @param(recruiting manager's id) - long
	 * @return list of MrfRecruiting Manager's - object
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruitingManager> getAllMrfsAssignedForRM(long id) {
		String hql = "SELECT mrfs FROM MRFRecruitingManager mrfs WHERE recruitingManager.id = :id";
		Query query = entityManager.createQuery(hql).setParameter("id", id);
		List<MRFRecruitingManager> results = query.getResultList();
		return results != null ? results : Collections.emptyList();
	}

	/**
	 * this method will accept Mrf's id and perform fetch operation and return MRF
	 * as object
	 * 
	 * @param(Mrf's id) - long
	 * @return MRF - object
	 **/
	@Override
	public MRF getMrfById(long id) {
		return entityManager.find(MRF.class, id);
	}

	/**
	 * this method will perform fetch operation of all Mrf assigned to vendors and
	 * return MRFVendor as object
	 * 
	 * @return list of MRFVendor - object
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfsVendors() {
		String hql = "SELECT mrfVen FROM MRFVendor mrfVen";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruiters> getAllRecruitersAssigned() {
		String hql = "SELECT recruiters FROM MRFRecruiters recruiters";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	public MRFRecruitingManager getMrfRecruitingManagerById(long id) {
		return entityManager.find(MRFRecruitingManager.class, id);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllRecruitersByRecruitingManagerId(long id) {
		String roleName= "Recruiter";
		String hql = "SELECT recruiters FROM Employee recruiters WHERE recruiters.managerId = :id AND recruiters.role.roleName= :roleName ";
		Query query = entityManager.createQuery(hql).setParameter("id", id );
		query.setParameter("roleName", roleName);
		List<Employee> results = query.getResultList();
		return results != null ? results : Collections.emptyList();
	}

	@Override
	public void updateMrfStage(MrfStatusDTO mrfStage, Long mrfId) {
		MRF mrf = getMrfById(mrfId);
		String status = mrfStage.getMrfStage();
		String reason = mrfStage.getReasonForLost();
		mrf.getMrfStatus().setMrfStage(status);
		mrf.getMrfStatus().setReasonForLost(reason);
		entityManager.merge(mrf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFRecruiters> getAssignedRecruitersByMrfRecruitingManagerId(long id) {
		String hql = "SELECT recruiters FROM MRFRecruiters recruiters WHERE recruiters.mrfRecruitingManager.mrfRecruitingManagerId = :id";
		Query query = entityManager.createQuery(hql).setParameter("id", id);
		List<MRFRecruiters> results = query.getResultList();
		return results != null ? results : Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllVendorsAssignedForMrf(long mrfId) {
		String hql = "SELECT vendors FROM MRFVendor vendors WHERE vendors.mrf.mrfId = :mrfId";
		Query query = entityManager.createQuery(hql).setParameter("mrfId", mrfId);
		List<MRFVendor> results = query.getResultList();
		return results != null ? results : Collections.emptyList();

	}

	@Override
	public List<MRFRecruiters> getRecruitersByMrfId(Long mrfId) {
		TypedQuery<MRFRecruiters> query = entityManager.createQuery(
				"SELECT r FROM MRFRecruiters r WHERE r.mrfRecruitingManager.mrf.mrfId = :mrfId", MRFRecruiters.class);
		query.setParameter("mrfId", mrfId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Candidate> getListOfCandidateByMrfId(Long mrfId) {
	    String hql = "select c from Candidate c join c.MrfJd m where m.mrfJdId IN (SELECT mrf.mrfJd.mrfJdId FROM MRF mrf WHERE mrf.mrfId = :mrfId)";
	    return entityManager.createQuery(hql).setParameter("mrfId", mrfId).getResultList();
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
	public List<Candidate> getCandidateByMrfAndIdVendorId(Long mrfId, Long vendorId) {
	    String hql = "select c from Candidate c join c.MrfJd m where c.source = 'VENDOR' and c.sourceId = :vendorId and m.mrfJdId = :mrfId";
	    Query q = entityManager.createQuery(hql);
	    q.setParameter("vendorId", vendorId);
	    q.setParameter("mrfId", mrfId);
	    return (List<Candidate>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MRFVendor> getAllMrfVendors() {
		String getAllMrfVendor = "SELECT mrfvendors FROM MRFVendor mrfvendors ";
		Query query = entityManager.createQuery(getAllMrfVendor);
		List<MRFVendor> rp = query.getResultList();
		return rp != null ? rp : Collections.emptyList();
	}

}
