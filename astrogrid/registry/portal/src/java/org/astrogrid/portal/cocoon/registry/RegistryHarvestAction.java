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
      String dateSince = request.getParameter("dateFrom");
      SimpleDateFormat sdf = null;
      Date dat = null;
      
      RegistryConfig.loadConfig();
      String url = null;
      //get the access url from the user.
      String accessURL = request.getParameter("accessurl"); 
      Document harvestDoc = null;
      String harvestResult = "";
      Document tempDoc = null;
      try {
         //get where you putting the harvest results to.
            
            /*
            int startTemp = -1;
            int endTemp = -1;
               if(registryXML != null &&  registryXML.length() > 0) {
                  startTemp = registryXML.indexOf("<AccessURL");            
                  endTemp = registryXML.indexOf("</AccessURL>");
               }//if
         
               if(startTemp != -1 && endTemp != -1) {
                  startTemp = registryXML.indexOf(">",startTemp) + 1;
                  System.out.println("the startTemp = " + startTemp + " and endTemp = " + endTemp);
                  accessURL = registryXML.substring(startTemp,endTemp);
               }//if
               System.out.println("the startTemp = " + startTemp + " endTemp = " + endTemp + " and accessurl = " + accessURL);
               if(accessURL.indexOf("?") == -1) {
                  accessURL += "?verb=ListRecords&from=" + sdf.format(dat);   
               }else {
                  accessURL += "&verb=ListRecords&from=" + sdf.format(dat);                  
               }
               System.out.println("the accessurl = " + accessURL);
               */
               DocumentBuilder registryBuilder = null;
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               registryBuilder = dbf.newDocumentBuilder();
               
               //Now check are calling updateDocument or addRegistryEntries.
               if(request.getParameter("addregistries") != null) {
                  if(accessURL == null || accessURL.trim().length() <= 0) {
                     errorMessage = "A URL was not given for grabbing the Registry metadata entries.";
                  }
                  if(errorMessage == null) {
                     url = RegistryConfig.getProperty("publish.registry.update.url");
                     //instantiate the delegate.
                     RegistryAdminService ras = new RegistryAdminService(url);
                     
                     harvestDoc = registryBuilder.parse(accessURL);
                     ras.addRegistryEntries(harvestDoc);
                     message = "Call in getting metadata from a URL.  " +
                               "Only the Registry entries below will be added to the registry.  " +
                               "No other metadata will be added." +
                               "This current version relies on the user going through the Query page for harvesting other registries." +
                               "Please click on the 'Query Registry Page' link provided to harvest one of these registries.";
                     results.put("addregistry","true");
                  }
                  
               } else {
                  if(registryXML != null) {
                     if(dateSince == null || dateSince.length() <= 0) {
                        errorMessage = "No date given. You must have a proper date given.  Please enter a date in the format of yyyy-mm-dd or yyyy-mm-ddThh:mm:ss";
                     }else {
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
                     }
                     
                     Reader reader2 = new StringReader(registryXML);
                     InputSource inputSource = new InputSource(reader2);
                     Document regDoc = registryBuilder.parse(inputSource);
                     NodeList nl = regDoc.getElementsByTagName("vg:Registry");
                     Node nd  = null;
                     Node attrNode = null;
                     RegistryConfig.loadConfig();
                     IRegistryInfo iri = RegistryConfig.loadRegistryInfo();
                     Document finalDoc = iri.getDocument();
                     System.out.println("the nodelist length = " + nl.getLength());
                     for(int i = 0;i < nl.getLength();i++) {
                        nd = nl.item(i);
                        if(nd.hasAttributes()) {
                           NamedNodeMap nnm = nd.getAttributes();
                           attrNode = nnm.getNamedItem("updated");
                           if(attrNode != null) {
                              System.out.println("Now setting the date = " + sdf.format(dat));
                              attrNode.setNodeValue(sdf.format(dat));
                           }else {
                              errorMessage = "Does not seem to be a correct Registry entry.  Needs an 'updated' attribute is required to do" +                                 "harvesting on this registry";
                           }//else
                        }//if
                        Node replacingNode = 
                            ((finalDoc).importNode(nd, true)); 
                        //The import node attaches it to DOM, but you msut still append it as a child to your root element.   
                        finalDoc.getDocumentElement().appendChild(replacingNode);
                     }//for
                     if(errorMessage == null) {
                        url = RegistryConfig.getProperty("registry.harvest.url");
                        RegistryHarvestService rhs = new RegistryHarvestService(url);
                        rhs.harvestRegistry(finalDoc);
                        message = "Harvesting has begun on the registry. Please see the Registry Status page on events happening to the Registry.";
                     }//if
                  } else {
                     errorMessage = "Incorrect access to this page.";
                  }//else                     
               }//else
               if(harvestDoc != null)
                  harvestResult = XMLUtils.DocumentToString(harvestDoc);
      }catch(Exception e) {
         e.printStackTrace();
         errorMessage = e.toString();         
      }
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      results.put("message",message);
      results.put("harvestResult",harvestResult);
      results.put("errorMessage",errorMessage);
      results.put("registryXML",registryXML);
      results.put("accessURL",accessURL);
      
      
      return results;
   }
        
}