/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.mch;

import java.io.InputStream;
import java.io.IOException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.service.SocketServer;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;

/**
 * A socket server implementation of an AstroGrid datacenter, serving
 * mySql data on my home machine...
 *
 * @author M Hill
 */

public class HomeClient
{
   public HomeClient() throws IOException
   {

   }


   /**
    *
    */
   public static void main(String[] args)
   {
      try
      {

         Log.logToConsole();
         Log.logToFile("error.log");
         Log.starting();

         //create a delegate to talk to it
         DatacenterDelegate delegate = DatacenterDelegate.makeDelegate("socket://kubwa:"+SocketServer.DEFAULT_PORT);

//         Log.trace("Pausing...");
//         Thread.currentThread().sleep(5000);

         //basic tests
         Element voRegistry = delegate.getRegistryMetadata();

         //query test
         InputStream in = HomeClient.class.getResourceAsStream("adqlQuery.xml");
         Element results = delegate.query(XMLUtils.newDocument(in).getDocumentElement());
          /**/
      }
      catch (Exception e)
      {
         Log.logError("",e);
      }
   }
}

/*
 $Log: HomeClient.java,v $
 Revision 1.2  2003/09/15 23:01:06  mch
 fix for delegate method rename

 Revision 1.1  2003/09/14 19:49:46  mch
 Tester for home environment


 */
