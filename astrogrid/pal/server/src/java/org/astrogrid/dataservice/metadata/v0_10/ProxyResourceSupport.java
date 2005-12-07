/*
 * $Id: ProxyResourceSupport.java,v 1.6 2005/12/07 15:55:21 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.v0_10;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Used to serve metadata generated from disk, or a URL, or some other service.
 * Provides methods for ensuring identifiers etc are correct for *this* service.
 * NB Expect to extend from this to localise special values, such as the CEA's
 * application <-> service links.
 *
 * <p>
 * @author M Hill
 */

public class ProxyResourceSupport extends VoResourceSupport {
   
   /** As makeLocal(Document) but takes stream to parse from
    */
   public String makeLocal(InputStream in) throws IOException {
      try {
         Document doc = DomHelper.newDocument(in);
         return makeLocal(doc);
      }
      catch (SAXException e) {
         log.error("Invalid metadata",e);
         throw new MetadataException("Server not configured properly - invalid metadata: "+e,e);
      }
      finally  {
         try  {
            in.close();
         }
         catch (IOException e)  {
            // not bothered
         }
      }
   }
   
   /** Takes the given resource document and sets IDs etc to reflect *this* service.  The
    * given doc may contain a list of <Resource>
    * elements or a Description element.  Returns the transformed document.
    */
   public String makeLocal(Document doc) throws IOException {
      
      String rootElementName = doc.getDocumentElement().getLocalName(); //get without namespace
      
      //if the root element is a resource, return that
      if (rootElementName.equals("Resource"))  {
         checkResource( doc.getDocumentElement());
         return DomHelper.ElementToString(doc.getDocumentElement()) ;
      }
      //if the root element is a vodescription, return all resource elements
      else if (rootElementName.equals("VOResources"))  {
         StringBuffer localRes = new StringBuffer();
         NodeList voResources = doc.getElementsByTagNameNS("*", "Resource");
         if (voResources.getLength()==0)  {
            throw new MetadataException("No <Resource> elements in document ");
         }
         
         for (int i = 0; i < voResources.getLength(); i++)  {
            Element resource = (Element) voResources.item(i);
            
            checkResource(resource);
            
            localRes.append(DomHelper.ElementToString(resource));
         }
         return localRes.toString();
      }
      else  {
         throw new MetadataException("Root element is "+rootElementName+"; not 'Resource' or 'VOResources' in metadata");
      }
   }
   
   
   /** Checks that a resource is OK for this datacenter, and sets appropriately if not */
   public void checkResource(Element remoteResource) throws IOException  {

      /* The remoteResource DOM will be modified using values from the locally
       * generated VoResource core metadata */
      remoteResource.setAttribute("status", "active");
      remoteResource.setAttribute("updated", toRegistryForm(new Date()));

      try {
         //make local core and parse
         Element localCore = DomHelper.newDocument(
             "<" + VORESOURCE_ELEMENT + " " +
                 VORESOURCE_ELEMENT_NAMESPACES + ">" + makeCore("") 
                 + "</" + VORESOURCE_ELEMENT + ">").getDocumentElement();
         
         //set id to local 'core' id + last /-seperated token of the remote id
         Element remoteIdNode = DomHelper.getSingleChildByTagName(remoteResource, "identifier");
         if (remoteIdNode == null) {
            throw new MetadataException("No <identifier> element");
         }
         String localStem = DomHelper.getValueOf(localCore, "identifier");
         String remoteId = DomHelper.getValueOf(remoteIdNode);
         String newId = localStem;
         int i = remoteId.lastIndexOf("/");
         if (i>-1) newId = newId + remoteId.substring(i);
         DomHelper.setElementValue(remoteIdNode, newId);
         
         //set reference url
         Element contentNode = DomHelper.getSingleChildByTagName(remoteResource, "content");
         if (contentNode != null) {
            Element refUrlNode = DomHelper.getSingleChildByTagName(contentNode, "referenceURL");
            String localRefUrl = DomHelper.getValueOf(DomHelper.getSingleChildByTagName(localCore, "content"), "referenceURL");
            DomHelper.setElementValue(refUrlNode, localRefUrl);
         }
         
         
      }
      catch (SAXException e) {
         throw new MetadataException(e+" in Core Generated VoResource: "+makeCore(""),e);
      }
   }
   

   
   
}


