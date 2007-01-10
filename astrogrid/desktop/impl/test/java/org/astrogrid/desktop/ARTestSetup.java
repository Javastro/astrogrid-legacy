/*$Id: ARTestSetup.java,v 1.2 2007/01/10 14:57:10 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;

/** sets up a fixture of a running in-process workbench,
 *
 * fixture is stored in a static variable from where it can be accessed by tests.
 * fthis class tests whether the fixture is already initialized - and will only create 
 *a workbench instnace if there is not onoe there already.
 *
 * This allows instances of this class to be nested in the test hierarchy. -
 * enabling tests to be run singularly or in bulk without major code gymnastics.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class ARTestSetup extends TestSetup{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ARTestSetup.class);

    /** Construct a new ACRTestSetup
     * @param arg0
     */
    public ARTestSetup(Test arg0) {
        this(arg0,false);
    }

    public ARTestSetup(Test arg0,boolean doLogin) {
        super(arg0);
        
        this.doLogin = doLogin;
    }
    


    protected boolean doLogin;
    protected boolean iCreatedFixture;

    protected void setUp() throws Exception {
    	if (fixture == null) { // no previous / parent test setup fixture created it..
    		iCreatedFixture = true;
    		createAR();
    		assertNotNull("Failed to create fixture",fixture);
    	}
    	if (doLogin) {
    			login(); 
    	}
    }

    protected void tearDown()  throws Exception{
    	if (iCreatedFixture) {
    		logger.info("Stopping AR as fixture");
    		fixture.stop();     
    		logger.info("ACR Fixture stopped");
    		fixture = null;
    		System.gc();
    	}
        super.tearDown();
    }   

    /**
     * 
     */
    private void login() {
    	try {
    		Community comm = (Community)fixture.getACR().getService(Community.class);
    		if (! comm.isLoggedIn()) {	    		
    			logger.info("Logging in");            
    			try {
    				// these properties are read from test/java/astrogrid.properties
    				// @todo verify this works.
    				String username = SimpleConfig.getProperty("ag.test.username");
    				String password = SimpleConfig.getProperty("ag.test.password");
    				String community = SimpleConfig.getProperty("ag.test.community");
    				comm.login(username,password,community);
    				logger.info("Logged in as " + username + " / " + community);
    			} catch (PropertyNotFoundException e) { // try ui login.
    				logger.warn("Failed to read login settings from astrogrid.properties - prompting using GUI");
    				comm.guiLogin();
    			}
    		}
    	} catch (Throwable e) {
    		logger.error("Fixture failed to login",e);
    		fail("Fixture failed to login: " + e.getMessage());
    	}
    }

	/**
	 * 
	 */
	private void createAR() {
		try {
    		logger.info("Creating AR fixture");			
    		System.setProperty("builtin.shutdown.exit", "false");
			fixture = new BuildInprocessWorkbench();
			//@todo add furtyher config in here - e.g. different java.util.properties base class,
			// custom system properties, etc.
			fixture.start();
    		logger.info("AR Fixture started");
		} catch (Throwable e) {
			logger.error("AR fixture threw exception on startup",e);
			fail("AR fixture threw exception on startup" + e.getMessage());
		}
	}
    
    // static hook from where tests can retrieve the fixture
	// not very nice way of doing it.but don't know how else to do this.
    public static BuildInprocessWorkbench fixture;


}


/* 
$Log: ARTestSetup.java,v $
Revision 1.2  2007/01/10 14:57:10  nw
organized imports.

Revision 1.1  2007/01/09 16:11:24  nw
freshened and fixed InARTestCase- now can be nested, and works within eclipse.

Revision 1.2  2006/06/15 16:34:50  nw
removed cruft.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/