/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/09 11:12:16 $</cvs:date>
 * <cvs:version>$Revision: 1.14 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: LoginAction.java,v $
 *   Revision 1.14  2004/01/09 11:12:16  dave
 *   Changes flag to healthy not pass - already used that.
 *
 *   Revision 1.13  2004/01/09 11:07:32  dave
 *   Added an early fail test to the login action
 *
 *   Revision 1.12  2004/01/09 10:17:54  dave
 *   Fixed account param in LoginAction
 *
 *   Revision 1.11  2004/01/09 10:13:42  dave
 *   Added message to login page
 *
 *   Revision 1.10  2004/01/09 10:07:39  dave
 *   Fixed reference to login-pass XSP page
 *
 *   Revision 1.9  2004/01/09 07:54:33  dave
 *   Added cvs logs
 *
 * </cvs:log>
 *
 */
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
 *
 */
public class LoginAction extends AbstractAction
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true;

	public static final String ACTION_PARAM = "action" ;
	public static final String LOGIN_ACTION = "login" ;

	public static final String NAME_PARAM = "name" ;
	public static final String PASS_PARAM = "pass" ;
	public static final String ACCOUNT_PARAM = "message" ;
	public static final String MESSAGE_PARAM = "account" ;

	public static final String POLICY_GROUP    = "guest" ;
	public static final String POLICY_ACTION   = "read" ;
	public static final String POLICY_RESOURCE = "portal.site" ;

	public static final String EMPTY_NAME_MESSAGE = "Please enter a valid account name" ;
	public static final String EMPTY_PASS_MESSAGE = "Please enter a valid password" ;
	public static final String SYSTEM_ERROR_MESSAGE = "System error - login failed" ;

	public static final String LOGIN_FAIL_MESSAGE = "Login rejected" ;
	public static final String LOGIN_PASS_MESSAGE = "Login accepted" ;

	public static final String POLICY_FAIL_MESSAGE = "Login rejected" ;
	public static final String POLICY_PASS_MESSAGE = "Login accepted" ;

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
		// Initialise our community config.
		init() ;
		//
		// Get our current request and session.
		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession();
		//
		// Get the action param from our request.
		String action = request.getParameter(ACTION_PARAM);
		if (DEBUG_FLAG) System.out.println("LoginAction:act()") ;
		if (DEBUG_FLAG) System.out.println("  Action : " + action) ;
		//
		// Setup our error message.
		String message = null ;
		//
		// Setup our security token.
		SecurityToken token = null ;
		//
		// Setup our authentication
		boolean authorized = false ;
		//
		// Setup our results map.
		Map results = null ;
		//
		// Setup our error flag.
		boolean healthy = true ;

		//
		// If the request action is login.
		if (LOGIN_ACTION.equals(action))
			{
			//
			// Clear the current account info.
			session.setAttribute("user", null) ;
			session.setAttribute("community_account", null) ;
			//
			// Get the user name and password from our request.
			String name = request.getParameter(NAME_PARAM);
			String pass = request.getParameter(PASS_PARAM);
			if (DEBUG_FLAG) System.out.println("LoginAction:login()") ;
			if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
			if (DEBUG_FLAG) System.out.println("  Pass : " + pass) ;

			if (healthy)
				{
				//
				// Check for a null or blank name.
				if(null == name)
					{
					message = EMPTY_NAME_MESSAGE ;
					}
				else {
					name = name.trim() ;
					if(name.length() <= 0)
						{
						healthy = false ;
						message = EMPTY_NAME_MESSAGE ;
						}
					}
				}

			if (healthy)
				{
				//
				// Check for a null or blank password.
				if(null == pass)
					{
					healthy = false ;
					message = EMPTY_PASS_MESSAGE ;
					}
				else {
					pass = pass.trim() ;
					if(pass.length() <= 0)
						{
						healthy = false ;
						message = EMPTY_PASS_MESSAGE ;
						}
					}
				}

			if (healthy)
				{
				//
				// Try creating a Community delegate.
				AuthenticationDelegate authenticator = new AuthenticationDelegate();
				if (null == authenticator)
					{
					if (DEBUG_FLAG) System.out.println("FAIL : Null AuthenticationDelegate") ;
					healthy = false ;
					message = SYSTEM_ERROR_MESSAGE ;
					}
				//
				// 
				else {
					if (DEBUG_FLAG) System.out.println("PASS : Got AuthenticationDelegate") ;
					//
					// Try logging in to the Community.
					try {
						token = authenticator.authenticateLogin(name, pass);
						if (null == token)
							{
							if (DEBUG_FLAG) System.out.println("FAIL : Null token") ;
							healthy = false ;
							message = LOGIN_FAIL_MESSAGE ;
							}
						else {
							if (DEBUG_FLAG) System.out.println("PASS : Got token") ;
							if (DEBUG_FLAG) System.out.println("  Token   : " + token) ;
							if (DEBUG_FLAG) System.out.println("  Token   : " + token.getToken()) ;
							if (DEBUG_FLAG) System.out.println("  Used    : " + token.getUsed()) ;
							if (DEBUG_FLAG) System.out.println("  Target  : " + token.getTarget()) ;
							if (DEBUG_FLAG) System.out.println("  Account : " + token.getAccount()) ;
							if (DEBUG_FLAG) System.out.println("  Start   : " + token.getStartDate()) ;
							if (DEBUG_FLAG) System.out.println("  End     : " + token.getExpirationDate()) ;
							message = LOGIN_PASS_MESSAGE ;
							}
						}
					catch(Exception ouch)
						{
						healthy = false ;
						message = SYSTEM_ERROR_MESSAGE ;
						ouch.printStackTrace();
						}
					}
				}

			if (healthy)
				{
				//
				// Try creating a policy delegate.
				PolicyServiceDelegate delegate = new PolicyServiceDelegate();
				if (null == delegate)
					{
					if (DEBUG_FLAG) System.out.println("FAIL : Null PolicyServiceDelegate") ;
					healthy = false ;
					message = SYSTEM_ERROR_MESSAGE ;
					}
				//
				// 
				else {
					if (DEBUG_FLAG) System.out.println("PASS : Got PolicyServiceDelegate") ;
					//
					// Check if the account has permission to see the site.
					try {
						authorized = delegate.checkPermissions(name, POLICY_GROUP, POLICY_RESOURCE, POLICY_ACTION);
						if (authorized)
							{
							if (DEBUG_FLAG) System.out.println("PASS : Permission check ok") ;
							message = POLICY_PASS_MESSAGE ;
							}
						else {
							if (DEBUG_FLAG) System.out.println("FAIL : Permission check failed") ;
							healthy = false ;
							message = POLICY_FAIL_MESSAGE ;
							}
						}
					catch(Exception ouch)
						{
						healthy = false ;
						message = SYSTEM_ERROR_MESSAGE ;
						ouch.printStackTrace();
						}
					}
				}

			//
			// If we pass the tests.
			if ((healthy) && (null != token) && (authorized))
				{
				//
				// Set the current account info in our session.
				session.setAttribute("user", name) ;
				session.setAttribute("community_account", token.getAccount()) ;
				//
				// Set the current account info in our request.
				request.setAttribute(ACCOUNT_PARAM, token.getAccount());
				//
				// Add our results.
				results = new HashMap() ;
				results.put("account", token.getAccount());
				}

			//
			// Add our display message.
			request.setAttribute(MESSAGE_PARAM, message);

			}

		return results;
		}

	/**
	 * Load our community config.
	 *
	 */
	public void init()
		{
		if (DEBUG_FLAG) System.out.println("LoginAction:init()") ;
		//
		// Load our community config.
		CommunityConfig.loadConfig();
		}

   /**
    * Our action method.
    *
    */
   public Map frog(
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


      
      //AdministrationDelegate adminDelegate = new AdministrationDelegate();
      CommunityConfig.loadConfig();
      String commName = CommunityConfig.getCommunityName();
      if(commName == null || commName.length() <= 0) {
         errorMessage = "No community name is found and is required for logging in.";
      }else {
         session.setAttribute("community_name", commName); 
      }
      message = "This portals community branch is " + commName;
      
      String action = request.getParameter(ACTION_PARAM);
      
      String secureConn = CommunityConfig.getProperty("portal.security","on");
      String securePort = CommunityConfig.getProperty("portal.secure.port",String.valueOf(request.getServerPort()));
      String nonSecurePort = CommunityConfig.getProperty("portal.nonsecure.port",String.valueOf(request.getServerPort()));      
//      System.out.println("secure port = " + securePort);
//      System.out.println("non-secure port = " + nonSecurePort);
      
      String secure_url = null;

	/*
	 *

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

	 *
	 */

      String admin = CommunityConfig.getAdministrator();
      String adminEmail = CommunityConfig.getAdministratorEmail();
      
      String user = request.getParameter("name");
      String password = request.getParameter("pass");
      
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
//            password = String.valueOf(password.hashCode());
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
//      results.put("secure_connection",secureConn);
//      results.put("secure_url",secure_url);
      results.put("message",message);
      results.put("errormessage",errorMessage);
      results.put("admin",admin);      
      results.put("adminEmail",adminEmail);      
      return results;
   }  
}
