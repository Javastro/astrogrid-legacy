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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import org.astrogrid.registry.common.RegistryConfig;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * Main Options component to let the user determine the next course of action which are Query, Add, or Harvest new
 * registry entry.
 *
 */
public class RegistryHarvestReader extends AbstractAction
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
      
         Request request = ObjectModelHelper.getRequest(objectModel);
         //load the config.
         RegistryConfig.loadConfig();
         Enumeration enum = request.getParameterNames();
         String param = null;
         //OAI requires having an element with all the requests passed in.
         //this will put all the request and values into a hashmap to be 
         //placed on an DOM element.
         HashMap hm = new HashMap();
         while(enum.hasMoreElements()) {
           param = (String)enum.nextElement();
           hm.put(param,request.getParameter(param));
         }
         //Get the date and if it does not exist then create a very old date.
         String dateSince = request.getParameter("from");
         if(dateSince == null || dateSince.trim().length() <= 0) {
            dateSince = "1950-02-02T00:00:00";   
         }
         
         //A verg and DateFrom are requried request variables, if they are not their
         //then default it to a required variables.
         if(request.getParameter("verb") == null) {
            hm.put("verb","ListRecords");
         }
         if(request.getParameter("from") == null) {
            hm.put("from",dateSince);
         }


         //Instantiate a new Registry Service for the query
         String url = RegistryConfig.getProperty("publish.registry.query.url");
         RegistryService rs = new RegistryService(url);
         //String resultDoc = XMLUtils.DocumentToString(harvestDoc);
                     
         try {
            //Now go and harvest the registry for changes since this date.
            Document tempDoc = rs.harvestQuery(dateSince);
            //The returned document is still in the IVOA schema xml format.
            //go and build the OAI result around it.
            Document harvestDoc = RegistryService.buildOAIDocument(tempDoc,source,dateSince,hm);
            request.setAttribute("resultHarvest",harvestDoc.getDocumentElement());
            //Writ it out to the outputstream for display.
            //XMLUtils.DocumentToStream(harvestDoc,out);
         }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
         }
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      return results;
   }
     
}