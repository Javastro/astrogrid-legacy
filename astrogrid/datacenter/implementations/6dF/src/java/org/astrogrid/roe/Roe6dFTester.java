/*
 * $Id: Roe6dFTester.java,v 1.2 2004/08/19 19:51:41 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.roe;


/**
 * A class used to connect to the 6dF database for test purposes.
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.TargetIndicator;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.JdbcConnections;
import org.astrogrid.datacenter.queriers.sql.JdbcPlugin;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

public class Roe6dFTester extends TestCase
{

   public void setUp() throws IOException {
      URL propertyFile =this.getClass().getResource("6dF.properties");
      assertNotNull("Could not open 6dF properties file", propertyFile);
      SimpleConfig.load(propertyFile);
      
      JdbcConnections.startDrivers();
   }

   public void testConnection() throws DatabaseAccessException, SQLException {
      //connect
      Connection connection = JdbcConnections.makeFromConfig().createConnection();
   }
   
   public void testCone() throws Exception {
      QuerierManager manager = new QuerierManager("6dFTest");
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), new TargetIndicator(sw), QueryResults.FORMAT_VOTABLE);
      manager.askQuerier(q);
      Document results = DomHelper.newDocument(sw.toString());
   }
   

    public void testAutoMetadata() throws Exception {
       QuerierPlugin plugin = new JdbcPlugin(null);

       //generate metadata
       Document metadata = plugin.getMetadata();
       
       //debug
       DomHelper.DocumentToStream(metadata, System.out);
       
    }

    /** Test harness - runs tests
   */
   public static void main(String[] args) {
      junit.textui.TestRunner.run(Roe6dFTester.class);
   }
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(Roe6dFTester.class);
   }
   
   
}
   /*
   $Log: Roe6dFTester.java,v $
   Revision 1.2  2004/08/19 19:51:41  mch
   Updated 6df config

   Revision 1.1  2004/07/06 17:14:41  mch
   Added direct client test

    */
