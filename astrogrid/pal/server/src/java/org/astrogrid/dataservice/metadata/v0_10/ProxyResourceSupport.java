/*
 * $Id: ProxyResourceSupport.java,v 1.1 2005/03/08 18:05:57 mch Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Used to serve metadata generated from disk, or a URL, or some other service.
 * Provides methods for ensuring identifiers etc are correct for *this* service
 * <p>
 * @author M Hill
 */

public class ProxyResourceSupport extends VoResourceSupport {
   
   /** As makeMine(Document) but takes stream to parse from
    */
   public String makeMine(InputStream in) throws IOException {
      try {
         Document doc = DomHelper.newDocument(in);
         return makeMine(doc);
      }
      catch (ParserConfigurationException e) {
         log.error("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e) {
         log.error("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata: "+e,e);
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
   public String makeMine(Document doc) throws IOException {
      StringBuffer resources = new StringBuffer();
      
      String rootElementName = doc.getDocumentElement().getNodeName();
      
      //if the root element is a resource, return that
      if (rootElementName.equals("Resource"))  {
         checkResource( doc.getDocumentElement());
         resources.append(DomHelper.ElementToString(doc.getDocumentElement())) ;
      }
         //if the root element is a vodescription, return all resource elements
      else if (rootElementName.equals("VODescription"))  {
         NodeList voResources = doc.getElementsByTagName("Resource");
         if (voResources.getLength()==0)  {
            throw new MetadataException("No <Resource> elements in document ");
         }
         
         for (int i = 0; i < voResources.getLength(); i++)  {
            Element resource = (Element) voResources.item(i);
            
            checkResource(resource);
            
            resources.append(DomHelper.ElementToString(resource));
         }
      }
      else  {
         throw new MetadataException("No 'Resource' or 'VODescription' element in metadata");
      }
      
      return resources.toString();
   }
   
   
   /** Checks that a resource is OK for this datacenter, and sets appropriately if not */
   public void checkResource(Element remoteResource) throws IOException  {
      
      //use file/url date if available, otherwise current time
      Date updated = null;
      
      remoteResource.setAttribute("status", "active");
      remoteResource.setAttribute("updated", toRegistryForm(updated));

      
      try {
         Element localCore = DomHelper.newDocument(makeConfigCore("")).getDocumentElement();
         
         //set id to local id
         Element localId = DomHelper.getSingleChildByTagName(localCore, "identifier");
         Element remoteId = DomHelper.getSingleChildByTagName(remoteResource, "identifier");
            
         //set access url to local service
         
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server not configured properly", e);
      }
      catch (SAXException e) {
         throw new MetadataException("Server error: Core Generated VoResource is invalid XML: "+makeConfigCore(""));
      }
   }
   

   public void checkIdentifierTag(Element resource)  {
      
      //check that each 'returns' resource has a child <Identifier>, as some early ones didn't and we want to catch them
      Element voIdTag = DomHelper.ensuredGetSingleChild(resource, "Identifier");
      
      String dsaAuthId = SimpleConfig.getSingleton().getString(AUTHID_KEY);
      String dsaResKey = SimpleConfig.getSingleton().getString(RESKEY_KEY);
      
      //we need to override/make sure that they are correct for *this* service, eg
      //we may be proxying to get the metadata
      Element authIdTag = DomHelper.ensuredGetSingleChild(voIdTag, "AuthorityID");
      String docAuthId = DomHelper.getValueOf(authIdTag);
      
      if (!docAuthId.equals(dsaAuthId))  {
         //don't bother with warning if authority is empty
         if (docAuthId.trim().length()>0)  {
            log.warn("Authority is '"+docAuthId+"', but datacenter is configured for "+dsaAuthId+", changing resource to config value in Resource type "+resource.getAttribute("xsi:type"));
         }
         DomHelper.setElementValue(authIdTag, dsaAuthId);
      }
      
      Element resKeyTag = DomHelper.ensuredGetSingleChild(voIdTag, "ResourceKey");
      String docResKey = DomHelper.getValueOf(resKeyTag);
      
      //bit messy - work out how to change resource key without knowing what the one is in the docuemnt.
      //just look for dsaResKey+"/" and replace that bit if nec.
      if (!docResKey.startsWith(dsaResKey+"/"))  {
         String newValue = dsaResKey+"/"+docResKey.substring(docResKey.indexOf("/")+1);
         DomHelper.setElementValue(resKeyTag, newValue);
      }
   }
   
   
}


