/*
 * $Id: AbstractCmdLineRealAppTestCase.java,v 1.3 2008/02/12 12:10:56 pah Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.test.MockMonitor;
import org.astrogrid.community.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractCmdLineRealAppTestCase extends
        AbstractCmdLineAppTestCase {

    /**
     * @param arg0
     */
    public AbstractCmdLineRealAppTestCase(TestAppInfo info, String arg0) {
        super(info, arg0);
       
    }

  
    protected void setUp() throws Exception {
     super.setUp();
     //naughty little kludge to get a noop app on unix.... we do not care if actually gets to execute for these tests
     testAppDescr.setExecutionPath("/bin/echo");

    }
    
    /**
     * This test is really to be able to see what the commandline that is generated looks like
     * @throws Exception
     */
    public void testParameterSetup() throws Exception
    {
        Application app = testAppDescr.initializeApplication("testExecution",
                new User(), buildTool(null));
        assertNotNull(app);
        assertTrue(app instanceof CommandLineApplication);
        CommandLineApplication cmdapp = (CommandLineApplication)app;
        // and now run it.
        MockMonitor monitor = new MockMonitor();
//        app.addObserver(controller);
        cmdapp.addObserver(monitor);
        cmdapp.setupParameters();

    }
}
