package org.astrogrid.portal.query;

public class DataSetColumn {
	private String name = null;
	private String type = null;
	
	
	public DataSetColumn(String name,String type) {
		this.name = name;
		this.type = type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
  	
	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}
  	
	public String getType() {
		return this.type;
	}
	
	
	public boolean equals(Object dsColumnObj) {
		DataSetColumn dsColumn = (DataSetColumn)dsColumnObj;
		if(this.name.equals(dsColumn.getName()) && this.type.equals(dsColumn.getType())) {
			return true;
		}
		return false;
	}
	
}