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

	public ArrayList getDataSetInformation() {
		return this.dsInformation;
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
		int i = -1;
		if( (i = dsInformation.indexOf(dsInfo)) > -1 ) {
			dsInformation.remove(i);
		}
	}

	public void clear() {
		dsInformation.clear();
	}

	public String formulateQuery() {
		String selectClause = null;
		String fromClause = null;
		String whereClause = null;
		String queryString = "";

		DataSetInformation dsInfo = null;
		DataSetColumn dsColumn = null;
		CriteriaInformation ci = null;
		for(int i = 0;i < dsInformation.size();i++) {
			dsInfo = (DataSetInformation)dsInformation.get(i);
			if(fromClause != null && fromClause.length() > 0) {
				fromClause += ", " + dsInfo.getName();
			}else {
				fromClause = dsInfo.getName();
			}

			for(int j=0;j < dsInfo.getDataSetColumns().size();j++) {
				dsColumn = (DataSetColumn)dsInfo.getDataSetColumns().get(j);
				if(selectClause != null && selectClause.length() > 0) {
					selectClause += "," + dsInfo.getName() + "." + dsColumn.getName();
				}else {
					selectClause = dsInfo.getName() + "." + dsColumn.getName();
				}//else
			}//for
			for(int j=0;j < dsInfo.getCriteriaInformation().size();j++) {
				ci = (CriteriaInformation)dsInfo.getCriteriaInformation().get(j);
				if(whereClause != null && whereClause.length() > 0) {
					if(ci.getLinkedCriteria() != null) {
						whereClause += ci.getJoinType() + " {" + j + "}((" + dsInfo.getName() + "." + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") " + checkOtherCriteria(dsInfo.getName(),ci) + ") ";
					}else {
						whereClause += ci.getJoinType() + " {" + j + "}(" + dsInfo.getName() + "." + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") ";
					}
				}else {
					if(ci.getLinkedCriteria() != null) {
						whereClause = " {" + j + "}((" + dsInfo.getName() + "." + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") " + checkOtherCriteria(dsInfo.getName(),ci) + ") ";
					}else {
						whereClause = " {" + j + "}(" + dsInfo.getName() + "." + ci.getDataSetColumn().getName() + " " + ci.getFilterType() + " " + ci.getValue() + ") ";
					}
				}//else
			}//for
		}//for
		if(selectClause != null && selectClause.length() > 0 && fromClause != null && fromClause.length() > 0) {
			queryString = "SELECT " + selectClause + " FROM " + fromClause;
			if(whereClause != null && whereClause.length() > 0) {
				 queryString += " WHERE " + whereClause;
			}
		}
		return queryString + "\n";
	}//formulateQuery

	private String  checkOtherCriteria(String dataSetName, CriteriaInformation ci) {
		if(ci.getLinkedCriteria() != null) {
			CriteriaInformation ciLinked = ci.getLinkedCriteria();
			return (ciLinked.getJoinType() + " (" + dataSetName + "." + ciLinked.getDataSetColumn().getName() + " " + ciLinked.getFilterType() + " " + ciLinked.getValue() + ") " + checkOtherCriteria(dataSetName,ciLinked));
		}
		return "";

	}

}