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
import org.astrogrid.registry.RegistryConfig;
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.w3c.dom.Document;
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
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_ACTION = "action";
   
   private static final String PARAM_CRITERIA_NUMBER = "criteria_number";   

   private static final String PARAM_MAIN_ELEMENT = "mainelement";   
   
   private static final Integer DEFAULT_CRITERIA_NUMBER = new Integer(1);
   
   private static final String QUERY_ACTION = "selectquery";

   private static final String ADD_CRITERIA_ACTION = "addcriteria";   
   
   private static final String SEARCH_REGISTRY_NAME = "searchregistryname";
      
   private static final String PUBLISH_REGISTRY_NAME = "publishregistryname";   
   

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
      
      String action = (String)request.getParameter(PARAM_ACTION);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
      }

      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
      if(DEBUG_FLAG) {
         System.out.println("the action is = " + action);      
         System.out.println("the mainElem = " + mainElem);
      }            
      RegistryConfig.loadConfig();
      
      File fi = null;
      //Load Templates.
      //Do the createMap.
      //Now go thorugh the map and put it in a Hashmap.
      //this Hashmap will be a choice box.
      if(RegistryOptionAction.ORGANISATION_OPTION.equals(mainElem)) {         
         fi = RegistryConfig.getRegistryOrganisationTemplate();
      }else if(RegistryOptionAction.AUTHORITY_OPTION.equals(mainElem)) {
         fi = RegistryConfig.getRegistryAuthorityTemplate();
      }else if(RegistryOptionAction.REGISTRY_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.SKYSERVICE_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.TABULARSKYSERVICE_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.DATACOLLECTION_OPTION.equals(mainElem)) {
         
      }
      
      Document registryDocument = null;
      //Create the Document object and throw it to createMap
      //now get the unique last text nodes of the map.
      //should be able to do an indexOf and see if their is an "attr" which you put in "@" in front of the string.
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
      Map mp = RegistryAdminDocumentHelper.createMap(registryDocument);
      Set st = mp.keySet();
      Iterator iter = st.iterator();
      TreeMap selectItems = new TreeMap();
      
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

      
      LinkedHashMap compareTypeList = new LinkedHashMap();
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

      
      ArrayList joinTypes = new ArrayList();
      joinTypes.add("AND");
      joinTypes.add("OR");
      request.setAttribute("JoinTypes",joinTypes);

      int crit_number = 0;
      String crit_number_str = request.getParameter(PARAM_CRITERIA_NUMBER);
      if(crit_number_str != null && crit_number_str.length() > 0) {
         crit_number = Integer.parseInt(crit_number_str);
      }      
      
      String searchRegName = RegistryConfig.getProperty("search.registry.name");
      String publishRegName = RegistryConfig.getProperty("publish.registry.name");

      String result = null;
      ArrayList resultXML = null;
      if(crit_number <= 0) {
         crit_number = 1;      
      }
      if(ADD_CRITERIA_ACTION.equals(action)) {
         crit_number++;
      }else if(QUERY_ACTION.equals(action)) {
         
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
            String url = null;
            String chosenReg = request.getParameter("registryname");
            if(chosenReg.equals(publishRegName)) {
               url = RegistryConfig.getProperty("publish.registry.query.url");
            }else {
               url = RegistryConfig.getProperty("search.registry.query.url");
            }
            
            System.out.println("okay calling rs with url = " + url + " and query = " + query);
            RegistryService rs = new RegistryService(url);
            Document doc = rs.submitQueryString(query);
            resultXML = createFormResults(XMLUtils.DocumentToString(doc),mainElem);
            request.setAttribute("resultxml",resultXML);
         }catch(Exception e) {
            e.printStackTrace();
         }
         //now call the client.
         
      }
      
      request.setAttribute(PARAM_CRITERIA_NUMBER,String.valueOf(crit_number));
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put(PARAM_MAIN_ELEMENT,mainElem);
      results.put(PARAM_CRITERIA_NUMBER,String.valueOf(crit_number));
      results.put(SEARCH_REGISTRY_NAME,searchRegName);
      results.put(PUBLISH_REGISTRY_NAME,publishRegName);
      if(result != null && result.length() > 0) {
         results.put("queryresult",result);
      }
      return results;
      
   }  
   
   private ArrayList createFormResults(String docResults,String mainElem) {
      String lookup = null;
      String endLookup = null;
      if(RegistryOptionAction.ORGANISATION_OPTION.equals(mainElem)) {         
         lookup = "<vc:" + mainElem;
         endLookup = "</vc:" + mainElem + ">";
      }else if(RegistryOptionAction.AUTHORITY_OPTION.equals(mainElem)) {
         lookup = "<vg:" + mainElem;
         endLookup = "</vg:" + mainElem + ">";
      }else if(RegistryOptionAction.REGISTRY_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.SKYSERVICE_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.TABULARSKYSERVICE_OPTION.equals(mainElem)) {
      }else if(RegistryOptionAction.DATACOLLECTION_OPTION.equals(mainElem)) {
         
      }
      int index = 0;
      int endIndex = 0;
      //String inputUpdateForms = "<form name=\"RegistryUpdate\" method=\"get\" action=\"registryupdate.html\">\n";
      //inputUpdateForms += "<input type=\"submit\" name=\"registryupdate\" value=\"Update This entry\" />";
      String results = "";
      int startindex = docResults.indexOf(lookup,index);
      int resultEndIndex = docResults.indexOf(endLookup,index) + endLookup.length();
      index = startindex;
      //System.out.println("start while startindex = " + startindex + " endindex = " + resultEndIndex + " lookup = " + lookup + " endlookup = " + endLookup);
      String temp = null;
      ArrayList al = new ArrayList();
      while(index != -1) {
         temp = "<vodescription>" + docResults.substring(startindex,resultEndIndex) + "</vodescription>";          
         al.add(temp);
         index++;
         index = docResults.indexOf(lookup,startindex+1);
         startindex = index;
         resultEndIndex = docResults.indexOf(endLookup,startindex) + endLookup.length();
         
         //System.out.println("in while index = " + index + " startindex = " + startindex + " endindex = " + resultEndIndex + " lookup = " + lookup + " endlookup = " + endLookup);
         //System.out.println("the substring from startindex = " + docResults.substring(startindex));         
         //System.out.println("the substring from index = " + docResults.substring(index));
      }//while
      System.out.println("the result is = " + results + " the end");
      
      return al;
   }
   
}