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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
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
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;

import org.astrogrid.config.Config;



/**
 * Handles the updating and adding of registry Data into the registry.
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
   
   private static final String REGISTRY_ITEMS_PARAM = "regitems";   
   
   private static final String CREATE_COPY_PARAM = "createcopy";   
   
   private static final String UPDATE_XML_PARAM = "updatexml";   
   

   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }


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
      Map mp = null;
      
      //Get the action if any.
      String action = (String)request.getParameter(PARAM_ACTION);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
      }      
      
      //Load Templates.
      //Do the createMap.
      //pass the map to the xsp page for generating the list.
      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
      
      Document returnDocument = null;
      
      Document registryDocument = null;
      DocumentBuilderFactory dbf = null;
      DocumentBuilder regBuilder = null;
      //See if a client passed in the IVOA xml to be updated.
      String updateXML = request.getParameter(UPDATE_XML_PARAM);
      boolean createCopy = false;
      //Are we doing an update or just grabbing the data for template purposes on an add.
      if(request.getParameter(CREATE_COPY_PARAM) != null) {
         createCopy = true;
      }
      String authID = "Identifier AuthorityID";
      String resKey = "Identifier ResourceKey";
      NodeList nl = null;
      if(action == null) {
         //Okay updateXML was used.
         if(updateXML != null && updateXML.length() > 0) {
            try {
            //turnt he xml into a DOM tree.
            Reader reader2 = new StringReader(updateXML);
            InputSource inputSource = new InputSource(reader2);
            regBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            registryDocument = regBuilder.parse(inputSource);
            }catch(Exception e) {
               e.printStackTrace();
            }

            //We want to blank out the authorityid and resource key for 
            //creating a copy area.
            if(createCopy) {
               nl = registryDocument.getElementsByTagName("AuthorityID");
               if(nl.getLength() > 0) {
                  nl.item(0).getFirstChild().setNodeValue("enter authority id");
               }
               nl = registryDocument.getElementsByTagName("ResourceKey");
               if(nl.getLength() > 0) {
                  nl.item(0).getFirstChild().setNodeValue("enter new resource key");
               }
            }
         
         }else {
            try {
              dbf = DocumentBuilderFactory.newInstance();
              dbf.setNamespaceAware(true);
              regBuilder = dbf.newDocumentBuilder();
              registryDocument = regBuilder.parse(RegistryOptionAction.getTemplate(request));
              nl = registryDocument.getElementsByTagName("AuthorityID");
              if(nl.getLength() > 0) {
                 nl.item(0).getFirstChild().setNodeValue("enter authority id");
              }
              nl = registryDocument.getElementsByTagName("ResourceKey");
              if(nl.getLength() > 0) {
                 nl.item(0).getFirstChild().setNodeValue("enter new resource key");
              }
              
            } catch (ParserConfigurationException e) {
              e.printStackTrace();
            } catch (IOException ioe) {
               ioe.printStackTrace();
            } catch (SAXException sax) {
               sax.printStackTrace();
            }         
         }
         //Create the map from the DOM tree.
         mp = RegistryAdminDocumentHelper.createMap(registryDocument);
        // printMap(mp);         
      }

      //Go ahead and load the ManagedAuthorities (Authorities this registry owns and is
      //allowed to update and add)
      HashMap hm = (HashMap)session.getAttribute("ManageAuthorities");
      RegistryService rs = null;
      if(hm == null || hm.size() <= 0) {
         rs = RegistryDelegateFactory.createQuery();
         try {
            hm = rs.managedAuthorities();
         }catch(Exception e) {
            e.printStackTrace();
            hm = null;
            message = "Could not load the Managed Authority ID's for this registry which is required to validate the update.";
         }
         if(hm != null)
            session.setAttribute("ManageAuthorities",hm);
      }               

      //Okay it is an update or add action.      
      if(ADD_ACTION.equals(action) || UPDATE_ACTION.equals(action)) {
         Enumeration enum = request.getParameterNames();
         //LinkedHashMap lhm = new LinkedHashMap();
         boolean validAuthority = true;
         if(hm == null) {
            validAuthority = false;
         }
         mp = (Map)session.getAttribute(REGISTRY_ITEMS_PARAM);
         //put the request results in a LinkedHashMap
         while(enum.hasMoreElements()) {
            String param = (String)enum.nextElement();
            
            if(param.indexOf("/") != -1) {
               String val = request.getParameter(param);
               //System.out.println("the param was = " + param + " and val = " + val);
               //if(val != null && val.trim().length() > 0) {
                  if(mp.containsKey(param)) {
                     mp.put(param,val);
                  }
                  //NOT NEEDED lhm.put(param,val);
               //}
               //make sure you have authority to make an add or update.
               if(param.indexOf("vg:") == -1 && param.indexOf("AuthorityID") != -1 && param.indexOf("Identifier") != -1) {
                  if(hm != null && !hm.containsKey(val.trim())) {
                     validAuthority = false;
                  }//if
               }//if
            }//if
         }//while
         //Debug lets print out the map.
//         printMap(mp);
         //make sure it is a valid authority.
         if(validAuthority) {         
         
         //Create the DOM tree from the map.
         Document finalDoc = RegistryAdminDocumentHelper.createDocument(mp);
         System.out.println("the finalDoc to be sending = " + XMLUtils.DocumentToString(finalDoc));
            if(validateUpdateDocument(finalDoc)) {
               
               //Now lets create a Mapping.
               //TODO this is not right need to create a mapping from the update service call below.
               
                  try {
                     //System.out.println("okay the url = " + url);
                     RegistryAdminService ras = RegistryDelegateFactory.createAdmin();
                     
                     if("add".equals(action)) {
                        returnDocument = ras.add(finalDoc);
                        if(finalDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Authority").getLength() > 0) {
                           RegistryService rs2 = RegistryDelegateFactory.createQuery();
                           hm = rs2.managedAuthorities();
                           session.setAttribute("ManageAuthorities",hm);
                        }
                     }else {
                        returnDocument = ras.update(finalDoc);
                     }
                     
                     message = getResultMessage(returnDocument);            
                  }catch(Exception e) {
                     e.printStackTrace();
                  }
            }else {
               message = "All the required parameters are not filled in. Authority ID, Title, Subject and Description are required.";   
            }
         }else {
            //errorMessage happened not a valid authorityid.
            message = "This does not seem to be a AuthorityID managed by this Registry.  Please add an Authority or change your AuthorityID";
         }
       //call the client service update method  
      }
      
      if(mp != null && mp.size() > 0) {
         session.setAttribute(REGISTRY_ITEMS_PARAM,mp);
      }//if
      
      //set the action for the portal.
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
      results.put("authID",authID);
      results.put("resKey",resKey);
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
   
   private boolean validateUpdateDocument(Document doc) {
      boolean val = true;
      NodeList nl = null;
      nl = doc.getElementsByTagName("AuthorityID");
      if(nl.getLength() <= 0 || !nl.item(0).hasChildNodes())
         val = false;
      nl = doc.getElementsByTagName("Description");
      if(nl.getLength() <= 0 || !nl.item(0).hasChildNodes())
         val = false;
      nl = doc.getElementsByTagName("Title");
      System.out.println("the title nl = " + nl.getLength() + "look at childnodes = " +  nl.item(0).hasChildNodes() + " and nodevalud = " + nl.item(0).getFirstChild().getNodeValue() + " and length2 = " + nl.item(0).getFirstChild().getNodeValue().trim().length());
      System.out.println("nodename = " + nl.item(0).getFirstChild().getNodeName() + " and number of children = " + nl.item(0).getChildNodes().getLength());
      if(nl.getLength() <= 0 || !nl.item(0).hasChildNodes() || nl.item(0).getFirstChild().getNodeValue() == null || nl.item(0).getFirstChild().getNodeValue().trim().length() <= 0) 
         val = false;
      nl = doc.getElementsByTagName("Subject");
      if(nl.getLength() <= 0 || !nl.item(0).hasChildNodes())
         val = false;
      System.out.println("okay in validateUpdatedocument and return = " + val);
      return val;
   }//validateUpdateDocument 
   
   private String getResultMessage(Document doc) {
      String message = "The Registry has been updated";
      if(doc != null) {
         NodeList nl = doc.getElementsByTagName("error");
         if(nl.getLength() > 0) {
            message = nl.item(0).getFirstChild().getNodeValue();
         }//if
      }
      return message;  
   }
   
   
}