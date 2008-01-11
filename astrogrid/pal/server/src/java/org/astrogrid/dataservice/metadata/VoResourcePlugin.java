/*
 * $Id: VoResourcePlugin.java,v 1.4 2008/01/11 15:58:25 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;

/**
 * The metadata plugins return a VoResource element as a string, or a list of them.
 * The VoResourcePlugin framework is used to generate v0.10 resources,
 * but not v1.0 resources.
 * <p>
 * @author M Hill
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 *
 */

public interface VoResourcePlugin
{
   
   /**
    * Returns a VOResource element of the metadata.  Returns a string (rather than
    * DOM element) so that we can combine them easily; some DOMs do not mix well.
    */
   public String getVoResource() throws IOException;
}
