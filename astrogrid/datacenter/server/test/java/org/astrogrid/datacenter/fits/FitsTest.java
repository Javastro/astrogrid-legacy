/*$Id: FitsTest.java,v 1.2 2003/11/27 17:28:09 nw Exp $
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

/** Test the Fits processing classes
 */
public class FitsTest extends TestCase
{
   
   public void testHeader() throws IOException
   {
      URL fits1 = getClass().getResource("iautestfits/tst0001.fits");
      
      assertNotNull(fits1);
      
      FitsReader reader = new FitsReader(fits1);
      assertNotNull(reader);
      FitsHeader header = reader.readHeader();
      assertNotNull(header);
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
 Revision 1.2  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.1  2003/11/25 11:06:07  mch
 New FITS io package


 */
