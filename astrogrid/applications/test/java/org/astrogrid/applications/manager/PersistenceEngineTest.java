/*
 * $Id: PersistenceEngineTest.java,v 1.2 2004/03/23 12:51:25 pah Exp $
 * 
 * Created on 05-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import org.astrogrid.applications.common.config.BaseDBTestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class PersistenceEngineTest extends BaseDBTestCase {

   private DatabasePersistenceEngine pe;

   /**
    * Constructor for PersistenceEngineTest.
    * @param arg0
    */
   public PersistenceEngineTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(PersistenceEngineTest.class);
   }

   /*
    * @see BaseDBTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      pe = DatabasePersistenceEngine.getInstance(config);
   }

   final public void testGetNewID() {
      int i = pe.getNewID();
      assertFalse("should not return 0", i == 0);
   }

}
