/*
 */

package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.MetadataException;

/**
 * Thrown when the guessTableIDForName method in the metadoc interpreter 
 * finds more than * one possible match for a table.
 *
 * @author K Andrews
 */

public class TooManyTablesException extends MetadataException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public TooManyTablesException(String message)
   {
      super(message);
   }

}

