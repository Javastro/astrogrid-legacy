package org.astrogrid.portal.cocoon.registry;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.astrogrid.registry.RegistryConfig;
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.util.Enumeration;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.RegistryConfig;


/**
 *
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

   private static final String PARAM_MAIN_ELEMENT = "mainelement";   
   
   private static final Integer DEFAULT_CRITERIA_NUMBER = new Integer(1);
   
   private static final String ADD_ACTION = "add";
   
   private static final String UPDATE_ACTION = "update";   
   
   private static final String REMOVE_ACTION = "remove";   

   private static final String ADD_CRITERIA_ACTION = "addcriteria";   
   

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
      String message = null;
      
      RegistryConfig.loadConfig();
      
      String action = (String)request.getParameter(PARAM_ACTION);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
      }      
      
      //Load Templates.
      //Do the createMap.
      //pass the map to the xsp page for generating the list.
      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
      RegistryConfig.loadConfig();
      
      File fi = null;
      Document registryDocument = null;
      DocumentBuilderFactory dbf = null;
      DocumentBuilder regBuilder = null;
      //Load Templates.
      //Do the createMap.
      //Now go thorugh the map and put it in a Hashmap.
      //this Hashmap will be a choice box.
      String updateXML = request.getParameter("updatexml");
      if(action == null) {
         if(updateXML != null && updateXML.length() > 0) {
            try {
            
            Reader reader2 = new StringReader(updateXML);
            InputSource inputSource = new InputSource(reader2);
            regBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            registryDocument = regBuilder.parse(inputSource);
            }catch(Exception e) {
               e.printStackTrace();
            }
         
         }else {
      
            if(RegistryOptionAction.ORGANISATION_OPTION.equals(mainElem)) {         
               fi = RegistryConfig.getRegistryOrganisationTemplate();
            }else if(RegistryOptionAction.AUTHORITY_OPTION.equals(mainElem)) {
               fi = RegistryConfig.getRegistryAuthorityTemplate();
            }else if(RegistryOptionAction.REGISTRY_OPTION.equals(mainElem)) {
            }else if(RegistryOptionAction.SKYSERVICE_OPTION.equals(mainElem)) {
            }else if(RegistryOptionAction.TABULARSKYSERVICE_OPTION.equals(mainElem)) {
            }else if(RegistryOptionAction.DATACOLLECTION_OPTION.equals(mainElem)) {
      
            }
            try {
              dbf = DocumentBuilderFactory.newInstance();
              dbf.setNamespaceAware(true);
              regBuilder = dbf.newDocumentBuilder();
              registryDocument = regBuilder.parse(fi);
             // System.out.println("the big xml registry = " + XMLUtils.DocumentToString(registryDocument));
            } catch (ParserConfigurationException e) {
              e.printStackTrace();
            } catch (IOException ioe) {
               ioe.printStackTrace();
            } catch (SAXException sax) {
               sax.printStackTrace();
            }         
         }
         Map mp = RegistryAdminDocumentHelper.createMap(registryDocument);
         request.setAttribute("regitems",mp);
         printMap(mp);         
      }
      
     //Create the Document object and throw it to createMap
     //now get the unique last text nodes of the map.
     //should be able to do an indexOf and see if their is an "attr" which you put in "@" in front of the string.
     
      //go through ALL the request variables
      //store it in a LinkedHashMap
      //pass it to the createDocument.            
      
      if(ADD_ACTION.equals(action) || UPDATE_ACTION.equals(action)) {
         Enumeration enum = request.getParameterNames();
         LinkedHashMap lhm = new LinkedHashMap();
         while(enum.hasMoreElements()) {
            String param = (String)enum.nextElement();
            if(param.indexOf("/") != -1) {
               String val = request.getParameter(param);
               if(val != null && val.trim().length() > 0) {
                  lhm.put(param,val);
               }
            }
         }
         printMap(lhm);
         System.out.println("now lets createdocument");
         Document finalDoc = RegistryAdminDocumentHelper.createDocument(lhm);
         System.out.println("the resulting document = " + XMLUtils.DocumentToString(finalDoc) );
         String url = RegistryConfig.getProperty("publish.registry.update.url");
         try {
            System.out.println("okay the url = " + url);
            RegistryAdminService ras = new RegistryAdminService(url);
            ras.update(finalDoc);
            message = "The Registry has been updated";            
         }catch(Exception e) {
            e.printStackTrace();
         }
       //call the client service update method  
      }else if(REMOVE_ACTION.equals(action)) {
//       call the client service remove method
      }
      
      if(mainElem != null && mainElem.length() > 0 ) {
         action = "add";
      }else {
         action = "update";
      }
      System.out.println("setting the action = " + action);
      request.setAttribute("action",action);
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put(PARAM_ACTION,action);
      results.put("message",message);
      return results;
   }
   
   private void printMap(Map tm) {
      Set keySet = tm.keySet();
      Iterator iter = keySet.iterator();
      String key = null;
      while(iter.hasNext()) {
         key = (String)iter.next();
         System.out.println(" The key = " + key + " The value = " + (String)tm.get(key));
      }//while      
   }   
     
}