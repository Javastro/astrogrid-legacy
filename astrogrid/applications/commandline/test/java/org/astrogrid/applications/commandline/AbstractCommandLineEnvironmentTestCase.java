/*
 * $Id: AbstractCommandLineEnvironmentTestCase.java,v 1.5 2006/03/07 21:45:26 clq2 Exp $
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
import junit.framework.TestCase;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;

/**
 * @author Paul Harrison (pharriso@eso.org) 20-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class AbstractCommandLineEnvironmentTestCase extends TestCase {

   protected CommandLineApplicationEnvironment env;
   protected File workingDir;
   protected BasicCommandLineConfiguration configuration;

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
      this.configuration = new BasicCommandLineConfiguration();
      env = new CommandLineApplicationEnvironment(new InMemoryIdGen(), this.configuration);
      assertNotNull(env);
      
      
   }

}


/*
 * $Log: AbstractCommandLineEnvironmentTestCase.java,v $
 * Revision 1.5  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.20.2  2006/01/26 13:16:34  gtr
 * BasicCommandLineConfiguration has absorbed the functions of TestCommandLineConfiguration.
 *
 * Revision 1.2.20.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
