package org.astrogrid.portal.query;
import java.util.ArrayList;

/**
 * Class Name: DataSetInformaiton
 * Purpose: This is sort of the main part of building a query because it holds all the necessary information associated
 * to a DataSet including Return Columns/UCDS which is stored in an ArrayList of DataSetColumn objects and also Criteria
 * that deals with the "where" part of the query.  So it holds an ArrayList of CriteriaInformation objects.
 * @author Kevin Benson
 *
 */
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
	
	public void setDataSetColumns(ArrayList dataSetColumns) {
		this.dataSetColumns = dataSetColumns;
	}
	
	public ArrayList getDataSetColumns() {
		return this.dataSetColumns;
	}

	/**
	 * add a new DataSetColumn with the Default type of Column.
	 * @param columnName
	 * @return
	 */
	public DataSetColumn addDataSetColumn(String columnName) {
		return addDataSetColumn(columnName,"COLUMN");
	}

	/**
	 * add a DataSetColumn object
	 * @param columnName
	 * @param type
	 * @return
	 */
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


	public CriteriaInformation addCriteriaInformation(String columnName,String filterType,String value,String joinType,String functionValues) {
		return addCriteriaInformation(columnName,null,filterType,value,joinType,null);
	}

	public CriteriaInformation addCriteriaInformation(String columnName,String type,String filterType,String value,String joinType,String functionValues) {
		return addCriteriaInformation(new DataSetColumn(columnName,type),filterType,value,joinType,functionValues);
	}

	public CriteriaInformation addCriteriaInformation(DataSetColumn dsColumn,String filterType,String value,String joinType,String functionValues) {
		CriteriaInformation ci = new CriteriaInformation(dsColumn,filterType,value,joinType,functionValues);
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