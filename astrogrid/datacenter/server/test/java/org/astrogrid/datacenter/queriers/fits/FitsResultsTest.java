/*$Id: FitsResultsTest.java,v 1.1 2004/08/04 07:49:24 KevinBenson Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.fits;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.fits.FitsTest;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.TargetIndicator;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;

/** Test the Fits processing classes
 */
public class FitsResultsTest extends TestCase
{

   String []fileURLS = {"http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0500",
                        "http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0600"};
   FitsResults fr = new FitsResults(fileURLS);
   QuerierProcessingResults qpr;
   protected Querier querier;
   protected StringWriter sw;      
   
   protected void setUp() throws Exception{
      SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY, FitsQuerierPlugin.class.getName());
      sw = new StringWriter();
      querier = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), new TargetIndicator(sw), QueryResults.FORMAT_VOTABLE);
      qpr = new QuerierProcessingResults(querier);             
      
   }

   public void testToVOotable() throws IOException   
   {
      PrintWriter pw = new PrintWriter(System.out);
      fr.toVotable(pw, qpr);      
   }

   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(FitsResultsTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) throws IOException
   {
      org.astrogrid.log.Log.logToConsole();
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: FitsResultsTest.java,v $
 Revision 1.1  2004/08/04 07:49:24  KevinBenson
 small unit test on the fitsresult class

 Revision 1.15  2004/03/14 04:13:16  mch
 Wrapped output target in TargetIndicator

 Revision 1.14  2004/03/13 23:38:56  mch
 Test fixes and better front-end JSP access

 Revision 1.13  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.12  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.11  2004/03/09 21:05:57  mch
 Removed hugely long Fits tests

 Revision 1.10  2004/03/08 15:58:26  mch
 Fixes to ensure old ADQL interface works alongside new one and with old plugins

 Revision 1.9  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.8  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.7  2004/02/16 23:07:05  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.6  2004/01/23 11:14:09  nw
 altered to extend org.astrogrid.test.OptionalTestCase -
 means that these tests can be disabled as needed

 Revision 1.5  2004/01/14 16:00:13  nw
 tidied up switching out long tests.

 Revision 1.4  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.3.6.2  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.3.6.1  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.3  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.2  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.1  2003/11/28 20:00:24  mch
 Added cone search test


 */
