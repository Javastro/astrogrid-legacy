/*$Id: AdqlConeSearcherTest.java,v 1.1 2004/01/23 09:08:09 nw Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.integrationTests.datacenter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.integrationTests.common.ConfManager;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/** Test the cone-search delegate against an astrogrid datacenter containing the merlin archive.
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jan-2004
 *
 */
public class AdqlConeSearcherTest extends TestCase {
   /**
    * Constructor for AdqlConeSearcherTest.
    * @param arg0
    */
   public AdqlConeSearcherTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(AdqlConeSearcherTest.class);
   }
   private static final Log log = LogFactory.getLog(FullSearcherTest.class);
   
   protected void setUp() throws Exception {
      String endpoint = ConfManager.getInstance().getMerlinDatacenterEndPoint();
      delegate = DatacenterDelegateFactory.makeConeSearcher(User.ANONYMOUS,endpoint,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);
            
   }
   protected ConeSearcher delegate;
   /** Right ascension parameter
    * @todo find an astronomer who can set this to a meaningful value
    */
   public final static double RA = 0.5;
   /** declination parameter
    * @todo get an astronomer to set this to something that makes sense
    */
   public final static double DEC = 0.5;
   /** search radius.
    * @todo - is this too small or too big?
    */
   public final static double RADIUS = 0.001;
   
   /**
    * test the cone search method of the delegate
    * @throws Exception
    * @todo add some checking of the results returned.
    */
   public void testConeSearch() throws Exception {
      InputStream is = delegate.coneSearch(RA,DEC,RADIUS);
      // dunno why this delegate doesn't return a datacenterResults too..
      assertNotNull(is);
   }
   
}


/* 
$Log: AdqlConeSearcherTest.java,v $
Revision 1.1  2004/01/23 09:08:09  nw
added cone searcher test too
 
*/