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
import org.w3c.dom.NamedNodeMap;
import java.util.Map;
import java.util.HashMap;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import org.astrogrid.registry.common.RegistryConfig;
import org.astrogrid.registry.common.versionNS.IRegistryInfo;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.harvest.RegistryHarvestService;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.apache.cocoon.components.request.multipart.FilePart;
import org.apache.cocoon.components.request.multipart.FilePartFile;
import java.io.File;





/**
 * Used for harvesting Metadata from another registry.  Depending on the request may harvest all metadata or
 * harvest a paricular registry data.
 *
 */
public class RegistryHarvestAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_ACTION = "action";
   
   private static final String PARAM_CRITERIA_NUMBER = "criteria_number";   

   private static final String PARAM_MAIN_SEARCH_ELEMENT = "searchelement";   
   
   private static final Integer DEFAULT_CRITERIA_NUMBER = new Integer(1);
   
   private static final String HARVEST_THIS_REGISTRY_ACTION = "harvestthis";
   
   private static final String HARVEST_OTHER_REGISTRY_ACTION = "harvestother";   
   
   private static final String ERROR_MESSAGE = "errormessage";   
      
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
      
      //
      // Get our current request and session.
      Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();
      Map results = new HashMap() ;

      String errorMessage = null;
      String message = null;
      //Was this coming from the query page with a huge xml string
      //containing how to call the registry.
      
      RegistryConfig.loadConfig();
      String url = null;
       
      Document harvestDoc = null;
      String harvestResult = "";
      url = RegistryConfig.getProperty("registry.harvest.url");
      Document resultDoc = null;
      RegistryAdminService ras = null;
      try {
         //get where you putting the harvest results to.
            
               DocumentBuilder registryBuilder = null;
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               registryBuilder = dbf.newDocumentBuilder();
               
               //Now check are calling updateDocument or addRegistryEntries.
               //TODO do this portal thing.
               if(request.getParameter("addmetadatafromurl") != null) {
                  String accessURL = request.getParameter("metadata_url");
                  if(accessURL == null || accessURL.trim().length() <= 0) {
                     errorMessage = "A URL was not given for harvesting.";
                  }
                  if(errorMessage == null) {
                     url = RegistryConfig.getProperty("publish.registry.update.url");
                     //instantiate the delegate.
                     ras = new RegistryAdminService(url);
                     
                     harvestDoc = registryBuilder.parse(accessURL);
                     
                     //ras.harvestFromUrl(accessURL);
                     resultDoc = ras.update(harvestDoc);
                     //Now see if their is a error element in the resultDoc
                     //ras.addRegistryEntries(harvestDoc);
                     //message = "A harvest has begun for url = " + accessURL;
                     //results.put("addregistry","true");
                  }//if                  
               } else if(request.getParameter("addmetadatafromfile") != null) {
                  FilePart filePart = (FilePart) request.get("metadata_file");
                  File file = ((FilePartFile)filePart).getFile();
                  url = RegistryConfig.getProperty("publish.registry.update.url");
                  ras = new RegistryAdminService(url);
                  harvestDoc = registryBuilder.parse(file);
                  resultDoc = ras.update(harvestDoc);
               } else if(request.getParameter("registryXML") != null) {
                  String registryXML = request.getParameter("registryXML");
                  String dateSince = request.getParameter("dateFrom");
                  SimpleDateFormat sdf = null;
                  Date dat = null;
                  if(dateSince != null && dateSince.trim().length() > 0) {                  
                     try {
                        if(dateSince.indexOf("T") == -1) {
                              sdf = new SimpleDateFormat("yyyy-MM-dd");
                              dat = sdf.parse(dateSince);
                              dateSince = dateSince.trim();
                              dateSince += "T00:00:00";   
                        }
                           sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                           dat = sdf.parse(dateSince);
                           //System.out.println("the date = " + dat.toString());
                     }catch(ParseException pe) {
                           pe.printStackTrace();
                           errorMessage = "You must have a proper date given.  Please enter a date in the format of yyyy-mm-dd or yyyy-mm-ddThh:mm:ss";
                     }         
                  }else {
                     dateSince = null;
                  }
                     
                  //change this do it from an identifier and a date on the web service..
                  Reader reader2 = new StringReader(registryXML);
                  InputSource inputSource = new InputSource(reader2);
                  harvestDoc = registryBuilder.parse(inputSource);
                  if(errorMessage == null) {
                     url = RegistryConfig.getProperty("registry.harvest.url");
                     RegistryHarvestService rhs = new RegistryHarvestService(url);
                     if(dat == null) {
                        rhs.harvestResource(harvestDoc);
                     }else {
                        rhs.harvestResource(harvestDoc);
                     }
                     message = "Harvesting has begun on the registry. Please see the Registry Status page on events happening to the Registry.";
                  }//if
            }//elseif
      }catch(Exception e) {
         e.printStackTrace();
         errorMessage = e.toString();         
      }
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      results.put("message",message);
      results.put("errorMessage",errorMessage);

      return results;
   }
        
}