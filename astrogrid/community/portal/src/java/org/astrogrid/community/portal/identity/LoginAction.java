/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/portal/src/java/org/astrogrid/community/portal/identity/Attic/LoginAction.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2004/01/07 10:45:40 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: LoginAction.java,v $
 * Revision 1.2  2004/01/07 10:45:40  dave
 * Merged development branch, dave-dev-20031224, back into HEAD
 *
 * Revision 1.1.2.1  2004/01/06 14:21:43  dave
 * Tidied up directory tree ....
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.community.portal.identity ;

//import java.io.File;
import java.util.Map;
import java.util.HashMap;
//import java.util.Iterator;

//import org.xml.sax.EntityResolver;

import org.apache.avalon.framework.parameters.Parameters;
//import org.apache.avalon.framework.parameters.ParameterException ;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;

/**
 * A Cocoon action to handle user login.
 *
 */
public class LoginAction
	extends AbstractAction
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * The name of the action request param.
	 *
	 */
	public static final String ACTION_PARAM_TAG = "action" ;

	/**
	 * The login action value.
	 *
	 */
	public static final String LOGIN_ACTION = "login" ;

	/**
	 * The name of the account name request param.
	 *
	 */
	public static final String LOGIN_NAME_TAG = "username" ;

	/**
	 * The name of the password request param.
	 *
	 */
	public static final String LOGIN_PASS_TAG = "password" ;

	/**
	 * The name of the account session param.
	 *
	 */
	public static final String ASTRO_ACCOUNT_TAG = "org.astrogrid.community.account" ;

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
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("LoginAction.act()") ;

		//
		// Get our current request and session.
		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession();
		//
		// Create a new HashMap for our results.
		Map results = new HashMap() ;

		//
		// Get our current account from our session.
		//String account = (String) session.getAttribute(ACCOUNT_TAG) ;
		//if (DEBUG_FLAG) System.out.println("Account : " + account) ;

		//
		// Get the current action from our request.
		String action = request.getParameter(ACTION_PARAM_TAG) ;
		if (DEBUG_FLAG) System.out.println("Action  : " + action) ;

		//
		// If the action is create-view.
		if (LOGIN_ACTION.equals(action))
			{
			//
			// Get the name and password from our request params.
			String name = request.getParameter(LOGIN_NAME_TAG) ;
			String pass = request.getParameter(LOGIN_PASS_TAG) ;
			if (DEBUG_FLAG) System.out.println("Name  : " + name) ;
			if (DEBUG_FLAG) System.out.println("Pass  : " + pass) ;
			}
		return results ;
		}
	}
