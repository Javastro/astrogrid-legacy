
package org.astrogrid.portal.cocoon.community;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.Map;
import java.util.ArrayList;
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
public class CredentialAction extends AbstractAction
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
   public static final String USER_PARAM_NAME = "user" ;
   
   private static final String PARAM_CREDENTIAL = "credential";
   
   private static final String PARAM_MESSAGE = "message";   
   
   private static final String ACTION_GET_COMMUNITY_GROUPS = "communitygroups";
   
   private static final String PARAM_COMMUNITY_LIST = "communitylist";
   
   private static final String PARAM_COMMUNITY_NAME = "community";   

   private static final String PARAM_GROUP_LIST = "grouplist";   
   
   private static final String ACTION = "action";   
   
   private static final String ACTION_SET_GROUP = "setgroup";

      
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
      
      String user = (String)session.getAttribute(USER_PARAM_NAME);

      String action = request.getParameter(ACTION);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
      }
      
      ArrayList communityList = (ArrayList)session.getAttribute(PARAM_COMMUNITY_LIST);
      
      if(communityList == null || communityList.size() <= 0) {
         //call delegate to get the communities trusted by this local community.
         //fill with dummy data for the moment.
         communityList.add("MSSL");
         communityList.add("Cambridge");
         communityList.add("Leicester");
         communityList.add("Edinburgh");
      }
      
      ArrayList groupList = (ArrayList)session.getAttribute(PARAM_GROUP_LIST);

      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      
      if(groupList == null || groupList.size() <= 0) {
         //call delegate to get the groups for the local community.
         //fill with dummy data for the moment.
         groupList.add("Solar_Flares@mssl");
         groupList.add("BigBang@mssl");
      }
      
      if(action.equals(ACTION_SET_GROUP)) {
         session.setAttribute(PARAM_CREDENTIAL,(String)request.getParameter(PARAM_CREDENTIAL));
         results.put(PARAM_CREDENTIAL,(String)request.getParameter(PARAM_CREDENTIAL));
      }
      
      if(action.equals(ACTION_GET_COMMUNITY_GROUPS)) {
         String communityName = (String)request.getParameter(PARAM_COMMUNITY_NAME);
         //call the delegate to do a groupList.addAll(ViewGroup(user,communityName));
      }

      return results;
   }  
}