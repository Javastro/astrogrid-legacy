/*
 * $Id: AbstractCommandLineEnvironmentTestCase.java,v 1.6 2006/03/11 05:57:54 clq2 Exp $
 * 
 * Created on 20-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import java.io.File;

import org.astrogrid.applications.manager.idgen.InMemoryIdGen;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pharriso@eso.org) 20-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class AbstractCommandLineEnvironmentTestCase extends TestCase {

   protected CommandLineApplicationEnvironment env;
   protected File workingDir;

   /**
    * 
    */
   public AbstractCommandLineEnvironmentTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public AbstractCommandLineEnvironmentTestCase(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   protected void setUp() throws Exception {
      super.setUp();
      workingDir = File.createTempFile("CmdLineApplicationEnvironmentTest",null);
      workingDir.delete();
      workingDir.mkdir();
      assertTrue(workingDir.exists());
      workingDir.deleteOnExit();
      env = new CommandLineApplicationEnvironment(new InMemoryIdGen(),new CommandLineApplicationEnvironment.WorkingDir() {
   
        public File getDir() {
            return workingDir;
        }
      });
      assertNotNull(env);
      
      
   }

}


/*
 * $Log: AbstractCommandLineEnvironmentTestCase.java,v $
 * Revision 1.6  2006/03/11 05:57:54  clq2
 * roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489
 *
 * Revision 1.4  2006/01/10 11:26:52  clq2
 * rolling back to before gtr_1489
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
