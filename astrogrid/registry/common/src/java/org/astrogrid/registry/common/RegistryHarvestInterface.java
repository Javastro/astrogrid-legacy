package org.astrogrid.registry.common;

import org.w3c.dom.Document;
import org.exolab.castor.xml.ValidationException;

/**
 * Small Interface used by the server side web service and the client delegate.  Used for
 * harvesting other registries.  A client may call this webservice, but typically it will 
 * automatically harvest from registries.
 * 
 * @author Kevin Benson
 *
 */
public interface RegistryHarvestInterface {

   /**
    * harvest a resource entry. Based off of the current modification date.
    * If this resource is a Registry resource and does not exist it will add it and automatically to the local registry
    * and start a harvest/replicate.
    * The modification date will only be used for Registry Resource entries. Hence it actually is harvestAll if it is not
    * a registry resource entry.  This method when it is not a Registry resource entry can only take Resource entries 
    * managed by this authority.
    * @param query
    * @return
    */
   public Document harvestResource(Document query) throws ValidationException;
   
   /**
    * This method takes in a date/timestamp with a Resource entry.
    * @param query
    * @return
    */
   public Document harvestFromResource(Document query)  throws ValidationException;
   
   
   /**
    * It can take in just a date/timestamp which will kick off a harvest of
    * this registry.
    * Is equivelant of calling harvestFromResource with the date and this registry resource entry.
    * @param query
    * @return
    */
   public Document harvestFrom(Document query);
   
   /**
    * Harvest all information in this registry, takes null as parameter.
    * Is equivelant of calling harvestFromResource with a null date and this registry resource entry.
    * @param query
    * @return
    */   
   public Document harvest(Document query);
   
   
   /**
    * Known as a replicate.  This interface can take a null and will kick off a harvest of the known registries.  Or
    * take in a Resource entry and attempt to harvest the information from that entry. 
    * This method when it is not a Registry resource entry passed in can only take Resource entries 
    * managed by this authority.
    * @param query
    * @return
    */   
   public Document harvestAll(Document query) throws ValidationException;
   
}