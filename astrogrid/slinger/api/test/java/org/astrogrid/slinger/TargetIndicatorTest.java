/*

 * $Id: TargetIndicatorTest.java,v 1.1 2005/02/14 20:47:38 mch Exp $

 *

 * (C) Copyright Astrogrid...

 */



package org.astrogrid.slinger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.slinger.targets.EmailTarget;
import org.astrogrid.slinger.targets.NullTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.slinger.targets.UrlTarget;
import org.astrogrid.slinger.vospace.IVORN;



/**

 * Tests the querier, using the dummy plugins. Subclass from SqlPluginTest for

 * the moment so we get all the dummy sql stuff set up

 * @author M Hill

 */



public class TargetIndicatorTest extends TestCase {



   public void testNullMaker() throws Exception {



      TargetIdentifier target = TargetMaker.makeTarget((String) null);

      assertTrue( (target instanceof NullTarget));

      

      target = TargetMaker.makeTarget("");

      assertTrue( (target instanceof NullTarget));

   }

   

   public void testIvornMaker() throws Exception {

      TargetIdentifier target = TargetMaker.makeTarget("ivo://wibble/key#path/to/file");

      assertNotNull(target);

      assertTrue(target instanceof IVORN);

   }

   

   /*

   public void testAgslMaker() throws Exception {

      TargetIndicator target = TargetMaker.makeIndicator("astrogrid:store:http://some/place/with/a/file");

      assertNotNull(target);

      assertTrue(target instanceof AgslTarget);

   }

    */

   public void testUrlMaker() throws Exception {

      TargetIdentifier target = TargetMaker.makeTarget("http://some/place/with/a/file");

      assertNotNull(target);

      assertTrue(target instanceof UrlTarget);

   }

   

   public void testEmailMaker() throws Exception {

      TargetIdentifier target = TargetMaker.makeTarget("mailto:mch@roe.ac.uk");

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



