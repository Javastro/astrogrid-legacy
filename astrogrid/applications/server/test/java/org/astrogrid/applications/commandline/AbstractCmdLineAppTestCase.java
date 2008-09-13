/*
 * $Id: AbstractCmdLineAppTestCase.java,v 1.4 2008/09/13 09:51:04 pah Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;


import static org.junit.Assert.*;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.security.SecurityGuard;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractCmdLineAppTestCase extends
        DescriptionBaseTestCase {


    @Override
    @Before
    public void setUp() throws Exception {
      super.setUp();
        
      ApplicationDescriptionLibrary dl  =
           manager.getApplicationDescriptionLibrary();
      assertNotNull("cannot create the DescriptionLoader", dl);
      descs = dl;
        
      testAppDescr = (CommandLineApplicationDescription)descs.getDescription(TESTAPPNAME);
      assertNotNull(testAppDescr);
      
      // now fix the execution path for this app description.
      Toolbuilder.fixupExecutionPath( testAppDescr);
      assertTrue(testAppDescr instanceof CommandLineApplicationDescription);
      ExecutionHistory history = manager.getExecutionHistoryService();
        
      controller = manager.getExecutionController();
    }

    protected ApplicationDescriptionLibrary descs;
    protected CommandLineApplicationDescription testAppDescr;
    protected ExecutionController controller;
    protected static final int WAIT_SECONDS = 300;
    
    /**
     * Create a tool instance to run.
     * @return
     */
    protected abstract Tool buildTool(String delay) throws Exception;

    @Test
    public void testCreateApplication() throws Exception {
        SecurityGuard user = new SecurityGuard();
        Application app = testAppDescr.initializeApplication("foo",user ,buildTool("1"));
        assertNotNull(app);
    
    }
    
    @Test
    public void testGetInterface() throws Exception {
       ApplicationInterface intf = testAppDescr.getInterface(appInfo.getInterfaceName());
       assertNotNull(intf);
    }


}
