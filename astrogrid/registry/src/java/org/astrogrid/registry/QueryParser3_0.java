 /**
 * Created on 25-Apr-2003
 * @author Elizabeth Auden
 */
package org.astrogrid.registry;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.*;
import java.io.Reader;

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
 * - Elizabeth Auden, 17 October 2003
 *
 */

public class QueryParser3_0
 {
	static String nodeDetails = "";
	static String searchElements = "";
	public static String parseQuery (String query) throws ClassNotFoundException {

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
		
		String xmlResponse = prepareQuery(query);
		String queryResponse = "";	
		queryResponse = returnRecordKeyPairs(xmlResponse);	
	    return queryResponse;
	}
	public static String parseFullNodeQuery (String query) throws ClassNotFoundException {

		/**
		* Input string "query" is the XML formatted query from the Registry
		* Interface3_0 web service.  First the prepareQuery method is called 
		* to reformat the query as an XQL query and send the query to Registry3_0
		* Then the received registry response XML formatted string is returned to
		* the RegistryInterface3_0 web service.
		* 
		*/

		String xqlResponse = prepareQuery(query);
		return xqlResponse;	
	}
	
	static String prepareQuery (String query) throws ClassNotFoundException {

		/**
		* Input string "query" is the XML formatted query from either the parseQuery
		* or parseFullNodeQuery method in this class. The prepareQuery method converts
		* this XML string into an XQL query and sends it to the Registry3_0 class.   
		* 
		*/

		/**
		* First build a DOM tree out of the XML query.  
		*/
	  Reader reader = new StringReader(query);
	  InputSource inputSource = new InputSource(reader);
	  String xqlResponse = "";
	  String queryResponse = "";
	  boolean goodQuery = true;
      
	  Document doc = null;
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder builder = null;
	  try {
		builder = factory.newDocumentBuilder();
		doc = builder.parse(inputSource);
	  }
	  catch (ParserConfigurationException e) {
		query = "QUERY - " + e.toString();
		goodQuery = false;
	  }
	  catch (Exception e){
		query = "QUERY - " + e.toString();
		goodQuery = false;
	  }

	  /**
	  * Create an instance of the Registry3_0 class. 
	  */
	  Registry3_0 reg = new Registry3_0();

	  /**
	  * Next, call the getRegistrySource method to determine whether
	  * the registry is stored as an XML file or a database.  The registry
	  * database has not yet been implemented, so proceed only if the 
	  * registry response is "xml".
	  */
	  String registrySource = getRegistrySource();

	  if (goodQuery == true){
		if (registrySource.equals("db")){
			query = "Database registry not implemented yet.  Use XML registry.";
			goodQuery = false;
		}
	
		else if (registrySource.equals("xml")){
			/**
			* The registry query that requires conversion to XQL will be contained 
			* inside an element called "selectionSequence".  If the selectionSequence 
			* element is not present, the query is incorrectly formatted. For an example 
			* of a registry query and links to the registry query schema, please see 
			* http://wiki.astrogrid.org/bin/view/Astrogrid/RegistryQuerySchema. 
			*/
			try{
				Element docElement = doc.getDocumentElement();
				NodeList ssList = docElement.getElementsByTagName("selectionSequence");
				if (ssList.getLength() != 0){
					/**
					* If the query is well formatted, proceed to convert query to XQL
					* by sending the top selectionSequence element and its children to
					* the xmlToXQL method.
					*/
					query = xmlToXQL(ssList.item(0));
				}
				else {
					query = "QUERY - Query does not conform to Astrogrid registry query schema.";
					goodQuery = false;
				} 
			}
			catch (Exception e) {
				query = "QUERY 1 - " + e.toString();
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
					query = "//service [" + query + "]";	
				}
				else if (searchElements.equals("identity")){
					query = "//service[" + query + "]/identity";	
				}	
				else if (searchElements.equals("curation")){
					query = "//service[" + query + "]/identity | //service[" + query + "]/curation";	
				}	
				else if (searchElements.equals("content")){
					query = "//service[" + query + "]/identity | //service[" + query + "]/content";	
				}
				else if (searchElements.equals("serviceMetadataConcept")){
					query = "//service[" + query + "]/identity | //service[" + query + "]/serviceMetadataConcept";	
				}
				
				/**
				* When the XQL query has been constructed, send it to the registry instance
				* by calling reg.xmlQuery.  Send the registry response from this method, along with
				* searchElements, to the xqlToXML method to filter the relevant metadata (ie, identity,
				* curation, content, serviceMetadataConcepts) out of the registry response.
				*/
				xqlResponse = xqlToXML(reg.xmlQuery(query), searchElements);								
                queryResponse = xqlResponse;
			}
			
			/**
			 * If the query is not well formatted, return the query with an error message.
			 */
			else queryResponse = "<queryResponse><recordKeyPair item='ERROR1:' value='" + query + "'></queryResponse>";
		}
		
		/**
		 * Return an error if the registry source is neither xml or db.
		 */
		else if (!(registrySource.equals("db") | registrySource.equals("xml"))){
			queryResponse = "<queryResponse><recordKeyPair item='ERROR2:' value='" +registrySource + "'></queryResponse>";
		}
	  }
	  
	  /**
	   * If anything else has gone wrong with the query, return an error.
	   */
	  else queryResponse = "<queryResponse><recordKeyPair item='ERROR3:' value='" + query + "'></queryResponse>";

      return queryResponse;
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
					itemOp = ">=";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LE")){
					itemOp = "<=";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("GT")){
					itemOp = ">";
				}
				else if (((Element) nlSS.item(z)).getAttribute("itemOp").equals("LT")){
					itemOp = "<";
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
	
 private static String getRegistrySource(){

    /**
     * The getRegistrySource method reads a URL containing an XML file that specifies whether
     * the registry is stored as a database or an XML file.  
     */
	Document parameterDoc = null;
	DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder parameterBuilder = null;
	String registrySource = null;
 	boolean goodRegSource = true;
 	
	Document doc = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = null;
 	
 	/**
 	 * First get read the XML file from the URL and build a DOM tree from the contents.
 	 */
	try {
		parameterBuilder = parameterFactory.newDocumentBuilder();
		parameterDoc = parameterBuilder.parse("http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/qpParameters.xml");
	}
	catch (ParserConfigurationException e) {
		registrySource = "REGISTRY SOURCE - " + e.toString();
		goodRegSource = false;
	}
	catch (Exception e){
		registrySource = "REGISTRY SOURCE - " + e.toString();
		goodRegSource = false;
	}

    /**
     * If the DOM tree was successfully built, identify element "registrySource".
     * If the value of this element is not "db" (for database) or "xml" (for XML file),
     * return an error message.
     */
	if (goodRegSource == true){
		Element parameterDocElement = parameterDoc.getDocumentElement();
		NodeList parameterNL = parameterDocElement.getElementsByTagName("registrySource");
		registrySource = parameterNL.item(0).getFirstChild().getNodeValue();
		if (!(registrySource.equals("db") | registrySource.equals("xml"))){
			registrySource = "REGISTRY SOURCE - Invalid registry source: must be database or xml file.";
		}
	}
	
	/** 
	 * Return the registry source as "xml", "db", or an error message.
	 */
	return registrySource;
 }
}
