package org.astrogrid.portal.datacenter.acting;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import org.astrogrid.query.sql.Sql2Adql;
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
import org.w3c.dom.Document;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.store.delegate.VoSpaceResolver;
import org.astrogrid.store.Ivorn;

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
    
    public static final String SESSIONKEY_ADQL_AS_STRING = "adqlQuery" ;
    
    public static final String SESSIONKEY_RESOURCE_ID = "uniqueID" ;
    
    public static final String SESSIONKEY_ADQL_ERROR = "query-builder-adql-error" ;
    
    public static final String SESSIONKEY_TABLENAME = "tableID" ; 
    
    public static final String SESSIONKEY_RESULT_SINGLE_CATALOG = "resultSingleCatalog" ;
    
    /*
     * Modified by PFO
    public static final String SEPARATOR = "/";
     */
    public static final String SEPARATOR = "!";
    
    
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
        
      session.removeAttribute( SESSIONKEY_ADQL_ERROR ) ;
      
      // Set the current user.
      User user = UserHelper.getCurrentUser(params, request, session);

//bug 609 - JBL      
      // Set MySpace end point.
//      String endPoint = utils.getAnyParameter( "myspace-end-point", params, request, session);
//      
//      // Set base AstroGrid storage location.
//      Agsl agsl = new Agsl(endPoint);
//      
//      // Get the storage client.
//      StoreClient storeClient = StoreDelegateFactory.createDelegate(user, agsl);
      
      // Get the store client from the VOSpaceResolver.
      Ivorn ivorn = (Ivorn) utils.getAnyParameterObject( SessionKeys.IVORN,params, request, session );
      StoreClient storeClient = VoSpaceResolver.resolveStore(user, ivorn);
      
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
         this.retrieveMetadata( resourceId, logger, request, session) ;
      }

      logger.debug( "[act] adql source set to: " + adqlSource ) ;
      logger.debug( "[act] resource key set to: " + resourceId ) ;
           
//      request.setAttribute( "adql-document", adqlSource );
//      request.setAttribute("adql-document-loaded", "true");

      logger.debug("[act] adql-document: " + adqlSource);
      logger.debug("[act] adql-document-loaded: true");

//      sitemapParams.put("adql-document", adqlSource);
//      sitemapParams.put("adql-document-loaded", "true");

      logger.debug( "[act]" + SESSIONKEY_ADQL_AS_STRING + ": " + session.getAttribute( SESSIONKEY_ADQL_AS_STRING ) );
      
    }
    catch( IOException myioex) {
        session.setAttribute( SESSIONKEY_ADQL_ERROR, myioex.getLocalizedMessage() ) ;
    }
//    catch(Throwable t) {
//      request.setAttribute("adql-document-loaded", "false");
//      request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
//      sitemapParams = null;
//      
//      logger.debug("[act] throwable: " + t.getClass() + ", msg: " + t.getLocalizedMessage());
//      logger.debug( "[act] outputBuffer: " + outputBuffer.toString() ) ;
//    }
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
  
  
  private void retrieveMetadata ( String resourceId, Logger logger, Request request, Session session ) {
      
      try { 
        String table = "";         
        String tableQuery = null;
        String authorityID = resourceId.substring(0,resourceId.indexOf(SEPARATOR)) ;
        String resourceKey = resourceId.substring(resourceId.indexOf(SEPARATOR)+1,resourceId.lastIndexOf(SEPARATOR)) ;
        table = resourceId.substring( resourceId.lastIndexOf(SEPARATOR)+1 ).trim() ;
 
        tableQuery = "<query>\n<selectionSequence>"
                   + "\n<selection item='searchElements' itemOp='EQ' value='Resource'/>"
                   + "\n<selectionOp op='$and$'/>"
                   + "<selection item='vr:Identifier/vr:AuthorityID' itemOp='EQ' value='"+authorityID+"'/>"
                   + "\n<selectionOp op='AND'/>"
                   + "\n<selection item='vr:Identifier/vr:ResourceKey' itemOp='EQ' value='"+resourceKey+"'/>"   
                   + "\n<selectionOp op='AND'/>"        
                   + "<selection item='vr:Type' itemOp='EQ' value='Catalog'/>"
                   + "\n</selectionSequence></query>";
        String sqlQuery = "Select * from Registry where vr:identifier='" + authorityID + "' and ";
        sqlQuery += " vr:content/vr:type='Catalog' ";
        
                                              
        logger.debug( "tableQuery = " + tableQuery);                              
        RegistryService rs = RegistryDelegateFactory.createQuery();
            
        //String adqlString = Sql2Adql.translateToAdql074(tableQuery);
        String adqlString = Sql2Adql.translateToAdql074(sqlQuery);
        logger.info("ADQL String in PORTAL for REGISTRY = " + adqlString);
        /*
         Document doc = rs.submitQuery( tableQuery );  
        */
        Document doc = rs.search(adqlString);        
             
//        request.setAttribute("tableName", table);
//        request.setAttribute("tableName", "testTable");
//        request.setAttribute("resultSingleCatalog", doc);
        session.setAttribute( SESSIONKEY_TABLENAME, table );
        session.setAttribute( SESSIONKEY_RESULT_SINGLE_CATALOG, doc );
        
      } 
      catch( NoResourcesFoundException nrfe ) 
      {
          logger.error( "Your query produced no results." );
      } 
      catch( RegistryException re ) 
      {
         logger.error( "A error occurred in processing your query with the Registry." );
         re.printStackTrace() ;
      } 
      catch( Exception e ) 
      {
         logger.debug( "Exception occurred. " );
         e.printStackTrace() ;
      }
            
  }
  
}
