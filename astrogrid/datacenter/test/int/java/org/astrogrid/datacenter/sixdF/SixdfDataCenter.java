/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.sixdF;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.SocketServer;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A socket server implementation of an AstroGrid datacenter, serving
 * 6dF data.
 *
 * @author M Hill
 */

public class SixdfDataCenter extends SocketServer
{
   public SixdfDataCenter() throws IOException
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
         URL metadataUrl = SixdfDataCenter.class.getResource("metadata.xml");
         Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY, metadataUrl.toString());
         
         //tell the queriers where to find the database connection
         Configuration.setProperty(SqlQuerier.JDBC_URL_KEY, "jdbc:microsoft:sqlserver://sql6df.roe.ac.uk:1433;Database=sixdf");
         Configuration.setProperty(SqlQuerier.USER_KEY, "dbreader");
         Configuration.setProperty(SqlQuerier.PASSWORD_KEY, "328purpleant");
         Configuration.setProperty(DatabaseQuerier.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.queriers.sql.SqlQuerier");
         
         
         Configuration.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, com.microsoft.jdbc.sqlserver.SQLServerDriver.class.getName());

         
         //spawn a thread to run the server
         SixdfDataCenter dataserver = new SixdfDataCenter();
         Thread serverThread = new Thread(dataserver);
         serverThread.start();
         
         //run a query direct
         InputStream is = SixdfDataCenter.class.getResourceAsStream("query.xml");
         Document doc = XMLUtils.newDocument(is);
         
         DatabaseQuerier querier = DatabaseQuerier.doQueryGetResults(doc.getDocumentElement());
         
         
         
         //create a delegate to talk to it
         DatacenterDelegate delegate = DatacenterDelegate.makeDelegate("socket://localhost:"+SocketServer.DEFAULT_PORT);
         
         //basic tests
         Element voRegistry = delegate.getRegistryMetadata();
      }
      catch (Exception e)
      {
         Log.logError("",e);
      }
   }
}

/*
 $Log: SixdfDataCenter.java,v $
 Revision 1.2  2003/09/17 14:53:02  nw
 tidied imports

 Revision 1.1  2003/09/11 17:44:53  mch
 New 6dF data service & integration test

 */
