/**
 * <cvs:id>$Id: LoginAction.java,v 1.20 2004/03/24 18:31:33 jdt Exp $</cvs:id>
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/03/24 18:31:33 $</cvs:date>
 * <cvs:version>$Revision: 1.20 $</cvs:version>
 */
package org.astrogrid.portal.cocoon.common;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate;
import org.astrogrid.community.common.config.CommunityConfig;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
/**
 * Login Action.  Extracts login parameters from the 
 * request action and checks them with the security 
 * delegate.
 * @TODO plumb in the policy delegate to check
 * we have permission to view the site!
 * @TODO plumb in Dave's rather clever security delegate factory
 * @author dave/jdt
 */
public final class LoginAction extends AbstractAction {
    /**
     * Name of JNDI property holding security delegate endpoint URL
     */
    public static final String ORG_ASTROGRID_PORTAL_COMMUNITY_URL = "org.astrogrid.portal.community.url";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String CREDENTIAL_SESH = "credential";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String COMMUNITY_NAME_SESH = "community_name";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String COMMUNITY_ACCOUNT_SESH = "community_account";
    /**
     * Parameter names to look for in the session object.
     */
    public static final String USER_SESH = "user";
    /**
     * Parameter names to look for in the request object. We could refer to
     * these vars for better safety in the xsps, but it makes the code so ugly
     * and difficult to read I've decided not to.
     */
    public static final String NAME_PARAM = USER_SESH;
    /**
     * Parameter names to look for in the request object.
     */
    public static final String COMMUNITY_PARAM = "community";
    /**
     * Parameter names to look for in the request object.
     */
    public static final String PASS_PARAM = "pass";
    /**
     * Key used in the request object and returned map
     * to pass back the account name
     */
    public static final String ACCOUNT_PARAM = "account";
    /**
     * Switch for our debug statements. 
     * JDT: I know we shouldn't be using System.outs but they
     * can be *so* useful. Retain, but switch off for deployment and
     * supplement with logging to files.
     *  
     */
    private static final boolean debugToSystemOutOn = true;
  
    /**
     * Our action method. Expects the "user", "community" and "pass" to be in
     * the request header.  Returns null if the user fails to
     * login, or a hashmap (currently empty) on success.
     * Disasters raise exceptions now.  @TODO find a pretty
     * way of dealing with these in cocoon, otherwise we'll have
     * to trap them here, and return an empty hashmap along with 
     * an error message in the session.
     * 
     * @param redirector see org.apache.cocoon.acting.Action#act
     * @param resolver see org.apache.cocoon.acting.Action#act
     * @param objectModel see org.apache.cocoon.acting.Action#act
     * @param source see org.apache.cocoon.acting.Action#act
     * @param params see org.apache.cocoon.acting.Action#act
     * @return null on failure to login, an empty HashMap on success
     * @throws LoginException if there's a problem with the 
     * input parameters or we're unable to get a delegate
     * @see org.apache.cocoon.acting.Action#act
     *      (org.apache.cocoon.environment.Redirector,
     *      org.apache.cocoon.environment.SourceResolver, java.util.Map,
     *      java.lang.String,
     *      org.apache.avalon.framework.parameters.Parameters)
     */
    public Map act(
        final Redirector redirector,
        final SourceResolver resolver,
        final Map objectModel,
        final String source,
        final Parameters params)
        throws LoginException {

        // Ensure logger is set correctly
        checkLogger();
        final Logger log = getLogger();
        //
        // Initialise our community config.
        init();
        //
        // Get our current request and session.
        final Request request = ObjectModelHelper.getRequest(objectModel);
        final Session session = request.getSession();

        //
        // Get the user name and password from our request.
        String user = request.getParameter(NAME_PARAM);
        String community = request.getParameter(COMMUNITY_PARAM);
        String pass = request.getParameter(PASS_PARAM);
        user = checkParameter(user, log);
        community = checkParameter(community, log);
        pass = checkParameter(pass, log);
        //
        // The new login action accepts the user name and
        // community as separate params, so needs to 
        // construct the unique id.  @TODO is this right?
        
        final String name = user + "@" + community;
        if (debugToSystemOutOn) {
            System.out.println("LoginAction:login()");
            System.out.println("  Name : " + name);
            System.out.println("  Pass : " + pass);
        }
        log.debug(
            "LoginAction: name=" + name + ", password null?" + (pass == null));

        //
        // Try creating a Community delegate.
        final SecurityServiceDelegate authenticator;
        try {
            authenticator = getSecurityDelegate();
        } catch (MalformedURLException ex) {
          log.error("Unable to create SecurityDelegate", ex);
          if (debugToSystemOutOn) {
              ex.printStackTrace();
          }
          throw new LoginException("Unable to create Security Delegate", ex);
        }
        if (null == authenticator) {
            if (debugToSystemOutOn) {
                System.out.println("FAIL : Null AuthenticationDelegate");
            }
            log.error("Unable to get SecurityServiceDelegate");
            throw new LoginException("Unable to get a SecurityServiceDelegate");
        }
        if (debugToSystemOutOn) {
            System.out.println("PASS : Got AuthenticationDelegate");
        }
        //
        // Try logging in to the Community.
        final SecurityToken token;
        
        try {
            token = authenticator.checkPassword(name, pass);
        } catch (CommunityServiceException e) {
            log.error("Exception from security delegate",e);
            throw new LoginException("Exception from security delegate",e);
        } catch (CommunitySecurityException e) {
            log.debug("Security check failed",e);
            return null; //failed to log in
        } catch (CommunityIdentifierException e) {
            log.debug("Account identifier invalid",e);
            return null; //failed to log in
        }
        
        assert token!=null : "Token should not be null" ;

        if (debugToSystemOutOn) {
            System.out.println("PASS : Got token");
            System.out.println("  Token   : " + token);
            System.out.println("  Token   : " + token.getToken());
            System.out.println("  Account : " + token.getAccount());
        }
        log.debug("Logged in");

        // We pass the tests so set the current account info in our session.
        session.setAttribute(USER_SESH, name);
        session.setAttribute(
            CREDENTIAL_SESH,
            "guest@" + CommunityConfig.getCommunityName());
        session.setAttribute(COMMUNITY_ACCOUNT_SESH, token.getAccount());
        session.setAttribute(
            COMMUNITY_NAME_SESH,
            CommunityConfig.getCommunityName());
        //
        // Set the current account info in our request.
        request.setAttribute(ACCOUNT_PARAM, token.getAccount());
        //
        // Add our results.
        final Map results = new HashMap();
        results.put(ACCOUNT_PARAM, token.getAccount());
        if (debugToSystemOutOn) {
            System.out.println("LoginAction:act()");
            System.out.println("exit");
            System.out.println("results=null? " + (results == null));
        }
        return results;
    }
    /**
     * These checks are also done by the sitemap now, but
     * no harm in doing them again.
     * @param param parameter to check
     * @param log logfile
     * @return trimmed param
     * @throws LoginException if the param is null or empty
     */
    private String checkParameter(final String param, final Logger log) throws LoginException {
        //
        // Check for a null or blank parameter.
        String newParam;
        if (null == param) {
            log.error("Null parameter");
            throw new LoginException("parameter is null");
        } else {
            newParam = param.trim();
            if (param.length() <= 0) {
                log.error("Empty parameter");
                throw new LoginException("parameter is empty");
            }
        }
        return newParam;
    }
    /**
     * Get the security delegate.  Looks for the
     * property org.astrogrid.portal.community.url
     * if this property is "dummy" then a mock delegate is
     * returned.
     * @return either a genuine or mock delegate
     * @throws MalformedURLException if the url is malformed
     * @TODO consider factoring this into a separate class
     */
    private SecurityServiceDelegate getSecurityDelegate() throws MalformedURLException {
        final Config config = SimpleConfig.getSingleton();
        final String endpoint = config.getString(ORG_ASTROGRID_PORTAL_COMMUNITY_URL);
        if ("dummy".equals(endpoint)) {
            return new SecurityServiceMockDelegate();
        } else {
            return new SecurityServiceSoapDelegate(endpoint);
        }
    }
    /**
     * Load our community config.
     *  
     */
    public void init() {
        if (debugToSystemOutOn) {
            System.out.println("LoginAction:init()");
        }
        //
        // Load our community config.
        CommunityConfig.loadConfig();
    }
    /**
     * During unit tests the logger isn't setup properly, hence this method to
     * use a console logger instead.  Also will log to console
     * if debugToSystemOutOn - can be useful.
     *  
     */
    private void checkLogger() {
        final Logger log = super.getLogger();
        if (log == null || debugToSystemOutOn) {
            enableLogging(new ConsoleLogger());
        }
    }
}
/**
 * <cvs:log>$Log: LoginAction.java,v $
 * <cvs:log>Revision 1.20  2004/03/24 18:31:33  jdt
 * <cvs:log>Merge from PLGN_JDT_bz#201
 * <cvs:log>
 * <cvs:log>Revision 1.19.2.3  2004/03/24 18:10:13  jdt
 * <cvs:log>refactored the way the parameters are checked, and what happens
 * <cvs:log>following a logon failure.
 * <cvs:log>
 * <cvs:log>Revision 1.19.2.2  2004/03/23 16:47:01  jdt
 * <cvs:log>Substantial refactoring, especially in the way that logging
 * <cvs:log>is done and that messages are passed back.  Problems
 * <cvs:log>are now thrown as exceptions. Let's hope that there's a 
 * <cvs:log>graceful way of dealing with them!
 * <cvs:log> Revision 1.19.2.1 2004/03/19 17:46:41
 * jdt Added most of the meat to MessageToAdminAction. Refactored LoginAction
 * to agree with community Itn05 refactoring.
 * 
 * Revision 1.19 2004/03/19 13:02:25 jdt Pruned the log messages - they cause
 * conflicts on merge, best just to reduce them to the merge message.
 * 
 * Revision 1.18 2004/03/19 12:40:09 jdt Merge from PLGN_JDT_bz199b. Refactored
 * log in pages to use xsp and xsl style sheets. Added pages for requesting a
 * login, and requesting a password reminder.
 * 
 * </cvs:log>
 */
