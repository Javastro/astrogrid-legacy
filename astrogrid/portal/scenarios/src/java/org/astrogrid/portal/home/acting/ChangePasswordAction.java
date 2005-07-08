package org.astrogrid.portal.home.acting;

import java.util.HashMap;
import java.util.Map;  
import java.net.URISyntaxException;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
//import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;

import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

import org.apache.avalon.framework.logger.ConsoleLogger;

import org.astrogrid.community.client.security.manager.SecurityManagerDelegate; 
import org.astrogrid.community.resolver.security.manager.SecurityManagerResolver ;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.CommunityPasswordResolver;
import org.astrogrid.community.resolver.exception.CommunityResolverException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;
/**
 * This class provides the Home page with the facility to
 * change password.
 * 
 * @author jeff.lusted <mailto:jl99@star.le.ac.uk />
 */
public class ChangePasswordAction extends AbstractAction {
    
    /**
     * Switch for our debug statements. 
     *  
     */
    private static final boolean DEBUG_TO_SYSTEM_OUT = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    
    public static final String PASSWORD_CHANGE = "password-change" ;
    
    /**
     * Parameter names to look for in the request object. 
     */
    public static final String OLD_PASSWORD = "opa";
    /**
     * Parameter names to look for in the request object.
     */
    public static final String NEW_PASSWORD = "npa";
    /**
     * Parameter names to look for in the request object.
     */
    public static final String NEW_PASSWORD_VERIFIED = "npav";    
    
    
    
     
  /**
   * Change a User's Password.
   * 
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Logger logger = this.retrieveLogger();
    if( TRACE_ENABLED ) logger.debug( "ChangePasswordAction.act() entry" ) ;
    
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
    AstrogridSession session = AstrogridSessionFactory.getSession(request.getSession(true));
    Map map = new HashMap() ;

    try{
        this.checkOldPassword( request, session, logger ) ;
        this.changePassword( request, session, logger );
        map.put( ChangePasswordAction.PASSWORD_CHANGE, "true" ) ;
    }
    catch( ChangePasswordException cpex ) {
        map.put( ChangePasswordAction.PASSWORD_CHANGE, "false" ) ;
    }
    finally {
        if( TRACE_ENABLED ) logger.debug( "ChangePasswordAction.act() exit" ) ;
    } 
    
    return map;
    
  }
  
  
  private void checkOldPassword( Request request
                               , AstrogridSession session
                               , final Logger log ) throws ChangePasswordException {
      
      CommunityPasswordResolver passwordResolver = new CommunityPasswordResolver();      
      String community = (String)session.getAttribute( AttributeKey.COMMUNITY_AUTHORITY ) ;
      String name = (String)session.getAttribute( AttributeKey.USER ) ;
      String pass = request.getParameter( OLD_PASSWORD ) ;
      
      // Not sure whether password is enforced...
      if( pass == null ){
          pass = "" ;
      }

      // Try logging in to the Community.
      final SecurityToken token;
      log.debug("Attempting to get token...");
      try {
          token = passwordResolver.checkPassword(name, pass);
      } catch (CommunityServiceException e) {
          log.debug("Security check failed",e);
          throw new ChangePasswordException( e ); //failed to log in
      } catch (CommunitySecurityException e) {
          log.debug("Security check failed",e);
          throw new ChangePasswordException( e ); //failed to log in
      } catch (CommunityIdentifierException e) {
          log.debug("Account identifier invalid",e);
          throw new ChangePasswordException( e ); //failed to log in
      } catch (CommunityResolverException e) {
          log.debug("CommunityResolverException from security delegate",e);
          throw new ChangePasswordException( e );  //failed to log in
      } catch (RegistryException e) {
          log.error("RegistryException from security delegate",e);
          throw new ChangePasswordException( "RegistryException from security delegate", e ) ;
      }
      
      if( token == null ) {
          log.debug( "Security token should not be null" );
          throw new ChangePasswordException( "Security token should not be null" ) ;
      }
   
  }
  
  
  private void changePassword( Request request
                             , AstrogridSession session
                             , final Logger log ) throws ChangePasswordException {

      SecurityManagerResolver securityManagerResolver = new SecurityManagerResolver() ;
      SecurityManagerDelegate securityManagerDelegate = null ;
      String community = Ivorn.SCHEME + "://" + (String)session.getAttribute( AttributeKey.COMMUNITY_AUTHORITY ) ;
      String newPassword = request.getParameter( NEW_PASSWORD ) ;
      String verifiedNewPassword = request.getParameter( NEW_PASSWORD_VERIFIED ) ;
      String user = (String)session.getAttribute( AttributeKey.USER ) ;
      log.debug( "community: " + community ) ;
      log.debug( "user: " + user ) ;
      
      try {
          if( newPassword == null ) {
              newPassword = "" ;
          }
          else {
              newPassword = newPassword.trim() ;
          }
          if( verifiedNewPassword == null ) {
              verifiedNewPassword = "" ;
          }
          else {
              verifiedNewPassword = verifiedNewPassword.trim() ;
          }
          if( !newPassword.equals( verifiedNewPassword ) ) {
              throw new ChangePasswordException( "New passwords not equal" ) ;
          }
          securityManagerDelegate = securityManagerResolver.resolve( new Ivorn( community ) ) ;
          securityManagerDelegate.setPassword( user, newPassword ) ;
      }
      catch( CommunityIdentifierException ciex ) {
          log.debug("Account identifier invalid",ciex);
          throw new ChangePasswordException( ciex ) ;
      }
      catch( CommunityResolverException crex ) {
          log.debug("CommunityResolverException from security delegate", crex);
          throw new ChangePasswordException( crex ) ;
      }
      catch( RegistryException rex ) {
          log.error("RegistryException from security delegate", rex );
          throw new ChangePasswordException( rex ) ;
      }
      catch( URISyntaxException usex ) {
          log.debug("Security check failed", usex );
          throw new ChangePasswordException( usex ) ;
      }
      catch( CommunityServiceException csex ) {
          log.debug("Security check failed", csex);
          throw new ChangePasswordException( csex ) ;
      }
      catch( CommunitySecurityException csex ) {
          log.debug("Security check failed", csex );
          throw new ChangePasswordException( csex ) ;
      }
      finally {
          ;
      }

  }
  
  
  /**
   * During unit tests the logger isn't setup properly, hence this method to
   * use a console logger instead.  Also will log to console
   * if debugToSystemOutOn - can be useful.
   *  
   */
  private Logger retrieveLogger() {
      Logger logger = super.getLogger();
      if (logger == null || DEBUG_TO_SYSTEM_OUT) {
          enableLogging(new ConsoleLogger());
          logger = super.getLogger();
      }
      return logger ;
  }  
  
  
}
