
package org.astrogrid.portal.cocoon.query;

import org.w3c.dom.*;
import java.util.*;

import java.net.URL;
import org.apache.axis.client.Call;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException ;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.lang.StringBuffer;
import java.util.Map;
import java.util.HashMap;
import org.astrogrid.portal.query.*;
import java.io.IOException;
import java.io.InputStream;


/**
 *
 * Class Name: DataQueryServlet
 * Purpose:  Main purpose to interact with DataQuery.jsp and give it all the necessary information for interacting with the user.
 * Also to take request from the DataQuery.jsp and act on those requests.
 * The types of request submitted are:
 * 1.) Send a Query - Which means sending a query to the JobController webservice.
 * 2.) Add Selection - Which means adding return columns/UCD's and Data Sets into the query
 * 3.) Remove Selection - Which means remove a column/UCD from the query
 * 4.) Add Criteria - Which means to add criteria to the "where" clause.
 * 5.) Remove Criteria - Which means to remove criteria from the where clause
 * 
 * Finally for initiation purposes the servlet will call the Registry WebService for getting all the DataSet names and their
 * corresponding columns and ucd's.  This will refreash after 5 hours in case the registry is changed.
 * 
 * @see org.astrogrid.portal.generated.jobcontroller.client
 * @see org.astrogrid.portal.generated.registry.client * 
 * @author Kevin Benson
 *
 */
public class QueryAction extends AbstractAction
{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = false ;
	
	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String USER_PARAM_NAME = "user-param" ;
	
	private static final String ACTION_ADD_SELECTION = "AddSelection";
	
	private static final String ACTION_REMOVE_SELECTION = "RemoveSelection";
	
	private static final String ACTION_ADD_CRITERIA = "AddCriteria";	
	
	private static final String ACTION_REMOVE_CRITERIA = "RemoveCriteria";
	
	private static final String ACTION_CLEAR_QUERY = "ClearQuery";
	
	private static final String ACTION_SUBMIT_QUERY = "SubmitQuery";
	
	
	
	private static final String LINK_TO = "LinkTo" ;
	
	private static final String DATASET_NAME = "DataSetName";
	
	private static final String RETURN_COLUMN = "ReturnColumn";
	
	private static final String FILTER_COLUMN = "FilterColumn";
	
	private static final String DATASET_NAME_CRITERIA = "DataSetNameCriteria";
	
	private static final String OPERATOR = "Operator";
	
	private static final String SPLIT_MARKER = "-";
	
	private static final String FUNCTION_VALUES = "FunctionValues";
	
	private static final String VALUE = "Value";
	
	private static final String JOIN_TYPE = "JoinType";
	
	private static final String DS_AGENT = "DataSetAgent";	
	
	
	private static final String CONFIG_FILENAME = "ASTROGRID_DataQueryPortal.properties";

	private static final String JES_URL_PROPERTY = "ASTROGRID.JES.URL";
	
	private static final String JES_NS_PROPERTY = "ASTROGRID.JES.NS";	

	private static final String JES_METHODCALL_PROPERTY = "ASTROGRID.JES.METHODCALL";		
	
	private static final String REGISTRY_URL_PROPERTY = "ASTROGRID.REGISTRY.URL";	
	
	private static final String REGISTRY_NS_PROPERTY = "ASTROGRID.REGISTRY.NS";	
	
	private static final String REGISTRY_METHODCALL_PROPERTY = "ASTROGRID.REGISTRY.METHODCALL";
	
	private static final String DEBUG_FLAG_PROPERTY = "DEBUG.FLAG";
	
	private static final String DS_URL_PROPERTY = "ASTROGRID.DS.URL";
	
	private static final String DS_NAME_PROPERTY = "ASTROGRID.DS.NAME";
	
	private static final String REQUEST_CRITERIA_NUMBER = "CriteriaNumber";
	
	private static final String REQUEST_QUERY_STRING_SENT = "QueryStringSent";
	
	private static final String MAP_QUERY_STRING = "QueryString";
	
	private static final String MAP_LAST_CALL_XML = "LastWebServiceXML";
	
	private static final String MAP_ERROR_MESSAGE = "ErrorMessage";		
	
	private static final String SESSION_QUERY_BUILDER = "QueryBuilder";
	
	private static final String REQUEST_DATASET_LIST = "DataSetArrayList";	
	
	private static final String DS_AGENT_LIST = "DataSetAgents";
	
	private String registryEndPoint = null;
	private String registryNS = null;
	private String registryMethodCall = null;

	private String jesEndPoint = null;
	private String jesNS = null;
	private String jesMethodCall = null;	
	
	private String dsUrl = null;
	private String dsName = null;	

	/**
	 * Our action method.
	 *
	 */
	public Map act(
		Redirector redirector, 
		SourceResolver resolver, 
		Map objectModel, 
		String source, 
		Parameters params)
		{
		
		//
		// Get our current request and session.
		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession();

		String tempStr = null;		
		
		Properties prop = new Properties();
		InputStream stream = this.getClass().
							 getResourceAsStream("QueryAction.properties");
		if (null != stream)	{
			try {
				prop.load(stream);
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		DEBUG_FLAG = new Boolean(prop.getProperty(DEBUG_FLAG_PROPERTY,"true")).booleanValue();
		registryEndPoint = prop.getProperty(REGISTRY_URL_PROPERTY);
		registryNS = prop.getProperty(REGISTRY_NS_PROPERTY);		
		registryMethodCall = prop.getProperty(REGISTRY_METHODCALL_PROPERTY);
		jesEndPoint = prop.getProperty(JES_URL_PROPERTY);
		jesNS = prop.getProperty(JES_NS_PROPERTY);
		jesMethodCall = prop.getProperty(JES_METHODCALL_PROPERTY);
		
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("QueryAction.act()") ;
		
		
		Hashtable dsUrlHash = new Hashtable();
		int iTemp = 0;
		dsUrl = prop.getProperty(DS_URL_PROPERTY + iTemp);
		dsName = prop.getProperty(DS_NAME_PROPERTY + iTemp);
		String t = DS_NAME_PROPERTY + iTemp;
		while(dsName != null) {
			dsUrlHash.put(dsName,dsUrl);
			iTemp++;
			dsUrl = prop.getProperty(DS_URL_PROPERTY + iTemp);
			dsName = prop.getProperty(DS_NAME_PROPERTY + iTemp);
		}
		session.setAttribute(DS_AGENT_LIST,dsUrlHash);
				

		
		//
		//Create a new HashMap for our results.  Will be used to
		//pass to the transformer (xsl page)
		Map results = new HashMap() ;
		
		//
		// Get our current user name from the session.
		String tag  = null ;
		String user = null ;
		try {
			tag = params.getParameter(USER_PARAM_NAME) ;
			}
		catch (ParameterException ouch)
			{
			tag = null ;
			}
		if (null != tag)
			{
			user = (String) session.getAttribute(tag) ;
			}
		if (DEBUG_FLAG) System.out.println("User tag  : " + tag) ;
		if (DEBUG_FLAG) System.out.println("User name : " + user) ;
		
		String errorMessage = null;

		String []reqTemp;
		String jobIDStr = null;
		
		


		//A QueryBuilder objects holds all the data the user
		//is inserting form the page for building up a query.
		//get it from the session
		QueryBuilder qb = (QueryBuilder)
		                   session.getAttribute(SESSION_QUERY_BUILDER);

		//Doesn't have it in the session then create a brand new one.
		if(qb == null) {
			qb = new QueryBuilder("JobTest");
			qb.setUserName(user);
			qb.setCommunity((String)session.getAttribute("community")); //change this.
		}

		
		//This is all the information from the registry such as
		//Columns and ucds.  See if it is in our request object
		//if not then go get it.
		ArrayList dsInfoFromRegistry = (ArrayList)
		                                session.getAttribute(REQUEST_DATASET_LIST);
		if(dsInfoFromRegistry == null || dsInfoFromRegistry.size() <= 0) {
			dsInfoFromRegistry = getDataSetsFromRegistry();
			session.setAttribute(REQUEST_DATASET_LIST,dsInfoFromRegistry);
		}
	
		String script = makeJavaScript(dsInfoFromRegistry);
		results.put("Script", script);
	
		tempStr = (String)request.getAttribute(REQUEST_QUERY_STRING_SENT);
		if(!validParameter(tempStr)) {
			tempStr = "";
		}else {
			results.put(REQUEST_QUERY_STRING_SENT,"\nSent Query:" + tempStr);
			request.setAttribute(REQUEST_QUERY_STRING_SENT,tempStr);			
		}
				
						
		
		String dsName = request.getParameter(DATASET_NAME);
		String dsNameCriteria = request.getParameter(DATASET_NAME_CRITERIA);
		String returnColumn = request.getParameter(RETURN_COLUMN);
		String filterColumn = request.getParameter(FILTER_COLUMN);
		String operator = request.getParameter(OPERATOR);
		String value = request.getParameter(VALUE);
		String joinType = request.getParameter(JOIN_TYPE);
		String functionValues = request.getParameter(FUNCTION_VALUES);
		String dsAgent = request.getParameter(DS_AGENT); 
		
		String actionRemoveSelection = request.getParameter(ACTION_REMOVE_SELECTION);
		String actionAddCriteria = request.getParameter(ACTION_ADD_CRITERIA);
		String actionRemoveCriteria = request.getParameter(ACTION_REMOVE_CRITERIA);
		String actionAddSelection = request.getParameter(ACTION_ADD_SELECTION);
		String actionClearQuery = request.getParameter(ACTION_CLEAR_QUERY);
		String actionSubmitQuery = request.getParameter(ACTION_SUBMIT_QUERY);
		
		//Start checking which button was pressed.  
		//With AddSelection being the first to be checked.
		if( validParameter(actionAddSelection) ) {
			//It was an AddSelection meaning they want to 
			//add colums/ucd or DataSet to the query.
			if(validParameter(dsName) &&
			   validParameter(returnColumn) ) {
				
				reqTemp = returnColumn.split(SPLIT_MARKER);
				DataSetInformation dsInfo = null;
				//check if this DataSet is already in the QueryBuilder.  
				//If not then add a new DataSet to QueryBuilder.
				dsInfo = qb.getDataSetInformation(dsName);
				if( dsInfo == null) {
					if(qb.getDataSetInformation().size() > 0) {
						errorMessage = "Currently you cannot Add more than one DataSet to the query.";
					}else {
						dsInfo = qb.addDataSetInformation(dsName);
					}//if					
				}//if
				
				//Add the ReturnColumn.
				if(errorMessage == null || errorMessage.length() <= 0) {
					dsInfo.addDataSetColumn(reqTemp[1],reqTemp[0]);
				}
			}else {
				errorMessage = "Tried to Add a selection with no datasetname " +
					"and/or return column specefied this is not allowed";
			}
		}else if(validParameter(actionRemoveSelection)) {
			  //hit the RemoveSelection button to remove a Column.
			if(validParameter(dsName) &&
			   validParameter(returnColumn)) {
				
				DataSetInformation dsInfo = null;
				reqTemp = returnColumn.split(SPLIT_MARKER);
				
				//Check to make sure that the DataSet exists in the Selection query.
				dsInfo = qb.getDataSetInformation(dsName);
				if( dsInfo != null) {
					//Now attempt to remove the column, if it does 
					//not exist then it won't be removed.
					dsInfo.removeDataSetColumn(reqTemp[1],reqTemp[0]);
					//if their are no more columns/ucd's associated with this dataset 
					//then remove the dataset from the QueryBuilder.
					if(dsInfo.getDataSetColumns().size() <= 0) {
						qb.removeDataSetInformation(dsName);
					}
				}else {
					errorMessage = "Could not find a Return Column to Remove";
				}
			}else {
				errorMessage = "Tried to Remove a selection with no " +
					"datasetname and/or return column specefied this is " +
					"not allowed";
			}
		}else if(validParameter(actionAddCriteria)) { 
			//user decided to add criteria to the where clause.
			if(validParameter(dsNameCriteria) &&
			   validParameter(filterColumn) &&
			   validParameter(operator) ) {
				
				DataSetInformation dsInfo = null;
				reqTemp = filterColumn.split(SPLIT_MARKER);
				
				//make sure the DataSet is in the Selection.
				if( (dsInfo = qb.getDataSetInformation(dsNameCriteria)) != null) {
					
					//see if their is a LinkTo which is for inserting criteria inside other parenthesis.
					//no linking just add it at the end of the where clause.
					dsInfo.addCriteriaInformation(reqTemp[1],reqTemp[0],
							operator,value,joinType,functionValues);
							
					//A CriteriaNumber is used in the session for determining if the LinkTo plus JoinType(And/OR) boxes need to be shown.
					request.setAttribute(REQUEST_CRITERIA_NUMBER,
										 new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find a Data Set Query to add a" +
						" Criteria to, be sure to fill in the above section first";
				}
			}else {
				errorMessage = "Tried to Add a criteria selection with no " +
					"datasetname and/or other parameters this is not allowed";
			}
		}else if(validParameter(actionRemoveCriteria)) { //decided to remove the criteria.
			if(validParameter(dsNameCriteria) &&
			   validParameter(filterColumn) &&
			   validParameter(operator) ) {
			   	
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter(FILTER_COLUMN).split("-");
				
				//make sure the dataset exists.
				dsInfo = qb.getDataSetInformation(dsNameCriteria);
				if( dsInfo != null) {
					//the removeCriteria piece is a method inside the DataSet.
					dsInfo.removeCriteriaInformation(reqTemp[1],reqTemp[0],operator,value);
					//A CriteriaNumber is used in the session for determining if the LinkTo plus JoinType(And/OR) boxes need to be shown.
					request.setAttribute(REQUEST_CRITERIA_NUMBER,
					        new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find Data Set Query to remove" +
						" any Criteria, be sure to fill in the above section" +
						" first";
				}
			}else {
				errorMessage = "Tried to Remove a criteria with no " +
					"datasetname and/or other parameters this is not" +
					" allowed. Be sure to select a Link Number {?}";
			}
		}else if(validParameter(actionClearQuery)) {
			//Okay he wants to start over so get rid of everything.
			qb.clear();
			qb = null;
			request.setAttribute(REQUEST_CRITERIA_NUMBER,null);
		}else if(validParameter(actionSubmitQuery)){
			//submitted a query to the jes system.  Send it to 
			//the jes system and get a jobid.  Then clear out the
			//QueryBuilder so he can start a new one.
			jobIDStr = submitJobQuery(qb,results,dsAgent);
			request.setAttribute(REQUEST_CRITERIA_NUMBER,null);
			tempStr = (String)request.getAttribute(REQUEST_QUERY_STRING_SENT);
			if(!validParameter(tempStr)) {
				tempStr = "";
			}
				
			tempStr = qb.formulateQuery() + "- JobID= " + jobIDStr + " " + tempStr;
			results.put(REQUEST_QUERY_STRING_SENT,"\nSent Query:" + tempStr);
			request.setAttribute(REQUEST_QUERY_STRING_SENT,tempStr);
			qb.clear();
			qb = null;
			
		}else {
			//okay might be the first time but it was not a action type request.
			//and not dealing with a query.
			qb = null;
		}


		//for a query string to show to the user.  If their is a query builder.
		results.put(MAP_QUERY_STRING,"");
		if(qb != null) {			
			results.put(MAP_QUERY_STRING,qb.formulateQuery());
		}

		//put any error messages in the map so we can show it later to the user
		results.put(MAP_ERROR_MESSAGE,
		            ((null != errorMessage) ? errorMessage : ""));
		
		session.setAttribute(SESSION_QUERY_BUILDER,qb);

		System.out.println("critnumber = " + request.getAttribute("CriteriaNumber") );
		//forward back to DataQuery.jsp
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return results;
	}


	/**
	 * Used by the init and checkDataSetTime methods; this method is for the actual retrieval of the registry information and stored
	 * in an ArrayList which is returned.  Uses the QueryRegistryInformation class for all the information needed from the registry.
	 * 
	 * @see org.astrogrid.portal.query.QueryRegistryInformation
	 * @return ArrayList
	 */
	private ArrayList getDataSetsFromRegistry() {
		String reqXmlString = QueryRegistryInformation.getAllDataSetInformationFromRegistry();
		String respXmlString = sendQuery(reqXmlString,registryEndPoint,registryNS,registryMethodCall);
		Object []dsItems = QueryRegistryInformation.getDataSetItemsFromRegistryResponse(respXmlString);
		ArrayList ds = new ArrayList(dsItems.length);
		
		for(int i=0;i< dsItems.length;i++) {
			reqXmlString = QueryRegistryInformation.getAllContentInformationFromRegistryForDataSet((String)dsItems[i]);
			respXmlString = sendQuery(reqXmlString,registryEndPoint,registryNS,registryMethodCall);			
			ArrayList dsColumns = QueryRegistryInformation.getItemsFromRegistryResponse(respXmlString);
			DataSetInformation dsInfo = new DataSetInformation((String)dsItems[i]);
			dsInfo.setDataSetColumns(dsColumns);
			dsInfo.addDataSetColumn(new DataSetColumn("CONE","FUNCTION"));
			ds.add(dsInfo);
		}
		return ds;
	}


	/**
	 * Used to make sure the parameter in the request is not null and has something in it.
	 * @param val
	 * @return
	 */
	private boolean validParameter(String val) {
		if(val != null && val.length() > 0) return true;
		return false;
	}
	
	/**
	 * Creates an XML document for the JES system based on the query builder.
	 * Then sends the XML document to the JES webservice.
	 * Stores the webservice call in the Hashmap to show to the user.
	 * @param qb Object that has all information to form the query.
	 * @param results
	 * @return
	 */
	private String submitJobQuery(QueryBuilder qb,Map results,String dsAgent) {
		//submited a query so send it to the JobController.
			String tempStr = "";
			String jobIDStr = null;
			String xmlBuildResult = null;
			
			try {
				CreateRequest cr = new CreateRequest();
				Document doc = cr.buildXMLRequest(qb,dsAgent);

				xmlBuildResult = cr.writeDocument(doc);
				tempStr = sendQuery(xmlBuildResult,jesEndPoint,jesNS,jesMethodCall);

				if(tempStr.indexOf("<jobid>") != -1) {
					jobIDStr = tempStr.substring((tempStr.indexOf("<jobid>") + 7),tempStr.indexOf("</jobid>"));
				}
				if(DEBUG_FLAG) {
					System.out.println("the jobid is = " + jobIDStr);					
				}
				results.put(MAP_LAST_CALL_XML,tempStr);				
				return jobIDStr;
			}catch(Exception wsException) {
				wsException.printStackTrace();
			}
			//set a Session of the request in xml format sent.
			return "";

	}

	private String makeJavaScript(ArrayList dataSets) {
		StringBuffer sb = new StringBuffer(500);
		int tempIncrement = 0;
		int tempColIncrement = 0;
		String val = null;
		//sb.append("<script>");
		sb.append("\n");
		//sb.append("<![CDATA[");
		if(dataSets != null && dataSets.size() > 0) {
			sb.append("var dsColArray = new Array(" + String.valueOf(dataSets.size()) + ");");
			sb.append("\n");
			for(int i = 0;i < dataSets.size();i++) {
				tempIncrement = i;
				DataSetInformation dsVal = (DataSetInformation)dataSets.get(i);
				sb.append("dsColArray[" + String.valueOf(i) + "] = new Array(" + String.valueOf(dsVal.getDataSetColumns().size()) + ");");
				sb.append("\n");
				for(int j = 0;j < dsVal.getDataSetColumns().size(); j++) {
				  tempColIncrement = j;
				  DataSetColumn dsCol = (DataSetColumn)dsVal.getDataSetColumns().get(j);
				  val = dsCol.getType();
				  if(validParameter(val)) {
				  	val += "-" + dsCol.getName();
				  }
				  else{
				  	val = dsCol.getName();
				  }
				  sb.append("dsColArray[" + String.valueOf(tempIncrement) + "][" + String.valueOf(tempColIncrement) + "] = \"" +
				  dsCol.getType() + "-" + dsCol.getName() + "\";");
				  sb.append("\n");				  
				}//for
			}//for			
		}//if
		
	
		sb.append("function updateCols(sIndex,colObj) {");
		sb.append("\n");
		sb.append("while(colObj.length > 0) { colObj.options[colObj.length-1] = null; }");
		sb.append("\n");
		sb.append("for(var i = 0;i < dsColArray[sIndex].length;i++) {");
		sb.append("\n");
		sb.append("colObj.options[colObj.length] = new Option(dsColArray[sIndex][i],dsColArray[sIndex][i],false,false);");
		sb.append("\n");
		sb.append("} }");
		sb.append("\n");		
		//sb.append("</script>");
		return sb.toString();
	}

	/**
	 * A generic method to call a webservice.  Used for the Registry and JES.
	 * @param req the xml string document request for the webservice.
	 * @param endPoint the location of the webservice
	 * @param nameSpace namespace if needed
	 * @param methodName the actual method name on the webservice.
	 * @return returns the xml response from the webservice.
	 */	
	public String sendQuery(String req,String endPoint,String nameSpace,String methodName) {
		try {
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(new URL(endPoint));
			call.addParameter( "request", XMLType.XSD_STRING, ParameterMode.IN );
			call.setOperationName(new javax.xml.namespace.QName(nameSpace, methodName));
			call.setReturnType(XMLType.XSD_STRING);
			if(DEBUG_FLAG) {
				System.out.println("request doc to the webservice = " + req);
			}
			java.lang.Object _resp = call.invoke(new java.lang.Object[] {req});
			String response = (String)_resp;
			if(DEBUG_FLAG) {
				System.out.println("the response from the call to the webservice = " + response);
			}			
			return response;
		}catch (Exception e) {
			  e.printStackTrace();
		}
		  return null;
	}	
}