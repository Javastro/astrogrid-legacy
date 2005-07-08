package org.astrogrid.portal.myspace.generation;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

import org.astrogrid.query.sql.Sql2Adql;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.astrogrid.portal.common.session.AstrogridSessionFactory ;
import org.astrogrid.portal.common.session.AstrogridSession ;



/**
 * @author jeff.lusted
 */
public class FileStoreListGenerator extends AbstractGenerator {
    
  private static final boolean TRACE_ENABLED = true ;
  private static final boolean DEBUG_ENABLED = true ;
  private static final String FILESTORE_QUERY = 
      "Select * from Registry where vr:content/vr:relationship/vr:relationshipType = 'derived-from' " +
      "and vr:content/vr:relationship/vr:relatedResource/@ivo-id = 'ivo://org.astrogrid/FileStoreKind' " +
      "and @status='active'" ; 

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

  }
 
  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
      if( TRACE_ENABLED) trace( "entry: FileStoreListGenerator.generate()" );
      
      try {        
          XMLReader xmlReader = XMLReaderFactory.createXMLReader() ;
          xmlReader.setContentHandler( super.xmlConsumer ) ;
          InputSource source = new InputSource( new StringReader( this.submitQuery() ) ) ;
          xmlReader.parse( source ) ;
      }
      catch( IOException iox ) {
          this.getLogger().error("Failure in finding file stores.");
          // throw new ProcessingException("");
      }
      finally {
          if( TRACE_ENABLED) trace( "exit: FileStoreListGenerator.generate()" );
      }
      
  }
  
  /**
   * This method submits query to Registry
   * 
   * @return String
   */
  private String submitQuery() {
    if( TRACE_ENABLED ) trace( "FileStoreListGenerator.submitQuery() entry" );
            
    StringBuffer buffer = new StringBuffer( 1024 ) ;
    String result = null ;
    
    try {
        
      RegistryService rs = RegistryDelegateFactory.createQuery( );
      debug( "Service = " + rs );

      String adqlString = Sql2Adql.translateToAdql074( FILESTORE_QUERY );
      debug("ADQL String in PORTAL for REGISTRY = " + adqlString);
      Document doc = rs.search(adqlString);
      NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*","Resource");
      
      buffer.append( "<file-stores " +
      		             "xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v0.10\" " +
      		             "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >" ) ;
      
      for(int i = 0; i < nodes.getLength(); i++) {
          String element = null;
          Node node = nodes.item(i);
          if( node instanceof org.w3c.dom.Element ) {
             element = XMLUtils.ElementToString( (Element) node );
             buffer.append( element );
          }                  
      }
      
      buffer.append( "</file-stores>" ) ;
      result = buffer.toString() ;
      debug( "result: \n" + result ) ;
      
    }    
    catch( Exception ex ) 
    {   
        ex.printStackTrace();     	
        debug("Error thrown whilst submitting query: "); 
        debug( ex.getMessage() ) ;        
    }
      
    finally {
      if( TRACE_ENABLED ) trace( "FileStoreListGenerator.submitQuery() exit" );
    }
    
    return result ;
    
  } // end of submitQuery()
  
  
  private void trace( String traceString ) {
      this.getLogger().debug( traceString );
      // System.out.println( traceString );
  }
    
  private void debug( String logString ){
      this.getLogger().debug( logString );
      // System.out.println( logString );
  }       
  
}