/**
 * <cvs:id>$Id: LoginAction.java,v 1.38 2005/11/21 12:32:20 clq2 Exp $</cvs:id>
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/login/src/java/org/astrogrid/portal/cocoon/common/LoginAction.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/11/21 12:32:20 $</cvs:date>
 * <cvs:version>$Revision: 1.38 $</cvs:version>
 */
package org.astrogrid.portal.cocoon.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.portal.common.session.AstrogridSessionFactory;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;
import org.astrogrid.portal.client.Screen ;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
import org.astrogrid.filemanager.client.FileManagerClientFactory ;
import org.astrogrid.filemanager.client.FileManagerClient ;


/**
 * Login Action.  Extracts login parameters from the 
 * request action and checks them with the security 
 * delegate.
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
    
    private static final String
    	AVAILABLE_HEIGHT = "available-height",
    	AVAILABLE_LEFT = "available-left",
    	AVAILABLE_TOP = "available-top",
    	AVAILABLE_WIDTH = "available-width",
    	COLOR_DEPTH = "color-depth", 
    	SCREEN_HEIGHT = "screen-height",
    	PIXEL_DEPTH = "pixel-depth",
    	SCREEN_WIDTH = "screen-width" ;

    /**
     * Logger
     */
    private Logger log;
  
  
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
        // Get our current request and session.
        final Request request = ObjectModelHelper.getRequest(objectModel);
        final AstrogridSession session = AstrogridSessionFactory.getSession( request.getSession() ) ;

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

        final CommunityPasswordResolver passwordResolver = new CommunityPasswordResolver();

        //
        // Try logging in to the Community.
        final SecurityToken token;
        log.debug("Attempting to get token...");
        try {
            token = passwordResolver.checkPassword(name, pass);
        } catch (CommunityServiceException e) {
            log.debug("Security check failed",e);
            return null; //failed to log in
        } catch (CommunitySecurityException e) {
            log.debug("Security check failed",e);
            return null; //failed to log in
        } catch (CommunityIdentifierException e) {
            log.debug("Account identifier invalid",e);
            return null; //failed to log in
        } catch (CommunityResolverException e) {
            //It's possible this should return null (ie a failed login)
            //rather than be treated as an error, because it probably indicates
            //that the user mistyped their community
            // ADDENDUM: CHANGED BY KEA TO RETURN NULL
            log.debug("CommunityResolverException from security delegate",e);
            return null;  //failed to log in
/*
            log.error("CommunityResolverException from security delegate",e);
            throw new LoginException("CommunityResolverException from security delegate.  Please check that your community has been entered correctly and that your registry is properly configured.",e);
*/
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
        session.setAttribute( AttributeKey.USER, name ) ;
        session.setAttribute( AttributeKey.CREDENTIAL, "guest@is.this.needed?" ) ;
        session.setAttribute( AttributeKey.COMMUNITY_ACCOUNT, token.getAccount() ) ;
        // session.setAttribute( AttributeKey.COMMUNITY_NAME, CommunityConfig.getCommunityName() ) ;
        session.setAttribute( AttributeKey.COMMUNITY_AUTHORITY, community) ;
           
        //
        // Set the current account info in our request.
        request.setAttribute(USER_PARAM, user);
        
        // Retrieve details of the user's screen attributes from the request
        // and store them in the session object...
        session.setAttribute( AttributeKey.CLIENT_SCREEN, getScreenDetails( request, log ) ) ;
       
        // Retrieve FileManagerClient for MySpace access and store in session object...
        // I dont think it is safe to move this from login at present. Because of the need
        // for a password or equivalent.
        FileManagerClient fmc = null ;
        try {
            // fmc = new FileManagerClientFactory().login( token ) ;  
            fmc = new FileManagerClientFactory().login( ivorn, pass ) ;              
            session.setAttribute( AttributeKey.FILE_MANAGER_CLIENT, fmc ) ;   
            session.setAttribute( AttributeKey.USER_IVORN, ivorn ) ;
        }
        catch( Exception ex ) {
            log.error( "FileManagerClientFactory().login( token ) failed", ex ) ;
            throw new LoginException( "FileManagerClientFactory().login( token ) failed", ex);        
        }

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
     * During unit tests the logger isn't setup properly, hence this method to
     * use a console logger instead. 
     *  
     */
    private void checkLogger() {
        log = super.getLogger();   
        if (log == null) {
            enableLogging(new ConsoleLogger());
            log = super.getLogger();
        }       
    }
    
    
    private Screen getScreenDetails( Request request, Logger log ) {
        Screen screen = null ;
        int screenWidth, screenHeight, availableWidth, availableHeight ;
    	int availableLeft, availableTop, colorDepth, pixelDepth ;
    	
//    	log.debug( "screen-width: " + request.getParameter( SCREEN_WIDTH ) ) ;
//      log.debug( "screen-height: " + request.getParameter( SCREEN_HEIGHT ) ) ;
//      log.debug( "available-width: " + request.getParameter( AVAILABLE_WIDTH ) ) ;
//      log.debug( "available-height: " + request.getParameter( AVAILABLE_HEIGHT ) ) ;
//      log.debug( "available-left: " + request.getParameter( AVAILABLE_LEFT ) ) ;
//      log.debug( "available-top: " + request.getParameter( AVAILABLE_TOP ) ) ;
//      log.debug( "color-depth: " + request.getParameter( COLOR_DEPTH ) ) ;
//      log.debug( "pixel-depth: " + request.getParameter( PIXEL_DEPTH ) ) ;
    	
    	try { screenWidth = Integer.valueOf( request.getParameter( SCREEN_WIDTH ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { screenWidth = -1 ; }
    
    	try { screenHeight = Integer.valueOf( request.getParameter( SCREEN_HEIGHT ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { screenHeight = -1 ; }

    	try { availableWidth = Integer.valueOf( request.getParameter( AVAILABLE_WIDTH ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { availableWidth = -1 ; }
    
    	try { availableHeight = Integer.valueOf( request.getParameter( AVAILABLE_HEIGHT ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { availableHeight = -1 ; }
    
    	try { availableLeft = Integer.valueOf( request.getParameter( AVAILABLE_LEFT ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { availableLeft = -1 ; }
    
    	try { availableTop = Integer.valueOf( request.getParameter( AVAILABLE_TOP ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { availableTop = -1 ; }

    	try { colorDepth = Integer.valueOf( request.getParameter( COLOR_DEPTH ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { colorDepth = -1 ; }
    
    	try { pixelDepth = Integer.valueOf( request.getParameter( PIXEL_DEPTH ) ).intValue() ; }
    	catch( NumberFormatException nfe ) { pixelDepth = -1 ; }
    	
//    	log.debug( "screenWidth: " + screenWidth 
//    	         + "\nscreenHeight: " + screenHeight
//    	         + "\navailableWidth: " + availableWidth
//    	         + "\navailableHeight: " + availableHeight
//    	         + "\navailableLeft: " + availableLeft
//    	         + "\navailableTop: " + availableTop
//    	         + "\ncolorDepth: " + colorDepth
//    	         + "\npixelDepth: " + pixelDepth ) ;
    
    	screen = new Screen( screenWidth
                       	   , screenHeight
                           , availableWidth
                           , availableHeight
                           , availableLeft
                           , availableTop
                           , colorDepth
                           , pixelDepth ) ;
    
        return screen ;  
        
    }
    
    
    
    
    
    
}
/**
 * <cvs:log>
 * $Log: LoginAction.java,v $
 * Revision 1.38  2005/11/21 12:32:20  clq2
 * branch por-jl-1374
 *
 * Revision 1.37.62.2  2005/08/25 14:50:24  jl99
 * Rationalize MySpace calls within Login. Had taken too much from session object.
 *
 * Revision 1.37.62.1  2005/08/25 10:57:29  jl99
 * Rationalize MySpace calls within Login
 *
 * Revision 1.37  2005/04/26 15:27:47  clq2
 * por_jl_1046
 *
 * Revision 1.36.2.1  2005/04/05 15:13:18  jl99
 * Added ability to retrieve details of the user's screen attributes and store them in the session object
 *
 * Revision 1.36  2005/03/29 22:07:56  clq2
 * jl908 merge
 *
 * Revision 1.31.30.10  2005/03/23 13:56:41  jl99
 * Changed login method to use ivorn and password
 *
 * Revision 1.31.30.9  2005/03/23 12:17:58  jl99
 * Experiment to see whether we can get the home page if we remove the FileManager stuff.
 *
 * Revision 1.31.30.8  2005/03/23 11:06:40  jl99
 * Extra  debugging
 *
 * Revision 1.31.30.7  2005/03/22 13:02:55  jl99
 * Refresh of Login
 *
 * Revision 1.35  2005/02/23 17:56:59  clq2
 * trying to fix the login problem
 *
 * Revision 1.34  2005/02/23 17:55:49  clq2
 * *** empty log message ***
 *
 * Revision 1.33  2005/02/23 17:33:45  clq2
 * trying to fix the login problem
 *
 * Revision 1.32  2005/01/31 19:39:00  jdt
 * Merges from POR_JDT_882
 *
 * Revision 1.31.28.2  2005/01/31 11:48:49  jdt
 * fixed some noncompiling tests - these need attention, and dropped the debug flag from the login action.
 *
 * Revision 1.31.28.1  2005/01/31 02:16:39  jdt
 * Rationalised config properties
 *
 * Revision 1.31  2004/12/07 16:26:04  clq2
 * portal_kea_719
 *
 * Revision 1.30.30.1  2004/12/06 17:56:23  kea
 * Fix for exception when bad community name given.
 *
 * Revision 1.30  2004/10/22 14:40:22  gps
 * - storing account space IVORN
 *
 * Revision 1.29  2004/10/05 14:32:55  gps
 * - myspace end point changes
 *
 * Revision 1.28  2004/05/27 17:17:13  jdt
 * removed obsolete reference to CommunityConfig
 *
 * Revision 1.27  2004/05/14 19:32:40  KevinBenson
 * new temporary session variable for the community authority
 *
 * Revision 1.26  2004/04/21 16:59:39  jdt
 * temporary change to accommodate bug 297
 *
 * Revision 1.25  2004/04/05 16:42:29  jdt
 * reinstated the init method till we decide whether it is really required.
 *
 * Revision 1.24  2004/04/02 11:53:17  jdt
 * Merge from PLGN_JDT_bz#281a
 *
 * 
 * </cvs:log>
 */
