package org.astrogrid.portal.myspace.generation;

import java.io.IOException;
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
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author peter.shillan
 */
public class StoreFilePropertiesGenerator extends AbstractGenerator {
  private static final String STORE_FILE_PROPS = "properties";
  private static final String STORE_FILE_CREATED = "created";
  private static final String STORE_FILE_MIME_TYPE = "mime-type";
  private static final String STORE_FILE_MODIFIED = "modified";
  private static final String STORE_FILE_NAME = "name";
  private static final String STORE_FILE_OWNER = "owner";
  private static final String STORE_FILE_PATH = "path";
  private static final String STORE_FILE_SIZE = "size";
  private static final String STORE_FILE_URL = "url" ;

  private ContextWrapper context;

  /* (non-Javadoc)
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
 
  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
    // TODO Auto-generated method stub
    String src = context.getParameter(MySpaceHandler.PARAM_SRC);
    
    // Validate the parameters.
    if(src != null && src.length() > 0) {
      StoreFile storeFile = context.getStoreClient().getFile(src);
      
      if(storeFile == null || !storeFile.isFile()) {
        throw new ProcessingException(src + " is not a file");
      }
      
      Document document = null;
      
      // Create document we are generating.
      try {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        document = builder.newDocument();
      }
      catch(Exception e) {
        new ProcessingException("failed to create new document", e);
      }
      
      Element rootElement = document.createElement(StoreFilePropertiesGenerator.STORE_FILE_PROPS);
      // TODO use java.text.DateFormat
      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_CREATED,
          storeFile.getCreated().toString());

      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_MIME_TYPE,
          storeFile.getMimeType());
      
      // TODO use java.text.DateFormat
/*      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_MODIFIED,
          storeFile.getModified().toLocaleString());
*/      
      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_NAME,
          storeFile.getName());
      
//      rootElement.setAttribute(
//          StoreFilePropertiesGenerator.STORE_FILE_OWNER,
//          storeFile.getOwner());
      
      //JL. This is a hack. The above is the kosher way.
      rootElement.setAttribute(
              StoreFilePropertiesGenerator.STORE_FILE_OWNER,
              context.getUser().getUserId() );
      
      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_PATH,
          storeFile.getPath());
      
      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_SIZE,
          Long.toString(storeFile.getSize()));
      
      Agsl agsl = context.getStoreClient().getAgsl(storeFile.getPath());
      
      rootElement.setAttribute(
          StoreFilePropertiesGenerator.STORE_FILE_URL,
          agsl.resolveURL().toString() );

      document.appendChild(rootElement);

      // Stream the fully produced document.
      DOMStreamer streamer = new DOMStreamer();
      streamer.setNormalizeNamespaces(false);
      streamer.setContentHandler(contentHandler);
      streamer.stream(document);
    }
    else {
      throw new ProcessingException("invalid store file");
    }
  }
}