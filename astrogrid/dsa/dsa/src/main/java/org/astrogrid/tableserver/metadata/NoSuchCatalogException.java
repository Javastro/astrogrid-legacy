/*
 */

package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.MetadataException;

/**
 * Thrown when a method in the metadoc interpreter is invoked for
 * a non-existent catalog.
 *
 * @author K Andrews
 */

public class NoSuchCatalogException extends MetadataException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public NoSuchCatalogException(String message)
   {
      super(message);
   }

}

