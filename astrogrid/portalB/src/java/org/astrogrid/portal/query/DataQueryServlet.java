
package org.astrogrid.portal.query;
import javax.servlet.*;
import org.w3c.dom.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.astrogrid.portal.generated.jobcontroller.client.*;


public class DataQueryServlet extends HttpServlet {



	protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}

	protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}



	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Okay in the processrequest");
		HttpSession session = request.getSession(true);
		QueryBuilder qb = (QueryBuilder)session.getAttribute("QueryBuilder");
		String queryString = (String)session.getAttribute("QueryStringSent");
		String errorMessage = null;
		String []reqTemp;
		if(qb == null) {
			System.out.println("QueryBuilder was null");
			qb = new QueryBuilder("JObTest");
		}

		ArrayList preFill = new ArrayList();
		DataSetInformation dsInfoTest = new DataSetInformation("USNOB");
		dsInfoTest.addDataSetColumn("ID","COLUMN");
		dsInfoTest.addDataSetColumn("URA","COLUMN");
		dsInfoTest.addDataSetColumn("UDEC","COLUMN");
		dsInfoTest.addDataSetColumn("PHOT_TYCHO","UCD");
		dsInfoTest.addDataSetColumn("CONE","FUNCTION");
		preFill.add(dsInfoTest);
		dsInfoTest = new DataSetInformation("PreFilTest-2");
		dsInfoTest.addDataSetColumn("Test1");
		dsInfoTest.addDataSetColumn("Test2");
		dsInfoTest.addDataSetColumn("Test3");
		preFill.add(dsInfoTest);
		session.setAttribute("DataSetArrayList",preFill);


		Enumeration en = request.getParameterNames();
		while(en.hasMoreElements()) {
			System.out.println(en.nextElement().toString());
		}


		if(validParameter(request.getParameter("AddSelection"))) {
			if(validParameter(request.getParameter("DataSetName")) &&
			   validParameter(request.getParameter("ReturnColumn")) ) {
				   System.out.println(request.getParameter("ReturnColumn"));
				   reqTemp = request.getParameter("ReturnColumn").split("-");
				   System.out.println(reqTemp[0]);

				DataSetInformation dsInfo = null;
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) == null) {
					dsInfo = qb.addDataSetInformation(request.getParameter("DataSetName"));
				}
				dsInfo.addDataSetColumn(reqTemp[1],reqTemp[0]);
			}else {
				errorMessage = "Tried to Add a selection with no datasetname and/or return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveSelection"))) {
			if(validParameter(request.getParameter("DataSetName")) && validParameter(request.getParameter("ReturnColumn"))) {
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter("ReturnColumn").split("-");
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) != null) {
					dsInfo.removeDataSetColumn(reqTemp[1],reqTemp[0]);
					if(dsInfo.getDataSetColumns().size() <= 0) {
						qb.removeDataSetInformation(request.getParameter("DataSetName"));
					}
				}else {
					errorMessage = "Could not find a Return Column to Remove";
				}

			}else {
				errorMessage = "Tried to Remove a selection with no datasetname and/or return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("AddCriteria"))) {
			if(validParameter(request.getParameter("DataSetNameCriteria")) && validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) ) {
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter("FilterColumn").split("-");
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {
					int iTemp = -1;
					if(validParameter(request.getParameter("LinkTo")) ) {iTemp = new Integer(request.getParameter("LinkTo")).intValue();}
					if(iTemp >= 0) {
						CriteriaInformation ci = new CriteriaInformation(new DataSetColumn(reqTemp[1],reqTemp[0]),request.getParameter("Operator"),request.getParameter("Value"),request.getParameter("JoinType"),request.getParameter("FunctionValues"));
						LinkCriteria((CriteriaInformation)dsInfo.getCriteriaInformation().get(iTemp),ci);
					}else {
						dsInfo.addCriteriaInformation(reqTemp[1],reqTemp[0],request.getParameter("Operator"),request.getParameter("Value"),request.getParameter("JoinType"),request.getParameter("FunctionValues"));
					}
					session.setAttribute("CriteriaNumber",new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find a Data Set Query to add a Criteria to, be sure to fill in the above section first";
				}
			}else {
				errorMessage = "Tried to Add a criteria selection with no datasetname and/or other parameters this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveCriteria"))) {
			if(validParameter(request.getParameter("DataSetNameCriteria")) &&
			   validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) &&
			   validParameter(request.getParameter("Value")) ) {
				DataSetInformation dsInfo = null;
				reqTemp = request.getParameter("FilterColumn").split("-");
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {
					dsInfo.removeCriteriaInformation(reqTemp[1],reqTemp[0],request.getParameter("Operator"),request.getParameter("Value"));
					session.setAttribute("CriteriaNumber",new Integer(dsInfo.getCriteriaInformation().size()));
				}else {
					errorMessage = "Could not find a Data Set Query to remove any Criteria, be sure to fill in the above section first";
				}
			}else {
				errorMessage = "Tried to Remove a criteria with no datasetname and/or other parameters this is not allowed";
			}
		}else if(validParameter(request.getParameter("ClearQuery"))) {
			qb.clear();
			qb = null;
			session.setAttribute("QueryString",null);
		}else if(validParameter(request.getParameter("SubmitQuery"))){
				String tempStr = send(qb);
				session.setAttribute("LastWebServiceXML",tempStr);
				if(queryString == null){queryString = "";}
				queryString = qb.formulateQuery() + "<br />" + queryString;
				session.setAttribute("QueryStringSent","\nSent Query:" + queryString);
				session.setAttribute("QueryString",null);
				qb.clear();
				qb = null;
		}else {
			//okay might be the first time but it was not a action type request.
			qb = null;
		}

		if(qb != null) {
			session.setAttribute("QueryString",qb.formulateQuery());
		}

		session.setAttribute("ErrorMessage",errorMessage);
		session.setAttribute("QueryBuilder",qb);
		response.sendRedirect("/DataQuery/servlets/DataQuery.jsp");
		//forward back to DataQuery.jsp
	}

	private void LinkCriteria(CriteriaInformation critSource,CriteriaInformation critTarget) {
		if(critSource.getLinkedCriteria() != null) {
			LinkCriteria(critSource.getLinkedCriteria(),critTarget);
		}else {
			critSource.setLinkedCriteria(critTarget);
		}
	}

	private boolean validParameter(String val) {
		if(val != null && val.length() > 0) return true;
		return false;
	}

	private String send(QueryBuilder qb) {
	  //this method will probably go away and call the Stubbs in the ASTQuery object to form
	  //the xml needed for the query and send it to the webservice.
        JobControllerServiceSoapBindingStub binding;
        String xmlBuildResult = null;
        try {
			CreateRequest cr = new CreateRequest();
			Document doc = cr.buildXMLRequest(qb);

			xmlBuildResult = cr.writeDocument(doc);
			//xmlBuildResult = xmlBuildResult.substring(xmlBuildResult.indexOf("<jo"));
			System.out.println("The XmL going to the webservice is = " + xmlBuildResult);

            binding = (org.astrogrid.portal.generated.jobcontroller.client.JobControllerServiceSoapBindingStub)
                          new org.astrogrid.portal.generated.jobcontroller.client.JobControllerServiceLocator().getJobControllerService();

        	String response = binding.submitJob(xmlBuildResult);
  //      	String response = binding.runQuery(new String());
			System.out.println("the response from the call to the webservice = " + response);
        }catch (Exception e) {
        	e.printStackTrace();
        }
        if(xmlBuildResult != null && xmlBuildResult.length() > 0) xmlBuildResult = xmlBuildResult.replaceAll(">",">\n");
        return xmlBuildResult;

	}

}
