package org.astrogrid.portal.registry;

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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
// import org.astrogrid.registry.client.harvest.RegistryHarvestService;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.astrogrid.util.DomHelper;
//import org.apache.cocoon.components.request.multipart.FilePart;
//import org.apache.cocoon.components.request.multipart.FilePartFile;
import org.apache.cocoon.servlet.multipart.*;
import java.io.File;
//import org.apache.cocoon.servlet.multipart.Part;





/**
 * Used for harvesting Metadata from another registry.  Depending on the request may harvest all metadata or
 * harvest a paricular registry data.
 *
 */
public class RegistryAdminAction extends AbstractAction
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
      Document harvestDoc = null;
      String harvestResult = "";
      Document resultDoc = null;
      RegistryAdminService ras = null;
      System.out.println("inside action of admin");
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
                     //instantiate the delegate.
                     ras = RegistryDelegateFactory.createAdmin();
                     
                     harvestDoc = registryBuilder.parse(accessURL);
                     
                     //ras.harvestFromUrl(accessURL);
                     resultDoc = ras.update(harvestDoc);
                     //ras.validateDocument(harvestDoc);
                     //Now see if their is a error element in the resultDoc
                     //ras.addRegistryEntries(harvestDoc);
                     //message = "A harvest has begun for url = " + accessURL;
                     //results.put("addregistry","true");
                  }//if                  
               } else if(request.getParameter("addmetadatafromfile") != null) {
                  //request.ge
  //                FilePart filePart = (FilePart) request.get("metadata_file");
                     Part part = (Part) request.get("metadata_file");
                     if (part != null) {
                        ras = RegistryDelegateFactory.createAdmin();
                        harvestDoc = registryBuilder.parse(part.getInputStream());
                        //System.out.println("the harvestdoc TEST = " + DomHelper.DocumentToString(harvestDoc));
                        //System.out.println("the filename = " + part.getFileName() + " the size = " + part.getSize() + " and harvestDoc = " + DomHelper.DocumentToString(harvestDoc));
                        resultDoc = ras.update(harvestDoc);
                        //ras.validateDocument(harvestDoc);
                        // do something with it
                     } else {
                        // parameter not found
                     }
  
  //                File file = ((FilePartFile)filePart).getFile();
  //                ras = RegistryDelegateFactory.createAdmin();
  //                harvestDoc = registryBuilder.parse(file);
  //                resultDoc = ras.update(harvestDoc);
               }
      }catch(Exception e) {
         e.printStackTrace();
         errorMessage = e.toString();         
      }
      
      System.out.println("leaving admin action");
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      results.put("message",message);
      results.put("errorMessage",errorMessage);

      return results;
   }
        
}
