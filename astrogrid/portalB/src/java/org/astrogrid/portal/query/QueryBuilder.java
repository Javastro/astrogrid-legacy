package org.astrogrid.portal.query;
import java.util.ArrayList;

/**
 * Clas Name:  QueryBuilder
 * Purpose:  To hold the necessary information for building a Query.  Which really only conists of a name associated to the
 * query and an ArrayList of DataSetInformation objects that holds all the necessary Information for the DataSet including
 * CriteriaInformation/Columns.
 * Another purpose is to printout/return the forumulated Query that is based around all these DataSet objects. 
 *
 * @author Kevin Benson
 *
 */
public class QueryBuilder {

	//ArrayList to hold DataSetInformation objects
	private ArrayList dsInformation = new ArrayList();
	//A name to associate with the Query.
	private String name = null;
	private String userName = null;
	private String community = null;

	public QueryBuilder() {

	}

	public QueryBuilder(String name) {
		this.name = name;
	}

	/**
	 * Property method for setting the name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Property method for getting the name.
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Property method for setting the name.
	 * @param name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Property method for getting the name.
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Property method for setting the name.
	 * @param name
	 */
	public void setCommunity(String community) {
		this.community = community;
	}

	/**
	 * Property method for getting the name.
	 * @return
	 */
	public String getCommunity() {
		return this.community;
	}
	

	/**
	 * Lookup in the ArrayList for a DataSetInfomation object with the given name.
	 * @param name
	 * @return
	 */
	public DataSetInformation getDataSetInformation(String name) {
		int index = dsInformation.indexOf(new DataSetInformation(name));
		if(index > -1) {
			return (DataSetInformation)dsInformation.get(index);
		}
		return null;
	}

	/**
	 * return the full ArrayList.
	 * @return
	 */
	public ArrayList getDataSetInformation() {
		return this.dsInformation;
	}

	/**
	 * Add a new DataSetInformation object to the arrayList.
	 * @param name
	 * @return
	 */
	public DataSetInformation addDataSetInformation(String name) {
		return addDataSetInformation(null,null,name);
	}

	/**
	 * No longer used.  Thought the DatasetInformation object might need to hold the servername and dbName, but currently
	 * not needed.
	 * @param serverName
	 * @param dbName
	 * @param name
	 * @return
	 */
	public DataSetInformation addDataSetInformation(String serverName,String dbName,String name) {
		return addDataSetInformation(new DataSetInformation(serverName,dbName,name));
	}

	/**
	 * The new DataSetInformation object to add to the arraylist.
	 * @param dsInfo
	 * @return
	 */
	public DataSetInformation addDataSetInformation(DataSetInformation dsInfo) {
		dsInformation.add(dsInfo);
		return dsInfo;
	}

	/**
	 * Remove a DataSetInformation objec from the ArrayList.
	 * @param name
	 */
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

	/**
	 * Delete all the DataSetInformation object from the ArrayList.  Clear them out.
	 *
	 */
	public void clear() {
		dsInformation.clear();
	}

	/**
	 * Go thorugh all the DataSetInformation objects, along with thier DataSetColumns and CriteriaInformation and formulate
	 * a general query to be returned.
	 * @return
	 */
	public String formulateQuery() {
		String selectClause = null;
		String fromClause = null;
		String whereClause = null;
		String queryString = "";

		DataSetInformation dsInfo = null;
		DataSetColumn dsColumn = null;
		CriteriaInformation ci = null;
		//for loop for the DataSetInformation objects.
		for(int i = 0;i < dsInformation.size();i++) {
			dsInfo = (DataSetInformation)dsInformation.get(i);
			if(fromClause != null && fromClause.length() > 0) {
				fromClause += ", " + dsInfo.getName();
			}else {
				fromClause = dsInfo.getName();
			}
			//for loop for the DataSetColumn objects which are the return columns the user wants to see.
			for(int j=0;j < dsInfo.getDataSetColumns().size();j++) {
				dsColumn = (DataSetColumn)dsInfo.getDataSetColumns().get(j);
				if(selectClause != null && selectClause.length() > 0) {
					selectClause += "," + dsInfo.getName() + "." + dsColumn.getName();
				}else {
					selectClause = dsInfo.getName() + "." + dsColumn.getName();
				}//else
			}//for
			//for loop for going though the CriteriaInformation object which is the where clauses.
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

	/**
	 * CriteriaInformaiton objects may hold another CriteriaInformation object in itself for parenthesis purposes.  So go through those
	 * other CriteriaInformation objects putting them inside the correct parenthesis.
	 * @param dataSetName
	 * @param ci
	 * @return
	 */
	private String  checkOtherCriteria(String dataSetName, CriteriaInformation ci) {
		if(ci.getLinkedCriteria() != null) {
			CriteriaInformation ciLinked = ci.getLinkedCriteria();
			return (ciLinked.getJoinType() + " (" + dataSetName + "." + ciLinked.getDataSetColumn().getName() + " " + ciLinked.getFilterType() + " " + ciLinked.getValue() + ") " + checkOtherCriteria(dataSetName,ciLinked));
		}
		return "";

	}

}