package org.astrogrid.portal.query;

import java.util.ArrayList;

public class QueryBuilder {

	private ArrayList dsInformation = new ArrayList();
	private String name = null;
	
	public QueryBuilder() {
		
	}
	
	public QueryBuilder(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public DataSetInformation getDataSetInformation(String name) {
		int index = dsInformation.indexOf(new DataSetInformation(name));
		if(index > -1) {
			return (DataSetInformation)dsInformation.get(index);	
		}
		return null;
	}
	
	public DataSetInformation addDataSetInformation(String name) {
		return addDataSetInformation(null,null,name);
	}
	
	public DataSetInformation addDataSetInformation(String serverName,String dbName,String name) {
		return addDataSetInformation(new DataSetInformation(serverName,dbName,name));
	}
	
	public DataSetInformation addDataSetInformation(DataSetInformation dsInfo) {
		dsInformation.add(dsInfo);
		return dsInfo;
	}
	
	public void removeDataSetInformation(String name) {
		removeDataSetInformation(null,null,name);
	}
	
	public void removeDataSetInformation(String serverName,String dbName, String name) {
		removeDataSetInformation(new DataSetInformation(serverName,dbName,name));	
	}
	
	public void removeDataSetInformation(DataSetInformation dsInfo) {
		dsInformation.remove(dsInformation.indexOf(dsInfo));	
	}
	
	public void clear() {
		dsInformation.clear();	
	}
	
	public String formulateQuery() {
		String selectClause = null;
		String fromClause = null;
		String whereClause = null;
		
		
		DataSetInformation dsInfo = null;
		DataSetColumn dsColumn = null;
		CriteriaInformation ci = null;
		for(int i = 0;i < dsInformation.size();i++) {
			dsInfo = (DataSetInformation)dsInformation.get(i);
			if(fromClause.length() > 0) {
				fromClause += ", " + dsInfo.getName();
			}else {
				fromClause = dsInfo.getName();
			}
			
			for(int j=0;j < dsInfo.getDataSetColumns().size();j++) {
				dsColumn = (DataSetColumn)dsInfo.getDataSetColumns().get(j);
				if(selectClause.length() > 0) {
					selectClause += "," + dsColumn.getName();				
				}else {
					selectClause = dsColumn.getName();
				}//else
			}//for
			for(int j=0;j < dsInfo.getCriteriaInformation().size();j++) {
				ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(j);
				if(whereClause.length() > 0) {
					whereClause += ", (" + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") ";				
				}else {
					whereClause = ", (" + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") ";
				}//else
			}//for
		}//for
		return "SELECT " + selectClause + " " + fromClause + " " + whereClause;
	}//formulateQuery
	
}