/*
 * Created on 20-May-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
 
package org.astrogrid.registry;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import de.gmd.ipsi.xql.*;
import de.gmd.ipsi.domutil.*;
import java.io.*;

public class Registry {
	String response = "";
	String queryResponse = "";
	
	
	public String xmlQuery(String query){

		Document parameterDoc = null;
		DocumentBuilderFactory parameterFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parameterBuilder = null;
		String registryFilename = null;
		String response = "";

		try {
		  parameterBuilder = parameterFactory.newDocumentBuilder();
//		  parameterDoc = parameterBuilder.parse("http://localhost:8080/org/astrogrid/registry/parameters.xml");
		  parameterDoc = parameterBuilder.parse("org/astrogrid/registry/parameters.xml");
		}
		catch (ParserConfigurationException e) {
		  System.err.println(e);
		}
		catch (Exception e){
		  System.out.println("oops!: " + e);
		}

		Element parameterDocElement = parameterDoc.getDocumentElement();
		NodeList parameterNL = parameterDocElement.getElementsByTagName("registryFilename");
    
		registryFilename = parameterNL.item(0).getFirstChild().getNodeValue();

		if (registryFilename == null){
		  queryResponse = "Invalid registry filename.";
		}  
		    
		else {
			Document doc = DOMUtil.createDocument();

			try {
			  DOMUtil.parseXML(
				  new FileInputStream(registryFilename),
				  doc,
				  false,
				  DOMUtil.SKIP_IGNORABLE_WHITESPACE
			  );
			}
			catch ( DOMParseException e) {
			  e.printStackTrace();
			}
			catch (FileNotFoundException e) {
			  e.printStackTrace();
			}

			Document resultDoc = DOMUtil.createDocument();
			Element root = resultDoc.createElement("queryResponse");
			resultDoc.appendChild(root);
			XQL.execute(query, doc, root);
	
			try{
			  Writer w = new StringWriter();
			  XMLWriter out = new XMLWriter(w);
			  out.formatOutput(true);
			  out.write(resultDoc);
			  response = response + out.toString();
			}
			catch (IOException e) {
			  e.printStackTrace();
			}
		  }
		return response;
	}
}

