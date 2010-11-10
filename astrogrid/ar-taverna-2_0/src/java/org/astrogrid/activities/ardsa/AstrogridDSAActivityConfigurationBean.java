package org.astrogrid.activities.ardsa;

public class AstrogridDSAActivityConfigurationBean {
	
	private String query = null;
	
	private String ivorn = null;
	
	private String saveLocation = null;
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setIvorn(String ivorn) {
		this.ivorn = ivorn;
	}
	
	public String getIvorn() {
		return this.ivorn;
	}
	public void setSaveLocation(String saveLocation) {
		this.saveLocation = saveLocation;
	}
	
	public String getSaveLocation() {
		return this.saveLocation;
	}

}
