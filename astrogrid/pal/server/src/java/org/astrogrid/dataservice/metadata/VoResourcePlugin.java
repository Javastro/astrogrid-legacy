/*
 * $Id: VoResourcePlugin.java,v 1.2 2005/03/08 18:05:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;

/**
 * The metadata plugins return a VoResource element as a string, or a list of them.
 * <p>
 * @author M Hill
 */

public interface VoResourcePlugin
{
   
   /**
    * Returns a VOResource element of the metadata.  Returns a string (rather than
    * DOM element) so that we can combine them easily; some DOMs do not mix well.
    */
   public String getVoResource() throws IOException;
   
}


