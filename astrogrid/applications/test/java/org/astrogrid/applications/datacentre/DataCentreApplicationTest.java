/*
 * $Id: DataCentreApplicationTest.java,v 1.2 2003/12/31 00:56:17 pah Exp $
 * 
 * Created on 11-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.datacentre;

import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DataCentreApplicationTest extends TestCase {

   /**
    * Constructor for DataCentreApplicationTest.
    * @param arg0
    */
   public DataCentreApplicationTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(DataCentreApplicationTest.class);
   }

   final public void testExecute() {
      //TODO trivial test - datacenter does not really exist yet
      boolean result = new DataCentreApplication(new CommandLineApplicationController(), new User()).execute();
      assertEquals( false, result); //this will be true when it exists
   }

}
