/*
 * $Id: MetadataGenerator.java,v 1.1 2004/08/18 18:44:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * Used to generate the service's metadata from the plugin.
 * <p>
 * At the moment is a one-off; @todo allow for an existing metadata file
 * so that <i>changes</i> are incorporated rather than from scratch every time.
 * <p>
 * @author M Hill
 */

public class MetadataGenerator
{
   protected static Log log = LogFactory.getLog(MetadataGenerator.class);

   public MetadataGenerator() {
   }
   
   public static void writeMetadata(Writer writer) throws IOException {
      try {
         QuerierPlugin plugin = QuerierPluginFactory.createPlugin(null);
         
         Document metadata = plugin.getMetadata();
         
         DomHelper.DocumentToWriter(metadata, writer);
         
      }
      catch (QuerierPluginException qpe) {
         throw new RuntimeException("Server Querier plugin mechanism not configured properly: "+qpe, qpe);
      }
   }
   
   /**
    * This can be run from the command line to generate the initial file
    */
   public static void main(String[] args) throws IOException {
      MetadataGenerator.writeMetadata(new OutputStreamWriter(System.out));
      
   }
   
}


