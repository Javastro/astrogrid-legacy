package org.astrogrid.registry.common.versionNS;

import org.astrogrid.registry.common.versionNS.IRegistryInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 



/**
 * Small Interface used by the server side web service and the client delegate.  Used for
 * harvesting other registries.  A client may call this webservice, but typically it will 
 * automatically harvest from registries.
 * 
 * @author Kevin Benson
 *
 */
public abstract class Registry implements IRegistryInfo{
   
   private final double VERSION_NUMBER = 0.9;
 
   public double getVersionNumber() {
      return this.VERSION_NUMBER;      
   }
   
   public Document getDocument() {
      Document registryDoc = null;
      System.out.println("yeah entered the realm of creating the document");
      try {
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         registryDoc = registryBuilder.newDocument();
         Element root = registryDoc.createElement("VODescription");
         root.setAttribute("xmlns","http://www.ivoa.net/xml/VOResource/v0.9");
         root.setAttribute("xmlns:vr","http://www.ivoa.net/xml/VOResource/v0.9");
         root.setAttribute("xmlns:vc","http://www.ivoa.net/xml/VOCommunity/v0.2");
         root.setAttribute("xmlns:vg","http://www.ivoa.net/xml/VORegistry/v0.2");
         root.setAttribute("xmlns:vs","http://www.ivoa.net/xml/VODataService/v0.4");
         root.setAttribute("xmlns:vt","http://www.ivoa.net/xml/VOTable/v0.1");
         root.setAttribute("xmlns:cs","http://www.ivoa.net/xml/ConeSearch/v0.2");
         root.setAttribute("xmlns:sia","http://www.ivoa.net/xml/SIA/v0.6");
         root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
         root.setAttribute("xsi:schemaLocation","http://www.ivoa.net/xml/VOResource/v0.9 " +
         "http://www.ivoa.net/xml/VOResource/VOResource-v0.9.xsd " +
         "http://www.ivoa.net/xml/VOCommunity/v0.2 " +
         "http://www.ivoa.net/xml/VOCommunity/VOCommunity-v0.2.xsd " +
         "http://www.ivoa.net/xml/VORegistry/v0.2" +
         " http://www.ivoa.net/xml/VORegistry/VORegistry-v0.2.xsd" +
         "  http://www.ivoa.net/xml/ConeSearch/v0.2" +
         "  http://www.ivoa.net/xml/ConeSearch/ConeSearch-v0.2.xsd" +
         " http://www.ivoa.net/xml/SIA/v0.6" +
         " http://www.ivoa.net/xml/SIA/SIA-v0.6.xsd");
         registryDoc.appendChild(root);
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }
      
      return registryDoc;
   }
   
}