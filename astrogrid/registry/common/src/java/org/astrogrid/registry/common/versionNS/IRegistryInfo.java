package org.astrogrid.registry.common.versionNS;

import org.w3c.dom.Document;

/**
 * Small Interface used by the server side web service and the client delegate.  Used for
 * harvesting other registries.  A client may call this webservice, but typically it will 
 * automatically harvest from registries.
 * 
 * @author Kevin Benson
 *
 */
public interface IRegistryInfo {

   public double getVersionNumber();
   
   public Document getDocument();
   
}