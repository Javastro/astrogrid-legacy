/*
 * $Id NvoConeTest.java $
 *
 */

package org.astrogrid.datacenter.delegate.nvocone;


/**
 *
 * @author M Hill
 */

import VOTableUtil.Votable;
import com.tbf.xml.XmlElement;
import com.tbf.xml.XmlParser;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.test.OptionalTestCase;

public class NvoConeTest extends OptionalTestCase
{
   
   /**
    * Tests the factory produces the right class
    */
   public void testFactory() throws MalformedURLException, DatacenterException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, "http://dummy.nvoconesearch/cone?cat=mycatalogue", DatacenterDelegateFactory.NVO_CONE_SERVICE);
      
      assertTrue(searcher instanceof NvoConeSearchDelegate);
   }
   
   
   /**
    * Tests the format of the url is right
    */
   public void testUrlFormat() throws IOException
   {
      NvoConeSearchDelegate searcher = new NvoConeSearchDelegate("http://dummy.nvoconesearch/cone?cat=mycatalogue");
      
      //this should throw an exception as the given url is wrong
      try {
         searcher.coneSearch(12.0,12.0,1.0);
         
         fail("Should have thrown an exception trying to run cone search on "+searcher);
      }
      catch (DatacenterException de)
      {
         //look for the url
         Throwable cause = de.getCause();
         
         assertTrue(cause instanceof UnknownHostException);
      }
      
   }
   
   public void checkResults(InputStream results)
   {
      //horrible horrible Javot requiring a particular parser...
      XmlParser parser = new XmlParser();
      XmlElement rootNode = parser.parse(new BufferedInputStream(results));
      
      assertNotNull(rootNode);
      
      Votable votable = new Votable(rootNode);
      
      //check there wer no errors
      Vector errors = votable.getValidationErrors();
      if (errors != null) {
         assertTrue(errors.size() == 0);
      }
      
      //check there is a resource component
      assertTrue(votable.getResourceCount()!=0); //table has no resource
      
   }
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(NvoConeTest.class);
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
 $Log: NvoConeTest.java,v $
 Revision 1.9  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.8.6.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.8  2004/09/28 15:50:50  mch
 Removed calls to deprecated methods

 Revision 1.7  2004/03/22 16:33:15  mch
 Added tests against real services

 Revision 1.6  2004/03/12 20:04:39  mch
 It05 Refactor (Client)

 Revision 1.5  2004/01/23 11:12:01  nw
 altered to extend org.astrogrid.test.OptionalTestCase -
 means that these tests can be disabled as needed

 Revision 1.4  2004/01/14 13:23:30  nw
 re-enabled messier test - service seems to be up again

 Revision 1.3  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.2  2003/11/25 15:26:00  nw
 temporarily disabled tests that were halting the build.
 server gone down?

 Revision 1.1  2003/11/17 17:11:12  mch
 Added cone search test

 */
