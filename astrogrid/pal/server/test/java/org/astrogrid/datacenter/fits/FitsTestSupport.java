/*$Id: FitsTestSupport.java,v 1.2 2005/03/13 11:43:44 KevinBenson Exp $
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
import junit.framework.TestSuite;
import org.astrogrid.test.OptionalTestCase;

/** Test the Fits processing classes
 */
public class FitsTestSupport extends OptionalTestCase
{


   /** Returns a list of the test URLS
    * NB These are all the same files at the moment
    * @todo - get some (small) samples
    */
   public static URL[] getTestFits()
   {
      return new URL[] {
            
            FitsTestSupport.class.getResource("examples/sample1.fits"),
            FitsTestSupport.class.getResource("examples/sample2.fits"),
            FitsTestSupport.class.getResource("examples/sample3.fits"),
            FitsTestSupport.class.getResource("examples/sample4.fits"),
            FitsTestSupport.class.getResource("examples/sample5.fits"),
            FitsTestSupport.class.getResource("examples/sample6.fits"),
            FitsTestSupport.class.getResource("examples/cdstest.fits")
      };
   }
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(FitsTestSupport.class);
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
 $Log: FitsTestSupport.java,v $
 Revision 1.2  2005/03/13 11:43:44  KevinBenson
 small change for the indextenerator to create directories under target

 Revision 1.1  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.6.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.13  2004/09/08 15:47:31  mch
 Fix to configure ra and dec axis

 Revision 1.12  2004/09/07 14:53:00  mch
 Fixes etc for SEC

 Revision 1.11  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.10  2004/03/09 12:17:11  mch
 Temporarily removed tests on large fits files

 Revision 1.9  2004/01/23 11:14:09  nw
 altered to extend org.astrogrid.test.OptionalTestCase -
 means that these tests can be disabled as needed

 Revision 1.8  2004/01/14 00:53:44  nw
 added switch to test - now able to skip long tests by setting
 a system property

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

