/* $Id: AllTests.java,v 1.1 2004/03/01 22:35:09 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.store;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.store.delegate.FactoryTest;
import org.astrogrid.store.delegate.LocalFileStoreTest;

/**
 */

public class AllTests {
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for org.astrogrid.store");

      suite.addTest(AgslTest.suite());
      suite.addTest(IvornTest.suite());
      suite.addTest(MsrlTest.suite());
      
      suite.addTest(FactoryTest.suite());
      suite.addTest(LocalFileStoreTest.suite());
      
      return suite;
   }
}

/*
 $Log: AllTests.java,v $
 Revision 1.1  2004/03/01 22:35:09  mch
 Tests for StoreClient

 */
