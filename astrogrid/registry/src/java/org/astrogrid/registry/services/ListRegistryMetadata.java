package org.astrogrid.registry.services;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ListRegistryMetadata {
	
	public static void main(String[] args){

		if (args.length !=2) {
			System.err.println("Usage: java ListRegistryMetadata <queryElement> <queryElementValue>");
		}
		String queryElement = args[0];
		String queryElementValue = args[1];

		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse("astrogrid/registry/services/registry.xml");
			ListRegistryMetadata dom = new ListRegistryMetadata();
			
			String response = dom.generateResponse(doc, queryElement, queryElementValue);
			System.out.println("Response: \n" + response);
		}
		catch (ParserConfigurationException e) {
			System.err.println(e);
		}
		catch (Exception e){
			System.out.println("oops!: " + e);
		}

	}

	public String generateResponse(Document doc, String queryElement, String queryElementValue){

		String response = "";

		Element docElement = doc.getDocumentElement();

		NodeList nl = docElement.getElementsByTagName(queryElement);

		for( int i=0;i<nl.getLength();i++){
			
			Element parent = (Element)nl.item(i).getParentNode();	
			
			String matchElementValue = 		
				parent.getElementsByTagName(queryElement).item(0).getFirstChild().getNodeValue();
			
			if (queryElementValue.equals(matchElementValue)){
				
				String id = parent.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
				String title = parent.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();					String publisher = 					parent.getElementsByTagName("publisher").item(0).getFirstChild().getNodeValue();
				String creator = 									parent.getElementsByTagName("creator").item(0).getFirstChild().getNodeValue();
				String description = 															parent.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
				String contributor = 															parent.getElementsByTagName("contributor").item(0).getFirstChild().getNodeValue();
				String date = parent.getElementsByTagName("date").item(0).getFirstChild().getNodeValue();
				String version = 					parent.getElementsByTagName("version").item(0).getFirstChild().getNodeValue();
				String uri = parent.getElementsByTagName("uri").item(0).getFirstChild().getNodeValue();						String resourceURL = 															parent.getElementsByTagName("resourceURL").item(0).getFirstChild().getNodeValue();
				String serviceURL= 					parent.getElementsByTagName("serviceURL").item(0).getFirstChild().getNodeValue();
				//String subjectList = 														parent.getElementsByTagName("subjectList").item(0).getFirstChild().getNodeValue();
				String name = parent.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				String email = parent.getElementsByTagName("email").item(0).getFirstChild().getNodeValue();

				response = response + "<id>"+id +"</id> \n";
				response = response + "<title>"+title +"</title> \n";
				response = response + "<publisher>"+publisher +"</publisher> \n";
				response = response + "<creator>"+creator +"</creator> \n";
				response = response + "<description>"+description +"</description> \n";
				response = response + "<contributor>"+contributor +"</contributor> \n";
				response = response + "<date>"+ date +"</date> \n";
				response = response + "<version>"+ version +"</version> \n";
				response = response + "<uri>"+ uri +"</uri> \n";
				response = response + "<resourceURL>"+ resourceURL +"</resourceURL> \n";
				response = response + "<serviceURL>"+ serviceURL +"</serviceURL> \n";
				//response = response + "<subjectList>"+ subjectList +"</subjectList> \n";
				response = response + "<name>"+ name +"</name> \n";
				response = response + "<email>"+ email +"</email> \n\n";
				System.out.println("Response here: \n");
			}
		}

		return response;

	}

}