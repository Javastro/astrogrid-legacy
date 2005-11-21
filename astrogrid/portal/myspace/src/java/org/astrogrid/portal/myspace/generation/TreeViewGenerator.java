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
//import org.astrogrid.store.Ivorn;

//import org.apache.log.Logger ;

import org.astrogrid.portal.myspace.filesystem.*;

/**
 * Generate <code>StoreClient</code> list of files as XML.
 * 
 * @author jeff.lusted
 */
public class TreeViewGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = true ;
  private static final boolean DEBUG_ENABLED = true ;
  private AstrogridSession session ;
  
  /**
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
  	  throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    ///ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
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
    if( TRACE_ENABLED) this.getLogger().debug( "enter: TreeViewGenerator.generate()" );
        
    try {        
        Tree fileTree = TreeHelper.getTree( session ) ;
        String[] openBranches = (String[])session.getAttribute(AttributeKey.MYSPACE_TREE_OPEN_BRANCHES ) ;
        if( DEBUG_ENABLED ) {
            if( openBranches == null ) {
                this.getLogger().debug( "openBranches[]: null" ) ;
            }
            else {
                for( int i=0; i < openBranches.length; i++ ) {
                    this.getLogger().debug( "openBranches[" + i + "]: " + openBranches[i] ) ;
                }
            }
        }
        XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
        xmlReader.setContentHandler( super.xmlConsumer ) ;
        InputSource source = new InputSource( new StringReader( fileTree.toXmlString( openBranches ) ) ) ;
        xmlReader.parse( source ) ;
    }
    catch( IOException iox ) {
        this.getLogger().error("cannot find files for user");
        // throw new ProcessingException("cannot find files for user");
    }
    finally {
        if( TRACE_ENABLED) this.getLogger().debug( "exit: TreeViewGenerator.generate()" );
    }
    
  } // end of generate()
  
} // end of class
