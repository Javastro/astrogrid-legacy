/*$Id: IndexGeneratorTest.java,v 1.1 2005/02/17 18:37:35 mch Exp $
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
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.fits.FitsTest;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/** Test the Fits processing classes
 */
public class IndexGeneratorTest extends TestCase
{
   
   /** Tests generating indexes from the test fits files.   */
   public void testInMemory() throws Exception
   {
      IndexGenerator generator = new IndexGenerator();
      generator.raAxis = 1;
      generator.decAxis = 2;
      String index = generator.generateIndex(FitsTest.getTestFits());
      assertNotNull(index);
      try {
         DomHelper.newDocument(index);  //check its' a valid XML doc
      }
      catch (SAXException e) {
         fail("Generated index is not valid xml"+e);
      }
   }

   /** Tests generating indexes from the test fits files.   */
   public void testToStream() throws Exception
   {
      DataOutputStream out = new DataOutputStream(new FileOutputStream("IndexGeneratorTestList.urls"));
      URL[] urls = FitsTest.getTestFits();
      for (int i = 0; i < urls.length; i++) {
         out.writeChars(urls[i].toString()+"\n");
      }
      out.close();
      IndexGenerator generator = new IndexGenerator();
      generator.raAxis = 1;
      generator.decAxis = 2;
      FileWriter indexOut = new FileWriter("IndexGeneratorTestIndex.xml");
      generator.generateIndex(new FileInputStream("IndexGeneratorTestList.urls"), indexOut);
      indexOut.close();
      try {
         DomHelper.newDocument(new FileInputStream("IndexGeneratorTestIndex.xml"));
      }
      catch (SAXException e) {
         fail("Generated index is not valid xml"+e);
      }
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
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */
