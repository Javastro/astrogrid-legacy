/*
 * $Id: MetadataGenerator.java,v 1.3 2004/08/20 16:42:20 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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
   
   public static Document generateMetadata() throws IOException {
      
      try {
         QuerierPlugin plugin = QuerierPluginFactory.createPlugin(null);
         
         log.debug("Generating metadata...");
         /*
          * Grrr some implementations don't support importNodes, so this is likely
          * to be a real pain...
         //Generate VODescription
         Document voDescription = DomHelper.newDocument("<VODescription/>");//there must be a better way of doing this
         Element voResource = voDescription.createElement("Resource");
         voDescription.appendChild(voResource);
         voResource.appendChild(voDescription.createElement("Identifier"));
         voResource.appendChild(voDescription.createElement("Title"));
         voResource.appendChild(voDescription.createElement("ShortName"));
         voResource.appendChild(voDescription.createElement("Summary"));
         
         Node pluginMetadata = voDescription.importNode(plugin.getMetadata(), true);
         
         //this will give us metadata about the data itself, but not of the service
         voDescription.appendChild(pluginMetadata);

         return voDescription;
          */
         
         return plugin.getMetadata();
      }
      catch (QuerierPluginException qpe) {
         throw new RuntimeException("Server Querier plugin mechanism not configured properly: "+qpe, qpe);
      }
      /*
      catch (SAXException e) {
         throw new RuntimeException("Bad hardcoded XML: "+e, e);
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server not set up properly: "+e, e);
      }
       */
   }
   
   /**
    * This can be run from the command line to generate the initial file
    */
   public static void main(String[] args) throws IOException {
      DomHelper.DocumentToStream(MetadataGenerator.generateMetadata(), System.out);
      
   }
   
}


