/*
 * $Id: IvornTest.java,v 1.1 2004/03/12 13:14:01 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;
import org.astrogrid.store.*;

import java.net.URISyntaxException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.mySpace.delegate.DummyDelegateTest;


/**
 * IVO Resource Name.  A URI used to name specific IVO resources.
 * I think there's some plugin mechanism to register this wuth URIs but not sure
 * what it is... and anyway in the meantime we want to pass around things that
 * are definitely IVORNs not any other URI.
 *
 * They act as keys to VO Registries; give the registry an IVORN and it will
 * return some value (that might be another IVORN...)
 *
 * IVORNs are of the form:
 *
 * ivo://something/anything/athing/etc#somethingwithmeaningtothiscontext
 *
 * For example:
 *
 * ivo://roe.ac.uk#mch
 *
 * might resolve to an Community web service endpoint, that takes 'mch' to return
 * an account, which <i>might</i> be of the form:
 *
 * ivo://roe.ac.uk/mch/
 *
 * Or:
 *
 * ivo://roe.ac.uk#mch/myspace
 *
 * might resolve to a Community web service as above, that takes 'mch/myspace' to
 * return that accounts myspace, probably as another ivo identifier:
 *
 * ivo://roe.ac.uk/myspace
 *
 * that resolves to a myspace delegate endpoint through the registry.
 *
 * So from the above:
 *
 * ivo://roe.ac.uk/myspace#roe.ac.uk/mch/famousData/BestResults.vot
 *
 * would resolve to a myspace delegate endpoint and a path to give it - ie
 * it refers to a file in myspace
 *
 * IVORNs are immutable - ie once created, they cannot be changed.  You can
 * make new ones out of old ones.
 *
 *
 * @author MCH, KMB, KTN, DM, ACD
 */

public class IvornTest extends TestCase
{

   /**
    * Basic tests
    */
   public void testIvorn()  {
      
      String validirn = "ivo://test.astrogrid.org/avodemo#serv1/query/mch-6dF-query.xml";
      
      try {
         Ivorn irn = new Ivorn(validirn);

         assertEquals(irn.toString(), validirn);
         
         assertEquals(irn.getPath(), "test.astrogrid.org/avodemo");
         assertEquals(irn.getFragment(), "serv1/query/mch-6dF-query.xml");
      }
      catch (URISyntaxException use) {
         fail("Couldn't cope with valid irn "+validirn);
      }
      
      Ivorn irn = new Ivorn("test.astrogrid.org/avodemo", "serv1/query/mch-6dF-query.xml");
      assert irn.toString().equals(validirn);

   }

   /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
     * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(IvornTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
   
}

/*
$Log: IvornTest.java,v $
Revision 1.1  2004/03/12 13:14:01  mch
Moved to common

Revision 1.1  2004/03/01 22:35:54  mch
Tests for StoreClient

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */

