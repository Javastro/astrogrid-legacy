
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
import java.util.LinkedHashMap;
import org.astrogrid.community.delegate.policy.AdministrationDelegate;
import org.astrogrid.community.policy.data.GroupData;
import org.astrogrid.community.common.CommunityConfig;
import org.astrogrid.community.policy.data.CommunityData;
import org.astrogrid.community.policy.data.AccountData;
import org.astrogrid.community.policy.data.ResourceData;

/**
 *
 * AdministrationAction is a custom cocoon Action used for the Administraiton page.  And is used in conjunction with
 * astrogrid.xsl and astrogrid.xsp
 * 
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
   
   private static final String ACTION_VIEW_ACCOUNTS = "viewaccounts";   
   
   private static final String ACTION_VIEW_COMMUNITY = "viewcommunity";   
   
   private static final String ACTION_VIEW_RESOURCES = "viewresources";
   
   private static final String ACTION_VIEW_MEMBERS = "viewmembers";   
   
   private static final String ACTION_CHANGE_PASSWORD = "changeofpassword";
   
   private static final String IDENT = "ident";
   
   private static final String PROCESS_ACTION = "processaction";   

   private static final String PARAM_COMMUNITY_LIST = "communitylist";   
   
   private static final String PARAM_RESOURCE_LIST = "resourcelist";   
   
   private static final String PARAM_ACCOUNT_LIST = "accountlist";   

   private static final String PARAM_GROUP_LIST = "grouplist";
      
   private static final String PARAM_MEMBERS_LIST = "memberslist";   
   
   private static final String HTTPS_CONNECTION = "on";   
   
		
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
	      
      String community_name = CommunityConfig.getCommunityName();
      System.out.println(community_name);
      
		//
		// Get our current request and session.
		Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();

      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      
            
      String errorMessage = null;
      String message = null;
      
		String action = request.getParameter(ACTION);
      String processAction = request.getParameter(PROCESS_ACTION);
      
      if(processAction != null && processAction.length() > 0 ) {
         action = processAction;
      }
      

      String secureConn = CommunityConfig.getProperty("portal.security","on");
      String securePort = CommunityConfig.getProperty("portal.secure.port",String.valueOf(request.getServerPort()));
      String nonSecurePort = CommunityConfig.getProperty("portal.nonsecure.port",String.valueOf(request.getServerPort()));      
      
      String redirect_url = null;
      if(HTTPS_CONNECTION.equals(secureConn) && 
         (ACTION_INSERT_ACCOUNT.equals(action) || ACTION_CHANGE_PASSWORD.equals(action) )) {
         if(!request.isSecure()) {
            if(securePort == null || securePort.length() <= 0) {
               errorMessage = "Cannot find a secure url port for redirecting which is required for this operation.";
               results.put(ACTION,action);
               results.put("errormessage",errorMessage);
               results.put("message",message);
               return results;               
            }else {
               try {
                  redirect_url = "https://" + request.getServerName() + ":" +
                               securePort + request.getRequestURI() + "?" +
                               request.getQueryString();
                  System.out.println(redirect_url);
                  redirector.redirect(true,redirect_url);
                }catch(Exception e) {
                   e.printStackTrace();
                }
            }
         }
      }else {
         if(request.isSecure()) {
            try {
               redirect_url = "http://" + request.getServerName() + ":" +
                            nonSecurePort + request.getRequestURI() + "?" +
                            request.getQueryString();
               System.out.println(redirect_url);
               redirector.redirect(true,redirect_url);
             }catch(Exception e) {
                e.printStackTrace();
             }
         }
      }
      
		if(DEBUG_FLAG) {
			System.out.println("the action is = " + action);		
		}
      AdministrationDelegate adminDelegate = new AdministrationDelegate();

      String ident = (String)request.getParameter(IDENT);
      if(ident != null && ident.trim().length() > 0 ) {
         ident = ident.trim();
         ident = ident.toLowerCase();  
      }
      boolean isAdmin = false;
      LinkedHashMap actionTable = new LinkedHashMap();
      String comm_account = (String)session.getAttribute("community_account");
      if(comm_account == null || comm_account.length() <= 0) {
         comm_account = (String)session.getAttribute("user");
      }

/*
      try {
         adminDelegate.getPassword(comm_account);
      }catch(Exception e) {
        e.printStackTrace();  
      }      
*/
      if(comm_account == null) {
         actionTable.put(ACTION_INSERT_ACCOUNT,"Insert Account");
      }else {
         try {
            isAdmin = adminDelegate.isAdminAccount(comm_account,community_name);
         }catch(Exception e) {
            e.printStackTrace();
            isAdmin = false;
         }

         if(isAdmin) {
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
            actionTable.put(ACTION_INSERT_PERMISSION,"Insert Permission");
            actionTable.put(ACTION_REMOVE_PERMISSION,"Remove Permission");
            actionTable.put(ACTION_VIEW_COMMUNITY,"View Community");
            actionTable.put(ACTION_VIEW_ACCOUNTS,"View Accounts");
            actionTable.put(ACTION_VIEW_RESOURCES,"View Resources");
            actionTable.put(ACTION_VIEW_GROUPS,"View Groups");
            actionTable.put(ACTION_CHANGE_PASSWORD,"Change of Password");
         }else {            
            actionTable.put(ACTION_VIEW_GROUPS,"View Groups");
            actionTable.put(ACTION_CHANGE_PASSWORD,"Change of Password");
         }
      }      
      
      session.setAttribute("actionlist",actionTable);
      
      String community = request.getParameter("community");
// DAVE 2003.09.18 19:53
//      if(community == null || community.length() > 0) {
      if(community == null || community.length() <= 0) {
         community = (String)session.getAttribute("community_name");
      }
      
      
      if(ACTION_REMOVE_GROUP.equals(action) || ACTION_VIEW_GROUPS.equals(action) ||
         ACTION_REMOVE_MEMBER.equals(action) || ACTION_INSERT_MEMBER.equals(action) ||
         ACTION_INSERT_PERMISSION.equals(action) || ACTION_REMOVE_PERMISSION.equals(action)) {
        try {
         ArrayList al = adminDelegate.getGroupList(community);
         session.setAttribute(PARAM_GROUP_LIST,al);
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      
      if(ACTION_REMOVE_COMMUNITY.equals(action) || ACTION_VIEW_COMMUNITY.equals(action) ||
         ACTION_INSERT_MEMBER.equals(action)) {
        try {
         ArrayList al = adminDelegate.getCommunityList();
         session.setAttribute(PARAM_COMMUNITY_LIST,al);
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      
      if(ACTION_REMOVE_RESOURCE.equals(action) ||  ACTION_INSERT_PERMISSION.equals(action)
         || ACTION_REMOVE_PERMISSION.equals(action) || ACTION_VIEW_RESOURCES.equals(action) ) {
        try {
         ArrayList al = adminDelegate.getResourceList();
         session.setAttribute(PARAM_RESOURCE_LIST,al);
           
        }catch(Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
        }
      }
      
      
      if(ACTION_REMOVE_ACCOUNT.equals(action) || ACTION_REMOVE_MEMBER.equals(action) 
         || ACTION_INSERT_MEMBER.equals(action) || ACTION_VIEW_ACCOUNTS.equals(action)) {
              try {
               ArrayList al = adminDelegate.getAccountList(community);
               session.setAttribute(PARAM_ACCOUNT_LIST,al);
              }catch(Exception e) {
                  errorMessage = e.toString();
                  e.printStackTrace();
              }
       }      
      
      if(ACTION_INSERT_GROUP.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            
            try {
               GroupData gd = adminDelegate.addGroup(ident);
               gd.setDescription(request.getParameter("description"));
               gd.setType(GroupData.MULTI_TYPE);
               adminDelegate.setGroup(gd);
               message = "Group was Inserted.";
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
            if(ident.indexOf("admin") != -1 || ident.indexOf("guest") != -1) {
               errorMessage = "You cannot delete Admin or Guest group";
            }else {
               try {
                  adminDelegate.delGroup(ident);
                  message = "Group was deleted";
               }catch(Exception e) {
                  errorMessage = e.toString();
                  e.printStackTrace();
               }
            }
         }else {
            errorMessage = "No group found to remove";
         } 
      } else if(ACTION_INSERT_COMMUNITY.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               CommunityData commData = adminDelegate.addCommunity(ident);
               String managerURL = request.getParameter("managerurl");
               String serviceURL = request.getParameter("serviceurl");
               message = "Community inserted.";
               if(managerURL != null && managerURL.length() > 0) {
                  commData.setManagerUrl(managerURL);

               }
               if(serviceURL != null && serviceURL.length() > 0) {
                  commData.setServiceUrl(serviceURL);
               }
               if(serviceURL != null || managerURL != null) {
                  adminDelegate.setCommunity(commData);                  
               }

            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "Cannot insert an empty community";
         } 
      } else if(ACTION_REMOVE_COMMUNITY.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delCommunity(ident);
               message = "Community deleted";
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No insert empty community";
         } 
      } else if(ACTION_INSERT_ACCOUNT.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            String pass = request.getParameter("password");
            if(pass == null || pass.trim().length() <= 0) {
               errorMessage = "You cannot have no or empty password";
            }
            else {
               int passwordHashed = pass.hashCode();
               try {
                  AccountData ad = adminDelegate.addAccount(ident);
                  if(ad != null) {
                     ad.setDescription(request.getParameter("description"));
                     ad.setPassword(String.valueOf(passwordHashed));
                     adminDelegate.setAccount(ad);                  
                  }
                  message = "Account inserted.";
               }catch(Exception e) {
                  errorMessage = e.toString();
                  e.printStackTrace();
               }
            }
         }else {
            errorMessage = "No account given to insert";
         } 
      } else if(ACTION_REMOVE_ACCOUNT.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               adminDelegate.delAccount(ident);
               message = "Account deleted";
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No account given to remove";
         } 
      } else if(ACTION_INSERT_PERMISSION.equals(processAction)) {

         if(ident != null && ident.length() > 0) {
            try {
               System.out.println("entered insertpermission = " + ident);
               String permGroup = request.getParameter("group");
               String permAction = request.getParameter("policy");
               if(permGroup == null || permGroup.trim().length() <= 0 ||
                  permAction == null || permAction.trim().length() <= 0) {
                  errorMessage = "both the group and action must be filled in.";
               }else {
                  permGroup = permGroup.trim().toLowerCase();
                  permAction = permAction.trim().toLowerCase();
                  adminDelegate.addPermission(ident,permGroup,permAction);
                  message = "Permission inserted";
               }
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "All parameters required for inserting permission";
         } 
      } else if(ACTION_REMOVE_PERMISSION.equals(processAction)) {
         if(ident != null && ident.length() > 0) {
            try {
               String permGroup = request.getParameter("group");
               String permAction = request.getParameter("policy");
               if(permGroup == null || permGroup.trim().length() <= 0 ||
                  permAction == null || permAction.trim().length() <= 0) {
                  errorMessage = "both the group and action must be filled in.";
               }else {
                  permGroup = permGroup.trim().toLowerCase();
                  permAction = permAction.trim().toLowerCase();                  
                  adminDelegate.delPermission(ident,permGroup,permAction);
                  message = "Permission removed";                  
               }               
               
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();
            }
         }else {
            errorMessage = "No Permission given to remove.";
         } 
      } else if(ACTION_CHANGE_PASSWORD.equals(processAction)) {
         ident = (String)session.getAttribute("community_account");
         if(ident != null && ident.length() > 0) {
            try {
               ident = (String)session.getAttribute("community_account");
               String accountPassword = adminDelegate.getPassword(ident);
               AccountData ad = adminDelegate.getAccount(ident);
               String password = request.getParameter("current_password");
               String newpassword = request.getParameter("new_password");
               String verifypassword = request.getParameter("verify_password");
               if(accountPassword.equals(password)) {
                  if(newpassword.equals(verifypassword)) {
                     if(newpassword == null || newpassword.trim().length() <= 0) {
                        errorMessage = "You cannot have an empty new password";
                     }else {
                        int newPasswordHashed = newpassword.hashCode();
                        ad.setPassword(String.valueOf(newPasswordHashed));
                        adminDelegate.setAccount(ad);
                        message = "Account's password changed.";                        
                     }
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
               message = "Member added.";
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
               message = "Member removed.";
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
                    message = "Resource inserted.";
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
                 message = "Resource removed.";
              }catch(Exception e) {
                 errorMessage = e.toString();
                 e.printStackTrace();
              }
           }else {
              errorMessage = "No insert empty community";
           } 
      }      
		
		//
		results.put(ACTION,action);
      results.put("errormessage",errorMessage);
      results.put("message",message);
		return results;
	}	
}