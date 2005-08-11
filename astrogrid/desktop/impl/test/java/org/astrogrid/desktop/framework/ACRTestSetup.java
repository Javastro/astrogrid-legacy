/*$Id: ACRTestSetup.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.PicoContainer;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.desktop.BuildInprocessACR;
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;

import junit.extensions.TestSetup;
import junit.framework.Test;

/** sets up a fixture of a running acr (workbench),
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class ACRTestSetup extends TestSetup{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ACRTestSetup.class);




    /** Construct a new ACRTestSetup
     * @param arg0
     */
    public ACRTestSetup(Test arg0) {
        this(arg0,false);
    }

    public ACRTestSetup(Test arg0,boolean doLogin) {
        super(arg0);
        this.doLogin = doLogin;
    }
    
    protected boolean doLogin;

    protected void setUp() {
        logger.info("Starting ACR as fixture");
        try {
      //not needed  System.setProperty("java.awt.headless","true");
        super.setUp();
        pico = new BuildInprocessACR();
        pico.start();
        } catch (Throwable e) {
            logger.error("ACR fixture threw exception on startup",e);
            fail(e.getMessage());
        }
        logger.info("ACR Fixture started");
        if (doLogin) {
            logger.info("Logging in");            
            try {
                Community comm = (Community)pico.getACR().getService(Community.class);
                try {
                String username = SimpleConfig.getProperty("ag.test.username");
                String password = SimpleConfig.getProperty("ag.test.password");
                String community = SimpleConfig.getProperty("ag.test.community");
                comm.login(username,password,community);
                } catch (PropertyNotFoundException e) { // try ui login.
                    comm.guiLogin();
                }
            } catch (Throwable e) {
                logger.error("ACR fixture threw exception on login",e);
                fail(e.getMessage());
            }
        }
    }
    
    // static, but don't know how else to do this.
    public static BuildInprocessACR pico;

    
    protected void tearDown()  {
        logger.info("Stopping ACR as fixture");
        try {
            pico.stop();     
        } catch (Throwable e) {
            logger.error("ACR fixture threw exception on shutdown",e);
            fail(e.getMessage());
        }
        logger.info("ACR Fixture stopped");


    }

}


/* 
$Log: ACRTestSetup.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/