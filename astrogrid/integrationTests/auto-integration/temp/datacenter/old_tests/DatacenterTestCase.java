/*$Id: DatacenterTestCase.java,v 1.2 2005/08/01 08:15:52 clq2 Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.io.Piper;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.astrogrid.datacenter.query.QueryException;

/**
 * methods for helping test datacenters
 *
 */
public class DatacenterTestCase extends TestCase {

   
   
   /**
    * Checks the given inputstream is aVOTable, returning it as a DOM
    */
   public Document assertVotable(String results) throws SAXException, IOException, ParserConfigurationException {
      assertNotNull(results);
      Document votDoc = DomHelper.newDocument(results);
      AstrogridAssert.assertVotable(votDoc);
      return votDoc;
   }
   
   /**
    * Checks the given inputstream is aVOTable, returning it as a DOM
    */
   public Document assertVotable(InputStream results) throws SAXException, IOException, ParserConfigurationException {
      assertNotNull(results);
      Document votDoc = DomHelper.newDocument(results);
      AstrogridAssert.assertVotable(votDoc);
      return votDoc;
   }
   /**
    * Run sample query on std PAL
    */
   public Query loadSampleQuery(Class testingClass, String filename) throws IOException, SAXException, ParserConfigurationException, QueryException  {
      //load query
      InputStream is = testingClass.getResourceAsStream(filename);
      assertNotNull(is);
      Query query = AdqlQueryMaker.makeQuery(is);
      return query;
   }

   /**
    * Checks the metadata is ok, returning it
    */
   public Document assertMetadata(InputStream is) throws SAXException, IOException, ParserConfigurationException  {
      Document metaDoc = DomHelper.newDocument(is);
      assertNotNull(metaDoc);
      return metaDoc;
   }
   
   
}


/*
$Log: DatacenterTestCase.java,v $
Revision 1.2  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.1.2.1  2005/07/12 11:21:06  KevinBenson
old datacenter moved out of the test area

Revision 1.5  2004/10/12 23:05:16  mch
Seperated tests properly

Revision 1.4  2004/10/08 15:59:22  mch
made stdkey strings non-static

Revision 1.3  2004/10/06 22:03:45  mch
Following Query model changes in PAL

Revision 1.2  2004/09/09 11:18:45  mch
Moved DeployedServicesTest to separate package

Revision 1.1  2004/09/08 13:58:48  mch
Separated out tests by datacenter and added some

Revision 1.7  2004/09/02 12:33:49  mch
Added better tests and reporting

Revision 1.6  2004/09/02 01:33:48  nw
added asssertions that valid VOTables are returned.

Revision 1.5  2004/08/30 22:11:46  KevinBenson
added a little more for a vizier test

Revision 1.4  2004/08/03 13:41:29  KevinBenson
result of a merge with Itn06_case3 to change to using registry-client-lite and add some more int-test for fits and sec datacenter

Revision 1.3.42.2  2004/07/30 13:04:41  KevinBenson
changed hardcoded urls to the appropriate endpoint.  Also added in the fits stuff

Revision 1.3.42.1  2004/07/30 12:44:45  KevinBenson
Added the Fits int test to it

Revision 1.3  2004/05/13 12:25:04  mch
Fixes to create user, and switched to mostly testing it05 interface

Revision 1.2  2004/04/26 12:16:25  nw
fixed static suite() method

Revision 1.1  2004/04/26 09:05:10  mch
Added adql test

Revision 1.3  2004/04/16 15:55:08  mch
added alltests

Revision 1.2  2004/04/16 15:17:14  mch
Copied in from integration tests

Revision 1.1  2004/04/16 15:14:54  mch
Auto cone test

Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.2  2004/02/17 23:59:17  jdt
commented out lines killing the build, and made to conform to

coding stds

Revision 1.1  2004/01/23 09:08:09  nw
added cone searcher test too
 
*/
