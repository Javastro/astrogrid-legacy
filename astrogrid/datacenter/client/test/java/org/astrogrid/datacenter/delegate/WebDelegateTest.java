/*
 * $Id: WebDelegateTest.java,v 1.4 2004/02/17 03:39:13 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Tests DatacenterDelegate - or at least the dummy one
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;

public class WebDelegateTest extends TestCase
{

	//an astrogrid web service
	private static String ROE_6DF = "http://grendel12.roe.ac.uk:8080/pal-SNAPSHOT/services/AxisDataServer";
	
   /**
    * Tests an SQL-pass through query on the dummy
    */
   public void testConeSearch() throws IOException
   {
      WebDelegate delegate = (WebDelegate) DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, ROE_6DF, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      
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


