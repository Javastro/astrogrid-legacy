/*
 * $Id: AgfmUrlHandlerTest.java,v 1.1 2009/05/13 13:21:04 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.slinger.agfm.FileManagerConnection;


/**
 * Tests that making 'homespace' URLs work OK
 * @author M Hill
 */

public class AgfmUrlHandlerTest extends TestCase {

   public void testValidUrls() throws Exception {

      new URL("agfm:ivo://roe.ac.uk/mch/path/path/file.ext");

      new URL("agfm:ivo://uk.ac.le.star/DSATEST1/path/path/file.ext");

      new URL("agfm:ivo://uk.ac.le.star/DSATEST1/banana");
      new URL("agfm:ivo://uk.ac.le.star/DSATEST1/banana/");
   }
   
   public void testInvalidUrls()  {
      try {
         new URL("agfm:wibble");
         fail("Failed to reject invalid AGFM URL");
      }
      catch (MalformedURLException mue) { /* good */ }
   }
   
   /** We can ensure that the connection object is correctly created, though
    * we shouldn't - as part of the normal unit tests - try to connect to it */
   public void testConnection() throws MalformedURLException, IOException {
      
      URL url = new URL("agfm:mch@roe.ac.uk/banana");
      
      URLConnection connection = url.openConnection();
      
      assertTrue("Connection is of type "+connection.getClass()+" should be "+FileManagerConnection.class, connection instanceof FileManagerConnection);
   }
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(HomespaceUrlHandlerTest .class);
   }

  
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }

}



