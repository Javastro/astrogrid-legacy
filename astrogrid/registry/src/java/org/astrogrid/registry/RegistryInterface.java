
package org.astrogrid.registry;

import org.w3c.dom.Document;

/**
 * Small Interface used by the server side web service and the client delegate.  For determining what
 * the web service methods are that can be performed.  This interface deals with the Querying of
 * the registry.
 * 
 * @author Kevin Benson
 *
 */
public interface RegistryInterface{
   
   public Document submitQuery(Document query) throws Exception;
   public Document fullNodeQuery(Document query) throws Exception;
   public Document harvestQuery(Document query) throws Exception;
   
}