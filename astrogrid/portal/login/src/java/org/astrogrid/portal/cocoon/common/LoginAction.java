/**
 * <cvs:id>$Id: LoginAction.java,v 1.19 2004/03/19 13:02:25 jdt Exp $</cvs:id>
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 13:02:25 $</cvs:date>
 * <cvs:version>$Revision: 1.19 $</cvs:version>
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
public class LoginAction extends AbstractAction {
    /**
     * Switch for our debug statements.
     *  
     */
    public static boolean DEBUG_FLAG = true;
    public static final String ACTION_PARAM = "action";
    public static final String LOGIN_ACTION = "login";
    public static final String NAME_PARAM = "name";
    public static final String PASS_PARAM = "pass";
    public static final String ACCOUNT_PARAM = "message";
    public static final String MESSAGE_PARAM = "account";
    public static final String POLICY_GROUP = "guest";
    public static final String POLICY_ACTION = "read";
    public static final String POLICY_RESOURCE = "portal.site";
    public static final String EMPTY_NAME_MESSAGE =
        "Please enter a valid account name";
    public static final String EMPTY_PASS_MESSAGE =
        "Please enter a valid password";
    public static final String SYSTEM_ERROR_MESSAGE =
        "System error - login failed";
    public static final String LOGIN_FAIL_MESSAGE = "Login rejected";
    public static final String LOGIN_PASS_MESSAGE = "Login accepted";
    public static final String POLICY_FAIL_MESSAGE = "Login rejected";
    public static final String POLICY_PASS_MESSAGE = "Login accepted";
    /**
     * Our action method.
     *  
     */
    public Map act(
        Redirector redirector,
        SourceResolver resolver,
        Map objectModel,
        String source,
        Parameters params) {
        //
        // Initialise our community config.
        init();
        //
        // Get our current request and session.
        Request request = ObjectModelHelper.getRequest(objectModel);
        Session session = request.getSession();
        //
        // Get the action param from our request.
        String action = request.getParameter(ACTION_PARAM);
        if (DEBUG_FLAG)
            System.out.println("LoginAction:act()");
        if (DEBUG_FLAG)
            System.out.println("  Action : " + action);
        //
        // Setup our error message.
        String message = null;
        //
        // Setup our security token.
        SecurityToken token = null;
        //
        // Setup our authentication
        boolean authorized = false;
        //
        // Setup our results map.
        Map results = null;
        //
        // Setup our error flag.
        boolean healthy = true;
        //
        // If the request action is login.
        if (LOGIN_ACTION.equals(action)) {
            //
            // Clear the current account info.
            session.setAttribute("user", null);
            session.setAttribute("community_account", null);
            session.setAttribute(
                "community_name",
                CommunityConfig.getCommunityName());
            //
            // Get the user name and password from our request.
            String name = request.getParameter(NAME_PARAM);
            String pass = request.getParameter(PASS_PARAM);
            if (DEBUG_FLAG)
                System.out.println("LoginAction:login()");
            if (DEBUG_FLAG)
                System.out.println("  Name : " + name);
            if (DEBUG_FLAG)
                System.out.println("  Pass : " + pass);
            if (healthy) {
                //
                // Check for a null or blank name.
                if (null == name) {
                    message = EMPTY_NAME_MESSAGE;
                } else {
                    name = name.trim();
                    if (name.length() <= 0) {
                        healthy = false;
                        message = EMPTY_NAME_MESSAGE;
                    }
                }
            }
            if (healthy) {
                //
                // Check for a null or blank password.
                if (null == pass) {
                    healthy = false;
                    message = EMPTY_PASS_MESSAGE;
                } else {
                    pass = pass.trim();
                    if (pass.length() <= 0) {
                        healthy = false;
                        message = EMPTY_PASS_MESSAGE;
                    }
                }
            }
            if (healthy) {
                //
                // Try creating a Community delegate.
                AuthenticationDelegate authenticator =
                    new AuthenticationDelegate();
                if (null == authenticator) {
                    if (DEBUG_FLAG)
                        System.out.println(
                            "FAIL : Null AuthenticationDelegate");
                    healthy = false;
                    message = SYSTEM_ERROR_MESSAGE;
                }
                //
                // 
                else {
                    if (DEBUG_FLAG)
                        System.out.println("PASS : Got AuthenticationDelegate");
                    //
                    // Try logging in to the Community.
                    try {
                        token = authenticator.authenticateLogin(name, pass);
                        if (null == token) {
                            if (DEBUG_FLAG)
                                System.out.println("FAIL : Null token");
                            healthy = false;
                            message = LOGIN_FAIL_MESSAGE;
                        } else {
                            if (DEBUG_FLAG)
                                System.out.println("PASS : Got token");
                            if (DEBUG_FLAG)
                                System.out.println("  Token   : " + token);
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  Token   : " + token.getToken());
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  Used    : " + token.getUsed());
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  Target  : " + token.getTarget());
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  Account : " + token.getAccount());
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  Start   : " + token.getStartDate());
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "  End     : " + token.getExpirationDate());
                            message = LOGIN_PASS_MESSAGE;
                        }
                    } catch (Exception ouch) {
                        healthy = false;
                        message = SYSTEM_ERROR_MESSAGE;
                        ouch.printStackTrace();
                    }
                }
            }
            if (healthy) {
                //
                // Try creating a policy delegate.
                PolicyServiceDelegate delegate = new PolicyServiceDelegate();
                if (null == delegate) {
                    if (DEBUG_FLAG)
                        System.out.println("FAIL : Null PolicyServiceDelegate");
                    healthy = false;
                    message = SYSTEM_ERROR_MESSAGE;
                }
                //
                // 
                else {
                    if (DEBUG_FLAG)
                        System.out.println("PASS : Got PolicyServiceDelegate");
                    //
                    // Check if the account has permission to see the site.
                    try {
                        authorized =
                            delegate.checkPermissions(
                                name,
                                POLICY_GROUP,
                                POLICY_RESOURCE,
                                POLICY_ACTION);
                        if (authorized) {
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "PASS : Permission check ok");
                            message = POLICY_PASS_MESSAGE;
                        } else {
                            if (DEBUG_FLAG)
                                System.out.println(
                                    "FAIL : Permission check failed");
                            healthy = false;
                            message = POLICY_FAIL_MESSAGE;
                        }
                    } catch (Exception ouch) {
                        healthy = false;
                        message = SYSTEM_ERROR_MESSAGE;
                        ouch.printStackTrace();
                    }
                }
            }
            //
            // If we pass the tests.
            if ((healthy) && (null != token) && (authorized)) {
                //
                // Set the current account info in our session.
                session.setAttribute("user", name);
                session.setAttribute(
                    "credential",
                    "guest@" + CommunityConfig.getCommunityName());
                session.setAttribute("community_account", token.getAccount());
                session.setAttribute(
                    "community_name",
                    CommunityConfig.getCommunityName());
                //
                // Set the current account info in our request.
                request.setAttribute(ACCOUNT_PARAM, token.getAccount());
                //
                // Add our results.
                results = new HashMap();
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
    public void init() {
        if (DEBUG_FLAG)
            System.out.println("LoginAction:init()");
        //
        // Load our community config.
        CommunityConfig.loadConfig();
    }
}
/**
 * <cvs:log>
 * $Log: LoginAction.java,v $
 * Revision 1.19  2004/03/19 13:02:25  jdt
 * Pruned the log messages - they cause conflicts on merge, 
 * best just to reduce them to the merge message.
 *
 * Revision 1.18  2004/03/19 12:40:09  jdt
 * Merge from PLGN_JDT_bz199b.
 * Refactored log in pages to use xsp and xsl style sheets.  
 * Added pages for requesting a login, and requesting
 * a password reminder.
 * 
 * </cvs:log>
 */
