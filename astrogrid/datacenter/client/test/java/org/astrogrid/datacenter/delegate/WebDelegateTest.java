/*
 * $Id: WebDelegateTest.java,v 1.9 2004/08/02 14:57:32 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Tests the web delegate.  This is a bit naughty as it's not really a unit
 * test - it requires a real PAL to connect to
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.agws.WebDelegate_v041;
import org.astrogrid.datacenter.delegate.agws.WebDelegate_v05;

public class WebDelegateTest extends TestCase
{

   //an astrogrid web service
   private static String TEST_PAL_041 = "http://twmbarlwm.star.le.ac.uk/astrogrid-pal-SNAPSHOT/services/AxisDataServer";

      //an astrogrid web service
   private static String TEST_PAL_05 = "http://twmbarlwm.star.le.ac.uk/astrogrid-pal-SNAPSHOT/services/AxisDataService05";

   /**
    * Tests a cone search on the old delegate
    */
   public void testConeSearch041() throws IOException
   {
      WebDelegate_v041 delegate = (WebDelegate_v041) DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, TEST_PAL_041, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      
      InputStream results = delegate.coneSearch(308, 60, 12);
      
      assertNotNull(results);
      
   }
   
   /**
    * Tests a cone search on the itn05 delegate
    */
   public void testConeSearch05() throws IOException
   {
      WebDelegate_v05 delegate = (WebDelegate_v05) DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, TEST_PAL_05, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      
      InputStream results = delegate.coneSearch(308, 60, 12);
      
      assertNotNull(results);
      
   }



   public static Test suite()
   {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(WebDelegateTest.class);
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
 * $Log: WebDelegateTest.java,v $
 * Revision 1.9  2004/08/02 14:57:32  mch
 * Fixed to use twmbarlwm as server to test against
 *
 * Revision 1.8  2004/08/02 14:48:01  mch
 * Fixed to use twmbarlwm as server to test against
 *
 * Revision 1.7  2004/03/12 20:04:39  mch
 * It05 Refactor (Client)
 *
 * Revision 1.6  2004/03/07 02:21:07  mch
 * Fixed url to vm07
 *
 * Revision 1.5  2004/02/17 15:12:49  mch
 * Fix to access test pal
 *
 * Revision 1.4  2004/02/17 03:39:13  mch
 * New Datacenter UIs
 *
 * Revision 1.3  2004/01/13 15:50:24  nw
 * fixed endpoint
 *
 * Revision 1.2  2004/01/13 00:32:47  nw
 * Merged in branch providing
 * * sql pass-through
 * * replace Certification by User
 * * Rename _query as Query
 *
 * Revision 1.1.4.2  2004/01/08 09:42:26  nw
 * tidied imports
 *
 * Revision 1.1.4.1  2004/01/07 13:01:44  nw
 * removed Community object, now using User object from common
 *
 * Revision 1.1  2003/12/16 11:14:32  mch
 * Added cone search tests for web delegate
 *
 * Revision 1.6  2003/12/02 17:56:39  mch
 * Added sql pass through test
 *
 * Revision 1.5  2003/11/26 16:31:46  nw
 * altered transport to accept any query format.
 * moved back to axis from castor
 *
 * Revision 1.4  2003/11/21 17:30:19  nw
 * improved WSDL binding - passes more strongly-typed data
 *
 * Revision 1.3  2003/11/17 15:40:51  mch
 * Package movements
 *
 * Revision 1.2  2003/11/13 22:58:08  mch
 * Added Log to end
 *
 */



