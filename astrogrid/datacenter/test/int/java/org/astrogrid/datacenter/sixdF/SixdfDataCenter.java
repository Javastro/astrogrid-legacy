/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.datacenter.sixdF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.SocketServer;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
      //start logging
      Log.logToConsole();
      Log.logToFile("error.log");
      Log.starting();
      
      //tell it the metadata file can be found here where the class is
      Log.logInfo("Initialising...");
      URL metadataUrl = SixdfDataCenter.class.getResource("metadata.xml");
      
      //for some reason new URL(url.toString()) dosen't work with file:/; one of the slashes is lost
      String metadataLoc = metadataUrl.toExternalForm();
      if (metadataUrl.getProtocol().equals("file"))
      {
         metadataLoc = "file:/"+metadataUrl.getPath();
      }
      
      Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY, metadataLoc);
      
      //tell the queriers where to find the database connection
      Configuration.setProperty(SqlQuerier.JDBC_URL_KEY, "jdbc:microsoft:sqlserver://sql6df.roe.ac.uk:1433;Database=sixdf");
      Configuration.setProperty(SqlQuerier.USER_KEY, "dbreader");
      Configuration.setProperty(SqlQuerier.PASSWORD_KEY, "328purpleant");
      Configuration.setProperty(DatabaseQuerier.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.queriers.sql.SqlQuerier");
      
      
      Configuration.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, com.microsoft.jdbc.sqlserver.SQLServerDriver.class.getName());
      
   }
   
   /**
    * Runs a set of internal tests to make sure things are set up properly
    */
   public void runInternalTests() throws SAXException, ParserConfigurationException, IOException, QueryException, ADQLException
   {
      
      //run a query direct
      Log.logInfo("Running test query...");
      InputStream is = SixdfDataCenter.class.getResourceAsStream("query.xml");
      Document doc = XMLUtils.newDocument(is);
      
      DatabaseQuerier querier = DatabaseQuerier.createQuerier(doc.getDocumentElement());
      QueryResults results = querier.doQuery();
      XMLUtils.DocumentToStream(results.toVotable(), new FileOutputStream("testResults.vot"));
      Log.logInfo("...test Query complete");
      
      
   }
   
   /**
    * Starts the service
    */
   public void startServing() throws IOException
   {
      //spawn a thread to run the server
      Log.logInfo("Spawning Server...");
      Thread serverThread = new Thread(this);
      serverThread.start();
   }
   
   /**
    * Runs socket tests - creates a delegate and calls some standard
    * methods
    * @todo - call *all* methods
    */
   public void runExternalTests() throws MalformedURLException, ServiceException, IOException, ParserConfigurationException, SAXException
   {
      //create a delegate to talk to it
      Log.logInfo("Testing using delegate...");
      DatacenterDelegate delegate = DatacenterDelegate.makeDelegate("socket://localhost:"+SocketServer.DEFAULT_PORT);
      
      //get metadata
      Element voRegistry = delegate.getVoRegistryMetadata();
      
      //submit blocking query tests
      InputStream is = SixdfDataCenter.class.getResourceAsStream("query.xml");
      Document doc = XMLUtils.newDocument(is);
      delegate.doQuery(doc.getDocumentElement());
      
      Log.logInfo("Delegate tests complete...");
   }
   
   /**
    *
    */
   public static void main(String[] args)
   {
      try
      {
         SixdfDataCenter datacenter = new SixdfDataCenter();
         datacenter.runInternalTests();
         datacenter.startServing();
         datacenter.runExternalTests();
         Log.logInfo("...Successfully started");
      }
      catch (Exception e)
      {
         Log.logError("",e);
      }
   }
}

/*
 $Log: SixdfDataCenter.java,v $
 Revision 1.4  2003/09/23 18:10:06  mch
 Fixes and added tests

 Revision 1.3  2003/09/18 13:16:15  nw
 renamed delegate methods to match those in web service

 Revision 1.2  2003/09/17 14:53:02  nw
 tidied imports

 Revision 1.1  2003/09/11 17:44:53  mch
 New 6dF data service & integration test

 */
