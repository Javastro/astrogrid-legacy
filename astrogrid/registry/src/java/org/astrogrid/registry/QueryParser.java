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
import java.sql.SQLException;

/**
 * @author Elizabeth Auden
 *
 */
public class QueryParser
 {
	static String nodeDetails = "";

	public static String parseQuery (String query) throws SQLException, ClassNotFoundException {

	  Reader reader = new StringReader(query);
	  InputSource inputSource = new InputSource(reader);
	  String xqlResponse = "";
	  String queryResponse = "";
      
	  Document doc = null;
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder builder = null;
	  try {
		builder = factory.newDocumentBuilder();
		doc = builder.parse(inputSource);
	  }
	  catch (ParserConfigurationException e) {
		System.err.println(e);
	  }
	  catch (Exception e){
		System.out.println("oops!: " + e);
	  }

      String sqlQuery = "";
	  Registry reg = new Registry();
	  String registrySource = getRegistrySource();
	  if (registrySource != null){
		if (registrySource.equals("db")){
			try{
				Element docElement = doc.getDocumentElement();
				if (docElement.getFirstChild().getNodeName().equals("selectionSequence")){
					sqlQuery = xmlToSQL(docElement.getFirstChild());
				}
			}
			catch (Exception e) {
			  queryResponse = "";
			}
		}
	
		else if (registrySource.equals("xml")){
			try{
				Element docElement = doc.getDocumentElement();
				if (docElement.getFirstChild().getNodeName().equals("selectionSequence")){
					query = xmlToXQL(docElement.getFirstChild());
				}
			}
			catch (Exception e) {
			  queryResponse = "";
			}
			query = "//service [" + query + "]";		
			xqlResponse = reg.xmlQuery(query);
			queryResponse = xqlToXML(xqlResponse);	  		
		}
		else if (!(registrySource.equals("db") | registrySource.equals("xml"))){
			queryResponse = "<queryResponse>" +registrySource + "</queryResponse>";
		}
	  }
	  return queryResponse;
	}

	private static String xmlToSQL(Node node) {
		String sqlQuery = "SELECT * FROM <registry> WHERE ";
		String response = "";
		response = response + "(\n";
		NodeList nlSS = node.getChildNodes();
		for (int z=0; z < nlSS.getLength(); z++){
			if (nlSS.item(z).getNodeName().equals("selection")){
				response = response + "(";
				response = response + ((Element) nlSS.item(z)).getAttribute("item");
				response = response + " "+((Element) nlSS.item(z)).getAttribute("itemOp")+ " ";
				response = response + ((Element) nlSS.item(z)).getAttribute("value");
				response = response + ")";
			}
			if (nlSS.item(z).getNodeName().equals("selectionOp")){
				response = response + "\n"+((Element) nlSS.item(z)).getAttribute("op")+ "\n";
			}
			if (nlSS.item(z).getNodeName().equals("selectionSequence")){
				response = response + xmlToSQL(nlSS.item(z));
			}			
		}
		response = response + "\n)";
		return response;
	}
    
	private static String xmlToXQL(Node node) {
		String response = "";
		nodeDetails = "";
		response = response + "(";
		NodeList nlSS = node.getChildNodes();
		for (int z=0; z < nlSS.getLength(); z++){
			if (nlSS.item(z).getNodeName().equals("selection")){
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
				response = response + "";
				response = response + "(.//";
				response = response + ((Element) nlSS.item(z)).getAttribute("item");
				response = response + " "+ itemOp + " ";
				response = response + "'"+((Element) nlSS.item(z)).getAttribute("value")+ "'";
				response = response + ")";
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
					selOp = "$not$";
				}
				response = response + " "+ selOp + " ";
			}
			if (nlSS.item(z).getNodeName().equals("selectionSequence")){
				response = response + xmlToXQL(nlSS.item(z));
			}			
		}
		response = response + ")";
		return response;
	}
	
	private static String xqlToXML(String xqlResponse){

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
			xmlResponse = e.toString();
		}
		catch (Exception e){
			xmlResponse = e.toString();
		}
		try{
			Element docElement2 = doc2.getDocumentElement();
			NodeList serviceNodes = docElement2.getElementsByTagName("service");
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
		catch (Exception e) {
		  xmlResponse = e.toString();
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
 	
 	Document doc = null;
 	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 	DocumentBuilder builder = null;
 	
 	try {
 		parameterBuilder = parameterFactory.newDocumentBuilder();
 		parameterDoc = parameterBuilder.parse("http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/qpParameters.xml");
 	}
 	catch (ParserConfigurationException e) {
 		registrySource = e.toString();
 	}
 	catch (Exception e){
		registrySource = e.toString();
 	}
 	Element parameterDocElement = parameterDoc.getDocumentElement();
 	NodeList parameterNL = parameterDocElement.getElementsByTagName("registrySource");
    registrySource = parameterNL.item(0).getFirstChild().getNodeValue();
    
    if (!(registrySource.equals("db") | registrySource.equals("xml"))){
    	registrySource = "Invalid registry source: must be database or xml file.";
    } 
	return registrySource;
 }
}