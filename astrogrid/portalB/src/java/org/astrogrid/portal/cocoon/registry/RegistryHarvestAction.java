package org.astrogrid.portal.cocoon.registry;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.w3c.dom.Document;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.RegistryConfig;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import java.net.URL;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;


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

      String action = request.getParameter("action");
      String errorMessage = null;
      String message = null;
      //Was this coming from the query page with a huge xml string
      //containing how to call the registry.
      String registryXML = request.getParameter("registryxml");
      if(registryXML != null && registryXML.length() > 0 ) {
         request.setAttribute("registryxml",registryXML);
      }
      //Get the date format.
      String dateSince = request.getParameter("date_since");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date dat = null;
      String newRegistry = request.getParameter("newregistry");
      if(newRegistry != null && newRegistry.trim().length() > 0) {
         dateSince = "1950-02-02T00:00:00";   
      }      
      if(dateSince == null || dateSince.length() <= 0) {
         System.out.println("no date found");
         //errorMessage = "No Date Yet specefied on the form";
      }else {
         try {
            dat = sdf.parse(dateSince);
            System.out.println("the date = " + dat.toString());
         }catch(ParseException pe) {
            pe.printStackTrace();
            errorMessage = "This does not seem to be a proper date";
         }         
      }
      
      RegistryConfig.loadConfig();
      String url = null;
      //get the access url from the user.
      String accessURL = request.getParameter("accessurl"); 
      RegistryAdminService ras = null;
      Document harvestDoc = null;
      String harvestResult = "";
      Document tempDoc = null;
      try {
         //get where you putting the harvest results to.
         url = RegistryConfig.getProperty("publish.registry.update.url");
         //instantiate the delegate.
         ras = new RegistryAdminService(url);
            
            
            int startTemp = -1;
            int endTemp = -1;
               if(registryXML != null &&  registryXML.length() > 0) {
                  startTemp = registryXML.indexOf("<AccessURL>");            
                  endTemp = registryXML.indexOf("</AccessURL>");
               }//if
               if(startTemp != -1 && endTemp != -1) {
                  accessURL = registryXML.substring(startTemp + "<AccessURL>".length(),endTemp);
               }//if
            if(accessURL != null) {
               if(accessURL.indexOf("?") == -1) {
                  accessURL += "?verb=ListRecords&dateFrom=" + sdf.format(dat);   
               }else {
                  accessURL += "&verb=ListRecords&dateFrom=" + sdf.format(dat);                  
               }
               System.out.println("the accessurl = " + accessURL);
               
               DocumentBuilder registryBuilder = null;
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               registryBuilder = dbf.newDocumentBuilder();
               harvestDoc = registryBuilder.parse(accessURL);
               
               //Now check are calling updateDocument or addRegistryEntries.
               if(request.getParameter("addregistries") != null) {
                  ras.addRegistryEntries(harvestDoc);
                  message = "Sucess call in getting metadata from a URL.  " +                            "Only the Registry entries below will be added to the registry.  " +                            "No other metadata will be added.";
               } else {
                  if(registryXML != null) {
                     ras.update(harvestDoc);
                     message = "Success call in getting metadata from a registry." +                               " The metadata below has been harvested and updated into the registry";
                  } else {
                     errorMessage = "Incorrect access to this page.";
                  }//else                     
               }//else
               harvestResult = XMLUtils.DocumentToString(harvestDoc);
               
               //System.out.println("the returned doc = " + XMLUtils.DocumentToString(harvestDoc));
               //resultDoc = XMLUtils.DocumentToString(harvestDoc);                      
            }else {
               errorMessage = "No Access URL could be found. Please enter this page through the Registry Browser page.";
            }
      }catch(Exception e) {
         e.printStackTrace();
         errorMessage = e.toString();         
      }
      System.out.println("Okay message = " + message + " errorMessage = " + errorMessage);
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put("message",message);
      results.put("harvestResult",harvestResult);
      results.put("errorMessage",errorMessage);
      results.put("registryXML",registryXML);
      results.put("accessURL",accessURL);
      return results;
   }  
}