/*
 * $Id: TargetIndicatorTest.java,v 1.4 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;


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

      TargetIndicator target = TargetIndicator.makeIndicator((String) null);
      assertTrue( (target instanceof WriterTarget));
      
      target = TargetIndicator.makeIndicator("");
      assertTrue( (target instanceof WriterTarget));
   }
   
   public void testIvornMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("ivo://wibble/key#path/to/file");
      assertNotNull(target);
      assertTrue(target instanceof IvornTarget);
   }
   
   public void testAgslMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("astrogrid:store:http://some/place/with/a/file");
      assertNotNull(target);
      assertTrue(target instanceof AgslTarget);
   }
   
   public void testEmailMaker() throws Exception {
      TargetIndicator target = TargetIndicator.makeIndicator("mailto:mch@roe.ac.uk");
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

