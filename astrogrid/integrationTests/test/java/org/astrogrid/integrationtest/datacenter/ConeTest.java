/*$Id: ConeTest.java,v 1.1 2004/03/22 17:23:06 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationtest.datacenter;

import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.RA;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.integrationtest.common.ConfManager;

/**
 * Test the cone-search delegate against NVO sites and std PAL
 *
 */
public class ConeTest extends TestCase {

   private static final Log log = LogFactory.getLog(QueryTest.class);

   /**
    * Run some cone searches on std PAL
    */
   public void testStdConeSearch() throws IOException {
      
      String endpoint = ConfManager.getInstance().getStdDatacenterEndPoint();
      ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS,endpoint,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      //should be empty votable
      
      is = delegate.coneSearch(30,30,6);
      assertNotNull(is);
      //should be some results
      
   
   }
   
   /**
    * Tests against a real service - Messier
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testMessier() throws IOException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         " http://virtualsky.org/servlet/cover?CAT=messier",
         DatacenterDelegateFactory.NVO_CONE_SERVICE
      );
      
      InputStream results = searcher.coneSearch(20,-30,20);
      
      assertNotNull(results);
   }
   
   /**
    * Tests against a real service - NCSA Radio
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testNcsa() throws IOException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://adil.ncsa.uiuc.edu/cgi-bin/vocone?survey=f",
         DatacenterDelegateFactory.NVO_CONE_SERVICE
      );
      
      InputStream results = searcher.coneSearch(120,90,10);
      
      assertNotNull(results);
   }
   
   
    /**/

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ConeTest.class);
   }
}


/*
$Log: ConeTest.java,v $
Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.2  2004/02/17 23:59:17  jdt
commented out lines killing the build, and made to conform to

coding stds

Revision 1.1  2004/01/23 09:08:09  nw
added cone searcher test too
 
*/
