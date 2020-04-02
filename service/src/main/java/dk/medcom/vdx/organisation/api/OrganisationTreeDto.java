package dk.medcom.vdx.organisation.api;

import java.util.List;

public class OrganisationTreeDto {

	int poolSize;
	
	String code;
	
	String name;
	
	List<OrganisationTreeDto> children;

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OrganisationTreeDto> getChildren() {
		return children;
	}

	public void setChildren(List<OrganisationTreeDto> children) {
		this.children = children;
	}
}
