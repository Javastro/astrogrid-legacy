  /*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.*;
import java.io.Reader;

/**
 * @author Elizabeth Auden
 *
 */
public class QueryParser3_0
 {
	static String nodeDetails = "";
	static String searchElements = "";
	public static String parseQuery (String query) throws ClassNotFoundException {

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

	  Registry3_0 reg = new Registry3_0();
	  String registrySource = getRegistrySource();

	  if (goodQuery == true){
		if (registrySource.equals("db")){
			query = "Database registry not implemented yet.  Use XML registry.";
			goodQuery = false;

		}
	
		else if (registrySource.equals("xml")){
			try{
				Element docElement = doc.getDocumentElement();
				NodeList ssList = docElement.getElementsByTagName("selectionSequence");
				if (ssList.getLength() != 0){
					query = xmlToXQL(ssList.item(0));
				}
				else {
					query = "QUERY - Query does not conform to Astrogrid registry query schema.";
					goodQuery = false;
				} 
			}
			catch (Exception e) {
				query = "QUERY - " + e.toString();
				goodQuery = false;
			}

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
				
				xqlResponse = reg.xmlQuery(query);				
				queryResponse = xqlToXML(xqlResponse, searchElements);						
			}
			else queryResponse = "<queryResponse><recordKeyPair item='ERROR:' value='" + query + "'></queryResponse>";

		}
		else if (!(registrySource.equals("db") | registrySource.equals("xml"))){
			queryResponse = "<queryResponse><recordKeyPair item='ERROR:' value='" +registrySource + "'></queryResponse>";
		}
	  }
	  else queryResponse = "<queryResponse><recordKeyPair item='ERROR:' value='" + query + "'></queryResponse>";
	  
	  return queryResponse;
	}

	private static String xmlToXQL(Node node) {
		String response = "";
		nodeDetails = "";
		response = response + "(";
		NodeList nlSS = node.getChildNodes();
		boolean se = false;
		boolean it = true;
		boolean itOp = true;
		boolean val = true;
		boolean goodQuery = true;
		
		for (int z=0; z < nlSS.getLength(); z++){
			if (nlSS.item(z).getNodeName().equals("selection")){
				se = true;
				String itemOp = "";
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
			if (nlSS.item(z).getNodeName().equals("selectionOp")){
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

		String xmlResponse = "";
		String nodeResponse = "";
		
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
			xmlResponse = "QUERY - " + e.toString();
		}
		catch (Exception e){
			xmlResponse = "QUERY - " + e.toString();
		}

		try{
			Element docElement2 = doc2.getDocumentElement();
			NodeList errorNodes = docElement2.getElementsByTagName("error");
			if (errorNodes.getLength() != 0) {
				xmlResponse = xmlResponse = "<recordKeyPair item='ERROR:' value='" + errorNodes.item(0).getFirstChild().getNodeValue() + "/>";
			}
			else {
				try{
					NodeList serviceNodes = docElement2.getElementsByTagName("service");
					if (serviceNodes.getLength() != 0) {
						Element serviceNodesParent = (Element)serviceNodes.item(0).getParentNode();
						Node node = serviceNodesParent;
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
						xmlResponse = "<recordKeyPair item='ERROR:' value='No matching registry entries found.'/>";
					}
				}
				catch (Exception e) {
				  xmlResponse = "<recordKeyPair item='ERROR:' value='QUERY - " + e.toString() + "/>";
				}
			}
		}
		catch (Exception e) {
		  xmlResponse = "<recordKeyPair item='ERROR:' value='QUERY - " + e.toString() + "/>";
		}


		xmlResponse = "<queryResponse>"+ xmlResponse + "</queryResponse>";
		return xmlResponse;
	}

	private static String getNodeDetails (Node node) {

		String content = "";
		String itemName = "";
		int type = node.getNodeType();
		
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

		
		if (type == Node.TEXT_NODE) {
			content = node.getNodeValue();
			itemName = node.getParentNode().getNodeName();
			if (!content.trim().equals("")){
				nodeDetails = nodeDetails + "<recordKeyPair item='" + itemName + "' value='" + content + "'/>";
			}			
		}
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int m=0; m< children.getLength(); m++) {
				getNodeDetails(children.item(m));
			}
		} 
		if ((type == Node.ELEMENT_NODE)&&(node.getNodeName().equals("service"))) {
		  nodeDetails = nodeDetails + "</responseRecord>";
		}  
		return nodeDetails;
	}
	
 private static String getRegistrySource(){

	Document parameterDoc = null;
	DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder parameterBuilder = null;
	String registrySource = null;
 	boolean goodRegSource = true;
 	
	Document doc = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = null;
 	
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

	if (goodRegSource == true){
		Element parameterDocElement = parameterDoc.getDocumentElement();
		NodeList parameterNL = parameterDocElement.getElementsByTagName("registrySource");
		registrySource = parameterNL.item(0).getFirstChild().getNodeValue();
		if (!(registrySource.equals("db") | registrySource.equals("xml"))){
			registrySource = "REGISTRY SOURCE - Invalid registry source: must be database or xml file.";
		}
	}
	return registrySource;
 }
}
