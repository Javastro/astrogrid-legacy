package org.astrogrid.registry.server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.server.RegistryFileHelper;


/**
 * Class Name: RegistryAdminService
 * Description: This class represents the web service to the Administration piece of the
 * registry.  This class will handle inserts/updates/remove's of data in the registry.
 * 
 * @see org.astrogrid.registry.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class RegistryAdminService implements org.astrogrid.registry.RegistryAdminInterface {

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
    try {
       //Load the registry first.
       RegistryFileHelper.translateRegistry(query);
       //call updateDocument method to go through the document objects
       //and look for the primary key (resourcekey,AuthorityID) and determine
       //an update or an add.

       RegistryFileHelper.updateDocument(query);
       
       NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Authority");
       if(nl.getLength() > 0) {
          RegistryFileHelper.updateRegistryEntry(query);
       }
       
    } catch (Exception e){
       e.printStackTrace();
    }
    return query;
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
 public Document addRegistryEntries(Document query) {
   try {
      //Load the registry first.
      RegistryFileHelper.translateRegistry(query);
      //call updateDocument method to go through the document objects
      //and look for the primary key (resourcekey,AuthorityID) and determine
      //an update or an add.
      NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Registry");
      for(int i = 0;i < nl.getLength();i++ ) {
         RegistryFileHelper.updateNode(nl.item(i));
      }//for
      return query;       
   } catch (Exception e){
      e.printStackTrace();
   }
   return query;
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
