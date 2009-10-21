/*
 * $Id: InstallationIvorn.java,v 1.2 2009/10/21 19:00:59 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;

/**
 * An IVORN for some part of a deployed-and-configured DSA installation.
 * The resource key and authority ID are taken from the configuration.
 *
 * @author M Hill, K Andrews, Guy Rixon
 */

public class InstallationIvorn {

  /**
   * The name of the property defining the publishing authority.
   */
  public static final String AUTHID_KEY = "datacenter.authorityId";

  /**
   * The name of the property defining the tresource key.
   */
  public static final String RESKEY_KEY = "datacenter.resourceKey"; 
   
  /**
   * Constructs an IVORN from an authority key and a resource key
   * and the given suffix. Ensures that the resource key and the
   * suffix are separated by a single slash.
   *
   * @param idEnd A suffix to come after the resource key.
   */
   public static String makeIvorn(String givenSuffix) throws IOException {
     String suffix =
         (givenSuffix.startsWith("/"))? givenSuffix.substring(1) : givenSuffix;

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

     String ivornID = "ivo://" + authID;
     if (authID.charAt(authID.length()-1) != '/') {
       ivornID = ivornID + "/"; // Append a '/' if not present
     }
     ivornID = ivornID + resKey;
     if ((suffix != null) && (!"".equals(suffix))) {
       if (resKey.charAt(resKey.length()-1) != '/') {
         ivornID = ivornID + "/"; // Append a '/' if not present
       }
       ivornID = ivornID + suffix;
     }
     return ivornID;
   }

}
