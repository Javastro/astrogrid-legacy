/*
 * $Id: VoResourceSupport.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.w3c.dom.Element;

/**
 * Helper methods for constructing VoResources documents
 *
 * <p>See package documentation.
 * <p>
 * @author M Hill
 */

public class VoResourceSupport {
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   public static final String AUTHID_KEY = "datacenter.authorityId";
   public static final String RESKEY_KEY = "datacenter.resourceKey";
   
   /** used to format dates so that the registry can process them. eg 2005-11-04T15:34:22Z -
    * the date must be GMT */
   public final static SimpleDateFormat REGISTRY_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

   
   /** Adds an Identifier tag to the given XmlPrinter */
   public void writeIdentifier(XmlPrinter parent, String resourceKeyEnd) throws IOException {
      XmlPrinter identifier = parent.newTag("Identifier");
      identifier.writeTag("AuthorityID", SimpleConfig.getSingleton().getString(AUTHID_KEY));
      identifier.writeTag("ResourceKey", SimpleConfig.getSingleton().getString(RESKEY_KEY)+resourceKeyEnd);
   }

   /** Writes out standard summary stuff to the given XmlPrinter */
   public void writeSummary(XmlPrinter parent) throws IOException {
      parent.writeTag("Title", DataServer.getDatacenterName());
      parent.writeTag("ShortName", SimpleConfig.getSingleton().getString("datacenter.shortname", ""));
      
      XmlPrinter summary = parent.newTag("Summary");
      summary.writeTag("Description", SimpleConfig.getSingleton().getString("datacenter.description", ""));
      summary.writeTag("ReferenceURL", SimpleConfig.getSingleton().getString("datacenter.url", ""));
   }
   
   /**  Writes out standard curation stuff to the given XmlPrinter  */
   public void writeCuration(XmlPrinter parent) throws IOException {
      XmlPrinter curation = parent.newTag("Curation");
      String publisher = SimpleConfig.getSingleton().getString("datacenter.publisher",null);
      if (publisher != null) {
         XmlPrinter publisherTag = curation.newTag("Publisher");
         publisherTag.writeTag("Title", publisher);
      }

      XmlPrinter contact = curation.newTag("Contact");
      contact.writeTag("Name", SimpleConfig.getSingleton().getString("datacenter.contact.name",""));
      contact.writeTag("Email", SimpleConfig.getSingleton().getString("datacenter.contact.email",""));
      contact.writeTag("Date", SimpleConfig.getSingleton().getString("datacenter.contact.date",""));
   }
   
   public void appendSummary(StringBuffer buffer) throws IOException {
      StringWriter sw = new StringWriter();
      XmlAsciiWriter xp = new XmlAsciiWriter(sw, false);
      writeSummary(xp);
      xp.close(); //make sure all tags are closed
      buffer.append(sw.toString());
   }

   public void appendIdentifier(StringBuffer buffer, String resourceKeyEnd) throws IOException {
      StringWriter sw = new StringWriter();
      XmlAsciiWriter xp = new XmlAsciiWriter(sw, false);
      writeIdentifier(xp, resourceKeyEnd);
      xp.close(); //make sure all tags are closed
      buffer.append(sw.toString());
   }

   public void appendCuration(StringBuffer buffer) throws IOException {
      StringWriter sw = new StringWriter();
      XmlAsciiWriter xp = new XmlAsciiWriter(sw, false);
      writeCuration(xp);
      xp.close(); //make sure all tags are closed
      buffer.append(sw.toString());
   }
   
   /** Checks that the identifier elements are there and set to the local values,
    * creating and setting if not.  The given resourceKeyEnd is appended to the
    datacenter's resourceKey to give the appropriate full resource key */
   public void ensureIdentifier(Element resource, String resourceKeyEnd) {

      //check that each 'returns' resource has a child <Identifier>, as some early ones didn't and we want to catch them
      Element voIdTag = DomHelper.ensuredGetSingleChild(resource, "Identifier");
      
      String dsaResKey = SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY);
   
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(voIdTag, "AuthorityID"), SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY));
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(voIdTag, "ResourceKey"), SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+resourceKeyEnd);
   }

   /** Checks the summary elements are there and set to the local values (creating it if not) */
   public void ensureSummary(Element resource) {
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(resource, "Title"), DataServer.getDatacenterName());
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(resource, "ShortName"), SimpleConfig.getSingleton().getString("datacenter.shortname", ""));

      Element summary = DomHelper.ensuredGetSingleChild(resource, "Summary");
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(summary, "Description"), SimpleConfig.getSingleton().getString("datacenter.description", ""));
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(summary, "ReferenceURL"), SimpleConfig.getSingleton().getString("datacenter.url", ""));
   }

   /**  Checks the curation stuff is present and set to the local values (creating it if not) */
   public void ensureCuration(Element resource)  {
      Element curation = DomHelper.ensuredGetSingleChild(resource, "Curation");
      String publisher = SimpleConfig.getSingleton().getString("datacenter.publisher",null);
      if (publisher != null) {
         Element title = DomHelper.ensuredGetSingleChild(curation, "Title");
         DomHelper.setElementValue(title, publisher);
      }

      Element contact = DomHelper.ensuredGetSingleChild(curation, "Contact");
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(contact, "Name"), SimpleConfig.getSingleton().getString("datacenter.contact.name", ""));
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(contact, "Email"), SimpleConfig.getSingleton().getString("datacenter.contact.email", ""));
      DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(contact, "Date"), SimpleConfig.getSingleton().getString("datacenter.contact.date", ""));
   }
   
   /** Converts date to string suitable for registry *
   public String toRegistryForm(Date givenDate) {
      //deprecated methods
      //long minsOffset = aDate.getTimezoneOffset();
      //Date gmtDate = new Date( aDate.getTime() + aDate.getTimezoneOffset());
      //this also leaves us with a date that has it's original timezone, but a new value.  It's really a different time...
      
      //cludge to get timezone of given date; write it out and then parse it...
      SimpleDateFormat offsetGetter = new SimpleDateFormat("Z");
      String offsetName = offsetGetter.format(givenDate);
      TimeZone givenZone = TimeZone.getTimeZone(offsetName);
      int offset = givenZone.getOffset(givenDate.getTime());
//      Date gmtDate = new Date(givenZone.getOffset();
      
      
      //convert to GMT
      Calendar localCalender = Calendar.getInstance();
      TimeZone localZone = localCalender.getTimeZone();
      Calendar ukcalender = new GregorianCalendar(Locale.ENGLISH);
      TimeZone gmtZone = TimeZone.getTimeZone("GMT");

      return "";
   }
    /**/
  
}








