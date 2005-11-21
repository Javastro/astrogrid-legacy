package org.astrogrid.portal.myspace.generation ;

import java.io.IOException;
import java.util.Map;
import java.io.StringReader ;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
//import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
//import org.astrogrid.community.User;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

//import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

//import org.apache.log.Logger ;

import org.astrogrid.portal.myspace.acting.framework.MySpaceHandler;
import org.astrogrid.portal.myspace.filesystem.*;
// import org.astrogrid.store.Ivorn;

/**
 * Generate <code>StoreClient</code> list of files as XML.
 * 
 * @author jeff.lusted
 */
public class DirectoryViewGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = true ;
  private static final boolean DEBUG_ENABLED = true ;
  private AstrogridSession session ;
  private Request request ;
  
  /**
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
  	  throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    ///ActionUtils utils = ActionUtilsFactory.getActionUtils();
    request = ObjectModelHelper.getRequest(objectModel);
    session = AstrogridSessionFactory.getSession( request.getSession(true) ) ;

    // Create environmental context.
    //context = ContextWrapperFactory.getContextWrapper(
    //    ContextWrapper.PARAM_PROTOCOL, utils, params, request, session);
  }

  /**
   * Generate <code>StoreClient</code> list of files into as XML.
   * 
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
    if( TRACE_ENABLED) this.getLogger().debug( "enter: DirectoryViewGenerator.generate()" );
    
    // path *may* contain the path of a requested directory to show in the view...
    String path = request.getParameter( MySpaceHandler.PARAM_DIRECTORY_VIEW_PATH ) ;
    this.getLogger().debug( "path: " + path ) ;
    
    // mode should only be present for the micro-browser.
    // It reflects the context from which the micro-browser has been invoked.
    // If present it should contain values like ...
    // "save-query-file" "load-query-file" for save/read functions in Query Editor
    // "save-workflow-file" "load-workflow-file" for save/read functions in Workflow Editor
    // "choose-file-for-workflow" for selecting files as parameters in Workflow Editor
    String mode = request.getParameter( MySpaceHandler.PARAM_REQUESTED_MODE ) ;
    this.getLogger().debug( "mode: " + mode ) ;
    
    Tree tree = TreeHelper.getTree( session ) ;
    try {       
        // First of all we try for a specifically requested directory.
        // This may not always be present, eg: on first display...
        Directory directory = tree.getDirectory( path ) ; 
        if( DEBUG_ENABLED && directory == null ) {
            this.getLogger().debug( "directory is null" ) ;
        }
        
        // Now for the pain of calling something modally...
        
        // Note in the following the strategy is to examine
        // what context the browser has been called in and - if no specific view
        // has been requested - invoke the last view used in that context.
        // If a specific view has been requested, we save it in the session object.       
        if( mode == null || mode.length() == 0 || mode.indexOf( "main" ) != -1 ) {
            // Absence of mode indicates this is the main browser MySpace Explorer...
            if( directory == null ) {
                path = (String)session.getAttribute( AttributeKey.MYSPACE_LAST_VIEW ) ;
                directory = tree.getDirectory( path ) ;
            }
            else {
                session.setAttribute( AttributeKey.MYSPACE_LAST_VIEW, path ) ;
            }
        }
        else if( mode.indexOf( "query-file" ) != -1) {
            // We have been invoked from the Query Editor...
            if( directory == null ) {
	            path = (String)session.getAttribute( AttributeKey.QUERY_EDITOR_LAST_MICROBROWSER_VIEW ) ;
	            directory = tree.getDirectory( path ) ;   
            }
            else {
                session.setAttribute( AttributeKey.QUERY_EDITOR_LAST_MICROBROWSER_VIEW, path ) ;
            }
        }
        else if( mode.indexOf( "workflow-file" ) != -1) {
            // We have been invoked from the Workflow Editor...
            if( directory == null ) {
	            path = (String)session.getAttribute( AttributeKey.WORKFLOW_EDITOR_LAST_MICROBROWSER_VIEW ) ;
	            directory = tree.getDirectory( path ) ; 
            }
            else {
                session.setAttribute( AttributeKey.WORKFLOW_EDITOR_LAST_MICROBROWSER_VIEW, path ) ;
            }
        }
        else if( mode.indexOf( "choose-file-for-workflow" ) != -1) {
            // We have been invoked from the Workflow Editor when
            // choosing a file as parameter in a workflow...
            if( directory == null ) {
	            path = (String)session.getAttribute( AttributeKey.PARAMETER_SELECTOR_LAST_MICROBROWSER_VIEW ) ;
	            directory = tree.getDirectory( path ) ; 
            }
            else {
                session.setAttribute( AttributeKey.PARAMETER_SELECTOR_LAST_MICROBROWSER_VIEW, path ) ;
            }
            
        }
        else {
            throw new ProcessingException( "MySpace Explorer or Micro-Browser invoked from unknown context") ;
        }

        if( directory == null ) {
            directory = tree ; // as a default, choose home directory
        }
        if( directory.isFilled() == false ) {
            tree.constructBranch( directory ) ;
        }

        XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
        xmlReader.setContentHandler( super.xmlConsumer ) ;
        InputSource source = new InputSource( new StringReader( directory.toDirectoryViewXmlString() ) ) ;
        xmlReader.parse( source ) ;
    }
    catch( IOException iox ) {
        this.getLogger().error("Cannot find directory for user");
        // throw new ProcessingException("cannot find files for user");
    }
    finally {
        if( TRACE_ENABLED) this.getLogger().debug( "exit: DirectoryViewGenerator.generate()" );
    }
    
  } // end of generate()
  
} // end of class
