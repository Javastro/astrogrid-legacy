package org.astrogrid.portal.myspace.generation ;

import java.io.IOException;
import java.util.Map;
import java.io.StringReader ;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.astrogrid.community.User;
// simport org.astrogrid.portal.login.common.SessionKeys;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapper;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapperFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import org.apache.log.Logger ;

import org.astrogrid.portal.myspace.filesystem.*;

/**
 * Generate <code>StoreClient</code> list of files as XML.
 * 
 * @author peter.shillan
 */
public class StorageFilesGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = false ;
  private static final boolean DEBUG_ENABLED = false ;
  private ContextWrapper context;
  
  /**
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
  	  throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);

    // Create environmental context.
    context = ContextWrapperFactory.getContextWrapper(
        ContextWrapper.PARAM_PROTOCOL, utils, params, request, session);
  }

  /**
   * Generate <code>StoreClient</code> list of files into as XML.
   * 
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
    if( TRACE_ENABLED) this.getLogger().debug( "enter: StorageFilesGenerator.generate()" );
        
    try {        
        Tree fileTree = Tree.newTree( context.getIvorn() ) ;
        XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
        xmlReader.setContentHandler( super.xmlConsumer ) ;
        InputSource source = new InputSource( new StringReader( fileTree.toXmlString() ) ) ;
        xmlReader.parse( source ) ;
    }
    catch( IOException iox ) {
        this.getLogger().error("cannot find files for user");
        // throw new ProcessingException("cannot find files for user");
    }
    finally {
        if( TRACE_ENABLED) this.getLogger().debug( "exit: StorageFilesGenerator.generate()" );
    }
    
  } // end of generate()
  
} // end of class StorageFilesGenerator
