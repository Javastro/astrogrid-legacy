
package org.astrogrid.portal.cocoon.common;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Context;
import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.delegate.authentication.AuthenticationDelegate;
import org.astrogrid.community.service.authentication.data.SecurityToken;


import org.astrogrid.community.common.CommunityConfig;
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
public class LoginAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   private static final String PARAM_ACTION = "action";
   
   private static final String LOGIN_ACTION = "login";
   
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
      
      //
      // Get our current request and session.
      Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();
      
      String errorMessage = null;
      String message = null;

      System.out.println("username = " + request.getParameter("Username"));
      
      //AdministrationDelegate adminDelegate = new AdministrationDelegate();
      CommunityConfig.loadConfig();
      String commName = CommunityConfig.getCommunityName();
      if(commName == null || commName.length() <= 0) {
         errorMessage = "No community name is found and is required for logging in.";
      }else {
         session.setAttribute("community_name", commName); 
      }
      message = "This portals community branch is " + commName;
      
      String action = request.getParameter(PARAM_ACTION);
      
      String secureConn = CommunityConfig.getProperty("portal.security","on");
      String securePort = CommunityConfig.getProperty("portal.secure.port",String.valueOf(request.getServerPort()));
      String nonSecurePort = CommunityConfig.getProperty("portal.nonsecure.port",String.valueOf(request.getServerPort()));      
      System.out.println("secure port = " + securePort);
      System.out.println("non-secure port = " + nonSecurePort);
      
      String secure_url = null;
      if(HTTPS_CONNECTION.equals(secureConn)) {
         
         if(!request.isSecure()) {
            if(securePort == null || securePort.length() <= 0) {
               errorMessage = "No security port could be found and is required for a secure ssl connection for loggin in.";   
            } else {
               try {
                  secure_url = "https://" + request.getServerName() + ":" + securePort + request.getRequestURI();
                  redirector.redirect(false,secure_url);
                }catch(Exception e) {
                   e.printStackTrace();
                }
            }
         }
      }
      
      String admin = CommunityConfig.getAdministrator();
      String adminEmail = CommunityConfig.getAdministratorEmail();
      
      String user = request.getParameter("Username");
      String password = request.getParameter("Password");
      
 // authentication
      
      if(LOGIN_ACTION.equals(action)) {
         if(user == null || user.trim().length() <= 0) {
            errorMessage = "User cannot be empty";
         }else {
            user = user.trim().toLowerCase();
         }
         if(password == null || password.trim().length() <= 0) {
            errorMessage = "Password cannot be empty";
         }else {
            password = password.trim();
         }
         
         if(errorMessage == null || errorMessage.trim().length() <= 0) {
            AuthenticationDelegate ad = new AuthenticationDelegate();
            try {
               SecurityToken secToken = ad.authenticateLogin(user,password);
               String token = secToken.getToken();
               session.setAttribute("token", secToken);
               System.out.println("the token = " + token);
               if(secToken.getUsed() == Boolean.TRUE)
               {
                  errorMessage = "Login Failed";
               }
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();              
            }
         }
         
         //call authenticateLogin()         
         if(errorMessage == null || errorMessage.trim().length() <= 0) {         
            PolicyServiceDelegate psd = new PolicyServiceDelegate();
            try {
               boolean authorized = psd.checkPermissions(user,"guest","portal.site","read");
               if(!authorized) {
                  errorMessage = "You are not authorized to enter the portal site.";  
               }
            }catch(Exception e) {
               errorMessage = e.toString();
               e.printStackTrace();  
            }
         }

         
         
         //call authorizePortal()
         if(errorMessage == null || errorMessage.trim().length() <= 0) {
            session.setAttribute("user",user);
            session.setAttribute("community_account",user);            
   
            String index_url = "http://" + request.getServerName() + ":" + nonSecurePort + "/cocoon/astrogrid/agindex.html";
            session.setAttribute("credential","guest@" + commName);
            System.out.println("the index_url = " + index_url);
            try {
               redirector.redirect(true,index_url);
            }catch(Exception e) {
               e.printStackTrace();
            }
         }
      }

      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put("secure_connection",secureConn);
      results.put("secure_url",secure_url);
      results.put("message",message);
      results.put("errormessage",errorMessage);
      results.put("admin",admin);      
      results.put("adminEmail",adminEmail);      
      return results;
   }  
}