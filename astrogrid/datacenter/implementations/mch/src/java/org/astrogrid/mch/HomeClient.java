/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.mch;

import java.io.IOException;
import java.io.InputStream;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.Metadata;
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
         Log.starting("HomeClient");

         //create a delegate to talk to it
         AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier("socket://kubwa:"+SocketServer.DEFAULT_PORT);

//         Log.trace("Pausing...");
//         Thread.currentThread().sleep(5000);

         //basic tests
         Metadata metadata = delegate.getMetadata();

         //query test
         InputStream in = HomeClient.class.getResourceAsStream("adqlQuery.xml");
         Element adqlDoc = XMLUtils.newDocument(in).getDocumentElement();

         Select adql = ADQLUtils.unmarshalSelect(adqlDoc);
         
         DatacenterResults results = delegate.doQuery(AdqlQuerier.VOTABLE, adql);
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
 Revision 1.1  2003/11/18 11:59:22  nw
 mavenized mch

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.5  2003/11/05 18:54:52  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.4  2003/09/18 13:15:23  nw
 renamed delegate methods to match those in web service

 Revision 1.3  2003/09/17 14:53:02  nw
 tidied imports

 Revision 1.2  2003/09/15 23:01:06  mch
 fix for delegate method rename

 Revision 1.1  2003/09/14 19:49:46  mch
 Tester for home environment


 */
