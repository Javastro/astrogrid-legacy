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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import org.astrogrid.registry.common.RegistryConfig;
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.astrogrid.registry.client.query.RegistryService;
import org.apache.axis.utils.XMLUtils;

/**
 *
 *
 */
public class RegistryQueryAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   private static final String PARAM_MAIN_ELEMENT = "mainelement";   
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_ACTION = "action";
   
   private static final String PARAM_CRITERIA_NUMBER = "criteria_number";      
   
   private static final Integer DEFAULT_CRITERIA_NUMBER = new Integer(1);
   
   private static final String QUERY_ACTION = "selectquery";

   private static final String ADD_CRITERIA_ACTION = "addcriteria";   
   
   private static final String SEARCH_REGISTRY_NAME = "searchregistryname";
      
   private static final String PUBLISH_REGISTRY_NAME = "publishregistryname";
   
   private static final String ERROR_MESSAGE = "errormessage";
   

   /**
    * Action page to do a query.
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
      String errorMessage = null;
      int crit_number = 0;
      Document registryDocument = null;
      LinkedHashMap compareTypeList = new LinkedHashMap();            
      ArrayList joinTypes = new ArrayList();
      ArrayList resultXML = null;      
      String result = null;
      String searchRegName = null;
      String publishRegName = null;

      RegistryConfig.loadConfig();            
      String action = (String)request.getParameter(PARAM_ACTION);
      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
      
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
         System.out.println("the mainElem = " + mainElem);
      }            

      
      //Load the template.
      File fi = RegistryOptionAction.getTemplate(request);
      if(fi == null) {
         errorMessage = "Cannot find the file Template for loading drop down boxes.";
         //TODO do some type of error logging and exit here.
         //darn some errror is happening.
      }else {
         //Create the Document object and throw it to createMap
         try {
           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           dbf.setNamespaceAware(true);
           DocumentBuilder regBuilder = dbf.newDocumentBuilder();
           registryDocument = regBuilder.parse(fi);
          // System.out.println("the big xml registry = " + XMLUtils.DocumentToString(registryDocument));
         } catch (ParserConfigurationException e) {
           e.printStackTrace();
         } catch (IOException ioe) {
            ioe.printStackTrace();
         } catch (SAXException sax) {
            sax.printStackTrace();
         }
         //now get the unique last text nodes of the map.
         //should be able to do an indexOf and see if their is an "attr" which you put in "@" in front of the string.      
         Map mp = RegistryAdminDocumentHelper.createMap(registryDocument);
         Set st = mp.keySet();
         Iterator iter = st.iterator();
         TreeMap selectItems = new TreeMap();
         //Go through the Map and put it in a TreeMap which is an alphabetical list.
         //And cut off everything but the last node which is the text or attribute node.
         while(iter.hasNext()) {
            String key = (String)iter.next();
            String []split = key.split("\\/");
            
            key = split[(split.length-1)];
            if(Character.isDigit(key.charAt(key.length()-1)))
               key = key.substring(0,key.length()-1);
            if(Character.isDigit(key.charAt(key.length()-1)))
               key = key.substring(0,key.length()-1);
            if(Character.isDigit(key.charAt(key.length()-1)))
               key = key.substring(0,key.length()-1);
            
            if("attr".indexOf(key) != -1) {
               selectItems.put("@" + key,null);
            }else {            
               selectItems.put(key,null);
            }//else
         }//while
         request.setAttribute("selectitems",selectItems);
         
         if(DEBUG_FLAG) {
            System.out.println("selectitems size = " + selectItems.size());
         }            
   
         
         //Create a Linked Hashmap of all the possible types.
         //LinkedHashmap preservest the order of the list.
         compareTypeList.put("Contains","CONTAINS");
         compareTypeList.put("equal","EQ");
         compareTypeList.put("not equal","NE");
         compareTypeList.put("Less Than","LT");
         compareTypeList.put("Greater Than","GT");
         compareTypeList.put("After (for dates)","AFTER");
         request.setAttribute("Comparisons",compareTypeList);
   
         if(DEBUG_FLAG) {
            System.out.println("comparetypelist size = " + compareTypeList.size());
         }            
   
         //Put in the join types.

         joinTypes.add("AND");
         joinTypes.add("OR");
         request.setAttribute("JoinTypes",joinTypes);
   

         //What is the current criteria number count.
         String crit_number_str = request.getParameter(PARAM_CRITERIA_NUMBER);
         if(crit_number_str != null && crit_number_str.length() > 0) {
            crit_number = Integer.parseInt(crit_number_str);
         }      
         
         searchRegName = RegistryConfig.getProperty("search.registry.name");
         publishRegName = RegistryConfig.getProperty("publish.registry.name");
   
         if(crit_number <= 0) {
            crit_number = 1;      
         }
         //What is the action if any.
         if(ADD_CRITERIA_ACTION.equals(action)) {
            //user wants to add more criteria's.
            crit_number++;
         }else if(QUERY_ACTION.equals(action)) {
            //Need to query.  So lets build up the XML for a query.
            String selItem = null;
            String selItemOperation = null;
            String selItemValue = null;
            String selJoinType = null;
            String query = "<query><selectionSequence>" +
            "<selection item='searchElements' itemOp='EQ' value='" + mainElem + "'/>";
            query += "<selectionOp op='$and$'/>";
            selItem = request.getParameter("selectitem0");
            selItemOperation = request.getParameter("selectitemop0");
            selItemValue = request.getParameter("selectitemvalue0");
               
            query += "<selection item='" + selItem + "' itemOp='" + selItemOperation + "' value='" + selItemValue + "'/>";
            for(int i = 1;i < crit_number;i++) {
               selJoinType = request.getParameter(("selectjointype" + i));
               query += "<selectionOp op='" + selJoinType + "'/>";
   
               selItem = request.getParameter("selectitem" + i);
               selItemOperation = request.getParameter("selectitemop" + i);
               selItemValue = request.getParameter("selectitemvalue" + i);
               
               query += "<selection item='" + selItem + "' itemOp='" + selItemOperation + "' value='" + selItemValue + "'/>";
            }
            query += "</selectionSequence></query>";
            try {
               //Now lets query.
               String url = null;
               url = RegistryConfig.getProperty("search.registry.query.url");
               
               RegistryService rs = new RegistryService(url);
               Document doc = rs.submitQueryString(query);
               errorMessage = getResultMessage(doc);
               if(errorMessage == null) {
                  //create the results and put it in the request.
                  resultXML = createFormResults(doc,mainElem);
                  request.setAttribute("resultxml",resultXML);
                  
                  NodeList nl = doc.getDocumentElement().getChildNodes();
                  System.out.println("the Elementtostring in queryaction = " + XMLUtils.ElementToString(doc.getDocumentElement()));
                  ArrayList resultNodes = new ArrayList();
                  resultNodes.add(doc.getDocumentElement());
                  /*for(int i = 0;i < nl.getLength();i++) {
                     resultNodes.add(nl.item(i)); 
                  }*/
                  request.setAttribute("resultNodes",resultNodes);      
                  
                  //Here are the managed authorities.  Which determine which AuthorityID
                  //the registry owns and hence can do an update for.
                  if(resultXML.size() > 0) {
                     HashMap hm = (HashMap)session.getAttribute("ManageAuthorities");
                     if(hm == null || hm.size() <= 0) {
                        url = RegistryConfig.getProperty("publish.registry.query.url");
                        rs = new RegistryService(url);
                        hm = rs.ManagedAuthorities();
                        session.setAttribute("ManageAuthorities",hm);
                     }//if              
                  }//if
               }//if
            }catch(Exception e) {
               e.printStackTrace();
            }
         }//else doing a query
         request.setAttribute(PARAM_CRITERIA_NUMBER,String.valueOf(crit_number));
      }//else         
      
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put(PARAM_MAIN_ELEMENT,mainElem);
      results.put(PARAM_CRITERIA_NUMBER,String.valueOf(crit_number));
      results.put(SEARCH_REGISTRY_NAME,searchRegName);
      results.put(PUBLISH_REGISTRY_NAME,publishRegName);
      results.put(ERROR_MESSAGE,errorMessage);
      if(result != null && result.length() > 0) {
         results.put("queryresult",result);
      }
      return results;
      
   }
   
   /**
    * This method will be deleted later or used with an XSL sheet to make a pretty response for the html.
    * Currently only displays the raw XML form coming back from the query web service response.
    * @param docResults Query results in a DOM tree format.
    * @param mainElem The main element we queried.
    * @return ArrayList of all the String XML results.
    */
   private ArrayList createFormResults(Document docResults,String mainElem) {
      String lookup = null;
      String endLookup = null;
      if(RegistryOptionAction.ORGANISATION_OPTION.equals(mainElem)) {         
         lookup = "<vc:" + mainElem;
         endLookup = "</vc:" + mainElem + ">";
      }else if(RegistryOptionAction.AUTHORITY_OPTION.equals(mainElem)) {
         lookup = "<vg:" + mainElem;
         endLookup = "</vg:" + mainElem + ">";
      }else if(RegistryOptionAction.REGISTRY_OPTION.equals(mainElem)) {
         lookup = "<vg:" + mainElem;
         endLookup = "</vg:" + mainElem + ">";         
      }else if(RegistryOptionAction.SKYSERVICE_OPTION.equals(mainElem)) {
         lookup = "<vs:" + mainElem;
         endLookup = "</vs:" + mainElem + ">";         
      }else if(RegistryOptionAction.TABULARSKYSERVICE_OPTION.equals(mainElem)) {
         lookup = "<vs:" + mainElem;
         endLookup = "</vs:" + mainElem + ">";         
      }else if(RegistryOptionAction.DATACOLLECTION_OPTION.equals(mainElem)) {
         lookup = "<vs:" + mainElem;
         endLookup = "</vs:" + mainElem + ">";
      }else if(RegistryOptionAction.SERVICE_OPTION.equals(mainElem)) {
         lookup = "<vr:" + mainElem;
         endLookup = "</vr:" + mainElem + ">";
      }
      NodeList nl = docResults.getDocumentElement().getChildNodes();
      ArrayList al = new ArrayList();
      for(int i = 0;i < nl.getLength();i++) {
         Element elem = (Element)nl.item(i);
         al.add("<vodescription>" + XMLUtils.ElementToString(elem) + "</vodescription>"); 
      }      
      return al;
   }
   
   private String getResultMessage(Document doc) {
      String message = null;
      NodeList nl = doc.getElementsByTagName("error");      
      if(nl.getLength() > 0) {
         message = nl.item(0).getFirstChild().getNodeValue();
      }//if
      return message;  
   }
}