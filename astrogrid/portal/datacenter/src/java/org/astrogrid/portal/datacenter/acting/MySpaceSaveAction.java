package org.astrogrid.portal.datacenter.acting;

import java.util.HashMap;
import java.util.Map;  
import java.io.IOException;  

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.community.User;
import org.astrogrid.portal.common.user.UserHelper;
//bug 609 - JBL
import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.store.delegate.VoSpaceResolver;

import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreClient; 
import org.astrogrid.store.delegate.StoreDelegateFactory; 
//bug 566 --clq2
//import org.astrogrid.datacenter.sqlparser.Sql2Adql074;
import org.astrogrid.query.sql.Sql2Adql;

import org.apache.avalon.framework.logger.ConsoleLogger;

/**
 * This class provides the DataCenter UI with the facility to
 * save a given MySpace named ADQL file from the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceSaveAction extends AbstractAction {
    
    /**
     * Switch for our debug statements. 
     *  
     */
    private static final boolean DEBUG_TO_SYSTEM_OUT = true;
     
    public static final String SESSIONKEY_ADQL_AS_STRING = "adqlQuery" ;
    
    public static final String SESSIONKEY_RESOURCE_ID = "uniqueID" ;
    
    public static final String SESSIONKEY_ADQL_ERROR = "query-builder-adql-error" ;
     
  /**
   * Save the required ADQL document to MySpace.
   * 
   * <p> 
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>adql-document-saved</code>: "true" if ADQL was successfully saved</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>adql-document-saved</code>: "true" if ADQL was successfully saved</li>
   *       <li><code>adql-document-error-message</code>: ADQL save error message</li>
   *     </ol>
   * </p>
   * 
   * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector, org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters params) {
    Logger logger = this.retrieveLogger();
    
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    try {
        
      // Set the current user.
      User user = UserHelper.getCurrentUser(params, request, session);

// Bug 609 - JBL...    
      // Get the store client from the VOSpaceResolver.
      Ivorn ivorn = (Ivorn) utils.getAnyParameterObject( SessionKeys.IVORN,params, request, session );
      StoreClient storeClient = VoSpaceResolver.resolveStore(user, ivorn);
      
//      // Set MySpace end point.
//      String endPoint = utils.getAnyParameter( "myspace-end-point", params, request, session );
//      
//      // Set base AstroGrid storage location.
//      Agsl agsl = new Agsl(endPoint);
//      
//      // Get the storage client.
//      StoreClient storeClient = StoreDelegateFactory.createDelegate(user, agsl);
      
      StringBuffer buffer = new StringBuffer( 512 ) ;
      String adqlAsXML ;
      String adqlAsString = utils.getAnyParameter(SESSIONKEY_ADQL_AS_STRING, params, request, session);
      session.setAttribute( SESSIONKEY_ADQL_AS_STRING, adqlAsString ) ; 
      session.removeAttribute( SESSIONKEY_ADQL_ERROR ) ;
      Object resourceId = session.getAttribute( SESSIONKEY_RESOURCE_ID ) ;  
      logger.debug("[act] adqlAsString (s): " + adqlAsString);
      
      try {

          //bug 566 -clq2
          //adqlAsXML = Sql2Adql074.translate( adqlAsString ) ;
		  adqlAsXML =  Sql2Adql.translateToAdql074( adqlAsString ) ;
          logger.debug("[act] adqlAsXML (x): " + adqlAsXML);
      
          // JL. This is a hack. 
          // The parser seems not to be supplying the xml "header";
          // ie: the xml Processing Instruction.
          // So, if it's not there, we supply it. 
          // I'm also raising a bug on the parser. 
          if( adqlAsXML.indexOf( "<?xml" ) < 0 ) {
              buffer
                .append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ) 
                .append( "<?qb-sql-source " )
                .append( adqlAsString )
                .append( " ?>" )
                .append( "<?qb-registry-resources " )
                .append( resourceId != null ? (String)resourceId : "none" )
                .append( " ?>" ) 
                .append( adqlAsXML ) ;
          }
          else {
              buffer
                .append( adqlAsXML.substring( 0, adqlAsXML.indexOf( "?>" ) + 2 ) ) 
                .append( "<?qb-sql-source " )
                .append( adqlAsString )
                .append( " ?>" )
                .append( "<?qb-registry-resources " )
                .append( resourceId != null ? (String)resourceId : "none" )
                .append( " ?>" ) 
                .append( adqlAsXML.substring( adqlAsXML.indexOf( "?>" ) + 2 ) ) ;
          }
            
          session.removeAttribute( SESSIONKEY_ADQL_ERROR ) ;
  
      }
      catch ( Exception ex ) {
          
          buffer
            .append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
            .append( "<?qb-sql-source " )
            .append( adqlAsString )
            .append( " ?>" )        
            .append( "<?qb-registry-resources " )
            .append( resourceId != null ? (String)resourceId : "none" )
            .append( " ?>" ) ;
                           
          session.setAttribute( SESSIONKEY_ADQL_ERROR, ex.getLocalizedMessage() ) ;
          logger.debug("[act] adql error: " + ex.getLocalizedMessage() ) ;
                  
      }

      String mySpaceName = utils.getAnyParameter("myspace-name", params, request, session);
      logger.debug("[act] mySpaceName: " + mySpaceName);

      logger.debug( "[act] adql final format: " + buffer.toString() ) ;
      storeClient.putString( buffer.toString(), mySpaceName, false );
//      request.setAttribute("adql-document-saved", "true");
//      sitemapParams.put("adql-document-saved", "true");
    }
		catch( IOException myioex) {
            session.setAttribute( SESSIONKEY_ADQL_ERROR, myioex.getLocalizedMessage() ) ;
		}
        
//        catch(Throwable t) {
//            request.setAttribute("adql-document-saved", "false");
//            request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
//            sitemapParams = null;
//        }
      
    return sitemapParams;
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
