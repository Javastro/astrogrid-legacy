
package org.astrogrid.portal.query;
import javax.servlet.*;
import org.w3c.dom.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.astrogrid.portal.generated.jobcontroller.client.*;

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
public class DataQueryServlet extends HttpServlet {


	/**
	 * This method is called by the ServletContainer only once in the beginning for any initialization.  This servlet gets all the
	 * registry information and stores it in the application session for retrieval.  Also stores a future Calendar time for 
	 * checking the need for refreash.
	 * @param conf
	 * @throws ServletException
	 */
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		ServletContext sc = conf.getServletContext();
		ArrayList dsInfo = (ArrayList)sc.getAttribute("DataSetArrayList");
		if(dsInfo == null || dsInfo.size() <= 0) {
			dsInfo = getDataSetsFromRegistry();
			System.out.println("it was called");
			sc.setAttribute("DataSetArrayList",dsInfo);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY,5);
			sc.setAttribute("DataSetInitTime",cal);
		}//if
	}

	/**
	 * Just call the processRequest to do the necessary request.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet( HttpServletRequest request, 
	                      HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}

	/**
	 * Just call the processRequest to do the necessary request.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost( HttpServletRequest request, 
	                       HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}

	/**
	 * This method will check and see if the current time is after the refreash time needed for the DataSets and Columns
	 * If the time has elapsed the call the registry interface for the information and reset a new object and time into the session.
	 * @param sc
	 * @param current
	 */
	private void checkDataSetTime(ServletContext sc, Calendar current) {
		Calendar dsTime = (Calendar)sc.getAttribute("DataSetInitTime");
		if(dsTime == null || current.after(dsTime)) {
			ArrayList dsInfo = getDataSetsFromRegistry();
			sc.setAttribute("DataSetArrayList",dsInfo);
			current.add(Calendar.HOUR_OF_DAY,5);
			sc.setAttribute("DataSetInitTime",current);
		}
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
		System.out.println(reqXmlString);
		String respXmlString = QueryRegistryInformation.sendRegistryQuery(reqXmlString);
		System.out.println(respXmlString);
		Object []dsItems = QueryRegistryInformation.getDataSetItemsFromRegistryResponse(respXmlString);
		ArrayList ds = new ArrayList(dsItems.length);
		if(dsItems.length > 0) {
			System.out.println("The item = " + dsItems[0]);
		}
		
		for(int i=0;i< dsItems.length;i++) {
			reqXmlString = QueryRegistryInformation.getAllContentInformationFromRegistryForDataSet((String)dsItems[i]);
			respXmlString = QueryRegistryInformation.sendRegistryQuery(reqXmlString);			
			ArrayList dsColumns = QueryRegistryInformation.getItemsFromRegistryResponse(respXmlString);
			DataSetInformation dsInfo = new DataSetInformation((String)dsItems[i]);
			dsInfo.setDataSetColumns(dsColumns);
			dsInfo.addDataSetColumn(new DataSetColumn("CONE","FUNCTION"));
			ds.add(dsInfo);
		}
		return ds;
	}

	/**
	 * This method does the real work for taking request from the DataQueryServlet.  It will:
	 * 1.) Get/initialize some variables.
	 * 2.) Check and make sure that the Registry.
	 * 3.) See what request is it.  Another words check which button was pressed on the DataQuery.jsp page.
	 * 4.) Act on that request by verifing all the needed request parameters and calling the needed objects.
	 * In dealing with a Query all information needed is stored in the QueryBuilder object which holds all the DataSets, Columns,
	 * Criteria needed for this particular query.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		QueryBuilder qb = (QueryBuilder)session.getAttribute("QueryBuilder");
		String queryString = (String)session.getAttribute("QueryStringSent");
		String errorMessage = null;
		String []reqTemp;
		String userName = null;
		String community = null;
		int iTemp = -1;
		if(qb == null) {
			qb = new QueryBuilder("JobTest");
		}

		checkDataSetTime(getServletConfig().getServletContext(),
						 Calendar.getInstance());
		ArrayList alTemp = (ArrayList)getServletContext().getAttribute("DataSetArrayList");
		if(alTemp == null || alTemp.size() <= 0) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							   "Cannot access service to get Data Set List");
		}
		
		if(!validParameter(qb.getUserName())) {
			userName = request.getParameter("username");
			if(!validParameter(userName)) {
				userName = (String)session.getAttribute("username");			
				if(!validParameter(userName)) {
					userName = "demouser";
				}
			}
			qb.setUserName(userName);
		}		

		if(!validParameter(qb.getCommunity())) {
			community = request.getParameter("community");
			if(!validParameter(community)) {
				community = (String)session.getAttribute("community");			
				if(!validParameter(community)) {
					community = "democomm";
				}//if
				session.setAttribute("community",community);							
			}//if
			qb.setCommunity(community);
		}
		System.out.println("the username = " + qb.getUserName());
		System.out.println("the community = " + qb.getCommunity());
		
		
		//Start checking which button was pressed.  
		//With AddSelection being the first to be checked.
		if(validParameter(request.getParameter("AddSelection"))) {
			//It was an AddSelection meaning they want to 
			//add colums/ucd or DataSet to the query.
			if(validParameter(request.getParameter("DataSetName")) &&
			   validParameter(request.getParameter("ReturnColumn")) ) {
				   System.out.println(request.getParameter("ReturnColumn"));
				   reqTemp = request.getParameter("ReturnColumn").split("-");
				DataSetInformation dsInfo = null;
				//heck if this DataSet is already in the QueryBuilder.  If not then add a new DataSet to QueryBuilder.
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) == null) {
					if(qb.getDataSetInformation().size() > 0) {
						errorMessage = "Currently you cannot Add more than one DataSet to the query.";
					}else {
						dsInfo = qb.addDataSetInformation(request.getParameter("DataSetName"));
					}					
				}
				//Add the ReturnColumn.
				if(errorMessage == null || errorMessage.length() <= 0) {
					dsInfo.addDataSetColumn(reqTemp[1],reqTemp[0]);
				}
				
			}else {
				errorMessage = "Tried to Add a selection with no datasetname " +					"and/or return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveSelection"))) {
			  //hit the RemoveSelection button to remove a Column.
			if(validParameter(request.getParameter("DataSetName")) &&
			   validParameter(request.getParameter("ReturnColumn"))) {
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter("ReturnColumn").split("-");
				//Check to make sure that the DataSet exists in the Selection query.
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) != null) {
					//Now attempt to remove the column, if it does 
					//not exist then it won't be removed.
					dsInfo.removeDataSetColumn(reqTemp[1],reqTemp[0]);
					//if their are no more columns/ucd's associated with this dataset 
					//then remove the dataset from the QueryBuilder.
					if(dsInfo.getDataSetColumns().size() <= 0) {
						qb.removeDataSetInformation(request.getParameter("DataSetName"));
					}
				}else {
					errorMessage = "Could not find a Return Column to Remove";
				}
			}else {
				errorMessage = "Tried to Remove a selection with no " +					"datasetname and/or return column specefied this is " +					"not allowed";
			}
		}else if(validParameter(request.getParameter("AddCriteria"))) { 
			//user decided to add criteria to the where clause.
			if(validParameter(request.getParameter("DataSetNameCriteria")) &&
			   validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) ) {
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter("FilterColumn").split("-");
				//make sure the DataSet is in the Selection.
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {
					//see if their is a LinkTo which is for inserting criteria inside other parenthesis.
					if(validParameter(request.getParameter("LinkTo")) ) {
						iTemp = new Integer(request.getParameter("LinkTo")).intValue();
					}
					//if he wants it linked then we need to get that 
					//Criteria he wants to link to.  And add this new Criteria 
					//inside it.
					if(iTemp >= 0) {
						CriteriaInformation ci = new CriteriaInformation(
						      new DataSetColumn(reqTemp[1],reqTemp[0]),
						      request.getParameter("Operator"),request.getParameter("Value"),
						      request.getParameter("JoinType"),
						      request.getParameter("FunctionValues"));
						      
						LinkCriteria((CriteriaInformation)dsInfo.getCriteriaInformation().get(iTemp),ci);
					}else {
						//no linking just add it at the end of the where clause.
						dsInfo.addCriteriaInformation(reqTemp[1],reqTemp[0],
							request.getParameter("Operator"),
							request.getParameter("Value"),
							request.getParameter("JoinType"),
							request.getParameter("FunctionValues"));
					}
					//A CriteriaNumber is used in the session for determining if the LinkTo plus JoinType(And/OR) boxes need to be shown.
					session.setAttribute("CriteriaNumber",
										 new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find a Data Set Query to add a" +						" Criteria to, be sure to fill in the above section first";
				}
			}else {
				errorMessage = "Tried to Add a criteria selection with no " +					"datasetname and/or other parameters this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveCriteria"))) { //decided to remove the criteria.
			if(validParameter(request.getParameter("DataSetNameCriteria")) &&
			   validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) &&
			   validParameter(request.getParameter("LinkTo")) ) {
				DataSetInformation dsInfo = null;
				iTemp = new Integer(request.getParameter("LinkTo")).intValue();
				reqTemp = request.getParameter("FilterColumn").split("-");
				//make sure the dataset exists.
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {
					//the removeCriteria piece is a method inside the DataSet.
					dsInfo.removeCriteriaInformation(reqTemp[1],reqTemp[0],request.getParameter("Operator"),request.getParameter("Value"),iTemp);
					//A CriteriaNumber is used in the session for determining if the LinkTo plus JoinType(And/OR) boxes need to be shown.
					session.setAttribute("CriteriaNumber",new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find Data Set Query to remove" +						" any Criteria, be sure to fill in the above section" +						" first";
				}
			}else {
				errorMessage = "Tried to Remove a criteria with no " +					"datasetname and/or other parameters this is not" +					" allowed. Be sure to select a Link Number {?}";
			}
		}else if(validParameter(request.getParameter("ClearQuery"))) {
			//Okay he wants to start over so get rid of everything.
			qb.clear();
			qb = null;
			session.setAttribute("QueryString",null);
		}else if(validParameter(request.getParameter("SubmitQuery"))){
			//submited a query so send it to the JobController.
				String tempStr = "";
				String jobIDStr = null;
				ArrayList alErrorQueries = (ArrayList)getServletContext().getAttribute("ErrorQueryBuilders");
				if(alErrorQueries != null) {				
					try {
						while(alErrorQueries.size() > 0) {
						  send((QueryBuilder)alErrorQueries.get(0));
						  alErrorQueries.remove(0);
						}					
					}catch(Exception wsException) {
						wsException.printStackTrace();
						getServletContext().setAttribute("ErrorQueryBuilders",alErrorQueries);
					}
				}
				try {
					tempStr = send(qb);
					if(tempStr.indexOf("<jobid>") != -1) {
						jobIDStr = tempStr.substring((tempStr.indexOf("<jobid>") + 7),tempStr.indexOf("</jobid>"));
					}					
					session.setAttribute("jobid",jobIDStr);
					System.out.println("the jobid is = " + jobIDStr);
					//now that it is sent blank out the QueryString and put it as part of their Sent Queries.
					if(queryString == null){queryString = "";}
					
					queryString = qb.formulateQuery() + "- JobID= " + jobIDStr + "<br />" + queryString;
					session.setAttribute("QueryStringSent","\nSent Query:" + queryString);
					session.setAttribute("QueryString",null);									
				}catch(Exception wsException) {
					wsException.printStackTrace();
					if(alErrorQueries == null) {
						alErrorQueries = new ArrayList();
					}
					alErrorQueries.add(qb);
					getServletContext().setAttribute("ErrorQueryBuilders",alErrorQueries);
					errorMessage = "An error happened trying to submit your query to our service" +
					               "The cause is the service is down.  Your query has been saved and " +
					               "will be sent when the service comes back up.";
				}
				//set a Session of the request in xml format sent.
				session.setAttribute("LastWebServiceXML",tempStr);
				qb.clear();
				qb = null;
		}else {
			//okay might be the first time but it was not a action type request.
			//and not dealing with a query.
			qb = null;
		}

		//if there is a QueryBuilder object then go formulate the proposed query to send to the JobController.
		if(qb != null) {			
			session.setAttribute("QueryString",qb.formulateQuery());
		}

		session.setAttribute("ErrorMessage",errorMessage);
		session.setAttribute("QueryBuilder",qb);
		response.sendRedirect("/DataQuery/servlets/DataQuery.jsp");
		//forward back to DataQuery.jsp
	}

	/**
	 * This little methods puts a CriterisObject inside another CriteriaObject for linking/paranthesizing.  
	 * This is used when the user wants to put criteria as part of another criteria another words part all inside the same
	 * parenthesis.
	 * @param critSource
	 * @param critTarget
	 */
	private void LinkCriteria(CriteriaInformation critSource,CriteriaInformation critTarget) {
		if(critSource.getLinkedCriteria() != null) {
			LinkCriteria(critSource.getLinkedCriteria(),critTarget);
		}else {
			critSource.setLinkedCriteria(critTarget);
		}
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
	 * Mehtod used to send to the JobController webservice.  Interacts with the JobController stubbs for this.
	 * 
	 * @param qb
	 * @return
	 */
	private String send(QueryBuilder qb) throws Exception {
        JobController binding;
        String xmlBuildResult = null;
        String response = null;
        	//CreateRequest is an object that creates the necessary xml document object to send to the job controller.
			CreateRequest cr = new CreateRequest();
			Document doc = cr.buildXMLRequest(qb);

			xmlBuildResult = cr.writeDocument(doc);
			//xmlBuildResult = xmlBuildResult.substring(xmlBuildResult.indexOf("<jo"));
			System.out.println("The XmL going to the webservice is = " + xmlBuildResult);
			binding = new JobControllerServiceLocator().getJobControllerService();
			//binding.setTimeout(30000);
			//submit the request and get a response back.
        	response = binding.submitJob(xmlBuildResult);
			System.out.println("the response from the call to the webservice = " + response);
        if(xmlBuildResult != null && xmlBuildResult.length() > 0) xmlBuildResult = xmlBuildResult.replaceAll(">",">\n");
        return "XML String sent to webservice = " + xmlBuildResult + "Response = " + response;
	}
}