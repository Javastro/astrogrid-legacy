/*$Id: RdbmsServerTest.java,v 1.1 2004/10/12 23:05:16 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration.serverside;

import java.io.StringWriter;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.integration.DatacenterTestCase;
import org.astrogrid.datacenter.integration.StdKeys;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.slinger.NullWriter;
import org.astrogrid.slinger.TargetIndicator;

/**
 * Test querying using ADQL against std PAL
 *
 */
public class RdbmsServerTest extends DatacenterTestCase implements StdKeys {

   DataServer server = new DataServer();
   
   public void setUp() {
      SampleStarsPlugin.initConfig();
   }
   
   /**
    * Run sample query on std PAL
    */
   public void testAdqlAsk() throws Throwable {

      StringWriter resultsWriter = new StringWriter();
      
      Query query = loadSampleQuery(RdbmsServerTest.class, "SimpleStarQuery-adql074.xml");
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);
      query.getResultsDef().setTarget(TargetIndicator.makeIndicator(resultsWriter));
      
      server.askQuery(Account.ANONYMOUS, query);

      assertVotable(resultsWriter.toString());
   }
   
   /**
    * Submits (asynchronous) sample query on std PAL
    */
   public void testAdqlSubmit() throws Throwable {

      Query query = loadSampleQuery(RdbmsServerTest.class, "SimpleStarQuery-adql074.xml");
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);
      query.getResultsDef().setTarget(TargetIndicator.makeIndicator(new NullWriter()));
      
      String queryId = server.submitQuery(Account.ANONYMOUS, query);

      assertNotNull(queryId);
   }

   /**
    * Runs a cone search expecting no results
    */
   public void testEmptyCone() throws Throwable {
      
      StringWriter resultsWriter = new StringWriter();
      
      Query query = SimpleQueryMaker.makeConeQuery(10, 10, 2);
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);
      query.getResultsDef().setTarget(TargetIndicator.makeIndicator(resultsWriter));

      server.askQuery(Account.ANONYMOUS, query);

      assertVotable(resultsWriter.toString());
   }
   
   /**
    * Runs a cone search
    */
   public void testCone() throws Throwable {
      StringWriter resultsWriter = new StringWriter();
      
      Query query = SimpleQueryMaker.makeConeQuery(30, 30, 6);
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);
      query.getResultsDef().setTarget(TargetIndicator.makeIndicator(resultsWriter));

      server.askQuery(Account.ANONYMOUS, query);

      assertVotable(resultsWriter.toString());
   }

   /**
    * Runs a cone search over the pole
    */
   public void testConeSPole() throws Throwable {
      StringWriter resultsWriter = new StringWriter();
      
      Query query = SimpleQueryMaker.makeConeQuery(30, -86, 6);
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);
      query.getResultsDef().setTarget(TargetIndicator.makeIndicator(resultsWriter));

      server.askQuery(Account.ANONYMOUS, query);

      assertVotable(resultsWriter.toString());
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(RdbmsServerTest.class));
   }
}


/*
$Log: RdbmsServerTest.java,v $
Revision 1.1  2004/10/12 23:05:16  mch
Seperated tests properly

 
*/
