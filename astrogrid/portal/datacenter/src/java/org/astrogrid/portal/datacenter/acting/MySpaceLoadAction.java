package org.astrogrid.portal.datacenter.acting;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.community.User;

import org.apache.avalon.framework.logger.ConsoleLogger;

/**
 * This class provides the DataCenter UI with the facility to
 * load a given MySpace named ADQL file into the Query Builder.
 * 
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceLoadAction extends AbstractAction {
    
    /**
     * Switch for our debug statements. 
     *  
     */
    private static final boolean DEBUG_TO_SYSTEM_OUT = true;
    
    public static final String SESSIONKEY_ADQL_AS_STRING = "adql-query" ;
    
    public static final String SESSIONKEY_RESOURCE_ID = "query-builder-resource-id" ;
    
    public static final String SESSIONKEY_ADQL_ERROR = "query-builder-adql-error" ;
    
    
  /**
   * Load the required ADQL document from MySpace.
   *
   * <p>
   *   SiteMap Outputs:
   *     <ol>
   *       <li><code>adql-document-loaded</code>: "true" if ADQL was successfully loaded</li>
   *     </ol>
   * </p>
   * <p>
   *   Request Attribute Outputs:
   *     <ol>
   *       <li><code>adql-document-loaded</code>: "true" if ADQL was successfully loaded</li>
   *       <li><code>adql-document</code>: ADQL document</li>
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
    InputStream inStream = null;
    StringBuffer outputBuffer = new StringBuffer();
    
    try {
      // Set the current user.
      User user = UserHelper.getCurrentUser(params, request, session);
      
      // Set MySpace end point.
      String endPoint = utils.getAnyParameter( "myspace-end-point", params, request, session);
      
      // Set base AstroGrid storage location.
      Agsl agsl = new Agsl(endPoint);
      
      // Get the storage client.
      StoreClient storeClient = StoreDelegateFactory.createDelegate(user, agsl);

      String mySpaceName = utils.getAnyParameter("myspace-name", params, request, session);
	  logger.debug("[act] mySpaceName: " + mySpaceName);

	  // Copy the input stream to the output buffer.
	  inStream = storeClient.getStream(mySpaceName);
      		
	  int bytesAvailable = inStream.available();
      char character ;
      
	  while(bytesAvailable > 0) {          
         character = (char)inStream.read();
	     outputBuffer.append(character);	      
	     bytesAvailable = inStream.available();        
	  }
      
      int targetIndex ;
	  String adqlDocument = outputBuffer.toString();
      targetIndex = adqlDocument.indexOf( "<?qb-sql-source ") + 16 ;
      String adqlSource = adqlDocument.substring( targetIndex
                                                , adqlDocument.indexOf( "?>", targetIndex ) - 1  ) ; 
      targetIndex = adqlDocument.indexOf( "<?qb-registry-resources ") + 24 ;                                          
      String resourceId = adqlDocument.substring( targetIndex 
                                                , adqlDocument.indexOf( "?>", targetIndex) - 1  ) ;
      
      session.setAttribute( SESSIONKEY_ADQL_AS_STRING, adqlSource ) ;
      if( resourceId.equals( "none" ) ) {
         session.removeAttribute( SESSIONKEY_RESOURCE_ID ) ;
      } 
      else {          
         session.setAttribute( SESSIONKEY_RESOURCE_ID, resourceId ) ;
      }
      session.removeAttribute( SESSIONKEY_ADQL_ERROR ) ;
      logger.debug( "[act] adql source set to: " + adqlSource ) ;
      logger.debug( "[act] resource key set to: " + resourceId ) ;
           
//      request.setAttribute( "adql-document", adqlSource );
//      request.setAttribute("adql-document-loaded", "true");

      logger.debug("[act] adql-document: " + adqlSource);
      logger.debug("[act] adql-document-loaded: true");

//      sitemapParams.put("adql-document", adqlSource);
//      sitemapParams.put("adql-document-loaded", "true");
    }
    catch(Throwable t) {
      request.setAttribute("adql-document-loaded", "false");
      request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
      sitemapParams = null;
      
      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
      logger.debug( "[act] outputBuffer: " + outputBuffer.toString() ) ;
    }
    finally {
      try {
        if(inStream != null) {
          inStream.close();
	      }
      }
      catch(Throwable t){
        // assume closure.
      }
    }    

    logger.debug("[act] sitemapParams: " + sitemapParams);
    
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
