/*$Id: AdqlTest.java,v 1.4 2004/08/03 13:41:29 KevinBenson Exp $
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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.io.Piper;
import javax.xml.rpc.ServiceException;

/**
 * Test querying using ADQL against std PAL
 *
 */
public class AdqlTest extends TestCase implements StdKeys {

   private static final Log log = LogFactory.getLog(AdqlTest.class);

   /**
    * Run sample query on std PAL
    */
   public void testAdqlSearch() throws IOException, ServiceException, IOException {
      //load query
      InputStream is = this.getClass().getResourceAsStream("SimpleStarQuery-adql05.xml");
      assertNotNull(is);
      StringWriter out = new StringWriter();
      Piper.pipe(new InputStreamReader(is),out);
      AdqlQuery query = new AdqlQuery(out.toString());
      
      QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS,PAL_v05_ENDPOINT,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream results = delegate.askQuery(query, "VOTABLE");
      assertNotNull(results);
      //should be empty votable
   }
   
   public void testAdqlSearchForSEC() throws IOException, ServiceException, IOException {
      //load query
      InputStream is = this.getClass().getResourceAsStream("SimpleSECQuery-adql05.xml");
      assertNotNull(is);
      StringWriter out = new StringWriter();
      Piper.pipe(new InputStreamReader(is),out);
      AdqlQuery query = new AdqlQuery(out.toString());
      
      QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS,PAL_v05_SEC_ENDPOINT,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream results = delegate.askQuery(query, "VOTABLE");
      assertNotNull(results);
      Piper.pipe(results,System.out);
      //StringWriter outResult = new StringWriter();
      //Piper.pipe(new InputStreamReader(results),System.out);
   }

   public void testAdqlSearchForFITS() throws IOException, ServiceException, IOException {
      //load query
      InputStream is = this.getClass().getResourceAsStream("SimpleFITSQuery-adql073.xml");
      assertNotNull(is);
      StringWriter out = new StringWriter();
      Piper.pipe(new InputStreamReader(is),out);
      AdqlQuery query = new AdqlQuery(out.toString());
      
      QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS,PAL_v05_FITS_ENDPOINT,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream results = delegate.askQuery(query, "VOTABLE");
      assertNotNull(results);
      //StringWriter outResult = new StringWriter();
      Piper.pipe(results,System.out);
   }
   
   
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AdqlTest.class);
    }
   
    /**/

   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
}


/*
$Log: AdqlTest.java,v $
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
