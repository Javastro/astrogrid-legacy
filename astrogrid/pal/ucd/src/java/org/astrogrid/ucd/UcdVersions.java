/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.ucd;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.ucd.UcdException;

/**
 * Defines UCD version information.
 *
 * @author K Andrews
 */

public class UcdVersions  {
   
   protected static final String DEFAULT_UCD_VERSION = "1+";
   protected static final String[] ALLOWED_UCD_VERSIONS = { "1", "1+" };
   protected static final String CONFIG_PROPERTY = "datacenter.ucd.version";

   /** Returns the version of the UCD standard that should be used.*/
   public static String getUcdVersion() throws UcdException
   {
      String ucdVersion = DEFAULT_UCD_VERSION;
      try {
         ucdVersion = ConfigFactory.getCommonConfig().getString(
              CONFIG_PROPERTY);
      }
      catch (PropertyNotFoundException e) {
        // If property not set, just fall back to default version
      }
      if (ucdVersion == null || ucdVersion.equals("")) {
        // If property blank, just fall back to default version
          ucdVersion = DEFAULT_UCD_VERSION;
      }
      else {
         // Check we have a valid UCD version
         boolean found = false;
         for (int i = 0; i < ALLOWED_UCD_VERSIONS.length; i++) {
            if (ucdVersion.equals(ALLOWED_UCD_VERSIONS[i])) {
               found = true;
               break;
            }
         }
         if (!found) {
            throw new UcdException("Requested UCD version '" + ucdVersion +
               "' in property 'datacenter.ucd.version' is not recognised!");
         }
      }
      return ucdVersion;
   }
}
