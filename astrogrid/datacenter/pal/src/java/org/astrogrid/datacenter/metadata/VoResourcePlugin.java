/*
 * $Id: VoResourcePlugin.java,v 1.3 2004/10/08 17:14:22 mch Exp $
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
   
   public final static String RESOURCE_PLUGIN_KEY = "datacenter.resource.plugin";
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException;
   
}


