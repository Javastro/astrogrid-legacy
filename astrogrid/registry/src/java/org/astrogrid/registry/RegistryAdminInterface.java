
package org.astrogrid.registry;

import org.w3c.dom.Document;

/**
 * Small Interface used by the server side web service and the client delegate.  For determining what
 * the web service methods are that can be performed.  This interface deals with the Administration of
 * the registry which is the insert/update/removal of registry data.
 * 
 * @author Kevin Benson
 *
 */
public interface RegistryAdminInterface {
 
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
   public Document update(Document query) throws Exception;
   
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
    * Currently not expected to be used by clients.  More for clean-up of registries server side.  A registry
    * when being removed should change their status to deleted.
    * 
    */   
   public Document remove(Document query) throws Exception;


   /**
    * 
    * @param query
    * @return
    * @throws Exception
    */
   public Document addRegistryEntries(Document query) throws Exception;

   
}