package org.astrogrid.portal.registry;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;

public class RegistryXMLGeneration extends AbstractGenerator {

  private Document registryDocument;
  private boolean oaiView = false;
  
  private Map reqMap = null;

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);

    Logger logger = this.getLogger();

    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);
    
    logger.debug("[act] params:  " + params);
    logger.debug("[act] request: " + request);
    logger.debug("[act] session: " + session);

    //String endPoint = utils.getAnyParameter("myspace-end-point", "http://localhost:8080/myspace", params, request, session);
    //logger.debug("[setup] endPoint: " + endPoint);

    try {
      RegistryService rs = RegistryDelegateFactory.createQuery();
      String ident = request.getParameter("identifier");
      if(ident != null) {
          registryDocument = rs.getResourceByIdentifier(ident);
      }//if
    }
    catch(Exception e) {
      registryDocument = null;
      e.printStackTrace();
    }
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
   public void generate()
    throws IOException, SAXException, ProcessingException {
   
      if(registryDocument != null && oaiView) {
//         registryDocument = 
//               RegistryService.buildOAIDocument(registryDocument,"registrydataview.html", null,reqMap);
      }
   
      if(registryDocument == null) {
         try {
            registryDocument = DomHelper.newDocument();
            Element root = registryDocument.createElement("error");
            root.appendChild(registryDocument.createTextNode("No xml found to display"));
            registryDocument.appendChild(root);
         }catch(Exception e) {
            e.printStackTrace();   
         }            
      }
         
      DOMStreamer streamer = new DOMStreamer();
      streamer.setNormalizeNamespaces(false);
      streamer.setContentHandler(contentHandler);
      streamer.stream(registryDocument);
      
   }
  
}
