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
import java.io.File;

/**
 * The Registry3_0 class accepts an XQL query from the
 * QueryParser3_0 class and executes the query against the registry
 * XML file, registry_1_1.xml.  The registry response is returned in the 
 * same XML format as the registry file itself.
 * 
 * - Elizabeth Auden, 24 October 2003
 *
 */

public class Registry3_0 {

	String response = "";
	String queryResponse = "";
		
	public String xmlQuery(String query) {
		
		/**
		 * The xmlQuery method accepts as XQL query as input.  The location
		 * of the registry file is determined, and the XQL query is executed
		 * against the registry XML file.
		 */

        /**
         * First, determine location of the registry XML file.  It is assumed 
         * that "user.dir" is $TOMCAT_HOME/bin, and that the parameters file 
         * containing the registry URL is stored in $TOMCAT_HOME/webapps/org/
         * astrogrid. 
         */

		String registryPathName = (System.getProperty("user.dir"));
		File f = new File(registryPathName);
		registryPathName = f.getAbsolutePath();
		String parametersFileName = registryPathName + "/../webapps/org/astrogrid/registry/parameters_v1_0.xml";
		File parametersFile = new File(parametersFileName);

		Document parameterDoc = null;
		DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parameterBuilder = null;
		String registryFilename = null;
		String response = "";
		boolean goodParameters = true;
		
		try {
		  parameterBuilder = parameterFactory.newDocumentBuilder();
		  parameterDoc = parameterBuilder.parse(parametersFile);
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

            /**
             * If there is no <registryFilename> element in the parameters
             * file, return an error.  Otherwise, use the URL listed in the 
             * <registryFilename> element as the registry filename.
             */
            
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
				
				/**
				 * Once the registry file has been successfully found and the 
				 * DOM tree built, execute the XQL query against the registry.
				 */
				
				try{
					XQL.execute(query, doc, root);
				}
				catch (Exception e){
					response = "<error>Malformed XQL statement.</error>";
				}
				
				/**
				 * Once the query has been executed (or an error has been
				 * caught), write the XML formatted results to a string.
				 */
				
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
		
		/**
		 * Reformat the XML response string with <queryResponse> elements and 
		 * clean up spare characters and strings inserted by the IPSI DOM XQL
		 * query execution.  Return the query response.
		 */
		
		if (response.indexOf("<queryResponse>") == -1){
			response = "<queryResponse>" + response+ "</queryResponse>";
		}
	
		if (response.indexOf("<?xml version=") > -1){
                        String editedResponse = response.replaceFirst("<?.*?>", "");
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

}

