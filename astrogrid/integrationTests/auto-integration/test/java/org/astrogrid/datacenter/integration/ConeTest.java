/*$Id: ConeTest.java,v 1.2 2004/04/16 15:17:14 mch Exp $
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
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.integrationtest.common.ConfManager;

/**
 * Test the cone-search delegate against std PAL
 *
 */
public class ConeTest extends TestCase {

   private static final Log log = LogFactory.getLog(ConeTest.class);

   /**
    * Run some cone searches on std PAL
    */
   public void testStdConeSearch() throws IOException {
      
      String endpoint = "http://localhost:8080/astrogrid-pal-SNAPSHOT/services/AxisDataServer";
      ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS,endpoint,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      //should be empty votable
      
      is = delegate.coneSearch(30,30,6);
      assertNotNull(is);
      //should be some results
      
   
   }
   
   
    /**/

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ConeTest.class);
   }
}


/*
$Log: ConeTest.java,v $
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
