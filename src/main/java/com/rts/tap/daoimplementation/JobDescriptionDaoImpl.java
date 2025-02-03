package com.rts.tap.daoimplementation;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rts.tap.dao.JobDescriptionDAO;
import com.rts.tap.model.JobDescription;
import com.rts.tap.model.MrfJd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class JobDescriptionDaoImpl implements JobDescriptionDAO {
	EntityManager entityManager;

	public JobDescriptionDaoImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public JobDescription saveJobDescription(JobDescription jobDescription) {
		entityManager.persist(jobDescription);
		return jobDescription;
	}

	@Override
	public JobDescription updateJobDescription(JobDescription jobDescription) {
		entityManager.merge(jobDescription);
		return jobDescription;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		Query query = entityManager.createQuery("From JobDescription");
		return query.getResultList();
	}

	@Override
	public JobDescription viewByJobDescriptionId(Long jobDescriptionId) {
		return entityManager.find(JobDescription.class, jobDescriptionId);
	}

	@Override
	public JobDescription viewByJobTitle(String JobTitle) {
		Query query = entityManager.createQuery("From JobDescription where jobTitle =: name");
		query.setParameter("name", JobTitle);
		return (JobDescription) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> viewAllJobTitles() {
		Query query = entityManager.createQuery("Select jobTitle from JobDescription");
		return query.getResultList();
	}

	@Override
	public MrfJd saveJobDescription(MrfJd mrfJd) {
		entityManager.persist(mrfJd);
		return mrfJd;
	}

	@Override
	public List<MrfJd> findByIds(Long mrfJdIds) {
		return entityManager.createQuery("SELECT m FROM MrfJd m WHERE m.id IN :ids", MrfJd.class)
				.setParameter("ids", mrfJdIds).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MrfJd> getAllMrfJd() {
		Query query = entityManager
				.createQuery("SELECT mrfJd FROM JobPosting jp " + "JOIN jp.mrf mrf " + "JOIN mrf.mrfJd mrfJd");
		return query.getResultList();
	}

	@Override
	public MrfJd viewByMrfJdId(Long mrfJdId) {
		return entityManager.find(MrfJd.class, mrfJdId);
	}

	@Override
	public MrfJd updateMrfJd(MrfJd mrfJd) {
		entityManager.merge(mrfJd);
		return mrfJd;
	}

}
