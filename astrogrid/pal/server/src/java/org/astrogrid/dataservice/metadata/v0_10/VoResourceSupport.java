/*
 * $Id: VoResourceSupport.java,v 1.4 2005/03/10 22:39:17 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.v0_10;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.Piper;
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;

/**
 * Helper methods for constructing basic VoResource documents
 *
 * <p>See package documentation.
 * <p>
 * @author M Hill
 */

public class VoResourceSupport {
   protected static Log log = LogFactory.getLog(VoResourceSupport.class);
   
   public static final String AUTHID_KEY = "datacenter.authorityId";
   public static final String RESKEY_KEY = "datacenter.resourceKey";
   
   /** used to format dates so that the registry can process them. eg 2005-11-04T15:34:22Z -
    * the date must be GMT */
   public final static SimpleDateFormat REGISTRY_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

   public static final String VORESOURCE_ELEMENT = "Resource";
   
   /** Constructs a miniumum set of core elements for a VOresource from the given
    * strings */
   public String makeCore(String title, String id, String publisher, String contactName, String contactEmail, String description, String refUrl, String type) {
   
      String core =
         "<title>"+title+"</title>"+
         "<identifier>"+id+"</identifier>"+
         "<curation>"+
           "<publisher>"+publisher+"</publisher>"+
           "<contact>"+
             "<name>"+contactName+"</name>"+
             "<email>"+contactEmail+"</email>"+
           "</contact>"+
         "</curation>"+
         "<content>"+
           "<description>"+description+"</description>"+
           "<referenceURL>"+refUrl+"</referenceURL>"+
           "<type>"+type+"</type>"+
         "</content>";
         
      return core;
   }

   /** Constructs  a VoResource opening tag with the given resource type */
   public String makeVoResourceElement(String vorType) {
      return "<"+VORESOURCE_ELEMENT+" status='active' updated='"+toRegistryForm(new Date())+"' xsi:type='"+vorType+"'>";
   }

   /** Constructs core VOResource elements with given string appended to the
    * identifier.  Looks at config file to see if a pre-prepared voresource core
    * file exists and uses that (incl validation) if so, and if not uses settings
    * in the configuration file to create it.
    */
   public String makeCore(String idEnd) throws IOException {
      String coreFile = SimpleConfig.getSingleton().getString("dataserver.metadata.core", null);
      if ( coreFile != null) {
         //use an on-disk resource file
         StringWriter sw = new StringWriter();
         FileReader reader = new FileReader(coreFile);
         Piper.pipe(reader, sw);
         try {
            DomHelper.newDocument(sw.toString());//validate
         }
         catch (SAXException e) {
            throw new MetadataException(e+" parsing Core Resource/Metadata document at "+coreFile,e);
         }
         return sw.toString();
      }
      else {
         return makeConfigCore(idEnd);
      }
   }

   /** Constructs core VOResource elements from settings in the configuration file */
   private String makeConfigCore(String idEnd) throws IOException {
      
         return makeCore(
            DataServer.getDatacenterName(),
            makeId(idEnd),
            SimpleConfig.getSingleton().getString("datacenter.publisher",null),
            SimpleConfig.getSingleton().getString("datacenter.contact.name", ""),
            SimpleConfig.getSingleton().getString("datacenter.contact.email", ""),
            //SimpleConfig.getSingleton().getString("datacenter.contact.date", ""));
            SimpleConfig.getSingleton().getString("data.description", ""),
            SimpleConfig.getSingleton().getString("datacenter.url", ""),
            "Other"
         );
   }
   
   /** Constructs an IVORN ID from an authority key and a resource key and the given extension */
   public String makeId(String idEnd) {
      return SimpleConfig.getSingleton().getString(AUTHID_KEY)+"/"+SimpleConfig.getSingleton().getString(RESKEY_KEY)+"/"+idEnd;
   }
   
   
   /** Converts date to string suitable for registry */
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
  
}








