/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.mch;

import java.io.IOException;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.SocketServer;
import org.astrogrid.log.Log;

/**
 * A socket server implementation of an AstroGrid datacenter, serving
 * mySql data on my home machine...
 *
 * @author M Hill
 */

public class HomeDataCenter extends SocketServer
{
   public HomeDataCenter() throws IOException
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
         Log.starting("HomeDatacenter");

         //tell it the metadata file can be found here where the class is
         //URL metadataUrl = HomeDataCenter.class.getResource("metadata.xml");
//         Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY, metadataUrl.toString());
         SimpleConfig.setProperty(ServiceServer.METADATA_FILE_LOC_KEY,
               "C:\\astrogrid\\class\\org\\astrogrid\\datacenter\\mch\\metadata.xml");

         //tell the queriers where to find the database connection
         SimpleConfig.setProperty(SqlQuerier.JDBC_URL_KEY, "jdbc:mysql://localhost:3306/Catalogue");
         //Configuration.setProperty(SqlQuerier.USER_KEY, "dbreader");
         //Configuration.setProperty(SqlQuerier.PASSWORD_KEY, "328purpleant");
         SimpleConfig.setProperty(DatabaseQuerierManager.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.queriers.sql.SqlQuerier");

         SimpleConfig.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, org.gjt.mm.mysql.Driver.class.getName());

         SqlQuerier.startDrivers();

         //spawn a thread to run the server
         HomeDataCenter dataserver = new HomeDataCenter();
         Thread serverThread = new Thread(dataserver);
         serverThread.start();

         //run a query direct
//         Log.trace("Running query directly...");
//         InputStream is = HomeDataCenter.class.getResourceAsStream("adqlQuery.xml");
//         Document doc = XMLUtils.newDocument(is);

//         DatabaseQuerier querier = DatabaseQuerier.createQuerier(doc.getDocumentElement());
 //        querier.doQuery();

      }
      catch (Exception e)
      {
        // Log.logError("",e);
      }
   }
}

/*
 $Log: HomeDataCenter.java,v $
 Revision 1.1  2003/11/18 11:59:22  nw
 mavenized mch

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.5  2003/11/05 18:54:52  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.4  2003/09/24 21:04:52  nw
 altered to use DatabaseQuerierManager

 Revision 1.3  2003/09/17 14:53:02  nw
 tidied imports

 Revision 1.2  2003/09/17 06:55:44  mch
 Changed metadata loc

 Revision 1.1  2003/09/15 11:46:18  mch
 MySql mchs home tester


 */
