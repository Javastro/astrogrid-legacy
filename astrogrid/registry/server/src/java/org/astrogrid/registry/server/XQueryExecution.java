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
/*
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Reader;
*/
import java.util.Date;
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import de.gmd.ipsi.xql.XQL;
//import de.gmd.ipsi.xql.XQLRelationship;
//import org.astrogrid.registry.server.RegistryFileHelper;
//import org.xmldb.api.*;
//import org.xmldb.api.base.*;
//import org.xmldb.api.modules.*;
//import org.exist.xmldb.*;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;


/**
 * The QueryParser3_0 class accepts an XML formatted query from the
 * RegistryInterface3_0 web service, converts the XML to an XQL query, 
 * and sends the XQL query to Registry3_0.  QueryParser3_0 then accepts
 * the XML registry response (formatted to schema registry_v1_1.xsd).
 * If the submitQuery method has been called, the registry response
 * is reformatted as a series of responseRecords with name / value
 * key pairs and returned to the RegistryInterface3_0 web service.
 * If the fullNodeQuery method has been called, the registry response
 * is returned to the web service in its registry schema format.
 * 
 * QueryParser3_0 also identifies how much metadata a web service user
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
	private static String nodeDetails = "";
	private static String searchElements = "";
   
   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }   
   
   public XQueryExecution() {
      System.out.println("empty constructor for queryparser3_0");
   }
   
   /**
    * parseQuery is for submitting a query and goes with the submitQuery from the server side
    * web service.  Currently being depegrecated.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.    
    * @author Kevin Benson
    */
	public static Document parseQuery (Document query) {

		/**
		* Input string "query" is the XML formatted query from the Registry
		* Interface3_0 web service.  First the prepareQuery method is called 
		* to reformat the query as an XQL query and send the query to Registry3_0
		* Then the received registry response XML formatted string is sent to the
		* returnRecordKeyPairs method to reformat the response as a set of 
		* response records with name / value key pairs. The response records are
		* returned to the RegistryInterface3_0 web service.
		* 
		*/
      //System.out.println("entered parseQuery");
      //addXQLRelationShips();
      return prepareQuery(query);
	}
   
   /**
    * parseFullQuery is for submitting a query and goes with the fullNodeQuery from the server side
    * web service.
    *
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    */   
	public static Document parseFullNodeQuery (Document query) {

		/**
		* Input string "query" is the XML formatted query from the Registry
		* Interface3_0 web service.  First the prepareQuery method is called 
		* to reformat the query as an XQL query and send the query to Registry3_0
		* Then the received registry response XML formatted string is returned to
		* the RegistryInterface3_0 web service.
		* 
		*/
      //addXQLRelationShips();
      return prepareQuery(query);
		//String xqlResponse = prepareQuery(query);
		//return xqlResponse;	
	}
   
	
   /**
    * Input Document is the XML formatted query from either the parseQuery
    * or parseFullNodeQuery method in this class. The prepareQuery method converts
    * this XML string into an XQL query and sends it to the Registry3_0 class.  
    * 
    * This method also registers four user-defined comparisons: isgt, isge, islt, and
    * isle.  The GMD-IPSI implementation of XQuery only predefines lexical comparisons
    * for the basic logical operators, so these four user-defined comparisons implement numerical
    * comparisons as well.
    *  
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @throws ClassNotFoundException
    */   
	private static Document prepareQuery (Document query) {
            
		/**
		* First build a DOM tree out of the XML query.  
		*/
	  String xqlResponse = "";
	  String queryResponse = "";
	  boolean goodQuery = true;
     Document doc = query;
     String msg = null;

     String xml_to_xql = null;
     
      //System.out.println("entered prepareQuery with = " + DomHelper.DocumentToString(query));
      /**
		 * The registry query that requires conversion to XQL will be contained 
			* inside an element called "selectionSequence".  If the selectionSequence 
			* element is not present, the query is incorrectly formatted. For an example 
			* of a registry query and links to the registry query schema, please see 
			* http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQuerySchema. 
			*/
			try{
				Element docElement = (Element)doc.getDocumentElement();//.getFirstChild();
				NodeList ssList = docElement.getElementsByTagName("selectionSequence");
            //System.out.println("lenght ofsslist = " + ssList.getLength());
				if (ssList.getLength() != 0){
					/**
					* If the query is well formatted, proceed to convert query to XQL
					* by sending the top selectionSequence element and its children to
					* the xmlToXQL method.
					*/
					xml_to_xql = xmlToXQL(ssList.item(0));
				}
				else {
					msg = "QUERY - Query does not conform to Astrogrid registry query schema.";
					goodQuery = false;
				} 
			}
			catch (Exception e) {
				msg = "QUERY 1 - " + e.toString();
				goodQuery = false;
            e.printStackTrace();
			}
         
         return runQuery(xml_to_xql);
            

	}
   
   public static URL getQueryUrl(String xql) {
      String location = conf.getString("exist.db.url");
      String numberOfResourcesReturned = conf.getString("exist.query.returncount");
      if(numberOfResourcesReturned == null || numberOfResourcesReturned.trim().length() <= 0)
         numberOfResourcesReturned = "25";
      
      URL fullQueryURL = null;
      try {
         String versionNumber = conf.getString("org.astrogrid.registry.version");
         location += "/servlet/db/astrogridv" + versionNumber + "?_query=" + URLEncoder.encode(xql,"UTF-8") + "&_howmany=" + numberOfResourcesReturned;
         System.out.println("the full query url = " + location);         
         fullQueryURL = new URL(location);
         System.out.println("the fullQueryURL = " + fullQueryURL.toString());
      }catch(MalformedURLException mue) {
         mue.printStackTrace();
      }catch(UnsupportedEncodingException uee) {
         uee.printStackTrace();
      }
      return fullQueryURL;
   }
   
   public static URL getUpdateURL(String collectionName, String xmlDocName) {
      String location = conf.getString("exist.db.url");
      location += "/servlet/db/" + collectionName + "/" + xmlDocName;
      URL fullQueryURL = null;
      System.out.println("the full update put url = " + location);
      try {
         fullQueryURL = new URL(location);
      }catch(MalformedURLException mue) {
         mue.printStackTrace();   
      } 
      return fullQueryURL;
   } 
    
   
   public static boolean validReplace(Document compareDoc, String compareElement) {
      
      return true;      
   }
   
   public static Document runQuery(String xql) {
         String fullQuery = null;
         System.out.println("the xmlxql query = " + xql);
         try {
            long beginQ = System.currentTimeMillis();
            System.out.println("Query begin time: " + beginQ);
            fullQuery = "//*:Resource[" + xql + "]";
            Document queryDoc = DomHelper.newDocument(getQueryUrl(fullQuery));
            long endQ = System.currentTimeMillis();
            System.out.println("Query end time: " + endQ);
            System.out.println("Query total time: " + (endQ - beginQ));
            return queryDoc;
         }catch(Exception e) {
            e.printStackTrace();   
         }
         return null;
   }
   
   public static void updateQuery(String type, String collectionName, String identifier, Node updateNode) {
      //Document queryDoc = runQuery(xql);      
      //if(!validReplace(queryDoc, identifier )) return;
      try {
         System.out.println("THE IDENTIFIER = " +  identifier);
         String xmlDocName = identifier.replaceAll("[^\\w*]","_") + "." + type;
         System.out.println("THE REPLACED DOC NAME = " + xmlDocName);
         HttpURLConnection huc = (HttpURLConnection) getUpdateURL(collectionName, xmlDocName).openConnection();
         huc.setRequestProperty("Content-Type", "text/xml");
         //huc.setDoInput(false);
         huc.setDoOutput(true);
         huc.setRequestMethod("PUT");
         huc.connect();
         DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
         DomHelper.ElementToStream((Element)updateNode,dos);
         dos.flush();
         System.out.println("closing outputstream and content type = " + huc.getContentType());
         dos.close();
         System.out.println("disconnecting");
         huc.disconnect();
      }catch(Exception e) {
         e.printStackTrace();   
      }
   }//updateQuery
   
   
   /*
    * 
    * 
  private static Collection loadDataBase() throws Exception {
     String driver = "org.exist.xmldb.DatabaseImpl";
     String uri = conf.getString("registry.exist.db.uri");//"xmldb:exist://localhost:8080/exist/xmlrpc"; 
      
     String collection = "/db";
     Class cl = Class.forName(driver);
     Database database = (Database)cl.newInstance();
     DatabaseManager.registerDatabase(database);
     System.out.println("Loading Database URI: " + uri + collection);
     Collection col = DatabaseManager.getCollection(uri+collection,"admin","");
     return col;
  }
    
   public static Document runQuery(String xql, String xmlDocName, Node updateNode) {
      
      Collection col = null;
      try {
         
         String fullQuery = null;
         System.out.println("the xmlxql query = " + xql);
         long beginQ = System.currentTimeMillis();
         col = loadDataBase();         
         XQueryService xqs = (XQueryService) col.getService("XQueryService", "1.0");
         System.out.println("Time taken to load database service = " + (System.currentTimeMillis() - beginQ));
         beginQ = System.currentTimeMillis();
         System.out.println("Query begin time: " + beginQ);
         fullQuery = "document()//*:Resource[" + xql + "]";
         System.out.println("Query being Ran = " + fullQuery);
         ResourceSet rset = xqs.query(fullQuery);
         long endQ = System.currentTimeMillis();
         System.out.println("Query end time: " + endQ);
         System.out.println("Query total time: " + (endQ - beginQ));
         Document resultDoc = DomHelper.newDocument();
         Element root = resultDoc.createElementNS("http://www.ivoa.net/xml/VOResource/v0.9","VODescription");
         root.setPrefix("vr");
         resultDoc.appendChild(root);
         resultDoc.
            
         Node nd = null;
         for(int i = 0;i < rset.getSize();i++) {
            beginQ = System.currentTimeMillis();
            XMLResource resTime = (XMLResource)rset.getResource(i);
//            System.out.println("the resTime ids = " + resTime.getId() + " the doc id = " + resTime.getDocumentId());
            nd = resTime.getContentAsDOM();
            Node resultNode = resultDoc.importNode(nd,true);
            root.appendChild(resultNode);
            if(updateNode != null && rset.getSize() == 1) {
               Resource deleteResource = col.getResource(resTime.getDocumentId());
               col.removeResource(deleteResource);
               System.out.println("Time taken to remove document = " + (System.currentTimeMillis() - beginQ));
            }
         }
         if(updateNode != null) {
            beginQ = System.currentTimeMillis();
            xmlDocName = xmlDocName.replaceAll("^\\w","_");
            System.out.println("Updating xml Document name = " + xmlDocName);            
            XMLResource addResource = (XMLResource)col.createResource(xmlDocName,"XMLResource");
            addResource.setContentAsDOM(updateNode);
            col.storeResource(addResource);
            System.out.println("Time taken to add/update document = " + (System.currentTimeMillis() - beginQ));
         }
         if(col != null) {
            col.close();
         }            
         return resultDoc;
      }catch(Exception e) {
         col = null;
         e.printStackTrace();   
      }
      return null;
   }
*/
	private static String xmlToXQL(Node node) {

		/**
		 * The xmlToXQL method converts an XML node query to XQL. Input 
		 * node generated from the top level selectionSequence element
		 */
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
		
      System.out.println("enters xmltoxql");
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
				 * The selection element contains three attributes: itemOp contains
				 * XML logical operators, item contains the element name to search on, 
				 * and value contains the element value to match.
				 * Convert XML logical operators to XQL logical operators.
				 */
				if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("EQ")){
					itemOp = "=";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("NE")){
					itemOp = "!=";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("GE")){
					itemOp = " ge ";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LE")){
					itemOp = " le ";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("GT")){
					itemOp = " gt ";
				}
            else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("CONTAINS")){
               itemOp = " |= ";
               wildcardon = true;
            }            
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LT")){
					itemOp = " lt ";
				}
				else itOp = false;
				
				if  ((((Element) nlSS.item(z)).getAttribute("item")).equals("")) {
					it = false;
				}			

				if  ((((Element) nlSS.item(z)).getAttribute("item")).equals("searchElements")) {
					searchElements = (((Element) nlSS.item(z)).getAttribute("value"));
					z = z + 1; 	
				}
				/**
				 * Build the query component contained in this selectionSequence element.
				 */
				else {
               
               itemName = ((Element) nlSS.item(z)).getAttribute("item");
               xqlStart = "(.//*:" + itemName;
               //xqlStart = "(*:" + itemName;
               if(itemName.indexOf("@") != -1) {
                  if(itemName.indexOf(":") != -1) {
                     itemName = itemName.substring(itemName.indexOf(":")+1);
                  } else{
                     itemName = itemName.substring(itemName.indexOf("@")+1);
                  }
                  xqlStart = "(.//@*:" + itemName; 			
                  //xqlStart = "(@*:" + itemName;
               }
					response = response + "";
					response = response + xqlStart;
					String value = ((Element) nlSS.item(z)).getAttribute("value");
					if  (!(value.equals("") || value.equals("*") || value.equals("all"))) {
						response = response + " "+ itemOp + " ";
                  if(wildcardon) {
                     response = response + "'*"+((Element) nlSS.item(z)).getAttribute("value")+ "*'";
                     wildcardon = false;
                  }else {
                     response = response + "'"+((Element) nlSS.item(z)).getAttribute("value")+ "'";
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
      System.out.println("the response = " + response);
		return response;
	}
		
}
