
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
 
   public Document update(Document query) throws Exception;
   public Document remove(Document query) throws Exception;
   
}