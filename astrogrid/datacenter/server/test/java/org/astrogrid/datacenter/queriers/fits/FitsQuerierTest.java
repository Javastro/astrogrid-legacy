/*$Id: FitsQuerierTest.java,v 1.1 2003/11/28 20:00:24 mch Exp $
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
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.queriers.fits.IndexGenerator;

/** Test the Fits processing classes
 */
public class FitsQuerierTest extends TestCase
{
   

   public void testCone() throws IOException
   {
      FitsQuerier querier = new FitsQuerier();
      
      querier.coneSearch(300,60,12);

   }
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(FitsQuerierTest.class);
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
 $Log: FitsQuerierTest.java,v $
 Revision 1.1  2003/11/28 20:00:24  mch
 Added cone search test


 */
