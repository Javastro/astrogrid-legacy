package org.astrogrid.registry.common;

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
   
   /**
    * Queries the registry based on an xml Document model that has the xml contents of the query.
    * The returned Document model is all the results from the query.
    * @param query
    * @return
    * @throws Exception
    */
   public Document submitQuery(Document query) throws Exception;
   /**
    * This method will be deleted in time. See submitQuery
    * @param query
    * @return
    * @throws Exception
    * @see submitQuery
    */
   public Document fullNodeQuery(Document query) throws Exception;
   
   /**
    * Queries for all the information changed since a particular date.  The DOM model passed in has only one main
    * element a date_since element with an appropriate xsd:datetime.  The result is all the elements returned since that
    * date located in the registry.
    * @param query
    * @return
    * @throws Exception
    */
   public Document harvestQuery(Document query) throws Exception;
   
   /**
    * Loads only the current registry entry. The query parameter passed in is ignored, but is required in use of the
    * axis message style. 
    * @param query an ignored parameter
    * @return A document object conforming of only this registry entry.
    * @throws Exception
    */
   public Document loadRegistry(Document query) throws Exception;   
   
}