package org.astrogrid.portal.datacenter.acting;

import java.util.HashMap;
import java.util.Map;  
import java.io.IOException; 
import java.io.OutputStream;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
//import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
//import org.astrogrid.community.User;
//import org.astrogrid.portal.common.user.UserHelper;
//bug 609 - JBL
//import org.astrogrid.portal.login.common.SessionKeys;
//import org.astrogrid.store.delegate.VoSpaceResolver;

import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
//import org.astrogrid.store.Agsl;
//import org.astrogrid.store.Ivorn;
//import org.astrogrid.store.delegate.StoreClient; 
//import org.astrogrid.store.delegate.StoreDelegateFactory; 

import org.astrogrid.portal.myspace.filesystem.Tree;
import org.astrogrid.portal.myspace.filesystem.File;
import org.astrogrid.portal.myspace.filesystem.Directory ;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;


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
    private static final boolean DEBUG_TO_SYSTEM_OUT = true ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
     
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
    if( TRACE_ENABLED ) logger.debug( "MySpaceSaveAction.act() entry" ) ;
    
    Map sitemapParams = new HashMap();
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    AstrogridSession session = AstrogridSessionFactory.getSession(request.getSession(true));
    Tree tree = null ;
    Directory directory = null ;
    File file = null ;
    OutputStream outStream = null ;
    
    try {
      
      StringBuffer buffer = new StringBuffer( 1024 ) ;
      String adqlAsXML ;
      String adqlAsString = utils.getAnyParameter(AttributeKey.ADQL_AS_STRING.toString(), params, request, request.getSession() );
      // String adqlAsString = request.getParameter( AttributeKey.ADQL_AS_STRING.toString() ) ;
      session.setAttribute( AttributeKey.ADQL_AS_STRING, adqlAsString ) ; 
      session.removeAttribute( AttributeKey.ADQL_ERROR ) ;
      String resourceId = (String)session.getAttribute( AttributeKey.RESOURCE_ID ) ;  
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
                .append( resourceId != null ? resourceId : "none" )
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
                .append( resourceId != null ? resourceId : "none" )
                .append( " ?>" ) 
                .append( adqlAsXML.substring( adqlAsXML.indexOf( "?>" ) + 2 ) ) ;
          }
            
          session.removeAttribute( AttributeKey.ADQL_ERROR ) ;
  
      }
      catch ( Exception ex ) {
          
          // At the moment I'm setting a dummy element since the xml
          // needs at least one root element other than the xml PI...
          buffer
            .append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
            .append( "<?qb-sql-source " )
            .append( adqlAsString )
            .append( " ?>" )        
            .append( "<?qb-registry-resources " )
            .append( resourceId != null ? resourceId : "none" )
            .append( " ?>" ) 
            .append( "<dummy>Above ADQL source contains errors</dummy>" );
          
          session.setAttribute( AttributeKey.ADQL_ERROR, ex.getLocalizedMessage() ) ;
          logger.debug("[act] adql error: " + ex.getLocalizedMessage() ) ;
                  
      }

      // mySpaceName contains the full path name from the home account space...
      String mySpaceName = "home/" + request.getParameter("myspace-name");
      logger.debug("[act] mySpaceName: " + mySpaceName);

      logger.debug( "[act] adql final format: " + buffer.toString() ) ;
      
      tree = (Tree)session.getAttribute( AttributeKey.MYSPACE_TREE ) ;
      file = tree.getFile( mySpaceName ) ;
	  
      // If file doesn't exist we will create it...
      if( file == null  ) {
          int separatorIndex = mySpaceName.lastIndexOf( '/' ) ;
          if( separatorIndex == -1 ) {
              throw new IOException( "File [" + mySpaceName + "] cannot be created."  ) ;
          }
          String fileName = mySpaceName.substring( separatorIndex + 1 ) ;
          String directoryPath = mySpaceName.substring( 0, separatorIndex + 1) ;
          file = tree.newFile( fileName, directoryPath ) ;
          if( file == null ) {
             throw new IOException( "File [" + mySpaceName + "] cannot be created."  ) ;
          }
      }
      
      try {
         outStream = file.getNode().writeContent() ;
      }
      catch ( Throwable ex ) {
         logger.error(  "Could not acquire OutputStream for File [" + mySpaceName + "]" , ex ) ;
         throw new IOException( "Could not acquire OutputStream for File [" + mySpaceName + "]") ;      
      }
      
      try {
          int bufferLength = buffer.length() ;
          for( int i=0; i<bufferLength; i++ ) {
             outStream.write( buffer.charAt( i ) ) ;
          }
      }
      catch ( Throwable ex ) {
          logger.error(  "Failure writing to OutputStream for File [" + mySpaceName + "]" , ex ) ;          
          throw new IOException( "Failure writing to OutputStream for File [" + mySpaceName + "]") ;      
      }     

//      request.setAttribute("adql-document-saved", "true");
//      sitemapParams.put("adql-document-saved", "true");
    }
	catch( IOException myioex) {
       session.setAttribute( AttributeKey.ADQL_ERROR, myioex.getLocalizedMessage() ) ;
    }   
//        catch(Throwable t) {
//            request.setAttribute("adql-document-saved", "false");
//            request.setAttribute("adql-document-error-message", t.getLocalizedMessage());
//            sitemapParams = null;
//        }
    finally {
        try{ if( outStream != null ) outStream.close(); } catch( Throwable t ){ ; }
        if( TRACE_ENABLED ) logger.debug( "MySpaceSaveAction.act() exit" ) ;
     }    
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
