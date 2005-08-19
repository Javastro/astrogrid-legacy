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
//import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
//import org.astrogrid.portal.common.user.UserHelper;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
//import org.astrogrid.store.Agsl;
//import org.astrogrid.store.delegate.StoreClient;
//import org.astrogrid.store.delegate.StoreDelegateFactory;
//import org.astrogrid.community.User;
import org.w3c.dom.Document;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.portal.myspace.filesystem.Tree;
import org.astrogrid.portal.myspace.filesystem.File;
//import org.astrogrid.portal.myspace.filesystem.Directory ;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

//import org.astrogrid.portal.login.common.SessionKeys;
//import org.astrogrid.store.delegate.VoSpaceResolver;
//import org.astrogrid.store.Ivorn;

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
    private static final boolean DEBUG_TO_SYSTEM_OUT = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    
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
    if( TRACE_ENABLED ) logger.debug( "MySpaceLoadAction.act() entry" ) ;
    
    Map sitemapParams = null ;
    ActionUtils utils = null ;
    Request request = null ;
    AstrogridSession session = null ;
    InputStream inStream = null;
    StringBuffer outputBuffer = null ;
    Tree tree = null ;
    File file = null ;
    
    try {
        
      sitemapParams = new HashMap();
      utils = ActionUtilsFactory.getActionUtils();
      request = ObjectModelHelper.getRequest(objectModel);
      session = AstrogridSessionFactory.getSession(request.getSession(true));
      outputBuffer = new StringBuffer(1024);        
      session.removeAttribute( AttributeKey.ADQL_ERROR ) ; 
      // Set the current user.
      //User user = (User)session.getAttribute( AttributeKey.USER );
      tree = (Tree)session.getAttribute( AttributeKey.MYSPACE_TREE ) ;
      String mySpaceName = "home/" + request.getParameter( "myspace-name" ) ;
	  logger.debug("[act] mySpaceName: " + mySpaceName);
	  file = tree.getFile( mySpaceName ) ;
	  
      if( file == null  ) {
         session.removeAttribute( AttributeKey.RESOURCE_ID ) ;
         session.setAttribute( AttributeKey.ADQL_AS_STRING, "." ) ;
         throw new IOException( "File [" + mySpaceName + "] does not exist."  ) ;
      }
	  // Copy the input stream to the output buffer.
      try {
	     inStream = file.getNode().readContent() ; 
      }
      catch ( Exception ex ) {
         session.removeAttribute( AttributeKey.RESOURCE_ID ) ;
         session.setAttribute( AttributeKey.ADQL_AS_STRING, "." ) ;
         throw new IOException( "Could not acquire InputStream for File [" + mySpaceName + "]") ;      
      }
      		
	  int bytesAvailable = inStream.available();
      char character ;
      
	  while(bytesAvailable > 0) {          
         character = (char)inStream.read();
	     outputBuffer.append(character);	      
	     bytesAvailable = inStream.available();        
	  }
      
      int targetIndex ;
	  String adqlDocument = outputBuffer.toString();
      targetIndex = adqlDocument.indexOf( "<?qb-sql-source ") ;
      if( targetIndex == -1 ) {
          session.removeAttribute( AttributeKey.RESOURCE_ID ) ;
          session.setAttribute( AttributeKey.ADQL_AS_STRING, "." ) ;
          throw new IOException( "File [" + mySpaceName + "] is not an ADQL file."  ) ;
      }
      targetIndex = targetIndex + 16;
      String adqlSource = adqlDocument.substring( targetIndex
                                                , adqlDocument.indexOf( "?>", targetIndex ) - 1  ) ; 
      targetIndex = adqlDocument.indexOf( "<?qb-registry-resources ") + 24 ;                                          
      String resourceId = adqlDocument.substring( targetIndex 
                                                , adqlDocument.indexOf( "?>", targetIndex) - 1  ) ;
      
      session.setAttribute( AttributeKey.ADQL_AS_STRING, adqlSource ) ;
      if( resourceId.equals( "none" ) ) {
         session.removeAttribute( AttributeKey.RESOURCE_ID ) ;
      } 
      else {          
         session.setAttribute( AttributeKey.RESOURCE_ID, resourceId ) ;
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

      logger.debug( "[act]" + AttributeKey.ADQL_AS_STRING + ": " + session.getAttribute( AttributeKey.ADQL_AS_STRING ) );
      
    }
    catch( IOException myioex) {
        myioex.printStackTrace() ;
        session.setAttribute( AttributeKey.ADQL_ERROR, myioex.getLocalizedMessage() ) ;
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
      try { if(inStream != null ) inStream.close(); } catch(Throwable t){ ; }
      if( TRACE_ENABLED ) logger.debug( "MySpaceLoadAction.act() exit" ) ;
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
  
  
  private void retrieveMetadata ( String resourceId, Logger logger, Request request, AstrogridSession session ) {
      
      try { 
        String table = "";         
        String tableQuery = null;
        String authorityID = resourceId.substring(0,resourceId.indexOf(SEPARATOR)) ;
        String resourceKey = resourceId.substring(resourceId.indexOf(SEPARATOR)+1,resourceId.lastIndexOf(SEPARATOR)) ;
        table = resourceId.substring( resourceId.lastIndexOf(SEPARATOR)+1 ).trim() ;
        String identifier = "ivo://" + authorityID + "/" + resourceKey ;  
 
//        tableQuery = "<query>\n<selectionSequence>"
//                   + "\n<selection item='searchElements' itemOp='EQ' value='Resource'/>"
//                   + "\n<selectionOp op='$and$'/>"
//                   + "<selection item='vr:Identifier/vr:AuthorityID' itemOp='EQ' value='"+authorityID+"'/>"
//                   + "\n<selectionOp op='AND'/>"
//                   + "\n<selection item='vr:Identifier/vr:ResourceKey' itemOp='EQ' value='"+resourceKey+"'/>"   
//                   + "\n<selectionOp op='AND'/>"        
//                   + "<selection item='vr:Type' itemOp='EQ' value='Catalog'/>"
//                   + "\n</selectionSequence></query>";
        // String sqlQuery = "Select * from Registry where vr:identifier='" + authorityID + "' and vr:content/vr:type='Catalog' ";
        String sqlQuery = "Select * from Registry where vr:identifier='" + identifier + "' and vr:content/vr:type='Catalog' ";
       // Select * from Registry where vr:identifier = 'ivo://irsa.ipac/COSMOS' and vr:content/vr:type  = 'Catalog'
                                              
        // logger.debug( "tableQuery = " + tableQuery);                              
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
        session.setAttribute( AttributeKey.TABLENAME, table );
        session.setAttribute( AttributeKey.RESULT_SINGLE_CATALOG, doc );
        
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

