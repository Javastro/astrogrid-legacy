package org.astrogrid.portal.query;

public class CriteriaInformation {

	private DataSetColumn dsColumn = null;
	private String filterType = null;
	private String value = null;
	private String joinType = null;
	private String functionValues = null;
	private CriteriaInformation linkedCriteria = null;


	public CriteriaInformation(DataSetColumn dsColumn,String filterType,String value) {
		this(dsColumn,filterType,value,null,null);
	}

	public CriteriaInformation(DataSetColumn dsColumn,String filterType,String value,String joinType) {
		this(dsColumn,filterType,value,joinType,null);
	}

	public CriteriaInformation(DataSetColumn dsColumn,String filterType,String value,String joinType,String functionValues) {
		this.dsColumn = dsColumn;
		this.filterType = filterType;
		this.value = value;
		this.joinType = joinType;
		this.functionValues = functionValues;
	}


	public void setDataSetColumn(DataSetColumn dsColumn) {
		this.dsColumn = dsColumn;
	}

	public DataSetColumn getDataSetColumn() {
		return this.dsColumn;
	}

	public void setLinkedCriteria(CriteriaInformation linkedCriteria) {
		this.linkedCriteria = linkedCriteria;
	}

	public CriteriaInformation getLinkedCriteria() {
		return this.linkedCriteria;
	}


	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterType() {
		return this.filterType;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setFunctionValues(String functionValues) {
		this.functionValues = functionValues;
	}

	public String getFunctionValues() {
		return this.functionValues;
	}


	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getJoinType() {
		return this.joinType;
	}


	public boolean equals(Object ciObj) {
		CriteriaInformation ci = (CriteriaInformation)ciObj;
		if( (this.value != null && this.value.equals(ci.getValue())) && (this.filterType != null && this.filterType.equals(ci.getFilterType())) &&
		     this.dsColumn != null && this.dsColumn.equals(ci.getDataSetColumn())) {
			return true;
		}
		return false;
	}


}
