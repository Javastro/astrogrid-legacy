/**
 * <cvs:id>$Id: LoginAction.java,v 1.23 2004/04/02 10:21:12 jdt Exp $</cvs:id>
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/04/02 10:21:12 $</cvs:date>
 * <cvs:version>$Revision: 1.23 $</cvs:version>
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
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.common.security.service.SecurityServiceMock;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.store.Ivorn;
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
     * Parameter names to look for in the request object. We could refer to
     * these vars for better safety in the xsps, but it makes the code so ugly
     * and difficult to read I've decided not to.
     */
    public static final String USER_PARAM = "user";
    /**
     * Parameter names to look for in the request object.
     */
    public static final String COMMUNITY_PARAM = "community";
    /**
     * Parameter names to look for in the request object.
     */
    public static final String PASS_PARAM = "pass";

    /**
     * Switch for our debug statements. 
     * JDT: I know we shouldn't be using System.outs but they
     * can be *so* useful. Retain, but switch off for deployment and
     * supplement with logging to files.
     *  
     */
    private static final boolean debugToSystemOutOn = true;
    /**
     * Logger
     */
    private Logger log;
  
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
        log = getLogger();
        //
        // Initialise our community config.
        init();
        //
        // Get our current request and session.
        final Request request = ObjectModelHelper.getRequest(objectModel);
        final Session session = request.getSession();

        //
        // Get the user name and password from our request.
        String user = request.getParameter(USER_PARAM);
        String community = request.getParameter(COMMUNITY_PARAM);
        String pass = request.getParameter(PASS_PARAM);
        user = checkParameter(user, log);
        community = checkParameter(community, log);
        pass = checkParameter(pass, log);

        log.debug("LoginAction:login()");
        log.debug("  user : " + user);
        log.debug("  community : " + community);
        log.debug("  Pass : " + pass);

        final Ivorn ivorn;
        try {
            ivorn = CommunityAccountIvornFactory.createIvorn(community, user);
        } catch (CommunityIdentifierException e1) {
            log.error("Unable to create ivorn", e1);
            throw new LoginException("Invalid user name or community - unable to create an Ivorn from those parameters", e1);
        }
        log.debug("  Ivorn : " + ivorn);
        final String name = ivorn.toString();
        //
        // Try creating a Community delegate.
        final SecurityServiceDelegate authenticator;
        try {
            authenticator = getSecurityDelegate();
        } catch (MalformedURLException ex) {
          log.error("Unable to create SecurityDelegate", ex);
          throw new LoginException("Unable to create Security Delegate", ex);
        }

        assert authenticator!=null : "Security service delegate was null";

        log.debug("PASS : Got AuthenticationDelegate");

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

        log.debug("PASS : Got token");
        log.debug("  Token   : " + token);
        log.debug("  Token   : " + token.getToken());
        log.debug("  Account : " + token.getAccount());

        // We pass the tests so set the current account info in our session.
        session.setAttribute(SessionKeys.USER, name);
        session.setAttribute(
            SessionKeys.CREDENTIAL,
            "guest@" + CommunityConfig.getCommunityName());
        session.setAttribute(SessionKeys.COMMUNITY_ACCOUNT, token.getAccount());
        session.setAttribute(
            SessionKeys.COMMUNITY_NAME,
            CommunityConfig.getCommunityName());
        //
        // Set the current account info in our request.
        request.setAttribute(USER_PARAM, user);
        //
        // Add our results.
        // Currently just use a non-null map to indicate success
        final Map results = new HashMap();
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
     * if this property is not found (or is set to "dummy")
     * then a mock delegate is
     * returned.  This mock delegate will only accept the password "secret".
     * @return either a genuine or mock delegate
     * @throws MalformedURLException if the url is malformed
     * @TODO consider factoring this into a separate class
     */
    private SecurityServiceDelegate getSecurityDelegate() throws MalformedURLException {
        final Config config = SimpleConfig.getSingleton();
        final String endpoint = config.getString(ORG_ASTROGRID_PORTAL_COMMUNITY_URL, "dummy");
        if ("dummy".equals(endpoint)) {
            SecurityServiceMock.setPassword("secret");
            log.debug("Using dummy delegate");
            return new SecurityServiceMockDelegate();
        } else {
            log.debug("Using delegate at "+endpoint);
            return new SecurityServiceSoapDelegate(endpoint);
        }
    }
    /**
     * Load our community config.
     *  
     */
    public void init() {
        log.debug("LoginAction:init()");

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
        final Logger logger = super.getLogger();
        if (logger == null || debugToSystemOutOn) {
            enableLogging(new ConsoleLogger());
        }
    }
}
/**
 * <cvs:log>
 * $Log: LoginAction.java,v $
 * Revision 1.23  2004/04/02 10:21:12  jdt
 * Construct the ivorn using community factory class.
 *
 * Revision 1.22  2004/03/26 18:08:39  jdt
 * Merge from PLGN_JDT_bz#275
 *
 * Revision 1.21.2.1  2004/03/26 17:43:03  jdt
 * Factored out the keys used to store the session into a separate
 * class that everyone can access.
 *
 * Revision 1.21  2004/03/25 15:18:13  jdt
 * Some refactoring of the debugging and added unit tests.
 *
 * Revision 1.20  2004/03/24 18:31:33  jdt
 * Merge from PLGN_JDT_bz#201
 * 
 * </cvs:log>
 */
