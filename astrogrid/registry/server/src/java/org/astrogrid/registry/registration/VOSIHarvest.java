package org.astrogrid.registry.registration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.astrogrid.util.DomHelper;

/**
 * Servlet to transcribe IVOA service-metadata into an IVOA registration
 * document. The service metadata are capabaility elements read from the
 * service using VOSI.
 *
 * @author Guy Rixon
 */
public class VOSIHarvest extends RegistrarServlet {
	
	public Node harvestCapabilities(Node resource) throws Exception {
		Element root;
		if(resource.getNodeType() == Node.ELEMENT_NODE) {
			root = (Element)resource;
		}else if(resource.getNodeType() == Node.DOCUMENT_NODE) {
			root = ((Document)resource).getDocumentElement();
		}else {
			root = null;
			throw new Exception("Could not determine Node Type to do VOSI Fetch/Harvest");
		}
		//System.out.println("Element toString of incoming = " + DomHelper.ElementToString(root));
		
		//get rid of the tables if there using vosi the xsl
		//will copy it back.
		NodeList tableNodes = root.getElementsByTagNameNS("*", "table");
		while(tableNodes.getLength() != 0) {
			tableNodes.item(0).getParentNode().removeChild(tableNodes.item(0));
		}
		String capURL = "";
		String tableURL = "";
		String ceaURL = "";
		NodeList urlText;
		NodeList capList = root.getElementsByTagName("capability");
		
		if(capList.getLength() == 0) {
			throw new Exception("No Capabilities to Analyze");
		}
		do {
			//System.out.println("capList length = " + capList.getLength());
			if(capList.getLength() > 0) {
				if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#capabilities") ) {
					//System.out.println("try to set cap value");
					urlText= ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
					for(int j = 0;j < urlText.getLength();j++) {
						if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
							//System.out.println("yes text node lets try to concat");
							capURL += urlText.item(j).getNodeValue();
						}
					}
					System.out.println("cap url set = " + capURL);
				}                                                                 ivo://org.astrogrid/std/VOSI/v0.3#tables
				if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#tables") ) {
					//System.out.println("try to set tableURL");
					urlText = ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
					for(int j = 0;j < urlText.getLength();j++) {
						if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
							//System.out.println("yes text node lets try to concat on tableURL");
							tableURL += urlText.item(j).getNodeValue();
						}
					}					
					System.out.println("table url set = " + tableURL);
				}
				if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication") ) {
					//System.out.println("try to set ceaURL");
					urlText = ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
					for(int j = 0;j < urlText.getLength();j++) {
						if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
							//System.out.println("yes text node lets try to concat on ceaURL");
							ceaURL += urlText.item(j).getNodeValue();
						}
					}					
					System.out.println("cea url set = " + ceaURL);
				}
				//remove any other capability there because the xsl stylesheet
				//will match on the 1 remaining capability and replace it with the
				//url to capabilities vosi.
				if(capList.getLength() != 1) {
					System.out.println("removing child at item 0");
					capList.item(0).getParentNode().removeChild(capList.item(0));
				}
			}//if
		}while(capList.getLength() != 1);
		
		if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#capabilities") ) {
			//System.out.println("try to set cap value");
			urlText= ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
			for(int j = 0;j < urlText.getLength();j++) {
				if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
					//System.out.println("yes text node lets try to concat");
					capURL += urlText.item(j).getNodeValue();
				}
			}
			System.out.println("cap url set = " + capURL);
		}                                                                 ivo://org.astrogrid/std/VOSI/v0.3#tables
		if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#tables") ) {
			//System.out.println("try to set tableURL");			
			urlText = ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
			for(int j = 0;j < urlText.getLength();j++) {
				if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
					//System.out.println("yes text node lets try to concat on tableURL");
					tableURL += urlText.item(j).getNodeValue();
				}
			}					
			System.out.println("table url set = " + tableURL);
		}
		if( ((Element)capList.item(0)).getAttribute("standardID").equals("ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication") ) {
			//System.out.println("try to set ceaURL");
			urlText = ((Element)capList.item(0)).getElementsByTagName("accessURL").item(0).getChildNodes();
			for(int j = 0;j < urlText.getLength();j++) {
				if(urlText.item(j).getNodeType() == Node.TEXT_NODE) {
					//System.out.println("yes text node lets try to concat on ceaURL");
					ceaURL += urlText.item(j).getNodeValue();
				}
			}					
			System.out.println("cea url set = " + ceaURL);
		}		
		
		URL transformUrl = this.getClass().getResource("/xsl/VOSIFetch.xsl");
        RegistryTransformer transformer = new RegistryTransformer(transformUrl);
        transformer.setTransformationSource(resource);
        transformer.setTransformationParameter("capURL", capURL);
        if(tableURL != null && tableURL.trim().length() > 0) {
        	transformer.setTransformationParameter("tableURL", tableURL);
        }
        if(ceaURL != null && ceaURL.trim().length() > 0) {
        	transformer.setTransformationParameter("ceaURL", ceaURL);
        }

        transformer.transform();
        Document reg = DomHelper.newDocument("<ri:VOResources from=\"1\" numberReturned=\"1\" more=\"false\" xmlns:cea=\"http://www.ivoa.net/xml/CEA/v1.0rc1\" xmlns:ri=\"http://www.ivoa.net/xml/RegistryInterface/v1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></ri:VOResources>");
        //transformer.getResultAsDomNode();
        //System.out.println("Node to be imported " + DomHelper.ElementToString((Element)(((Document)transformer.getResultAsDomNode()).getDocumentElement())));
        Node importNode = reg.importNode(((Document)transformer.getResultAsDomNode()).getDocumentElement()/*.getElementsByTagNameNS("*","Resource").item(0)*/,true);
        reg.getDocumentElement().appendChild(importNode);
        
        if(ceaURL != null && ceaURL.trim().length() > 0) {
        	transformUrl = this.getClass().getResource("/xsl/VOSICEA.xsl");
        	transformer = new RegistryTransformer(transformUrl);
        	//transformer.setTransformationSource(reg);
        	transformer.setTransformationSource(importNode);
        	transformer.setTransformationParameter("ceaURL", ceaURL);
            transformer.transform();
            //System.out.println("Node to be imported " + DomHelper.ElementToString((Element)(((Document)transformer.getResultAsDomNode()).getDocumentElement().getElementsByTagNameNS("*","Resource").item(0))));
            Node ceaImportNode = reg.importNode(((Document)transformer.getResultAsDomNode()).getDocumentElement()/*.getElementsByTagNameNS("*","Resource").item(0)*/,true);
            reg.getDocumentElement().appendChild(ceaImportNode);
        }
        //System.out.println("final Document = " + DomHelper.DocumentToString(reg));
        return reg;
	}
	
}