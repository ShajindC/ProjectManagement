package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.exception.ProjectNotFoundException;
import com.example.model.ReqHolder;
import com.example.model.Requirement;

@Service

public class RequirementService {

	@Autowired
	private MongoTemplate mongotemplate;

	/**
	 * Method to add Requirements for the Project in the Database
	 * 
	 * @param the Project id and RequirementModel is passed.
	 * @return status of the Added Requirement .
	 */

	public void addRequirement(Requirement requirement, String projectId) {

		ReqHolder req = new ReqHolder();
		req.setProjectId(projectId);

		ReqHolder reqHolder = mongotemplate.findById(projectId, ReqHolder.class);
		if (reqHolder == null) {
			ReqHolder req_ = new ReqHolder();
			req_.setProjectId(projectId);
			requirement.setRequirementId(projectId + "req" + Integer.toString(1));
			List<Requirement> reqArr = new ArrayList<>();
			reqArr.add(requirement);
			req_.setRequirement(reqArr);

			mongotemplate.save(req_);

		} else {
			List<Requirement> r = reqHolder.getRequirement();
			requirement.setRequirementId(projectId + "req" + Integer.toString((r.size() + 1)));
			r.add(requirement);
			req.setRequirement(r);
			mongotemplate.save(req);

		}

	}

	public List<ReqHolder> viewReq() {

		return mongotemplate.findAll(ReqHolder.class);

	}

	public void updateReq(Requirement requirement, String requirementId, String projectId) {

		ReqHolder reqHolder = mongotemplate.findById(projectId, ReqHolder.class);
		List<Requirement> req = reqHolder.getRequirement();

		for (Requirement r : req) {
			if (r.getRequirementId().equals(requirementId)) {
				r.setRequirementDescription(requirement.getRequirementDescription());
				r.setStatus(requirement.getStatus());
			}
		}
		reqHolder.setRequirement(req);

		mongotemplate.save(reqHolder);
	}

	/**
	 * Method to delete Requirements for the Project in the Database
	 * 
	 * @param the Requirement id and project id is passed.
	 * @return status of the Deleted Requirement .
	 * @throws ProjectNotFoundException
	 */

	public void deleteReq(String requirementId, String projectId) throws ProjectNotFoundException {
		ReqHolder reqHolder = mongotemplate.findById(projectId, ReqHolder.class);
		if (reqHolder == null) {
			throw new ProjectNotFoundException("req not found");
		}
		List<Requirement> req = reqHolder.getRequirement();

		for (Requirement r : req) {
			if (r.getRequirementId().equals(requirementId)) {
				req.remove(r);
				break;
			}
		}
		reqHolder.setRequirement(req);
		mongotemplate.save(reqHolder);

	}

}