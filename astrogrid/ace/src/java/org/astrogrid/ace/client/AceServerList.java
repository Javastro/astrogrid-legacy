/*
   $Id: AceServerList.java,v 1.1 2003/08/25 18:36:04 mch Exp $

   Date       Author      Changes
   $date$     M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.client;

import java.util.Properties;
import java.io.*;

import org.astrogrid.log.Log;
/**
 * A singleton listing all the ace servers available from this client.  Used
 * for drop=down boxes etc
 *
 * @version %I%
 * @author M Hill
 */
   
public class AceServerList
{
//   private static Hashtable servers = new Hashtable();

   private static Properties properties = new Properties();
   
   public static String LOCAL = "Local";
   public static String INSTALLED = "<Installed>";   //calls directly

   public static String FILENAME = "serverlist.cfg";
   
   private static AceServerList instance = new AceServerList();
   
   private AceServerList()
   {
      try
      {
         load();
      }
      catch (IOException ioe)
      {
         //couldn't load - eg doesn't exist.  Set defaults
         setSoftwiredDefaults();
      }
      
   }
   
   public static void load() throws IOException
   {
      properties.clear();
      properties.load(new FileInputStream(new File(FILENAME)));
   }
   
   public static void setSoftwiredDefaults()
   {
      //create 'standand' server lookups
      properties.setProperty(INSTALLED,"");
      //properties.setProperty(LOCAL,"http://localhost:8080/something");

      //create (softwired) server lookups
      properties.setProperty("Cambridge","http://astrogrid.ast.cam.ac.uk:8080/axis/services/AceService");
      //properties.setProperty("ROE","http://grendel12.roe.ac.uk:8080/something");
   }

   /**
    * Returns the URL of the given server
    */
   public static String getUrl(String server)
   {
      return (String) properties.getProperty(server);
   }

   /**
    * Stores the list
    */
   public static void store()
   {
      try
      {
         properties.store(new FileOutputStream(FILENAME), "List of ACE Servers");
      }
      catch (IOException ioe)
      {
         Log.logError("Could not store server list",ioe);
      }
   }
   
   /**
    * Returns a list of the servers
    */
   public static String[] getServers()
   {
      return (String[]) properties.keySet().toArray(new String[] {});
   }
   /**
    * Returns true if the Ace Server is local - ie the image does not need
    * to be publicised
    */
   public static boolean isLocal(String server)
   {
      return (server.equals(INSTALLED) || server.equals(LOCAL));
   }

   /**
    * Returns true if the Ace Server to be used is an installed version that
    * we invoke directly rather than as a service
    */
   public static boolean isInstalled(String server)
   {
      return (server.equals(INSTALLED));
   }

}

