/*$Id: FitsTest.java,v 1.7 2004/01/13 00:33:14 nw Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.fits;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.queriers.fits.IndexGenerator;

/** Test the Fits processing classes
 */
public class FitsTest extends TestCase
{

   /** Tests reading the header = ie loading and parsing keywords, etc */
   public void testHeader() throws IOException
   {
      URL fits1 = getClass().getResource("iautestfits/tst0001.fits");
      
      assertNotNull(fits1);
      
      FitsReader reader = new FitsStreamReader(fits1);
      assertNotNull(reader);
      FitsHeader header = new FitsHeader();
      reader.readHeaderKeywords(header, null);
      assertNotNull(header.get("END"));
   }

   /** Tests generating indexes from three fits files.  This will test also
    * the full keyword/etc parsing */
   public void testIndexGenerator() throws IOException
   {
      URL[] fits = new URL[] {
            new URL("http://www.roe.ac.uk/~mch/r169411.fit"),
            new URL("http://www.roe.ac.uk/~mch/r169097.fit"),
            new URL("http://www.roe.ac.uk/~mch/r169101.fit")
     };
      fail("takes too long for a unit test");
      String index = IndexGenerator.generateIndex(fits);
      assertNotNull(index);
   }

   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(FitsTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) throws IOException
   {
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: FitsTest.java,v $
 Revision 1.7  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.6.10.2  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.6.10.1  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.6  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.5  2003/11/28 19:57:45  mch
 Cone Search now works

 Revision 1.4  2003/11/28 18:22:53  mch
 Added index generator tests

 Revision 1.3  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.2  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.1  2003/11/25 11:06:07  mch
 New FITS io package


 */
