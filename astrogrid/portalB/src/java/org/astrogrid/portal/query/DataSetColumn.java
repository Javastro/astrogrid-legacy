package org.astrogrid.portal.query;

public class DataSetColumn {
	private String name = null;
	private String type = null;

	public final String OTHER_TYPE = "other";
	public final String COLUMN_TYPE = "column";
	public final String UCD_TYPE = "ucd";
	public final String FUNCTION_TYPE = "function";

	public DataSetColumn(String name,String type) {
		this.name = name;
		this.type = type;
		if(this.type == null){this.type="";}
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
		if( (this.name != null && this.name.equals(dsColumn.getName()))  ) {
			return true;
		}
		return false;
	}

}