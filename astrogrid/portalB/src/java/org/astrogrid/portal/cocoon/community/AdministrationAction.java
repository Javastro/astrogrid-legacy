
package org.astrogrid.portal.cocoon.community;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.Map;
import java.util.HashMap;

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
public class AdministrationAction extends AbstractAction
{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true;
	
	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String USER_PARAM_NAME = "user-param" ;
	
	private static final String ACTION = "action";
	
	private static final String ACTION_INSERT_RESOURCE = "insertresource";

	private static final String ACTION_INSERT_MEMBER = "removeresource";
	
	private static final String ACTION_INSERT_PERMISSION = "insertpermission";
	
	private static final String ACTION_INSERT_GROUP = "insertgroup";
	
	private static final String ACTION_INSERT_COMMUNITY = "insertcommunity";
	
	private static final String ACTION_REMOVE_RESOURCE = "removeresource";

	private static final String ACTION_REMOVE_MEMBER = "removemember";

	private static final String ACTION_REMOVE_PERMISSION = "removepermission";

	private static final String ACTION_REMOVE_GROUP = "removegroup";
	
	private static final String ACTION_REMOVE_COMMUNITY = "removecommunity";
	
	private static final String ACTION_REMOVE_ACCOUNT = "removeaccount";
	
	private static final String ACTION_CHANGE_RESOURCE_OWNER = "changeresourceowner";
	
	private static final String ACTION_CHANGE_GROUP_OWNER = "changegroupowner";
	
	private static final String ACTION_CHANGE_PASSWORD = "changeofpassword";
	
	private static final String ACTION_VIEW_GROUPS = "viewgroups";	
	
		
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

		String action = request.getParameter(ACTION);
		if(DEBUG_FLAG) {
			System.out.println("the action is = " + action);		
		}
		
		//
		//Create a new HashMap for our results.  Will be used to
		//pass to the transformer (xsl page)
		Map results = new HashMap() ;
		results.put(ACTION,action);
		return results;
	}	
}