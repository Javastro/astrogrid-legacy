/*
 * $Id: TargetIndicatorTest.java,v 1.2 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;
import org.astrogrid.slinger.targets.*;


import org.astrogrid.slinger.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the querier, using the dummy plugins. Subclass from SqlPluginTest for
 * the moment so we get all the dummy sql stuff set up
 * @author M Hill
 */

public class TargetIndicatorTest extends TestCase {

   public void testNullMaker() throws Exception {

      TargetIndicator target = TargetMaker.makeIndicator((String) null);
      assertTrue( (target instanceof NullTarget));
      
      target = TargetMaker.makeIndicator("");
      assertTrue( (target instanceof NullTarget));
   }
   
   public void testIvornMaker() throws Exception {
      TargetIndicator target = TargetMaker.makeIndicator("ivo://wibble/key#path/to/file");
      assertNotNull(target);
      assertTrue(target instanceof IvornTarget);
   }
   
   /*
   public void testAgslMaker() throws Exception {
      TargetIndicator target = TargetMaker.makeIndicator("astrogrid:store:http://some/place/with/a/file");
      assertNotNull(target);
      assertTrue(target instanceof AgslTarget);
   }
    */
   public void testUrlMaker() throws Exception {
      TargetIndicator target = TargetMaker.makeIndicator("http://some/place/with/a/file");
      assertNotNull(target);
      assertTrue(target instanceof UrlTarget);
   }

   public void testMsrlMaker() throws Exception {
      TargetIndicator target = TargetMaker.makeIndicator("myspace:http://someserver/services/Manager#with/a/file");
      assertNotNull(target);
      assertTrue(target instanceof MySpaceTarget);
   }
   
   public void testEmailMaker() throws Exception {
      TargetIndicator target = TargetMaker.makeIndicator("mailto:mch@roe.ac.uk");
      assertNotNull(target);
      assertTrue(target instanceof EmailTarget);
   }
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(TargetIndicatorTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

