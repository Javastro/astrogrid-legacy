/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.mch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.SocketServer;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
         Log.starting();

         //tell it the metadata file can be found here where the class is
         //URL metadataUrl = HomeDataCenter.class.getResource("metadata.xml");
//         Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY, metadataUrl.toString());
         Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY,
               "C:\\astrogrid\\datacenter\\class\\org\\astrogrid\\datacenter\\mch\\metadata.xml");

         //tell the queriers where to find the database connection
         Configuration.setProperty(SqlQuerier.JDBC_URL_KEY, "jdbc:mysql://localhost:3306/Catalogue");
         //Configuration.setProperty(SqlQuerier.USER_KEY, "dbreader");
         //Configuration.setProperty(SqlQuerier.PASSWORD_KEY, "328purpleant");
         Configuration.setProperty(DatabaseQuerier.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.queriers.sql.SqlQuerier");

         Configuration.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, org.gjt.mm.mysql.Driver.class.getName());

         SqlQuerier.startDrivers();

         //spawn a thread to run the server
         HomeDataCenter dataserver = new HomeDataCenter();
         Thread serverThread = new Thread(dataserver);
         serverThread.start();

         //run a query direct
         Log.trace("Running query directly...");
         InputStream is = HomeDataCenter.class.getResourceAsStream("adqlQuery.xml");
         Document doc = XMLUtils.newDocument(is);

         DatabaseQuerier querier = DatabaseQuerier.doQueryGetResults(doc.getDocumentElement());

      }
      catch (Exception e)
      {
        // Log.logError("",e);
      }
   }
}

/*
 $Log: HomeDataCenter.java,v $
 Revision 1.1  2003/09/15 11:46:18  mch
 MySql mchs home tester


 */
