/*
   AceServers.java

   Date       Author      Changes
   $date$     M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.client;

import java.util.Hashtable;

/**
 * AceServers...
 *
 * @version %I%
 * @author M Hill
 */
   
public class AceServers
{
   Hashtable servers = new Hashtable();

   public static String LOCAL = "Local";
   public static String INSTALLED = "<Installed>";   //calls directly

   public AceServers()
   {
         //create (softwired) server lookups
      servers.put(INSTALLED,"");
      servers.put(LOCAL,"localhost/");
      servers.put("Cambridge","Somewhere");
      servers.put("ROE","grendel12.roe.ac.uk");
   }

   public String getUrl(String server)
   {
      return (String) servers.get(server);
   }

   public String[] getServers()
   {
      return (String[]) servers.keySet().toArray(new String[] {});
   }
   /**
    * Returns true if the Ace Server is local - ie the image does not need
    * to be publicised
    */
   public boolean isLocal(String server)
   {
      return (server.equals(INSTALLED) || server.equals(LOCAL));
   }

   /**
    * Returns true if the Ace Server to be used is an installed version that
    * we invoke directly rather than as a service
    */
   public boolean isInstalled(String server)
   {
      return (server.equals(INSTALLED));
   }

}

