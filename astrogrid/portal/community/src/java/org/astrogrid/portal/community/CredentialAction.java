
package org.astrogrid.portal.cocoon.community;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.astrogrid.community.common.CommunityConfig;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.astrogrid.community.delegate.policy.AdministrationDelegate;

/**
 *
 * CredentialAction is a small custom cocoon Action object for handling the credentials page.  Which consist of letting the
 * user chose his/her credentials from a list of groups that come from a local or a remote community.
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
   
   private static final String PARAM_COMMUNITY = "community";   

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
      
      String comm_account = (String)session.getAttribute("community_account");
      if(comm_account == null || comm_account.length() <= 0) {
         comm_account = (String)session.getAttribute("user");
      } 


      String action = request.getParameter(ACTION);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
      }
      
      AdministrationDelegate adminDelegate = new AdministrationDelegate();
      ArrayList communityList = (ArrayList)session.getAttribute(PARAM_COMMUNITY_LIST);
      
      if(communityList == null || communityList.size() <= 0) {
         //call delegate to get the communities trusted by this local community.
         try {
            communityList = adminDelegate.getCommunityList();
         }catch(Exception e) {
            e.printStackTrace();
         }
         session.setAttribute(PARAM_COMMUNITY_LIST,communityList);
      }
     
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
          
      String community = (String)request.getParameter(PARAM_COMMUNITY);
      if(community == null || community.length() <= 0) {
         community = CommunityConfig.getCommunityName();
      }
      
      if(ACTION_SET_GROUP.equals(action)) {
         session.setAttribute(PARAM_CREDENTIAL,(String)request.getParameter(PARAM_CREDENTIAL));
         results.put(PARAM_CREDENTIAL,(String)request.getParameter(PARAM_CREDENTIAL));
      }
      
         try {
            ArrayList groupList = adminDelegate.getAccountGroups(comm_account, community);
            session.setAttribute(PARAM_GROUP_LIST,groupList);
         }catch(Exception e) {
            e.printStackTrace();
         }
         
         String credential = (String)session.getAttribute(PARAM_CREDENTIAL);
         results.put(PARAM_CREDENTIAL,credential);         

      return results;
   }  
}