package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Reader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import de.gmd.ipsi.xql.XQL;
import de.gmd.ipsi.xql.XQLRelationship;
import org.astrogrid.registry.server.RegistryFileHelper;


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

public class QueryParser3_0
 {
	static String nodeDetails = "";
	static String searchElements = "";
   
   /**
    * parseQuery is for submitting a query and goes with the submitQuery from the server side
    * web service.  Currently being depegrecated.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query. 
    * @deprecated Not used at the moment and will be factored out in later versions.
    * @author Kevin Benson
    */
	public static Document parseQuery (Document query) throws ClassNotFoundException {

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
      addXQLRelationShips();
      return prepareQuery(query);
	}
   
   /**
    * parseFullQuery is for submitting a query and goes with the fullNodeQuery from the server side
    * web service.
    *
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    */   
	public static Document parseFullNodeQuery (Document query) throws ClassNotFoundException {

		/**
		* Input string "query" is the XML formatted query from the Registry
		* Interface3_0 web service.  First the prepareQuery method is called 
		* to reformat the query as an XQL query and send the query to Registry3_0
		* Then the received registry response XML formatted string is returned to
		* the RegistryInterface3_0 web service.
		* 
		*/
      addXQLRelationShips();
      return prepareQuery(query);
		//String xqlResponse = prepareQuery(query);
		//return xqlResponse;	
	}
   
   private static void addXQLRelationShips() {
      

      /**
       * User-defined comparison to test whether a text element (Object l) is numerically
       * greater than the number value (Object r) supplied. If either the text element 'l' or
       * the supplied number value 'r' does not evaluate to a number, the test fails.
       */
      XQL.addRelationship("$isgt$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
         double element = XQL.number(l);
         double value = XQL.number(r);
         if (((new Double(element).isNaN())) || ((new Double(value).isNaN()))) return false;
         return (element > value);
        }
       });
       
      /**
       * User-defined comparison to test whether a text element (Object l) is numerically
       * greater than or equal to the number value (Object r) supplied. If either the 
       * text element 'l' or the supplied number value 'r' does not evaluate to a number, 
       * the test fails.
       */
       XQL.addRelationship("$isge$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
         double element = XQL.number(l);
         double value = XQL.number(r);
         if (((new Double(element).isNaN())) || ((new Double(value).isNaN()))) return false;
         return (element >= value);
        }
       });
       
      /**
       * User-defined comparison to test whether a text element (Object l) is numerically
       * greater than or equal to the number value (Object r) supplied. If either the 
       * text element 'l' or the supplied number value 'r' does not evaluate to a number, 
       * the test fails.
       */
      
       XQL.addRelationship("$after$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          String lValue = XQL.text(l);
          String rValue = XQL.text(r);
          Date lDate = null;
          Date rDate = null;
          try {
             lDate = sdf.parse(lValue);
             rDate = sdf.parse(rValue);
          }catch(java.text.ParseException pe) {
            pe.printStackTrace();
            lDate = null;
            rDate = null;  
          }
         if(lDate != null && rDate != null) {
            return lDate.after(rDate);
         }else {
            System.out.println("problem supposed to be a date and it is not.");
            return false;
         }
        }
       });
       
       
      /**
       * User-defined comparison to test whether a text element (Object l) is numerically
       * less than the number value (Object r) supplied. If either the text element 'l' or
       * the supplied number value 'r' does not evaluate to a number, the test fails.
       */
      XQL.addRelationship("$islt$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
         double element = XQL.number(l);
         double value = XQL.number(r);
         if (((new Double(element).isNaN())) || ((new Double(value).isNaN()))) return false;
         return (element < value);
        }
       });
       
      /**
       * User-defined comparison to test whether a text element (Object l) is numerically
       * less than or equal to the number value (Object r) supplied. If either the 
       * text element 'l' or the supplied number value 'r' does not evaluate to a number, 
       * the test fails.
       */
      XQL.addRelationship("$isle$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
         double element = XQL.number(l);
         double value = XQL.number(r);
         if (((new Double(element).isNaN())) || ((new Double(value).isNaN()))) return false;
         return (element <= value);
        }
       });
      
      XQL.addRelationship("$contains$",
       new XQLRelationship() {
        public boolean holdsBetween(Object l, Object r) {
           String lValue = XQL.text(l).toLowerCase();
           String rValue = XQL.text(r).toLowerCase();
          return lValue.indexOf(rValue) >= 0;
        }
       });       
       
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
	static Document prepareQuery (Document query) throws ClassNotFoundException {

		/**
		* 
		*/

		/**
		* First build a DOM tree out of the XML query.  
		*/
	  String xqlResponse = "";
	  String queryResponse = "";
	  boolean goodQuery = true;
     Document doc = query;
     String msg = null;

     String xml_to_xql = null;
      System.out.println("entered prepareQuery");
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
            System.out.println("lenght ofsslist = " + ssList.getLength());
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
			}

			/**
			* Now determine how much metadata the user has requested with the "searchElements" 
			* variable and complete the conversion to XQL:
			* 
			* searchElements           -> Return metadata
			* --------------              ---------------
			* "all" or "" or "*"       -> return all metadata
			* "identity"               -> return identity metadata only
			* "curation"               -> return identity and curation metadata
			* "content"                -> return identity and content metadata
			* "serviceMetadataConcept" -> return identity and serviceMetadataConcept metadata
			*/
			if (goodQuery == true){
            if (searchElements.equals("") || searchElements.equals("all") || searchElements.equals("*")){
               xml_to_xql = "//VODescription/* [" + xml_to_xql + "]";  
            } else if (searchElements.equals("Organisation")){
               xml_to_xql = "//VODescription/vc:Organisation [" + xml_to_xql + "]";   
            } else if (searchElements.equals("Authority")){
               xml_to_xql = "//VODescription/vg:Authority [" + xml_to_xql + "]";   
            } else if (searchElements.equals("DataCollection")){
               xml_to_xql = "//VODescription/vs:DataCollection [" + xml_to_xql + "]";   
            } else if (searchElements.equals("Service")){
               xml_to_xql = "//VODescription/Service [" + xml_to_xql + "]";   
            } else if (searchElements.equals("Service")){
               xml_to_xql = "//VODescription/Service [" + xml_to_xql + "]";   
            } else if (searchElements.equals("SkyService")){
               xml_to_xql = "//VODescription/vs:SkyService [" + xml_to_xql + "]";   
            } else if (searchElements.equals("TabularSkyService")){
               xml_to_xql = "//VODescription/vs:TabularSkyService [" + xml_to_xql + "]";   
            } else if (searchElements.equals("Registry")){
               xml_to_xql = "//VODescription/vg:Registry [" + xml_to_xql + "]";   
            }  
           

            /*
				if (searchElements.equals("") || searchElements.equals("all") || searchElements.equals("*")){
               xml_to_xql = "//service [" + xml_to_xql + "]";	
				}
				else if (searchElements.equals("identity")){
               xml_to_xql = "//service[" + xml_to_xql + "]/identity";	
				}	
				else if (searchElements.equals("curation")){
               xml_to_xql = "//service[" + xml_to_xql + "]/identity | //service[" + xml_to_xql + "]/curation";	
				}	
				else if (searchElements.equals("content")){
               xml_to_xql = "//service[" + xml_to_xql + "]/identity | //service[" + xml_to_xql + "]/content";	
				}
				else if (searchElements.equals("serviceMetadataConcept")){
               xml_to_xql = "//service[" + xml_to_xql + "]/identity | //service[" + xml_to_xql + "]/serviceMetadataConcept";	
				}
            */
				System.out.println("the query = " + xml_to_xql);
				/**
				* When the XQL query has been constructed, send it to the registry instance
				* by calling reg.xmlQuery.  Send the registry response from this method, along with
				* searchElements, to the xqlToXML method to filter the relevant metadata (ie, identity,
				* curation, content, serviceMetadataConcepts) out of the registry response.
				*/
				//xqlResponse = xqlToXML(new Registry3_0().xmlQuery(xml_to_xql,RegistryFileHelper.loadRegistryFile()), searchElements);								
            return new Registry3_0().xmlQuery(xml_to_xql,RegistryFileHelper.loadRegistryFile());
            //System.out.println("the xqlresponse = " + xqlResponse);
            //queryResponse = xqlResponse;
			} else {
            //throw a not valid formatted document which should not happen since this afterall now a Document object.
            return null;
          //   queryResponse = "<queryResponse><recordKeyPair item='ERROR1:' value='" + query + "' /></queryResponse>";
         }
	}

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
		
      System.out.println("enters xmltoxql");
		/**
		 * Loop through the child nodes of selectionSequence to add all search
		 * elements, operators, and element values to the XQL query.
		 */      
		for (int z=0; z < nlSS.getLength(); z++){
			if (nlSS.item(z).getNodeName().equals("selectionOp")){				
				/**
				 * The selectionOp element contains one attribute specifying XML 
				 * boolean operators. Convert to XQL boolean operators.
				 */
				String selOp = "";
				if (((Element) nlSS.item(z)).getAttribute("op").equals("AND")){
					selOp = "$and$";
				}
				if (((Element) nlSS.item(z)).getAttribute("op").equals("OR")){
					selOp = "$or$";
				}
				if (((Element) nlSS.item(z)).getAttribute("op").equals("NOT")){
					selOp = "$and$ $not$"; //Added 21/08/03
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
					itemOp = "$isge$";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LE")){
					itemOp = "$isle$";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("GT")){
					itemOp = "$isgt$";
				}
            else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("AFTER")){
               itemOp = "$after$";
            }
            else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("CONTAINS")){
               itemOp = "$contains$";
            }            
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LT")){
					itemOp = "$islt$";
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
					response = response + "";
					response = response + "(.//";
					response = response + ((Element) nlSS.item(z)).getAttribute("item");
					String value = ((Element) nlSS.item(z)).getAttribute("value");
					if  (!(value.equals("") || value.equals("*") || value.equals("all"))) {
						response = response + " "+ itemOp + " ";
						response = response + "'"+((Element) nlSS.item(z)).getAttribute("value")+ "'";
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
	
	private static String xqlToXML(String xqlResponse, String searchElements){
		
		/**
		 * The xqlToXML method takes an XML formatted registry response and filters out
		 * the appropriate metadata: all, identity, curation, content, or serviceMetadataConcepts.
		 * Identity metadata is always returned, either alone or with curation, content, 
		 * serviceMetadataConcept, or all metadata.
		 */
		String response = "";
      System.out.println("xqltoxml enter 2 params");
		if (searchElements.equals("identity")){
			response = xqlResponse.replaceAll("<identity", "<service><identity");
			xqlResponse = null;
			xqlResponse = response.replaceAll("</identity>", "</identity></service>");	
		}	
		else if (searchElements.equals("curation")){
			response = xqlResponse.replaceAll("<identity", "<service><identity");
			xqlResponse = null;
			xqlResponse = response.replaceAll("</curation>", "</curation></service>");		
		}	
		else if (searchElements.equals("content")){
			response = xqlResponse.replaceAll("<identity", "<service><identity");
			xqlResponse = null;
			xqlResponse = response.replaceAll("</content>", "</content></service>");		
		}
		else if (searchElements.equals("serviceMetadataConcept")){
			response = xqlResponse.replaceAll("<identity", "<service><identity");
			xqlResponse = null;
			xqlResponse = response.replaceAll("</serviceMetadataConcept>", "</serviceMetadataConcept></service>");
		}
		String xmlResponse = xqlResponse.replaceAll("<.*xql.*>", "");
		return xmlResponse;
	}
	
   /**
    * @deprecated Not used anymore.
    * 
    * @param xqlResponse
    * @return
    */
	private static String returnRecordKeyPairs(String xqlResponse){
		
		/**
		 * The returnRecordKeyPairs method reformats a registry response from
		 * the registry_v1_1.xsd schema format to the registryQueryResponse schema format
		 * specified at http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQueryResponseSchema.
		 */
		
        String xmlResponse = "";
		String nodeResponse = "";
		
		/**
		 * First build a DOM tree from the registry response XML string.
		 */
		
		Reader reader2 = new StringReader(xqlResponse);
		InputSource inputSource = new InputSource(reader2);
		String queryResponse = "";
      
		Document doc2 = null;
		DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder2 = null;
		try {
			builder2 = factory2.newDocumentBuilder();
			doc2 = builder2.parse(inputSource);
		}
		catch (ParserConfigurationException e) {
			xmlResponse = "QUERY 2 - " + e.toString();
		}
		catch (Exception e){
			xmlResponse = "QUERY 3 - " + e.toString();
		}

		try{
			Element docElement2 = doc2.getDocumentElement();

			/**
			 * Next, check whether there were any error messages from the registry.  If so,
			 * construct a item / value key pair containing the error message.
			 */
			
			NodeList errorNodes = docElement2.getElementsByTagName("error");
			if (errorNodes.getLength() != 0) {
				xmlResponse = xmlResponse = "<recordKeyPair item='ERROR:' value='" + errorNodes.item(0).getFirstChild().getNodeValue() + "/>";
			}
			
			/**
			 * If there were no error messages, extract the metadata elements with corresponding
			 * text child nodes containing element values.  Present these elements and values as
			 * item / value key pairs.
			 */
			
			else {
				try{
					/**
					 * Build a nodeList from the top level element "service" contained in each 
					 * matching service in the registry.
					 */
					NodeList serviceNodes = docElement2.getElementsByTagName("service");
					if (serviceNodes.getLength() != 0) {
						Element serviceNodesParent = (Element)serviceNodes.item(0).getParentNode();
						Node node = serviceNodesParent;
						
						/**
						 * Recursively call getNodeDetails for each childNode and its children to
						 * build them into item / value key pairs.  WARNING: recursive calls can 
						 * make your brain hurt.
						 */
						if(node.hasChildNodes()) {
							NodeList children = node.getChildNodes();
							if (children != null) {
								for (int k=1; k< children.getLength(); k=k+2) {
									if(children.item(k).hasChildNodes()) {
										String nodeDetails = "";
										nodeResponse = getNodeDetails(children.item(k));
										nodeDetails = null;
										if (k == (children.getLength()-2)){
											xmlResponse = nodeResponse;
										}
										nodeResponse = null;
									}
								}
							}
						}
					}
					else {
						/** 
						 * If there were no matching registry entries, return an item / value
						 * key pair with an error message to that effect.
						 */						
						xmlResponse = "<recordKeyPair item='ERROR:' value='No matching registry entries found.'/>";
					}
				}
				catch (Exception e) {
				  /**
				   * If something went wrong building the service element child nodes, return an
				   * error message. 
				   */				 
				  xmlResponse = "<recordKeyPair item='ERROR:' value='QUERY 4- " + e.toString() + "'/>";
				}
			}
		}
		catch (Exception e) {
  		   /**
		    * If something went wrong building the registry response DOM tree, return an
		    * error message. 
		    */		
		    xmlResponse = "<recordKeyPair item='ERROR:' value='QUERY 5- " + e.toString() + "'/>";
		}
		xmlResponse = "<queryResponse>"+ xmlResponse + "</queryResponse>";
 
        /**
         * Return the xmlResponse back to the parseQuery method in this class.  This string contains
         * one or more response records containing item / value key pairs.  Error messages are in
         * this format too.
         */
		return xmlResponse;
	}

   /**
    * @deprecated Not used anymore.
    * @param node
    * @return
    */
	private static String getNodeDetails (Node node) {

        /**
         * The getNodeDetails method is called recursively from other methods to build
         * the strings containing each response record and its item / value key pairs.
         */
        
		String content = "";
		String itemName = "";
		int type = node.getNodeType();
		
		/**
		 * First construct the top level responseRecord element and specify which category
		 * of metadata (identity, curation, etc) that subsequent item / value key pairs will be. 
		 */
		
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("service"))) {
		  nodeDetails = nodeDetails + "<responseRecord>";
		}  

		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("identity"))) {
		  nodeDetails = nodeDetails + "<recordKeyPair item='metadataType' value='identity'/>";
		}  
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("curation"))) {
		  nodeDetails = nodeDetails + "<recordKeyPair item='metadataType' value='curation'/>";
		}
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("content"))) {
		  nodeDetails = nodeDetails + "<recordKeyPair item='metadataType' value='content'/>";
		}
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("serviceMetadataConcept"))) {
		  nodeDetails = nodeDetails + "<recordKeyPair item='metadataType' value='serviceMetadataConcept'/>";
		}

		/**
		 * If the node is a text node, then the value of the text node is added to the nodeDetails 
		 * string as the "value" attribute. The name of its parent is added to nodeDetails as the
		 * "item" attribute. Thus, the format of the key pair is 
		 * <recordKeyPair item="[parent node name]" value="[text node value]"/>
		 */
		if (type == Node.TEXT_NODE) {
			content = node.getNodeValue();
			itemName = node.getParentNode().getNodeName();
			if (!content.trim().equals("")){
				nodeDetails = nodeDetails + "<recordKeyPair item='" + itemName + "' value='" + content + "'/>";
			}			
		}
		NodeList children = node.getChildNodes();
		
		/**
		 * If the element node has children, recursively call getNodeDetails with this
		 * element node.
		 */
		
		if (children != null) {
			for (int m=0; m< children.getLength(); m++) {
				getNodeDetails(children.item(m));
			}
		}
		
		/**
		 * If the element name "service" is found at this point, it indicates the end of
		 * a complete matching service entry.  End the response record here.
		 */ 
		
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("service"))) {
		  nodeDetails = nodeDetails + "</responseRecord>";
		}
		
		/**
		 * Return the nodeDetails string; it will either be a complete list of response records
		 * or contain a partial list that will be further used in recursive calls to this 
		 * method.
		 */  
		return nodeDetails;
	}	
}
