
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
import java.util.Hashtable;
import org.astrogrid.community.delegate.policy.AdministrationDelegate;
import org.astrogrid.community.policy.data.GroupData;
import org.astrogrid.community.policy.data.AccountData;
import org.astrogrid.community.policy.data.ResourceData;

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

	private static final String ACTION_INSERT_MEMBER = "insertmember";
	
	private static final String ACTION_INSERT_PERMISSION = "insertpermission";
	
	private static final String ACTION_INSERT_GROUP = "insertgroup";
	
	private static final String ACTION_INSERT_COMMUNITY = "insertcommunity";
	
	private static final String ACTION_REMOVE_RESOURCE = "removeresource";

	private static final String ACTION_REMOVE_MEMBER = "removemember";

	private static final String ACTION_REMOVE_PERMISSION = "removepermission";

	private static final String ACTION_REMOVE_GROUP = "removegroup";
	
	private static final String ACTION_REMOVE_COMMUNITY = "removecommunity";
	
	private static final String ACTION_REMOVE_ACCOUNT = "removeaccount";
   
   private static final String ACTION_INSERT_ACCOUNT = "insertaccount";   
		
	private static final String ACTION_VIEW_GROUPS = "viewgroups";	
   
   private static final String ACTION_CHANGE_PASSWORD = "changeofpassword";
   
   private static final String IDENT = "ident";
   
   private static final String PROCESS_ACTION = "processaction";   

   private static final String PARAM_COMMUNITY_LIST = "communitylist";   
   
   private static final String PARAM_RESOURCE_LIST = "resourcelist";   
   
   private static final String PARAM_ACCOUNT_LIST = "accountlist";   

   private static final String PARAM_GROUP_LIST = "grouplist";   
	
		
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
      
            
      String errorMessage = null;

		String action = request.getParameter(ACTION);
      String processAction = request.getParameter(PROCESS_ACTION);
      
      if(processAction != null && processAction.length() > 0 ) {
         action = processAction;
      }
		if(DEBUG_FLAG) {
			System.out.println("the action is = " + action);		
		}
      AdministrationDelegate adminDelegate = new AdministrationDelegate();
      String ident = (String)request.getParameter(IDENT);

      Hashtable actionTable = new Hashtable();
      actionTable.put(ACTION_INSERT_ACCOUNT,"Insert Account");
      actionTable.put(ACTION_REMOVE_ACCOUNT,"Remove Account");
      actionTable.put(ACTION_INSERT_GROUP,"Insert Group");
      actionTable.put(ACTION_REMOVE_GROUP,"Remove Group");
      actionTable.put(ACTION_INSERT_COMMUNITY,"Insert Community");
      actionTable.put(ACTION_REMOVE_COMMUNITY,"Remove Community");
      actionTable.put(ACTION_INSERT_RESOURCE,"Insert Resource");
      actionTable.put(ACTION_REMOVE_RESOURCE,"Remove Resource");
      actionTable.put(ACTION_INSERT_MEMBER,"Insert Member");
      actionTable.put(ACTION_REMOVE_MEMBER,"Remove Member");
      actionTable.put(ACTION_VIEW_GROUPS,"View Groups");
      actionTable.put(ACTION_CHANGE_PASSWORD,"Change of Password");
      
      
      session.setAttribute("actionlist",actionTable);
      
      if(ACTION_REMOVE_GROUP.equals(action) || ACTION_VIEW_GROUPS.equals(action) ||
         ACTION_REMOVE_MEMBER.equals(action) || ACTION_INSERT_MEMBER.equals(action)) {
        try {
         ArrayList al = adminDelegate.getGroupList();
         session.setAttribute(PARAM_GROUP_LIST,al);
           
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      if(ACTION_REMOVE_COMMUNITY.equals(action)) {
        try {
         ArrayList al = adminDelegate.getCommunityList();
         session.setAttribute(PARAM_COMMUNITY_LIST,al);
           
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      
      if(ACTION_REMOVE_RESOURCE.equals(action)) {
        try {
         ArrayList al = adminDelegate.getResourceList();
         session.setAttribute(PARAM_RESOURCE_LIST,al);
           
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      
      
      if(ACTION_REMOVE_ACCOUNT.equals(action) || ACTION_REMOVE_MEMBER.equals(action) 
         || ACTION_INSERT_MEMBER.equals(action)) {
              try {
               ArrayList al = adminDelegate.getAccountList();
               session.setAttribute(PARAM_ACCOUNT_LIST,al);
              }catch(Exception e) {
                  errorMessage = e.toString();
                  e.printStackTrace();
              }
       }      
      
      if(ACTION_INSERT_GROUP.equals(processAction)) {
         System.out.println("Entering INSERT Group ident val = " + ident);
         if(ident != null && ident.length() > 0) {
            
            try {
               GroupData gd = adminDelegate.addGroup(ident);
               gd.setDescription(request.getParameter("description"));
               gd.setType(GroupData.MULTI_TYPE);
               adminDelegate.setGroup(gd);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "Cannot insert an empty group";
         }
      }
      else if(ACTION_REMOVE_GROUP.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delGroup(ident);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No group found to remove";
         } 
      } else if(ACTION_INSERT_COMMUNITY.equals(processAction)) {
         System.out.println("Entering INSERT Community ident val = " + ident);
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.addCommunity(ident);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_REMOVE_COMMUNITY.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delCommunity(ident);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_INSERT_ACCOUNT.equals(processAction)) {
         System.out.println("Entering INSERT account ident val = " + ident);
         if(ident != null && ident.length() > 0) {
            try {
               AccountData ad = adminDelegate.addAccount(ident);
               ad.setDescription(request.getParameter("description"));
               ad.setPassword(request.getParameter("password"));
               adminDelegate.setAccount(ad);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_REMOVE_ACCOUNT.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delAccount(ident);
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_CHANGE_PASSWORD.equals(processAction)) {
         ident = (String)session.getAttribute("community_account");
         if(ident != null && ident.length() > 0) {
            try {
               ident = (String)session.getAttribute("community_account");
               AccountData ad = adminDelegate.getAccount(ident);
               String password = request.getParameter("current_password");
               String newpassword = request.getParameter("new_password");
               String verifypassword = request.getParameter("verify_password");
               if(ad.getPassword().equals(password)) {
                  if(newpassword.equals(verifypassword)) {
                     ad.setPassword(newpassword);
                     adminDelegate.setAccount(ad);
                  }else {
                     errorMessage = "Your new password and the verification password do not match.";
                  }
               }else {
                  errorMessage = "Your current password is not what is in our system.";
               }
               
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_INSERT_MEMBER.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.addGroupMember(ident,request.getParameter("group"));
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_REMOVE_MEMBER.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delGroupMember(ident,request.getParameter("group"));
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_INSERT_RESOURCE.equals(processAction)) {
              if(ident != null && ident.length() > 0) {
                 try {
                    ResourceData rd = adminDelegate.addResource(ident);
                    rd.setDescription(request.getParameter("description"));
                    adminDelegate.setResource(rd);
                 }catch(Exception e) {
                    errorMessage = e.toString();
                    e.printStackTrace();
                 }
              }else {
                 errorMessage = "No insert empty community";
              } 
      } else if(ACTION_REMOVE_RESOURCE.equals(processAction)) {
           if(ident != null && ident.length() > 0) {
              try {
                 adminDelegate.delResource(ident);
              }catch(Exception e) {
                 errorMessage = e.toString();
                 e.printStackTrace();
              }
           }else {
              errorMessage = "No insert empty community";
           } 
      }      


      
		
		//
		//Create a new HashMap for our results.  Will be used to
		//pass to the transformer (xsl page)
		Map results = new HashMap();
		results.put(ACTION,action);
		return results;
	}	
}