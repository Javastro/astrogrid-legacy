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
import java.net.MalformedURLException;
import java.net.URL;


public class Registry {
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

		try {
		  parameterBuilder = parameterFactory.newDocumentBuilder();
		  parameterDoc = parameterBuilder.parse("http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/parameters.xml");
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

			URL registryURL;
			InputStream is;
			try {
				registryURL = new URL(registryFilename);
				is = registryURL.openStream();
				DOMUtil.parseXML(is, doc, false, DOMUtil.SKIP_IGNORABLE_WHITESPACE);
			}
			catch ( DOMParseException e) {
			  e.printStackTrace();
			}
			catch (FileNotFoundException e) {
			  e.printStackTrace();
			}
			catch (MalformedURLException e) {
			  e.printStackTrace();
			}
			catch (IOException e) {
			  e.printStackTrace();
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
				System.err.println(e);
			}
			catch (Exception e){
				System.out.println("oops!: " + e);
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

