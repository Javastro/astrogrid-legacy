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

	public void removeCriteriaInformation(String columnName,String type,String filterType,String value,int link) {
		CriteriaInformation ci = (CriteriaInformation)criteriaInfo.get(link);
		CriteriaInformation rem = new CriteriaInformation(new DataSetColumn(columnName,type),filterType,value);
		if(rem.equals(ci)) {
			if(ci.getLinkedCriteria() != null) {
				criteriaInfo.set(link,ci.getLinkedCriteria());
			}else {
				criteriaInfo.remove(link);
			}
		}
		findAndRemoveCriteria(ci,ci,rem);
	}

	private void findAndRemoveCriteria(CriteriaInformation parent,CriteriaInformation src,CriteriaInformation target) {
		boolean found = false;
		if(target.equals(src)) {
		  found = true;
		}
		if(found) {
			if(src.getLinkedCriteria() != null) {
				parent.setLinkedCriteria(src.getLinkedCriteria());
			}else {
				parent.setLinkedCriteria(null);
			}
			return;
		}

		if(src.getLinkedCriteria() != null) {
			findAndRemoveCriteria(src,src.getLinkedCriteria(),target);
		}
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