/*
 * $Id: DataCentreApplicationTest.java,v 1.3 2004/04/19 17:34:08 pah Exp $
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

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.manager.ApplicationExitMonitor;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DataCentreApplicationTest extends TestCase implements ApplicationExitMonitor {

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

   final public void testExecute() throws CeaException {
      //TODO trivial test - datacenter does not really exist yet
      boolean result = new DataCentreApplication(new CommandLineApplicationController(), new User()).execute(this);
      assertEquals( false, result); //this will be true when it exists
   }

   /** 
    * @see org.astrogrid.applications.manager.ApplicationExitMonitor#registerApplicationExit(org.astrogrid.applications.AbstractApplication)
    */
   public void registerApplicationExit(AbstractApplication app) {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("DataCentreApplicationTest.registerApplicationExit() not implemented");
   }

}
