package org.astrogrid.portal.query;

import java.util.ArrayList;

public class DataSetInformation {

	private String name = null;

	private String serverName = null;

	private String dbName = null;

	private String otherInfo = null;

	private ArrayList dataSetColumns = new ArrayList();

	private ArrayList criteriaInfo = new ArrayList();

	public DataSetInformation(String name) {
  		this.name=name;
  	}

	public DataSetInformation(String serverName, String dbName, String name) {
		this.name=name;
		this.serverName=serverName;
		this.dbName=dbName;
	}

  	public void setName(String name) {
  		this.name = name;
  	}

  	public String getName() {
  		return this.name;
  	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbName() {
		return this.dbName;
	}

	public DataSetColumn addDataSetColumn(String columnName) {
		return addDataSetColumn(columnName,"");
	}

  	public DataSetColumn addDataSetColumn(String columnName,String type) {
  		return addDataSetColumn(new DataSetColumn(columnName,type));
  	}

	public DataSetColumn addDataSetColumn(DataSetColumn dsColumn) {
		dataSetColumns.add(dsColumn);
		return dsColumn;
	}

	public void removeDataSetColumn(String columnName) {
		removeDataSetColumn(new DataSetColumn(columnName,null));
	}

  	public void removeDataSetColumn(String columnName,String type) {
  		removeDataSetColumn(new DataSetColumn(columnName,type));
  	}

  	public void removeDataSetColumn(DataSetColumn dsColumn) {
		int i = -1;
		if( (i = dataSetColumns.indexOf(dsColumn)) > -1) {
  			dataSetColumns.remove(i);
		}
  	}

  	public ArrayList getDataSetColumns(){
  		return this.dataSetColumns;
  	}

	public CriteriaInformation addCriteriaInformation(String columnName,String filterType,String value) {
		return addCriteriaInformation(columnName,null,filterType,value);
	}

	public CriteriaInformation addCriteriaInformation(String columnName,String type,String filterType,String value) {
		return addCriteriaInformation(new DataSetColumn(columnName,type),filterType,value);
	}

	public CriteriaInformation addCriteriaInformation(DataSetColumn dsColumn,String filterType,String value) {
		CriteriaInformation ci = new CriteriaInformation(dsColumn,filterType,value);
		criteriaInfo.add(ci);
		return ci;
	}

	public void removeCriteriaInformation(String columnName,String filterType,String value) {
		removeCriteriaInformation(columnName,null,filterType,value);
	}

	public void removeCriteriaInformation(String columnName,String type,String filterType,String value) {
		removeCriteriaInformation(new DataSetColumn(columnName,type),filterType,value);
	}

	public void removeCriteriaInformation(DataSetColumn dsColumn,String filterType,String value) {
		CriteriaInformation ci = new CriteriaInformation(dsColumn,filterType,value);
		int i = -1;
		if ( (i = criteriaInfo.indexOf(ci)) > -1) {
			criteriaInfo.remove(i);
		}
	}

	public ArrayList getCriteriaInformation(){
		return this.criteriaInfo;
	}

	public boolean equals(Object dsInfoObj) {
		DataSetInformation dsInfo = (DataSetInformation)dsInfoObj;
		if(this.name.equals(dsInfo.getName())) {
			return true;
		}
		return false;
	}

}

class CriteriaInformation {

	private DataSetColumn dsColumn = null;
	private String filterType = null;
	private String value = null;


	public CriteriaInformation(DataSetColumn dsColumn,String filterType,String value) {
		this.dsColumn = dsColumn;
		this.filterType = filterType;
		this.value = value;
	}

	public void setDataSetColumn(DataSetColumn dsColumn) {
		this.dsColumn = dsColumn;
	}

	public DataSetColumn getDataSetColumn() {
		return this.dsColumn;
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

	public boolean equals(Object ciObj) {
		CriteriaInformation ci = (CriteriaInformation)ciObj;
		if( (this.value != null && this.value.equals(ci.getValue())) && (this.filterType != null && this.filterType.equals(ci.getFilterType())) &&
		     this.dsColumn != null && this.dsColumn.equals(ci.getDataSetColumn())) {
			return true;
		}
		return false;
	}


}
