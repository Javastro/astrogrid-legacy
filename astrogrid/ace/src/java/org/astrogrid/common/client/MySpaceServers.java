
/*
   $Id: MySpaceServers.java,v 1.1 2003/08/25 18:36:21 mch Exp $

   Date       Author      Changes
   Nov 02     M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.client;
import org.astrogrid.common.myspace.*;

import java.util.Hashtable;


import java.io.IOException;

/**
 * A list of the instances of myspace servers that already exist, including
 * instances of the MySpaceXxxx required to access them.
 *
 * @version %I%
 * @author M Hill
 */
   
public class MySpaceServers
{
   Hashtable servers = new Hashtable();
   
   public final static String LOCAL = "Local";
   public final static String ROE_GRENDEL = "ROE FTP";
   public final static String URL_READONLY = "Already public (Url Read only)";

   public MySpaceServers()
   {
      //'dummy' local
      servers.put(LOCAL,new MySpaceLocalClient());

      //cambridge
      //      servers.put("Cambridge Grid",new MySpaceGridFtpClient());

      //roe ordinary ftp
      MySpaceSunFtpClient roeFtp = new MySpaceSunFtpClient("grendel12.roe.ac.uk");
      roeFtp.setLogin("astrogrid","astrogrid");
      servers.put(ROE_GRENDEL,roeFtp);
      
      //generic read only (where image is already public)
      servers.put(URL_READONLY,new MySpaceUrlClient());
      
      //Richards GOODS copy
//      servers.put("Richards GOODS",new MySpaceUrlClient());
      
      
   }

   public String[] getServers()
   {
      return (String[]) servers.keySet().toArray(new String[] {});
   }

   public MySpaceClient getMySpaceClient(String server)
   {
      return (MySpaceClient) servers.get(server);
   }
}

