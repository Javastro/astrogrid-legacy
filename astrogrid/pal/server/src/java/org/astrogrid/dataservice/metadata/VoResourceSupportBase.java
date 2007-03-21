/*
 * $Id: VoResourceSupportBase.java,v 1.1 2007/03/21 18:54:04 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Helper methods for constructing basic VoResource documents
 *
 * @author M Hill, K Andrews
 */

public class VoResourceSupportBase {
   protected static Log log = LogFactory.getLog(VoResourceSupportBase.class);
   
   public static final String AUTHID_KEY = "datacenter.authorityId";
   public static final String RESKEY_KEY = "datacenter.resourceKey";
   
   /** used to format dates so that the registry can process them. eg 2005-11-04T15:34:22Z -
    * the date must be GMT */
   public final static SimpleDateFormat REGISTRY_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


   /** Constructs a minimum set of core elements for a VOresource from 
    * the given strings 
    */
   public String makeCore(String title, String id, String publisher, String contactName, String contactEmail, String creatorName, String creatorLogo, String description, String rootUrl, String refUrl, String type) {
   
      String core =
         "\n<title>"+title+"</title>\n"+
         "<identifier>"+id+"</identifier>\n"+
         "<curation>\n"+
           "<publisher>"+publisher+"</publisher>\n"+
           "<creator>\n"+
           "<name>"+creatorName+"</name>\n" +
           "<logo>"+creatorLogo+"</logo>\n" +
           "</creator>\n"+
           "<contact>\n"+
             "<name>"+contactName+"</name>\n"+
             "<email>"+contactEmail+"</email>\n"+
           "</contact>\n"+
         "</curation>\n"+
         "<content>\n"+
           "<description>"+description+"</description>\n"+
           "<referenceURL>"+refUrl+"</referenceURL>\n"+
           "<type>"+type+"</type>\n"+
         "</content>\n";
         
      return core;
   }

   /** Constructs core VOResource elements with given string appended to the
    * identifier.  Looks at config file to see if a pre-prepared voresource core
    * file exists and uses that (incl validation) if so, and if not uses settings
    * in the configuration file to create it.
    */
   public String makeCore(String idEnd) throws IOException {
      String coreFile = ConfigFactory.getCommonConfig().getString("dataserver.metadata.core", null);
      if ( coreFile != null) {
         //use an on-disk resource file
         //schemaLocations + 
         try {
            Document coreDoc = DomHelper.newDocument(ConfigReader.resolveFilename(coreFile));//validate & load

            //set ID
            NodeList idNodes = coreDoc.getElementsByTagName("identifier");
            if (idNodes.getLength() != 1) {
               throw new MetadataException("Should be exactly one identifier node in core resource document at "+coreFile);
            }
            Element idNode = (Element) idNodes.item(0);
            DomHelper.setElementValue(idNode, DomHelper.getValueOf(idNode)+idEnd);
            
            return DomHelper.DocumentToString(coreDoc);
         }
         catch (SAXException e) {
            throw new MetadataException(e+" parsing Core Resource/Metadata document at "+coreFile,e);
         }

      }
      else {
         return makeConfigCore(idEnd);
      }
   }

   /** Constructs core VOResource elements from settings in the configuration file */
   private String makeConfigCore(String idEnd) throws IOException {
      
         String refUrl = 
           ConfigFactory.getCommonConfig().getString("datacenter.reference.url", "");
         if ((refUrl == null) || ("".equals(refUrl))) { 
           refUrl = ConfigFactory.getCommonConfig().getString("datacenter.url", "");
         }
         String creatorLogo = 
           ConfigFactory.getCommonConfig().getString("datacenter.data.creator.logo", "");
         if ((creatorLogo == null) || ("".equals(creatorLogo))) { 
           creatorLogo = ConfigFactory.getCommonConfig().getString(
               "datacenter.url", "")+"/logo.gif";
         }
         return makeCore(
            DataServer.getDatacenterName(),
            makeId(idEnd),
            ConfigFactory.getCommonConfig().getString("datacenter.publisher",null),
            ConfigFactory.getCommonConfig().getString("datacenter.contact.name", ""),
            ConfigFactory.getCommonConfig().getString("datacenter.contact.email", ""),
            ConfigFactory.getCommonConfig().getString("datacenter.data.creator.name", ""),
            creatorLogo,
            ConfigFactory.getCommonConfig().getString("datacenter.description", ""),
            ConfigFactory.getCommonConfig().getString("datacenter.url", ""),
            refUrl,
            "Catalog"
         );
   }
   
   /** Constructs an IVORN ID from an authority key and a resource key and the given extension */
   public String makeId(String idEnd) throws IOException {
      // KEA: Need to check for rogue "/"
      String authID = ConfigFactory.getCommonConfig().getString(AUTHID_KEY);
      String resKey = ConfigFactory.getCommonConfig().getString(RESKEY_KEY);
      if ( (authID == null) || ("".equals(authID)) ) {
        throw new IOException("Expected configuration key " + AUTHID_KEY +
              " is not set, please check your configuration.");
      }
      if ( (resKey == null) || ("".equals(resKey)) ) {
        throw new IOException("Expected configuration key " + RESKEY_KEY +
              " is not set, please check your configuration.");
      }
      // The checking for '/' below is probably unnecessary, but best
      // to be sure.
      String ivornID = "ivo://" + authID;
      if (authID.charAt(authID.length()-1) != '/') {
        ivornID = ivornID + "/"; // Append a '/' if not present
      }
      ivornID = ivornID + resKey;
      if (resKey.charAt(resKey.length()-1) != '/') {
        ivornID = ivornID + "/"; // Append a '/' if not present
      }
      ivornID = ivornID + idEnd;
      return ivornID;
   }

   /** Constructs an IVORN ID for the corresponding Authority ID registration
    * from an authority key */
   public static String makeAuthorityId() throws IOException {
      // KEA: Need to check for rogue "/"
      String authID = ConfigFactory.getCommonConfig().getString(AUTHID_KEY);
      if ( (authID == null) || ("".equals(authID)) ) {
        throw new IOException("Expected configuration key " + AUTHID_KEY +
              " is not set, please check your configuration.");
      }
      // The checking for '/' below is probably unnecessary, but best
      // to be sure.
      String ivornID = "ivo://" + authID;
      if (authID.charAt(authID.length()-1) != '/') {
        ivornID = ivornID + "/"; // Append a '/' if not present
      }
      ivornID = ivornID + "authority";
      return ivornID;
   }
   
   
   /** Converts date to string suitable for registry */
   public String toRegistryForm(Date givenDate) {
/*
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
 */
      //
      // Copied from registry code (probably won't work outside the UK).
      /*
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      return sdf.format(
          new Date()
          );
     */

      // Adjusted by KEA just to report date (not time) - see
      // www.astrogrid.org/bugzilla ticket 1920.
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.format(
          new Date()
      );
    }
}

