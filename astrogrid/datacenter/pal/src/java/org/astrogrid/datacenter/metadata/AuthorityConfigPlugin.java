/*
 * $Id: AuthorityConfigPlugin.java,v 1.6 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;

/**
 * Generates an Authority Resource from information stored in the configuration
 * file
 * <p>
 * @author M Hill
 */

public class AuthorityConfigPlugin implements VoResourcePlugin {
   
   /**
    * Returns an authority resource element, based on values in the configuration
    * file
    */
   public String[] getVoResources() throws IOException {
      StringWriter sw = new StringWriter();
      
      XmlPrinter resourceSnippet = new XmlPrinter(sw, false);
      XmlTagPrinter authResource = resourceSnippet.newTag("Resource",
            new String[] {
               "xsi:type='AuthorityType'",
               "status='active'",
               "updated='"+new Date()+"'"
            });
      XmlTagPrinter identifier = authResource.newTag("Identifier");
      identifier.writeTag("AuthorityID", SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY));
      identifier.writeTag("ResourceKey", "authority");

      /** This isn't right - it's submitting datacenter information as part of the organisation's information */
      identifier.writeTag("Title", DataServer.getDatacenterName());
      identifier.writeTag("ShortName", SimpleConfig.getSingleton().getString("datacenter.shortname", ""));
      
      XmlTagPrinter summary = identifier.newTag("Summary");
      summary.writeTag("Description", SimpleConfig.getSingleton().getString("datacenter.description", ""));
      summary.writeTag("ReferenceURL", SimpleConfig.getSingleton().getString("datacenter.url", ""));
   
      //VoDescriptionServer.writeCuration(authResource);

      resourceSnippet.close();
      sw.close();
      String s = sw.toString(); //so we can pause here with a breakpoint and see what it is
      return new String[] { s };
   }
   
}


