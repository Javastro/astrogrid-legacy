/*
 * $Id: MetadataPlugin.java,v 1.1 2004/08/18 18:44:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import org.w3c.dom.Document;

/**
 * Defines the interface a plugin must implement
 * <p>
 * @author M Hill
 */

public interface MetadataPlugin
{
   /**
    * Returns the VODescription element of the metadata
    * If there is more than one, logs an error but does not fail.
    */
   public Document getMetadata() throws IOException;
   
}


