/*$Id: FitsResultsTest.java,v 1.7 2005/11/21 12:54:18 clq2 Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.fits;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.UrlListResults;
import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;

/** Test the Fits processing classes
 */
public class FitsResultsTest extends TestCase
{

   String [] exampleUrls = {"http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0500",
                            "http://msslxy.mssl.ucl.ac.uk:8080/TraceFits/ObtainFITS?_file=trace4a/tri/week20020728/tri20020728.0600"};

   UrlListResults fixedResults;
   Querier testQuerier = null;
                            
   public void setUp() throws IOException {
      SampleStarsPlugin.initConfig(); //this isn't the right plugin for fits results, but it keeps the Querier bit happy
                               
       testQuerier = Querier.makeQuerier(LoginAccount.ANONYMOUS, SimpleQueryMaker.makeConeQuery(30, 40, 6), this);
       testQuerier.setStatus(new QuerierProcessingResults(testQuerier.getStatus()));
      fixedResults = new UrlListResults(testQuerier, exampleUrls);
   }

   public void testToVotable() throws IOException, SAXException, ParserConfigurationException
   {
      StringWriter sw = new StringWriter();
      fixedResults.sendTable(new ReturnTable(new WriterTarget(sw), MimeTypes.VOTABLE), LoginAccount.ANONYMOUS);
      
      // For viewing output VOTable in testing 
      //System.out.println("===================================");
      //System.out.println(sw.toString());
      //System.out.println("===================================");
      //
      //check results
      DomHelper.newDocument(sw.toString());
      
      //doesn't compile on mch's PC AstrogridAssert.assertVotable(sw.toString());
   }

   /*
   public void testToHtml() throws IOException, SAXException, ParserConfigurationException
   {
      StringWriter sw = new StringWriter();
      fixedResults.writeHtml(TargetMaker.makeTarget(sw), (QuerierProcessingResults) testQuerier.getStatus());
      
      //check results
      DomHelper.newDocument(sw.toString());
   }

   public void testToCSV() throws IOException
   {
      StringWriter sw = new StringWriter();
      fixedResults.writeCSV(TargetMaker.makeTarget(sw), (QuerierProcessingResults) testQuerier.getStatus());
   }
    */
   
   // Reflection is used here to add all the testXXX() methods to the suite.
   public static Test suite()
   {
      return new TestSuite(FitsResultsTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[])    {
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: FitsResultsTest.java,v $
 Revision 1.7  2005/11/21 12:54:18  clq2
 DSA_KEA_1451

 Revision 1.6.38.1  2005/11/15 15:42:57  kea
 Debugging code added.

 Revision 1.6  2005/05/27 16:21:07  clq2
 mchv_1

 Revision 1.5.10.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.5.10.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.5  2005/03/31 09:43:09  mch
 Some fixes

 Revision 1.4  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.3  2005/03/10 20:19:21  mch
 Fixed tests more metadata fixes

 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.4.2.3  2004/11/23 11:55:06  mch
 renamved makeTarget methods

 Revision 1.4.2.2  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.4.2.1  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.4  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/08 17:14:23  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.7  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.6  2004/09/02 11:21:31  mch
 Removed AstrogridAssert

 Revision 1.5  2004/09/02 10:25:43  mch
 Added tests

 Revision 1.4  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.3  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.2  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

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
