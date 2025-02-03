package com.rts.tap.dto;
 
 
public class MrfStatusDTO {
 
    private Long mrfStatusId;
    private String mrfApprovalStatus;
    private String mrfStage;
    private String mrfType;
    private String descriptionForChanges;
    private int requirementFilled;
    private String reasonForLost;
 
    public MrfStatusDTO() {
    	super();
    }
    
    public Long getMrfStatusId() {
        return mrfStatusId;
    }
 

	public void setMrfStatusId(Long mrfStatusId) {
        this.mrfStatusId = mrfStatusId;
    }
    
 
    public String getReasonForLost() {
		return reasonForLost;
	}

	public void setReasonForLost(String reasonForLost) {
		this.reasonForLost = reasonForLost;
	}

	public String getMrfApprovalStatus() {
        return mrfApprovalStatus;
    }
 
    public void setMrfApprovalStatus(String mrfApprovalStatus) {
        this.mrfApprovalStatus = mrfApprovalStatus;
    }
 
    public String getMrfStage() {
        return mrfStage;
    }
 
    public void setMrfStage(String mrfStage) {
        this.mrfStage = mrfStage;
    }
 
    public String getMrfType() {
        return mrfType;
    }
 
    public void setMrfType(String mrfType) {
        this.mrfType = mrfType;
    }
 
    public String getDescriptionForChanges() {
        return descriptionForChanges;
    }
 
    public void setDescriptionForChanges(String descriptionForChanges) {
        this.descriptionForChanges = descriptionForChanges;
    }
 
    public int getRequirementFilled() {
        return requirementFilled;
    }
 
    public void setRequirementFilled(int requirementFilled) {
        this.requirementFilled = requirementFilled;
    }
}