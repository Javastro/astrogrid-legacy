/*
 * $Id: VoResourcePlugin.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;

/**
 * The metadata plugins just have to return VoResource elements (children of
 * a VoDescription element)
 * <p>
 * @author M Hill
 */

public interface VoResourcePlugin
{
   /**
    * Returns the VOResource element of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String getVoResource() throws IOException;
   
}


