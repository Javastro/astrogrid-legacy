package org.astrogrid.registry.services;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ListServiceMetadataFormat {
	
	public static void main(String[] args){

		if (args.length !=2) {
			System.err.println("Usage: java ListServiceMetadataFormat <queryElementValue>");
		}
		String queryElement = args[0];
		String queryElementValue = args[1];

		Document doc = null;
		Document doc2 = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse("astrogrid/registry/services/services.xml");
			doc2 = builder.parse("astrogrid/registry/services/metadataFormat.xml");

			ListServiceMetadataFormat dom = new ListServiceMetadataFormat();
			
			String response = dom.generateResponse(doc, doc2, queryElement, queryElementValue);
			System.out.println(response);
		}
		catch (ParserConfigurationException e) {
			System.err.println(e);
		}
		catch (Exception e){
			System.out.println("oops!: " + e);
		}

	}

	public String generateResponse(Document doc, Document doc2, String queryElement, String queryElementValue){

		String response = "";

		Element docElement = doc.getDocumentElement();
		NodeList nl = docElement.getElementsByTagName(queryElement);

		for( int i=0;i<nl.getLength();i++){
			
			Element parent = (Element)nl.item(i).getParentNode();	
			
			String matchElementValue = 		
				parent.getElementsByTagName(queryElement).item(0).getFirstChild().getNodeValue();
			if (queryElementValue.equals(matchElementValue)){
				String id = parent.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
				String title = parent.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();	
				String metadataFormat = 														parent.getElementsByTagName("metadataFormat").item(0).getFirstChild().getNodeValue();	
				String catalogElements = getMF(doc2, metadataFormat);

				response = response + "<id>"+id +"</id> \n";
				response = response + "<title>"+title +"</title> \n";
				response = response + "<metadataFormat>"+metadataFormat+"</metadataFormat>\n\n";
				response = response + "<catalogElements>\n"+catalogElements+"</catalogElements>\n\n";
			}
		}

		return response;

	}

	public String getMF(Document doc2, String metadataFormat){

		String catalogElements = "";
		Element docElementMF = doc2.getDocumentElement();
		NodeList nlMF = docElementMF.getElementsByTagName("id");

		for( int i=0;i<nlMF.getLength();i++){

			Element parentMF = (Element)nlMF.item(i).getParentNode();
			String matchMFValue = parentMF.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			if (matchMFValue.equals(metadataFormat)){
				for (int j=0; j<parentMF.getElementsByTagName("catalogElement").getLength();j++) {

				catalogElements = catalogElements + "<catalogElement>" + parentMF.getElementsByTagName("catalogElement").item(j).getFirstChild().getNodeValue() + "</catalogElement>\n";
				}
			}
		}
		return catalogElements;
	}

}