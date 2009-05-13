/*
 * $Id: HomespaceUrlHandlerTest.java,v 1.1 2009/05/13 13:21:04 gtr Exp $
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
import org.astrogrid.slinger.homespace.HomespaceName;


/**
 * Tests that making 'homespace' URLs work OK
 * @author M Hill
 */

public class HomespaceUrlHandlerTest extends TestCase {

   public void testValidUrls() throws Exception {

      new URL("homespace:mch@roe.ac.uk/path/path/file.ext");

      new URL("homespace:DSATEST1@uk.ac.le.star/path/file.ext");

      new URL("homespace:mch@roe.ac.uk/banana");
   
      new URL("homespace:mch@roe.ac.uk/banana/");

   }
   
   public void testInvalidUrl1()  {
      assertInvalidUrl("homespace:wibble");
   }

   public void testInvalidUrl2()  {
      assertInvalidUrl("homespace:@there");
   }
   public void testInvalidUrl3()  {
      assertInvalidUrl("homespace:here@");
   }
   public void testInvalidUrl4()  {
      assertInvalidUrl("homespace:mch@roe.ac.uk#somepath/file.ext"); //hashes shouldn't be allowed
   }

   public void assertInvalidUrl(String invalidUrl)  {
      try {
         new URL(invalidUrl);
         fail("Failed to reject invalid homespace URL '"+invalidUrl+"'");
      }
      catch (MalformedURLException mue) { /* good */ }
   }

   public void testParsing() throws URISyntaxException {
      HomespaceName name = new HomespaceName("homespace:mch@roe.ac.uk/path/path/file.ext");
      
      assertEquals("Homespace incorrectly parsed", "mch@roe.ac.uk", name.getAccountName());
      assertEquals("Homespace incorrectly parsed", "mch", name.getIndividual());
      assertEquals("Homespace incorrectly parsed", "roe.ac.uk", name.getCommunity());
      assertEquals("Homespace incorrectly parsed", "/path/path/file.ext", name.getPath());
   }
   
   /** Tests backwards compatibility - will it convert correctly from an ivo form
    * to homespace back to ivorn */
   public void testOldForm() {
      String old="ivo://uk.ac.le.star/anitarichards#Output/twomass.vot";
      HomespaceName name = HomespaceName.fromIvorn(old);
      
      String s = name.toString();
      assertEquals(s, "homespace:anitarichards@uk.ac.le.star/Output/twomass.vot");
      
      assertEquals(old, name.toIvorn());
      
   }
   
   /** We can ensure that the connection object is correctly created, though
    * we shouldn't - as part of the normal unit tests - try to actually connect
    * to the target */
   public void testConnection() throws MalformedURLException, IOException {
      
      URL url = new URL("homespace:mch@roe.ac.uk/banana");
      
      URLConnection connection = url.openConnection();
      
      assertNotNull(connection);
   }

   /** Naughty 'live' connection to the real thing */
   public void testLive() throws URISyntaxException {
      ConfigFactory.getCommonConfig().setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");

      ConfigFactory.getCommonConfig().setProperty("homespace.agdemo1@org.astrogrid","ivo://astrogrid.org/myspace");
      
      HomespaceName name = new HomespaceName("homespace:agdemo1@org.astrogrid/agdemo1/votable/MartinPlayingWithNamesYetAgain");
      System.out.println(name);

//      IVOSRN ivosrn = name.resolveIvosrn();
//      System.out.println("->"+ivosrn);
      
      name = new HomespaceName("homespace:guest04@uk.ac.le.star/votable/secresult1.vot");
      System.out.println(name);

//      ivosrn = name.resolveIvosrn();
//      System.out.println("->"+ivosrn);
      /*
//      SRL msrl = ivosrn.resolveSrl();
//      System.out.println("->"+ivosrnmsrl);
      
      OutputStream out = ((MSRL) msrl).resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("This could be rude and you'd never know cos it's about to be overwritten".getBytes());
      out.close();
      
      out = name.resolveOutputStream(LoginAccount.ANONYMOUS);
      out.write("Hello there everyone".getBytes());
      out.close();
      
      System.out.println("done");
       */
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



