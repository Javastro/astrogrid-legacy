/*
 * $Id: MakerTest.java,v 1.1 2009/05/13 13:21:04 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.slinger.sourcetargets.HomespaceSourceTarget;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.homespace.HomespaceName;


/**
 * Tests that the homespace/url maker works OK
 * @author M Hill
 */

public class MakerTest extends TestCase {

   public void testHomespace() throws Exception {
      
      if (!(URISourceTargetMaker.makeSourceTarget("homespace:wibble@wobble/hatstand") instanceof HomespaceSourceTarget)) {
         fail("Maker does not return homespace");
      }
   }
   
   public void testUrl() throws Exception {
      
      if (!(URISourceTargetMaker.makeSourceTarget("ftp://ftp.etc.etc/path/file") instanceof UrlSourceTarget)) {
         fail("Maker does not return url");
      }
   }

   public void testInvalidUri()  {
      try {
         URISourceTargetMaker.makeSourceTarget("silly://ftp.etc.etc/path/file");
         
         fail("Maker did not fail with bad uri");
      }
      catch (URISyntaxException e) {}  //fine, ignore
      catch (MalformedURLException e) {} //fine, ignore
   }
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(MakerTest .class);
   }

  
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }

}



