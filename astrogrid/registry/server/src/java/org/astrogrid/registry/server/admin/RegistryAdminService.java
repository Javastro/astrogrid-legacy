package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.server.RegistryFileHelper;
import javax.xml.parsers.ParserConfigurationException;

import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;

import org.astrogrid.config.Config;
import org.astrogrid.registry.common.XSLHelper;



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
                          
   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
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
    * @param update Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
  public Document update(Document update) {
    Document doc = null;
    /*
    try {
       VODescription vo = (VODescription)Unmarshaller.unmarshal(VODescription.class,query);
       DocumentBuilder registryBuilder = null;
       registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
       newDoc = registryBuilder.newDocument();
       Marshaller.marshal(vo,newDoc);
    }catch(MarshalException me) {
       me.printStackTrace();
    }catch(ParserConfigurationException pce) {
       pce.printStackTrace();
    }
    */
    
    //Load the registry first.
    //RegistryFileHelper.translateRegistry(query);
    //call updateDocument method to go through the document objects
    //and look for the primary key (resourcekey,AuthorityID) and do an update
    //doc = RegistryFileHelper.updateDocument(newDoc.getDocumentElement(),false, true);
    XSLHelper xs = new XSLHelper();
    Document xsDoc = xs.transformDatabaseProcess(update);
    //System.out.println("This is xsDoc = " + XMLUtils.DocumentToString(xsDoc));
    doc = RegistryFileHelper.updateResources(xsDoc,true, true);
    RegistryFileHelper.writeRegistryFile();
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
   * @param add Document a XML document dom object to be updated on the registry.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
   public Document add(Document add) {
      Document doc = null;
      Document newDoc = null;
      /*
      try {
         VODescription vo = (VODescription)Unmarshaller.unmarshal(VODescription.class,query);
         DocumentBuilder registryBuilder = null;         
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         newDoc = registryBuilder.newDocument();
         Marshaller.marshal(vo,newDoc);         
      }catch(MarshalException me) {
         me.printStackTrace();
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }
      */
      //Load the registry first.
      //RegistryFileHelper.translateRegistry(query);
      //call updateDocument method to go through the document objects
      //and look for the primary key (resourcekey,AuthorityID) and determine
      //an update or an add.
      //doc = RegistryFileHelper.addDocument(newDoc.getDocumentElement(), true);
      doc = RegistryFileHelper.addDocument(add.getDocumentElement(), true);
      //query to see if this is an authority type.
      /*      
      NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Authority");
      if(nl.getLength() > 0) {
        System.out.println("calling updateRegistryEntry");
        RegistryFileHelper.updateRegistryEntry(query);
      }
      */
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
   * @param status This DOM object really should be null each time.
   * @return the document updated on the registry is returned.
   * @author Kevin Benson
   * 
   */   
 public Document getStatus(Document status) {
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
