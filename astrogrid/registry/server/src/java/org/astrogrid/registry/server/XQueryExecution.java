package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The XQueryExecution class accepts an XML formatted query from the
 * RegistryInterface3_0 web service, converts the XML to an XQL query, 
 * and sends the XQL query to Registry3_0.  XQueryExecution then accepts
 * the XML registry response (formatted to schema registry_v1_1.xsd).
 * If the submitQuery method has been called, the registry response
 * is reformatted as a series of responseRecords with name / value
 * key pairs and returned to the RegistryInterface3_0 web service.
 * If the fullNodeQuery method has been called, the registry response
 * is returned to the web service in its registry schema format.
 * 
 * XQueryExecution also identifies how much metadata a web service user
 * has requested: identity only, identity and curation, identity and
 * content, identity and service metadata concepts, or all metadata.
 * The registry response is filtered accordingly.  This software is 
 * based on the IVOA's Resource Service Metadata document v0.7 - it does
 * not yet conform to the IVOA's VOResource 0.9 schema.
 * 
 * @author Kevin Benson
 *
 */

public class XQueryExecution
{
   private static final Log log = LogFactory.getLog(XQueryExecution.class);
   private static String nodeDetails = "";
   private static String searchElements = "";
   
   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }   
   
   public XQueryExecution() {
      log.error("empty constructor for XQueryExecution");
   }
   
   /**
    * Input Document is the XML formatted query from either the parseQuery
    * or parseFullNodeQuery method in this class. The prepareQuery method 
    * converts this XML string into an XQL query and sends it to the 
    * Registry3_0 class.  
    * 
    * This method also registers four user-defined comparisons: isgt, isge, 
    * islt, and isle.  The GMD-IPSI implementation of XQuery only predefines
    * lexical comparisons for the basic logical operators, so these four 
    * user-defined comparisons implement numerical comparisons as well.
    *  
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @throws ClassNotFoundException
    */   
   public static String createXQL (Document query) {

   /**
    * First build a DOM tree out of the XML query.  
    */
      String xqlResponse = "";
      String queryResponse = "";
      boolean goodQuery = true;
      Document doc = query;
      String msg = null;

      String xml_to_xql = null;
     
      //log.info("entered prepareQuery with = " +
      //          DomHelper.DocumentToString(query));
      log.debug("start prepareQuery");

   /**
    * The registry query that requires conversion to XQL will be contained 
    * inside an element called "selectionSequence".  If the selectionSequence 
    * element is not present, the query is incorrectly formatted. For an 
    * example of a registry query and links to the registry query schema, 
    * please see
    * http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQuerySchema. 
    */

      try{
         Element docElement = (Element)doc.getDocumentElement();//.getFirstChild();
         NodeList ssList = docElement.
                           getElementsByTagName("selectionSequence");
         if (ssList.getLength() != 0){
         /**
          * If the query is well formatted, proceed to convert query to XQL
          * by sending the top selectionSequence element and its children to
          * the xmlToXQL method.
          */

            xml_to_xql = "//vr:Resource[" + xmlToXQL(ssList.item(0)) + "]";
            return xml_to_xql;
         }
         else {
            msg = 
         "QUERY - Query does not conform to Astrogrid registry query schema.";
            goodQuery = false;
         } 
      }
      catch (Exception e) {
         msg = "QUERY 1 - " + e.toString();
         goodQuery = false;
         e.printStackTrace();
         log.error(e);
      }
      log.debug("end prepareQuery");
      //throw an excepiton instead of null.
      return null;
   }
   
   private static String xmlToXQL(Node node) {
    /**
     * The xmlToXQL method converts an XML node query to XQL. Input 
     * node generated from the top level selectionSequence element
     */

      log.debug("start xmlToXQL");
      String response = "";
      nodeDetails = "";
      response = response + "(";
      NodeList nlSS = node.getChildNodes();
      boolean se = false;
      boolean it = true;
      boolean itOp = true;
      boolean val = true;
      boolean goodQuery = true;
      String itemName = null;
      String xqlStart = null;
		
      log.info("enters xmltoxql");
      /**
       * Loop through the child nodes of selectionSequence to add all search
       * elements, operators, and element values to the XQL query.
       */      
      for (int z=0; z < nlSS.getLength(); z++){
         boolean wildcardon = false;
         if (nlSS.item(z).getNodeName().equals("selectionOp")){
	 /**
          * The selectionOp element contains one attribute specifying XML 
          * boolean operators. Convert to XQL boolean operators.
          */
            String selOp = "";
            if (((Element) nlSS.item(z)).getAttribute("op").equals("AND")){
               selOp = " and ";
	    }
            if (((Element) nlSS.item(z)).getAttribute("op").equals("OR")){
               selOp = " or ";
            }
            if (((Element) nlSS.item(z)).getAttribute("op").equals("NOT")){
               selOp = " and not"; //Added 21/08/03
            }
            response = response + " "+ selOp + " ";
         }

         if (nlSS.item(z).getNodeName().equals("selection")){
            se = true;
            String itemOp = "";
           
				
            /**
             * The selection element contains three attributes: itemOp 
             * contains XML logical operators, item contains the element 
             * name to search on,  and value contains the element value to
             * match.
             * Convert XML logical operators to XQL logical operators.
             */

            if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("EQ")){
               itemOp = "=";
            }
            else if (((Element) 
                    nlSS.item(z)).getAttribute("itemOp").equals("NE")){
               itemOp = "!=";
            }
            else if (((Element)
                    nlSS.item(z)).getAttribute("itemOp").equals("GE")){
               itemOp = " ge ";
            }
            else if (((Element)
                    nlSS.item(z)).getAttribute("itemOp").equals("LE")){
               itemOp = " le ";
            }
            else if (((Element)
                    nlSS.item(z)).getAttribute("itemOp").equals("GT")){
               itemOp = " gt ";
            }
            else if (((Element)
                    nlSS.item(z)).getAttribute("itemOp").equals("CONTAINS")){
               itemOp = " |= ";
               wildcardon = true;
            }            
            else if (((Element)
                    nlSS.item(z)).getAttribute("itemOp").equals("LT")){
               itemOp = " lt ";
            }
            else itOp = false;
				
	    if  ((((Element) nlSS.item(z)).getAttribute("item")).equals("")) {
               it = false;
            }			
            if  ((((Element)
                nlSS.item(z)).getAttribute("item")).equals("searchElements"))
            {
               searchElements = (((Element)
                                nlSS.item(z)).getAttribute("value"));
               z = z + 1; 	
            }
            /**
	     * Build the query component contained in this 
             * selectionSequence element.
             */
            else {
               
               itemName = ((Element) nlSS.item(z)).getAttribute("item");
               xqlStart = "(.//*:" + itemName;
               //xqlStart = "(*:" + itemName;
               if(itemName.indexOf("@") != -1) {
                  if(itemName.indexOf(":") != -1) {
                     itemName = itemName.substring(itemName.indexOf(":")+1);
                  } else {
                     itemName = itemName.substring(itemName.indexOf("@")+1);
                  }
                  xqlStart = "(.//@*:" + itemName;
                  //xqlStart = "(@*:" + itemName;
               }
               response = response + "";
               response = response + xqlStart;
               String value = ((Element) nlSS.item(z)).getAttribute("value");
               if  (!(value.equals("")  ||
                      value.equals("*") ||
                      value.equals("all")))
               {
                  response = response + " "+ itemOp + " ";
                  if(wildcardon) {
                     response = response + "'*"+ ((Element) nlSS.item(z)).
                                                getAttribute("value")+ "*'";
                     wildcardon = false;
                  } else {
                     response = response + "'"+((Element) nlSS.item(z)).
                                                getAttribute("value")+ "'";
                  }
               }
               response = response + ")";
            }
         }

       /**
        * If the XML query contains nested selectionSequence elements,
        * recursively call the xmlToXQL method here.
        */
         if (nlSS.item(z).getNodeName().equals("selectionSequence")){
            response = response + xmlToXQL(nlSS.item(z));
         }
      }
      response = response + ")";
      goodQuery = (se && it && itOp && val);
      goodQuery = true;  //EDIT THIS LATER
      if (goodQuery == false){
         response = null;
         response = ("Selection, item, itemOp, or value is missing.");
      }
      log.info("the response = " + response);
      log.debug("end xmlToXQL");
      return response;
   }
}
