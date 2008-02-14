/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.util.Vector;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;

// Can't import the VoDescriptionGenerator classes by name here,
// or the compiler gets confused (even if they are referred to
// by fully qualified name as below), so import by *
//import org.astrogrid.dataservice.metadata.v0_10.*;
import org.astrogrid.dataservice.metadata.v1_0.*;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
//import org.astrogrid.registry.client.DelegateProperties;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.astrogrid.slinger.ivo.IVORN;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;

/**
 * Assembles the various VoResource elements provided by the plugins, and
 * serves them all up wrapped in a VoDescription element for submitting to registries
 *
 * @see VoResourceSupport for how resource elements are generated
 * @see package documentation
 * <p>
 * @author M Hill
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 *
 */

public class VoDescriptionServer {

   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);

   // Versioned description generators
   //protected static org.astrogrid.dataservice.metadata.v0_10.VoDescriptionGenerator generator_v0_10 = new org.astrogrid.dataservice.metadata.v0_10.VoDescriptionGenerator();

   protected static org.astrogrid.dataservice.metadata.v1_0.VoDescriptionGenerator generator_v1_0 = new org.astrogrid.dataservice.metadata.v1_0.VoDescriptionGenerator();

   // Caches for document versions
   //protected static Document cache_v0_10 = null;
   protected static Document cache_v1_0 = null;

   //These are the VOResource versions that the DSA knows about.
   //public final static String V0_10        = "v0_10";   
   public final static String V1_0         = "v1_0";
   public final static String VERSIONS[] = { V1_0 };

   /**
    * Returns true if the specified version is supported by DSA, 
    * false otherwise.
    *
    */
   public static boolean isSupported(String version) 
   {
      /*
      if (V0_10.equals(version)) {
         return true;
      }
      */
      if (V1_0.equals(version)) {
         return true;
      }
      return false;
   }

   /**
    * Returns true if the specified version is supported by DSA AND 
    * enabled in the DSA configuration, false otherwise.
    *
    */
   public static boolean isEnabled(String version) 
   {
      /*
      if (V0_10.equals(version)) {
         // Check whether v0.10 resources are required
         String s = ConfigFactory.getCommonConfig().getString(
              "datacenter.resource.register.v0_10",null);
         if ( (s != null) && ( s.toLowerCase().equals("enabled") ) ) {
           return true; // v0.10 resources are enabled
         }
      }
      */
      if (V1_0.equals(version)) {
         // Check whether v0.10 resources are required
         String s = ConfigFactory.getCommonConfig().getString(
              "datacenter.resource.register.v1_0",null);
         if ( (s != null) && ( s.toLowerCase().equals("enabled") ) ) {
           return true; // v1.0 resources are enabled
         }
      }
      return false;
   }
   
   /**
    * Returns an appropriately-versioned VOResources document describing
    * the resources in this DSA.
    *
    */
   public synchronized static Document getVoDescription(String version) 
            throws IOException, MetadataException 
   {
      // Deal with v0.10 resources
      /*
      if (V0_10.equals(version)) {
        if (isEnabled(version)) {
          if (cache_v0_10 == null) {
            cache_v0_10 = generator_v0_10.getVoDescription();
          }
          return cache_v0_10;
        }
        else {
            throw new MetadataException("Support for v0.10 resources is disabled in DSA configuration file.");
        }
      } 
      */
      // Deal with v1.0 resources
      if (V1_0.equals(version)) {
        if (isEnabled(version)) {
          if (cache_v1_0 == null) {
            cache_v1_0 = generator_v1_0.getVoDescription();
          }
          return cache_v1_0;
        }
        else {
            throw new MetadataException("Support for v1.0 resources is disabled in DSA configuration file.");
        }
      }
      // Deal with unrecognized resources
      else {
         throw new MetadataException("Unknown resources version '" +
                version + "' requested.");
      }
   }

   /**
    * Clears the caches.
    */
   public synchronized static void clearCaches()
   {
      //cache_v0_10 = null;
      cache_v1_0 = null;
   }

   /**
    * Returns an appropriately-versioned VOResources document describing
    * the requested resource in this DSA.
    */
   public static Document getWrappedResource(String version, String resourceType) throws IOException, MetadataException 
   {
      // Deal with v0.10 resources
      /*
      if (V0_10.equals(version)) {
        if (isEnabled(version)) {
          return generator_v0_10.getWrappedResource(resourceType);
        }
        else {
            throw new MetadataException("Support for v0.10 resources is disabled in DSA configuration file.");
        }
      } 
      */
      // Deal with v1.0 resources
      if (V1_0.equals(version)) {
        if (isEnabled(version)) {
          return generator_v1_0.getWrappedResource(resourceType);
        }
        else {
            throw new MetadataException("Support for v1.0 resources is disabled in DSA configuration file.");
        }
      }
      // Deal with unrecognized resources
      else {
         throw new MetadataException("Unknown resources version '" +
                version + "' requested.");
      }
   }

   /**
    * Sends the voDescription to the given registry URL, returning list of Registries that
    * it was sent to
    */
   public static void pushToRegistry(String version, URL targetRegistry) 
         throws IOException, RegistryException {
      // Deal with v0.10 resources
   /*
      if (V0_10.equals(version)) {
        if (isEnabled(version)) {
          generator_v0_10.pushToRegistry(targetRegistry, "0.1");
        }
        else {
            throw new MetadataException("Support for v0.10 resources is disabled in DSA configuration file.");
        }
      } 
      */
      // Deal with v1.0 resources
      if (V1_0.equals(version)) {
        if (isEnabled(version)) {
          generator_v1_0.pushToRegistry(targetRegistry, "1.0");
        }
        else {
            throw new MetadataException("Support for v1.0 resources is disabled in DSA configuration file.");
        }
      }
      // Deal with unrecognized resources
      else {
         throw new MetadataException("Unknown resources version '" +
                version + "' requested.");
      }
   }
}
