package org.astrogrid.portal.query;

/**
 * ClassName:  CriteriaInformation
 * Purpose:  This object used by the DataSetInformation object.  Holds CriteriaInformation such as what is after the "where" in
 * a sql statement.  It holds a DataSetColumn which might be a Column/ucd/ or function, the filterType which is the EQUAL,LESSTHAN
 * the value to compare against and the jointype such as an AND/OR.
 * It also carries another CriteriaInformation object for linking/parenthesizing.
 * @author Kevin Benson
 *
 */
public class CriteriaInformation {

	private DataSetColumn dsColumn = null;
	private String filterType = null;
	private String value = null;
	private String joinType = null;
	private String functionValues = "";
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
		if(this.functionValues == null) {
			this.functionValues = "";
		}
	}

	/**
	 * Property method for setting the DataSetColumn object.
	 * @param dsColumn
	 */
	public void setDataSetColumn(DataSetColumn dsColumn) {
		this.dsColumn = dsColumn;
	}
	
	/**
	 * Property method for getting the DataSetColumn object.
	 * @return
	 */
	public DataSetColumn getDataSetColumn() {
		return this.dsColumn;
	}

	/**
	 * Property method for setting the other/linked CriteriaInformation object.
	 * @param linkedCriteria
	 */
	public void setLinkedCriteria(CriteriaInformation linkedCriteria) {
		this.linkedCriteria = linkedCriteria;
	}

	/**
	 * Property method for getting the other/linked CriteriaInformation object.
	 * @return
	 */
	public CriteriaInformation getLinkedCriteria() {
		return this.linkedCriteria;
	}


	/**
	 * Property method for setting the filter type (EQUAL,LESSTHAN,etc..)
	 * @param filterType
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	/**
	 * Property method for getting the filter type (EQUAL,etc..)
	 * @return
	 */
	public String getFilterType() {
		return this.filterType;
	}

	/**
	 * Property method for setting the value string.
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Property method for getting the value.
	 * @return
	 */
	public String getValue() {
		return this.value;
	}

	/** 
	 * Property method for setting the function values.  Comma seperated list.
	 * @param functionValues
	 */
	public void setFunctionValues(String functionValues) {
		this.functionValues = functionValues;
	}

	/**
	 * Property method for getting the function values.
	 * @return
	 */
	public String getFunctionValues() {
		return this.functionValues;
	}


	/**
	 * Property method for setting the join type (AND/OR)
	 * @param joinType
	 */
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	/**
	 * Property method for getting the join type (AND/OR)
	 * @return
	 */
	public String getJoinType() {
		return this.joinType;
	}


	/**
	 * Equals method used to be used for ArrayLists indexOf method for determining if this object equals another Criteria object.
	 */
	public boolean equals(Object ciObj) {
		CriteriaInformation ci = (CriteriaInformation)ciObj;
		if( (this.value != null && this.value.equals(ci.getValue())) && (this.filterType != null && this.filterType.equals(ci.getFilterType())) &&
		     this.dsColumn != null && this.dsColumn.equals(ci.getDataSetColumn())) {
			return true;
		}
		return false;
	}
}
