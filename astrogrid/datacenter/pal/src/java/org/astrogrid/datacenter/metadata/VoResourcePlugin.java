/*
 * $Id: VoResourcePlugin.java,v 1.2 2004/10/05 20:26:43 mch Exp $
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
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException;
   
}


