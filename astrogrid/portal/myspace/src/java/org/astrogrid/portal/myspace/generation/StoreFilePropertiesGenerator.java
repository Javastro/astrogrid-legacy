package org.astrogrid.portal.myspace.generation;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapper;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapperFactory;
import org.astrogrid.portal.myspace.acting.framework.MySpaceHandler;
import org.astrogrid.portal.myspace.filesystem.Tree;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
//import org.astrogrid.store.Agsl;
//import org.astrogrid.store.delegate.StoreFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;
import org.astrogrid.portal.common.session.AttributeKey ;

import org.astrogrid.portal.myspace.filesystem.File ;

/**
 * @author peter.shillan
 */
public class StoreFilePropertiesGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = true ;
  private static final boolean DEBUG_ENABLED = true ;
    
  private static final String FILE_PROPS = "properties";
  private static final String FILE_CREATED = "created";
  private static final String FILE_MIME_TYPE = "mime-type";
  private static final String FILE_MODIFIED = "modified";
  private static final String FILE_NAME = "name";
  private static final String FILE_OWNER = "owner";
  private static final String FILE_PATH = "path";
  private static final String FILE_SIZE = "size";
  private static final String FILE_URL = "url" ;

//  private ContextWrapper context;
  private Request request ;
  private Parameters params ;
  private AstrogridSession session ;

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
      
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    // ActionUtils utils = ActionUtilsFactory.getActionUtils();
    this.request = ObjectModelHelper.getRequest(objectModel);
    this.params = params ;
    this.session = AstrogridSessionFactory.getSession ( request.getSession(true) ) ;

    // Create environmental context.
    //    context = ContextWrapperFactory.getContextWrapper(
    //    ContextWrapper.PARAM_PROTOCOL, utils, params, request, session);
  }
 
  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
      if( TRACE_ENABLED) this.getLogger().debug( "entry: StorageFilePropertyGenerator.generate()" );
      
//    String src = context.getParameter(MySpaceHandler.PARAM_SRC);
      String src = request.getParameter( MySpaceHandler.PARAM_SOURCE_PATH ) ;
      
      try {        
          File file = ((Tree)session.getAttribute(AttributeKey.MYSPACE_TREE)).getFile( src ) ;
          if( file == null ) {
              throw new ProcessingException( "Source [" + src + "] is not a file") ;
          }
          XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
          xmlReader.setContentHandler( super.xmlConsumer ) ;
          InputSource source = new InputSource( new StringReader( file.toDetailsViewXmlString() ) ) ;
          xmlReader.parse( source ) ;
      }
      catch( IOException iox ) {
          this.getLogger().error("Cannot find files for user");
          // throw new ProcessingException("cannot find files for user");
      }
      finally {
          if( TRACE_ENABLED) this.getLogger().debug( "exit: StorageFilePropertyGenerator.generate()" );
      }
      
  }
  
}