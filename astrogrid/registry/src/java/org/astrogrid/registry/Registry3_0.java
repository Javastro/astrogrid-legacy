/*
 * Created on 20-May-2003
 *
 * @author=Elizabeth Auden
 */
 
package org.astrogrid.registry;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import de.gmd.ipsi.xql.*;
import de.gmd.ipsi.domutil.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class Registry3_0 {
	static int counter = 0;
	static int tabCounter = 0;
	int nodeNumber = 0;
	String response = "";
	String queryResponse = "";
		
	public String xmlQuery(String query) {

		Document parameterDoc = null;
		DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parameterBuilder = null;
		String registryFilename = null;
		String response = "";
		boolean goodParameters = true;
		

		
		try {
		  parameterBuilder = parameterFactory.newDocumentBuilder();
		  parameterDoc = parameterBuilder.parse("http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/parameters_v1_0.xml");
		  //parameterDoc = parameterBuilder.parse("http://localhost:8080/org/astrogrid/registry/parameters_v1_0.xml");
		}
		catch (ParserConfigurationException e) {
		  response = "<error>REGISTRY - " + e.toString() + "</error>";
		  goodParameters = false;
		}

		catch (Exception e){
			response = "<error>REGISTRY - " + e.toString()+ "</error>";
			goodParameters = false;
		}

		if (goodParameters == true) {
			Element parameterDocElement = parameterDoc.getDocumentElement();
			NodeList parameterNL = parameterDocElement.getElementsByTagName("registryFilename");
			registryFilename = parameterNL.item(0).getFirstChild().getNodeValue();

			if (registryFilename == null){
				response = "<error>REGISTRY - Invalid file name.</error>";
			}  
		    
			else {
				Document doc = DOMUtil.createDocument();

				URL registryURL;
				InputStream is;
				try {
					registryURL = new URL(registryFilename);
					is = registryURL.openStream();
					DOMUtil.parseXML(is, doc, false, DOMUtil.SKIP_IGNORABLE_WHITESPACE);
				}
				catch ( DOMParseException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
				catch (FileNotFoundException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
				catch (MalformedURLException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
				catch (IOException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}

				int tabCounter = 0;
				int counter = 0;
				Document registryDoc = null;
				DocumentBuilderFactory registryFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder registryBuilder = null;
				try {
					registryBuilder = registryFactory.newDocumentBuilder();
					registryDoc = registryBuilder.parse(registryFilename);
				}
				catch (ParserConfigurationException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
				catch (Exception e){
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
				Document resultDoc = DOMUtil.createDocument();
				Element root = resultDoc.createElement("queryResponse");
				resultDoc.appendChild(root);
				try{
					XQL.execute(query, doc, root);
				}
				catch (Exception e){
					response = "<error>Malformed XQL statement.</error>";
				}
				
				try{
				  Writer w = new StringWriter();
				  XMLWriter out = new XMLWriter(w);
				  out.formatOutput(true);
				  out.write(resultDoc);
				  response = response + out.toString();

				}
				catch (IOException e) {
					response = "<error>REGISTRY - " + e.toString()+ "</error>";
				}
			}
		}
		if (response.indexOf("<queryResponse>") == -1){
			response = "<queryResponse>" + response+ "</queryResponse>";

		}
	
		if (response.indexOf("<?xml version=") > -1){
                        String editedResponse = response.replaceFirst("<?.*?>", "");
			//String editedResponse = response.substring(0, response.indexOf("?xml")) + response.substring(response.indexOf("<?xml version")+24, response.length());  
			response = null;
			response = editedResponse;
		}
		if (response.indexOf("<queryResponse/>") > -1){
			String editedResponse2 = response.substring(0, response.indexOf("<queryResponse/>")) + response.substring(response.indexOf("<queryResponse/>")+16, response.length());  
			response = null;
			response = editedResponse2;
		}
		return response;
	}
	
	private static int getNodeNumbers(Node node, int tabCounter, int counter){
		if(node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			if (children != null) {
				for (int k=1; k< children.getLength(); k=k+2) {
					if(children.item(k).getNodeType() == Node.ELEMENT_NODE) {
						counter++;
						if (counter > tabCounter) tabCounter++; 
						tabCounter = getNodeNumbers(children.item(k), tabCounter, counter);
					}
					counter--;
				}
			}
		}
		return tabCounter;
	}	
}

