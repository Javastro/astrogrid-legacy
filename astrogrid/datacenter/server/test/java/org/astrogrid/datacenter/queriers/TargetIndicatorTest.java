/*
 * $Id: TargetIndicatorTest.java,v 1.1 2004/07/15 17:06:45 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;
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

      TargetIndicator target = TargetIndicator.makeIndicator(null);
      assertNull(target);
      
      target = TargetIndicator.makeIndicator("");
      assertNull(target);
   }
   
   public void testIvornMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("ivo://wibble/key#path/to/file");
      assertNotNull(target);
      assertTrue(target.isIvorn());
   }
   
   public void testAgslMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("astrogrid:store:http://some/place/with/a/file");
      assertNotNull(target);
      assertFalse(target.isIvorn());
   }
   
   public void testEmailMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("mailto:mch@roe.ac.uk");
      assertNotNull(target);
      assertFalse(target.isIvorn());
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

