/*$Id: IndexGeneratorTest.java,v 1.14 2005/05/27 16:21:08 clq2 Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.fits;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.astrogrid.config.SimpleConfig;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.datacenter.fits.FitsTestSupport;
import org.astrogrid.fitsserver.setup.IndexGenerator;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.fitsserver.fits.FitsQuerierPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.io.File;

/** Test the Fits processing classes
 */
public class IndexGeneratorTest extends TestCase
{
   
   /** Tests generating indexes from the test fits files.   */
   public void testGenerate() throws Exception
   {
      URL []genURLS = FitsTestSupport.getTestFits();
      System.out.println("length of urls = " + genURLS.length);
      ConfigFactory.getCommonConfig().setProperty("indexgen.path","." + File.separator + "target");
      IndexGenerator generator = new IndexGenerator();
      generator.raAxis = 1;
      generator.decAxis = 2;
      File indexDir = generator.generateIndex(genURLS);
      assertNotNull(indexDir);
      assertTrue(indexDir.isDirectory());
      assertEquals(FitsTestSupport.getTestFits().length,indexDir.list().length);
      //make sure they are xml documents.
      try {
         File fi = (indexDir.listFiles())[0];
         DomHelper.newDocument(fi);  //check its' a valid XML doc
      }
      catch (SAXException e) {
         fail("Generated index is not valid xml"+e);
      }
   }
   
   /** Tests generating indexes from the test fits files.   */
   public void testGenerateAndUpdate() throws Exception
   {
      URL []genURLS = FitsTestSupport.getTestFits();
      ConfigFactory.getCommonConfig().setProperty("upload.collection","IndexGenTest/CDSData");
      ConfigFactory.getCommonConfig().setProperty("test.bypass","yes");
      ConfigFactory.getCommonConfig().setProperty("xmldb.uri", "xmldb:exist://");
      ConfigFactory.getCommonConfig().setProperty("xmldb.driver", "org.exist.xmldb.DatabaseImpl");
      ConfigFactory.getCommonConfig().setProperty("xmldb.query.service", "XQueryService");
      ConfigFactory.getCommonConfig().setProperty("xmldb.admin.user", "admin");
      ConfigFactory.getCommonConfig().setProperty("xmldb.admin.password", "");
      ConfigFactory.getCommonConfig().setProperty("exist.config.file", "target/test-classes/exist.xml");

      System.out.println("length of urls = " + genURLS.length);
      ConfigFactory.getCommonConfig().setProperty("indexgen.path","." + File.separator + "target");
      IndexGenerator generator = new IndexGenerator();
      generator.raAxis = 1;
      generator.decAxis = 2;
      File indexDir = generator.generateIndex(genURLS);
      assertNotNull(indexDir);
      assertTrue(indexDir.isDirectory());
      assertEquals(FitsTestSupport.getTestFits().length,indexDir.list().length);
      //make sure they are xml documents.
      try {
         File fi = (indexDir.listFiles())[0];
         DomHelper.newDocument(fi);  //check its' a valid XML doc
      }
      catch (SAXException e) {
         fail("Generated index is not valid xml"+e);
      }
      generator.updateXMLDB(indexDir);
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, FitsQuerierPlugin.class.getName());
      //IndexFitsQuerierTest ifqt = new IndexFitsQuerierTest();
      //ifqt.testQuery()
      
   }

   /** Tests generating indexes from the test fits files.
   public void testToStream() throws Exception
   {
      DataOutputStream out = new DataOutputStream(new FileOutputStream("IndexGeneratorTestList.urls"));
      URL[] urls = FitsTestSupport.getTestFits();
      for (int i = 0; i < urls.length; i++) {
         out.writeChars(urls[i].toString()+"\n");
      }
      out.close();
      IndexGenerator generator = new IndexGenerator();
      generator.raAxis = 1;
      generator.decAxis = 2;
      FileWriter indexOut = new FileWriter("IndexGeneratorTestIndex.xml");
      generator.generateIndex(new FileInputStream("IndexGeneratorTestList.urls"));
      indexOut.close();
      try {
         DomHelper.newDocument(new FileInputStream("IndexGeneratorTestIndex.xml"));
      }
      catch (SAXException e) {
         fail("Generated index is not valid xml"+e);
      }
   }
*/
   
   protected Document askQueryFromFile(String queryFile) throws Exception {
       System.out.println("entered askQueryFromFile");

       assertNotNull(queryFile);
       InputStream is = this.getClass().getResourceAsStream(queryFile);
       
       assertNotNull("Could not open query file :" + queryFile,is);
       Document queryDoc = DomHelper.newDocument(is);
       
       //Document queryDoc = DomHelper.newDocument(new File(queryFile));
       return queryDoc;
   }



   
   
   // Reflection is used here to add all the testXXX() methods to the suite.
   public static Test suite()
   {
      return new TestSuite(IndexGeneratorTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[])
   {
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: IndexGeneratorTest.java,v $
 Revision 1.14  2005/05/27 16:21:08  clq2
 mchv_1

 Revision 1.13.14.1  2005/05/13 10:13:45  mch
 'some fixes'

 Revision 1.13  2005/03/22 22:47:29  KevinBenson
 changed back to commonconfig stuff.

 Revision 1.12  2005/03/22 13:28:48  mch
 Temporarily removed call to generator as it asks a question - stalls unit tests

 Revision 1.11  2005/03/22 10:05:23  KevinBenson
 new tests for indexgenerator

 Revision 1.9  2005/03/14 16:09:31  KevinBenson
 Fixed up some more tests for the IndexGenerator

 Revision 1.8  2005/03/13 11:43:44  KevinBenson
 small change for the indextenerator to create directories under target

 Revision 1.7  2005/03/11 16:19:58  KevinBenson
 new indexgenerator worked around.

 Revision 1.6  2005/03/11 14:50:59  KevinBenson
 added catch for parserconfigurationexception

 Revision 1.5  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.4  2005/03/10 13:59:00  KevinBenson
 corrections to fits and testing to fits

 Revision 1.3  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */
