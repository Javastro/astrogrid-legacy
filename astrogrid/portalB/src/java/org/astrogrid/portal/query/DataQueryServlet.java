
package org.astrogrid.portal.query;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;


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
		if(qb == null) {
			System.out.println("QueryBuilder was null");
			qb = new QueryBuilder();
		}

		Enumeration en = request.getParameterNames();
		while(en.hasMoreElements()) {
			System.out.println(en.nextElement().toString());
		}


		if(validParameter(request.getParameter("AddSelection"))) {
			if(validParameter(request.getParameter("DataSetName")) &&
			   validParameter(request.getParameter("ReturnColumn")) ) {
				   System.out.println("DataSetName = " + request.getParameter("DataSetName"));
				   System.out.println("ReturnColumn = " + request.getParameter("ReturnColumn"));

				DataSetInformation dsInfo = null;
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) == null) {
					System.out.println("QueryBuilder did not have this dataset");
					dsInfo = qb.addDataSetInformation(request.getParameter("DataSetName"));
				}
				dsInfo.addDataSetColumn(request.getParameter("ReturnColumn"));
			}else {
				errorMessage = "Tried to Add a selection with no datasetname and return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveSelection"))) {
			if(validParameter(request.getParameter("DataSetName")) && validParameter(request.getParameter("ReturnColumn"))) {
				DataSetInformation dsInfo = null;
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetName"))) != null) {
					dsInfo.removeDataSetColumn(request.getParameter("ReturnColumn"));
					if(dsInfo.getDataSetColumns().size() <= 0) {
						qb.removeDataSetInformation(request.getParameter("DataSetName"));
					}
				}else {
					errorMessage = "Could not find a Return Column to Remove";
				}

			}else {
				session.setAttribute("ErrorMessage","Tried to Remove a selection with no datasetname and return column specefied this is not allowed");
			}
		}else if(validParameter(request.getParameter("AddCriteria"))) {
			if(validParameter(request.getParameter("DataSetNameCriteria")) && validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) && validParameter(request.getParameter("Value"))) {
				DataSetInformation dsInfo = null;
				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {

					dsInfo.addCriteriaInformation(request.getParameter("FilterColumn"),request.getParameter("Operator"),request.getParameter("Value"));
				}else {
					errorMessage = "Could not find a Data Set Query to add a Criteria to, be sure to fill in your Return Columns first";
				}
			}else {
				errorMessage = "Tried to Add a selection with no datasetname and/or return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("RemoveCriteria"))) {
			if(validParameter(request.getParameter("DataSetNameCriteria")) &&
			   validParameter(request.getParameter("FilterColumn")) &&
			   validParameter(request.getParameter("Operator")) &&
			   validParameter(request.getParameter("Value")) ) {
				DataSetInformation dsInfo = null;

				if( (dsInfo = qb.getDataSetInformation(request.getParameter("DataSetNameCriteria"))) != null) {
					dsInfo.removeCriteriaInformation(request.getParameter("FilterColumn"),request.getParameter("Operator"),request.getParameter("Value"));
				}else {
					errorMessage = "Could not find a Data Set Query to remove any Criteria, be sure to fill in your Return Columns first";
				}
			}else {
				errorMessage = "Tried to Remove a selection with no datasetname and return column specefied this is not allowed";
			}
		}else if(validParameter(request.getParameter("ClearQuery"))) {
			qb.clear();
			qb = null;
			session.setAttribute("QueryString",null);
		}else if(validParameter(request.getParameter("SubmitQuery"))){
			send();
			queryString = qb.formulateQuery() + queryString;
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
		if(errorMessage != null) {
			session.setAttribute("ErrorMessage",errorMessage);
		}
		session.setAttribute("QueryBuilder",qb);
		response.sendRedirect("/DataQuery/servlets/DataQuery.jsp");
		//forward back to DataQuery.jsp
	}

	private boolean validParameter(String val) {
		if(val != null && val.length() > 0) return true;
		return false;
	}

	private void send() {
	  //this method will probably go away and call the Stubbs in the ASTQuery object to form
	  //the xml needed for the query and send it to the webservice.
	}

}