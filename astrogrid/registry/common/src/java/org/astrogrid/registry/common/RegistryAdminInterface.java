package org.astrogrid.registry.common;

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
   public Document add(Document query);


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
   public Document update(Document query);
   
   
   /**
    * 
    * @param query
    * @return
    * @throws Exception
    */
   public Document getStatus(Document query);
   
}