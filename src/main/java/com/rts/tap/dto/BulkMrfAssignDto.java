package com.rts.tap.dto;

import java.util.List;

public class BulkMrfAssignDto {

	long rmId;
	List<MRFRecruiterDto> mrfRecruiterDtos;
	List<MRFVendorDto> mrfVendorDtos;

	public BulkMrfAssignDto() {
		super();
	}

	public long getRmId() {
		return rmId;
	}

	public void setRmId(long rmId) {
		this.rmId = rmId;
	}

	public List<MRFRecruiterDto> getMrfRecruiterDtos() {
		return mrfRecruiterDtos;
	}

	public void setMrfRecruiterDtos(List<MRFRecruiterDto> mrfRecruiterDtos) {
		this.mrfRecruiterDtos = mrfRecruiterDtos;
	}

	public List<MRFVendorDto> getMrfVendorDtos() {
		return mrfVendorDtos;
	}

	public void setMrfVendorDtos(List<MRFVendorDto> mrfVendorDtos) {
		this.mrfVendorDtos = mrfVendorDtos;
	}

}
