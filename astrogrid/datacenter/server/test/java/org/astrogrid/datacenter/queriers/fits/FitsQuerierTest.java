/*$Id: FitsQuerierTest.java,v 1.3 2003/12/03 19:37:03 mch Exp $
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.fits.IndexGenerator;

/** Test the Fits processing classes
 */
public class FitsQuerierTest extends TestCase
{
   

   public void testCone() throws IOException
   {
      FitsQuerier querier = new FitsQuerier("DummyId", null);

      setIndex(querier);
      
      querier.coneSearch(300,60,12);

   }

   public void testLots() throws IOException
   {
      org.astrogrid.log.Log.logToConsole();

      FitsQuerier querier = new FitsQuerier("test",null);
      setIndex(querier);

      org.astrogrid.log.Log.trace("Starting cone search...");
      String[] foundUrls = querier.coneSearch(308,60,12);
      
      org.astrogrid.log.Log.trace("Found "+foundUrls.length+":");
      
      for (int i=0;i<foundUrls.length;i++)
      {
         org.astrogrid.log.Log.trace(foundUrls[i].toString());
      }
   }

   public void setIndex(FitsQuerier querier) throws IOException
   {
      File indexFile = new File("fitsIndex.xml");
      if (!indexFile.exists())
      {
         org.astrogrid.log.Log.trace("Generating index...");
         //create index file
         String rawIndex = IndexGenerator.generateIndex(
            new URL[] {
               new URL("http://www.roe.ac.uk/~mch/r169411.fit"),
                  new URL("http://www.roe.ac.uk/~mch/r169097.fit"),
                  new URL("http://www.roe.ac.uk/~mch/r169101.fit")
            }
         );
         org.astrogrid.log.Log.trace(rawIndex);
         FileOutputStream out = new FileOutputStream(indexFile);
         out.write(rawIndex.getBytes());
      }
      
      querier.setIndex(new FileInputStream(indexFile));
   }

   /**
    *  Tests that it works OK as a plugin
    */
   public void testPlugin() throws DatabaseAccessException
   {
      SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY, FitsQuerier.class.getName());
      
      Querier querier = QuerierManager.createQuerier(null);
      
      assertTrue(querier instanceof FitsQuerier);
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
      org.astrogrid.log.Log.logToConsole();
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: FitsQuerierTest.java,v $
 Revision 1.3  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.2  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.1  2003/11/28 20:00:24  mch
 Added cone search test


 */
