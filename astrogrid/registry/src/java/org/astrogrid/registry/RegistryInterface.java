
package org.astrogrid.registry;

import org.w3c.dom.Document;

public interface RegistryInterface{
   
   public Document submitQuery(Document query) throws Exception;
   public Document fullNodeQuery(Document query) throws Exception;
   
}