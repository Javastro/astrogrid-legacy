
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

	private static final String ACTION_PARAM = "action" ;
	private static final String LOGIN_ACTION = "login" ;

	private static final String NAME_PARAM = "name" ;
	private static final String PASS_PARAM = "pass" ;

	private static final String POLICY_GROUP    = "guest" ;
	private static final String POLICY_ACTION   = "read" ;
	private static final String POLICY_RESOURCE = "portal.site" ;

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
		// Setup the error message.
		String error = null ;
		//
		// Setup our security token.
		SecurityToken token = null ;
		//
		// Setup our authentication
		boolean authorized = false ;
		//
		// If the action is login.
		if (LOGIN_ACTION.equals(action))
			{
			//
			// Get the user name and password from our request.
			String name = request.getParameter(NAME_PARAM);
			String pass = request.getParameter(PASS_PARAM);
			if (DEBUG_FLAG) System.out.println("LoginAction:login()") ;
			if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
			if (DEBUG_FLAG) System.out.println("  Pass : " + pass) ;

			//
			// Check for a null or blank name.
			if(null == name)
				{
				error = "Null name" ;
				}
			else {
				name = name.trim() ;
				if(name.length() <= 0)
					{
					error = "Blank name" ;
					}
				}

			//
			// Check for a null or blank password.
			if(null == pass)
				{
				error = "Null password" ;
				}
			else {
				pass = pass.trim() ;
				if(pass.length() <= 0)
					{
					error = "Blank password" ;
					}
				}

			//
			// Try creating a Community delegate.
			AuthenticationDelegate authenticator = new AuthenticationDelegate();
			if (null == authenticator)
				{
				if (DEBUG_FLAG) System.out.println("FAIL : Null AuthenticationDelegate") ;
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
						}
					}
				catch(Exception ouch)
					{
					error = "Login failed" ;
					ouch.printStackTrace();
					}
				}
			//
			// Try creating a policy delegate.
			PolicyServiceDelegate delegate = new PolicyServiceDelegate();
			if (null == delegate)
				{
				if (DEBUG_FLAG) System.out.println("FAIL : Null PolicyServiceDelegate") ;
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
						}
					else {
						if (DEBUG_FLAG) System.out.println("FAIL : Permission check failed") ;
						}
					}
				catch(Exception ouch)
					{
					error = "Permission check failed" ;
					ouch.printStackTrace();
					}
				}
			}

		Map results = new HashMap() ;
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
