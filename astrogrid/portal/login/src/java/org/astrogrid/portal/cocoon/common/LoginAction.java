/**
 * <cvs:id>$Id: LoginAction.java,v 1.24 2004/04/02 11:53:17 jdt Exp $</cvs:id>
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/04/02 11:53:17 $</cvs:date>
 * <cvs:version>$Revision: 1.24 $</cvs:version>
 */
package org.astrogrid.portal.cocoon.common;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.astrogrid.community.common.config.CommunityConfig;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.common.security.service.SecurityServiceMock;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
/**
 * Login Action.  Extracts login parameters from the 
 * request action and checks them with the security 
 * delegate.
 * The portal expects to be told the endpoint of a registry service (see configuration
 * property below).  If this is unset, then a local, properties-based registry
 * will be used. See the Registry docs http://www.astrogrid.org/maven/docs/snapshot/registry/index.html
 * for more details.
 * Note that using an incorrect community will cause this class' act() method
 * to throw an exception, since an
 * attempt is made to look up the community in the registry.  If you have not configured
 * a registry then you can use the back door community "org.astrogrid.mock" to avoid this.  
 * This is provided for testing purposes and is configured so that the password "secret"
 * will allow access.
 * 
 * @TODO plumb in the policy delegate to check
 * we have permission to view the site!
 * @author dave/jdt
 */
public final class LoginAction extends AbstractAction {
    /**
     * Name of JNDI property holding security delegate endpoint URL
     */
    public static final String ORG_ASTROGRID_PORTAL_REGISTRY_URL = "org.astrogrid.portal.registry.url";
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
     * Add's a password to Dave's backdoor - see class description
     * @TODO remove this in production
     */
    static {
        SecurityServiceMock.setPassword("secret");
    }
  
    /**
     * Our action method. Expects the "user", "community" and "pass" to be in
     * the request header.  Returns null if the user fails to
     * login, or a hashmap (currently empty) on success.
     * Disasters raise exceptions now.  
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
        //
        // Initialise our community config.
        // init(); @TODO jdt find out if we need to do this.
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
        // Create a community password resolver
        // This attempts to connect to the registry, and then
        // automatically connects the correct community
        // based on the community parameter in the Ivorn
        // If a registry endpoint hasn't been set up, then a "local"
        // registry is used instead.
        final CommunityPasswordResolver passwordResolver;
        try {
            final Config config = SimpleConfig.getSingleton();
            final String endpoint = config.getString(ORG_ASTROGRID_PORTAL_REGISTRY_URL, null);
            log.debug("Registry endpoint:"+endpoint);
            if (endpoint!=null) {
                passwordResolver = new CommunityPasswordResolver(new URL(endpoint));
            } else {
                passwordResolver = new CommunityPasswordResolver();
        }
            log.debug("Got CommunityPasswordResolver "+passwordResolver);
        } catch (MalformedURLException e1) {
            log.error("Unable to create registry URL", e1);
            throw new LoginException("Unable to contact registry.  Please check your registry URL",e1);
        }



        //
        // Try logging in to the Community.
        final SecurityToken token;
        log.debug("Attempting to get token...");
        try {
            token = passwordResolver.checkPassword(name, pass);
        } catch (CommunityServiceException e) {
            log.error("CommunityServiceException from security delegate",e);
            throw new LoginException("Exception from security delegate",e);
        } catch (CommunitySecurityException e) {
            log.debug("Security check failed",e);
            return null; //failed to log in
        } catch (CommunityIdentifierException e) {
            log.debug("Account identifier invalid",e);
            return null; //failed to log in
        } catch (CommunityResolverException e) {
            log.error("CommunityResolverException from security delegate",e);
            throw new LoginException("CommunityResolverException from security delegate.  Please check that your community has been entered correctly and that your registry is properly configured.",e);
        } catch (RegistryException e) {
            log.error("RegistryException from security delegate",e);
            throw new LoginException("RegistryException from security delegate",e);
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
        log = super.getLogger();
        if (log == null || debugToSystemOutOn) {
            enableLogging(new ConsoleLogger());
            log = super.getLogger();
        }
    }
}
/**
 * <cvs:log>
 * $Log: LoginAction.java,v $
 * Revision 1.24  2004/04/02 11:53:17  jdt
 * Merge from PLGN_JDT_bz#281a
 *
 * 
 * </cvs:log>
 */
