package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.server.RegistryFileHelper;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Class Name: RegistryAdminService
 * Description: This class represents the web service to the Administration piece of the
 * registry.  This class will handle inserts/updates/remove's of data in the registry.
 * 
 * @see org.astrogrid.registry..common.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class RegistryAdminService implements
                       org.astrogrid.registry.common.RegistryAdminInterface {

   /**
    * Takes an XML Document and will either update and insert the data in the registry.  If a client is
    * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
    * an automatic update will occur.  This method will only update main pieces of data/elements
    * conforming to the IVOA schema.
    * 
    * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
    * DataCollection 
    * 
    * @param query Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
  public Document update(Document query) {
    Document doc = null;
    //Load the registry first.
    RegistryFileHelper.translateRegistry(query);
    //call updateDocument method to go through the document objects
    //and look for the primary key (resourcekey,AuthorityID) and do an update
    doc = RegistryFileHelper.updateDocument(query.getDocumentElement(),false, true);
    return doc;
  }

  /**
   * Takes an XML Document and will either update and insert the data in the registry.  If a client is
   * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
   * an automatic update will occur.  This method will only update main pieces of data/elements
   * conforming to the IVOA schema.
   * 
   * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
   * DataCollection 
   * 
   * @param query Document a XML document dom object to be updated on the registry.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
   public Document add(Document query) {
      Document doc = null;
      //Load the registry first.
      RegistryFileHelper.translateRegistry(query);
      //call updateDocument method to go through the document objects
      //and look for the primary key (resourcekey,AuthorityID) and determine
      //an update or an add.
      doc = RegistryFileHelper.addDocument(query.getDocumentElement(), true);
      NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Authority");
      if(nl.getLength() > 0) {
        System.out.println("calling updateRegistryEntry");
        RegistryFileHelper.updateRegistryEntry(query);
      }
      return doc;
   }  
   
 /**
   * Takes an XML Document and will either update and insert the data in the registry.  If a client is
   * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
   * an automatic update will occur.  This method will only update main pieces of data/elements
   * conforming to the IVOA schema.
   * 
   * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
   * DataCollection 
   * 
   * @param query Document a XML document dom object to be updated on the registry.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
 public Document getStatus(Document query) {
   Document doc = null;
   try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
      
         Element elem = doc.createElement("status");
         elem.appendChild(doc.createTextNode(RegistryFileHelper.getStatusMessage()));
         doc.appendChild(elem);
         System.out.println("Document returned for Status message = " + XMLUtils.DocumentToString(doc));
   } catch (ParserConfigurationException pce){
      pce.printStackTrace();
   }
   return doc;
 } 
  
  /**
   * Takes an XML Document and will remove the data in the registry.  This method will
   * not remove small individual element pieces, but main pieces of the IVOA schema.  This
   * method will look for the primary key first (resourceKey, AuthorityID) and then remove
   * the main piece of the data in the registry.
   *
   * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
   * DataCollection 
   *  
   * @param query Document a XML document dom object to be removed to the registry.
   * @return the document removed from the registry is returned.
   * @author Kevin Benson
   * 
   */
  public Document remove(Document query) {
     try {
        RegistryFileHelper.removeDocument(query);
     } catch (Exception e){
        e.printStackTrace();
     }
     return query;
  }
   
}
