/*
 * $Id: AuthorityConfigPlugin.java,v 1.2 2004/09/06 20:42:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.config.SimpleConfig;
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
   public String getVoResource() throws IOException {
      StringWriter sw = new StringWriter();
      
      XmlPrinter resourceSnippet = new XmlPrinter(sw, false);
      XmlTagPrinter authResource = resourceSnippet.newTag("Resource", new String[] {"xsi:type='AuthorityType'"});
      XmlTagPrinter identifier = authResource.newTag("Identifier");
      identifier.writeTag("AuthorityID", SimpleConfig.getSingleton().getString("datacenter.authorityId"));
      identifier.writeTag("ResourceKey", SimpleConfig.getSingleton().getString("datacenter.resourceKey"));
      authResource.writeTag("Title", DataServer.getDatacenterName());
      authResource.writeTag("ShortName", SimpleConfig.getSingleton().getString("datacenter.shortname", ""));
      
      XmlTagPrinter summary = authResource.newTag("Summary");
      summary.writeTag("Description", SimpleConfig.getSingleton().getString("datacenter.description", ""));
      summary.writeTag("ReferenceURL", SimpleConfig.getSingleton().getString("datacenter.url", ""));
      XmlTagPrinter curation = authResource.newTag("Curation");
      
      String publisher = SimpleConfig.getSingleton().getString("datacenter.publisher",null);
      if (publisher != null) {
         XmlTagPrinter publisherTag = curation.newTag("Publisher");
         publisherTag.writeTag("Title", publisher);
      }

      XmlTagPrinter contact = curation.newTag("Contact");
      contact.writeTag("Name", SimpleConfig.getSingleton().getString("datacenter.contact.name",""));
      contact.writeTag("Email", SimpleConfig.getSingleton().getString("datacenter.contact.email",""));
      contact.writeTag("Date", SimpleConfig.getSingleton().getString("datacenter.contact.date",""));
      
      resourceSnippet.close();
      sw.close();
      String s = sw.toString(); //so we can pause here with a breakpoint and see what it is
      return s;
   }
   
   
}


