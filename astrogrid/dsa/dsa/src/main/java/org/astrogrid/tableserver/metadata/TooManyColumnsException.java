/*
 * $Id DatabaseAccessException.java $
 *
 */

package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.MetadataException;


/**
 * Thrown when the guessColumn method in the metadoc interpreter finds more than
 * one possible match for a column.
 *
 * @author M Hill
 */

public class TooManyColumnsException extends MetadataException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public TooManyColumnsException(String message)
   {
      super(message);
   }

}

