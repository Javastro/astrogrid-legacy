/*
 * $Id: VoResourceSupport.java,v 1.1 2005/03/08 18:05:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.v0_10;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.service.DataServer;

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

   public String makeCore(String title, String id, String publisher, String contactName, String contactEmail, String description, String refUrl, String type) {
   
      String core =
         "<title>"+title+"</title>"+
         "<identifier>"+id+"<identifier>"+
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
           "<type>"+type+"</publisher>"+
         "</content>";
         
      return core;
   }

   public String makeVoResourceElement(String xsiType) {
      return "<vor:Resource status='active' updated='"+toRegistryForm(new Date())+"' xsi:type='"+xsiType+"'>";
   }
   

   /** Constructs core VOResource elements from settings in the configuration file */
   public String makeConfigCore(String idEnd) {
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
      return SimpleConfig.getSingleton().getString(AUTHID_KEY)+SimpleConfig.getSingleton().getString(RESKEY_KEY)+idEnd;
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








