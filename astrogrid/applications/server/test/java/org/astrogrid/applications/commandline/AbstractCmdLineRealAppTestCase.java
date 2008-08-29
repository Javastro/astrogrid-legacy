/*
 * $Id: AbstractCmdLineRealAppTestCase.java,v 1.1 2008/08/29 07:28:27 pah Exp $
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
import org.astrogrid.applications.test.MockMonitor;
import org.astrogrid.community.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
@RunWith(SpringJUnit4ClassRunner.class) 
public abstract class AbstractCmdLineRealAppTestCase extends
        AbstractCmdLineAppTestCase {

 
  
    public void setUp() throws Exception {
     super.setUp();
     //naughty little kludge to get a noop app on unix.... we do not care if actually gets to execute for these tests
     testAppDescr.setExecutionPath("/bin/echo");

    }
    
    /**
     * This test is really to be able to see what the commandline that is generated looks like
     * @throws Exception
     */
    @Test
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
