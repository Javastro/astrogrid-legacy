package org.astrogrid.portal.cocoon.registry;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.StringReader;
import java.io.Reader;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import org.astrogrid.registry.RegistryConfig;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * Main Options component to let the user determine the next course of action which are Query, Add, or Harvest new
 * registry entry.
 *
 */
public class RegistryMetaDataReader extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;

   /**
    * Our action method.
    *
    */
   public Map act(
      Redirector redirector, 
      SourceResolver resolver, 
      Map objectModel, 
      String source, 
      Parameters params)
      {
      
         Element elem = null;
         Request request = ObjectModelHelper.getRequest(objectModel);
         String xmlContent = request.getParameter("xmlcontent");



         //Instantiate a new Registry Service for the query
         String url = RegistryConfig.getProperty("publish.registry.query.url");
         RegistryService rs = new RegistryService(url);
         Document doc = null;
         //String resultDoc = XMLUtils.DocumentToString(harvestDoc);
         try {
            if(xmlContent != null && xmlContent.trim().length() > 0) {
               Reader reader2 = new StringReader(xmlContent);
               InputSource inputSource = new InputSource(reader2);
               DocumentBuilder registryBuilder = null;
               registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
               doc = registryBuilder.parse(inputSource);
            }
         }catch(Exception e) {
            doc = null;
            e.printStackTrace();
         }
                     
         try {
            
            
            //Now go and harvest the registry for changes since this date.
            if(elem == null) {
               doc = rs.loadRegistry(null);
            }
            if(doc != null) {            
               elem = doc.getDocumentElement();
               request.setAttribute("resultXML",elem);
            }
            //Writ it out to the outputstream for display.
            //XMLUtils.DocumentToStream(harvestDoc,out);
         }catch(Exception e) {
            e.printStackTrace();
         }
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      return results;
   }
     
}