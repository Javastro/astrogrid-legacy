/*$Id: ConeTest.java,v 1.5 2004/05/13 12:25:04 mch Exp $
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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;

/**
 * Test the cone-search delegate against std PAL
 *
 */
public class ConeTest extends TestCase implements StdKeys {

   private static final Log log = LogFactory.getLog(ConeTest.class);

   /**
    * Run some cone searches on std PAL it04
    */
   public void testStdConeSearch04() throws IOException {
      
      ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, PAL_v041_ENDPOINT, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      //should be empty votable
      
      is = delegate.coneSearch(30,30,6);
      assertNotNull(is);
      //should be some results
      
   
   }
   
   /**
    * Run some cone searches on std PAL it05
    */
   public void testStdConeSearch05() throws IOException {
      
      ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, PAL_v05_ENDPOINT, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      //should be empty votable
      
      is = delegate.coneSearch(30,30,6);
      assertNotNull(is);
      //should be some results
      
   
   }
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ConeTest.class);
    }
   
    /**/

   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
}


/*
$Log: ConeTest.java,v $
Revision 1.5  2004/05/13 12:25:04  mch
Fixes to create user, and switched to mostly testing it05 interface

Revision 1.4  2004/05/12 09:17:51  mch
Various fixes - forgotten whatfors...

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
