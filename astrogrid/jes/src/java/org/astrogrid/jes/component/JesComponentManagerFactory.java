/*$Id: JesComponentManagerFactory.java,v 1.2 2004/07/09 09:30:28 nw Exp $
 * Created on 16-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.component.production.PolicyDrivenComponentManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 Static singleton that contains a single instance of a ComponentManager. 
 Used so that the same component manager (and so same components) can be accessed from different parts of the server - in particular the job monitor and jobscheduler
 web services.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2004
 *
 */
public class JesComponentManagerFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JesComponentManagerFactory.class);
    /** key to look in config for cea component manager implementation class */
    public static final String COMPONENT_MANAGER_IMPL = "jes.component.manager.class";



    /** get the instnace.
     * lazily initialized - parses configuration and creates components on first call
     * @return
     */
    public static synchronized JesComponentManager getInstance() throws ComponentManagerException {
        if (theInstance == null) {
             logger.info("Creating component manager");
             String componentManagerClass = null;
             try {
                 componentManagerClass = SimpleConfig.getSingleton().getString(COMPONENT_MANAGER_IMPL,PolicyDrivenComponentManager.class.getName());
                 logger.info("Will instantiate component manager class '" + componentManagerClass + "'");
                 theInstance = (JesComponentManager)Class.forName(componentManagerClass).newInstance();
                 logger.info("Instantiated. Now Starting");
                 theInstance.start();
                 logger.info("Successfully created component manager");
             } catch (Throwable e) {
                 logger.fatal("Could not create component manager (class '" + componentManagerClass + "')",e);
                 StringWriter sw = new StringWriter();
                 e.printStackTrace(new PrintWriter(sw));
                 logger.info(sw.toString());
                 throw new ComponentManagerException("Could not create componentManager : " + componentManagerClass,e);
             }
         }           
        return theInstance;
    }
    
    protected static JesComponentManager theInstance;
        
    /** unsafe, useful for testing */
    public static void _setInstance(JesComponentManager mgr) {
        theInstance = mgr;
        theInstance.start();
    }
    
    private JesComponentManagerFactory() {    
    }
    
  
    /** static method - makes this look like a normal JUnit test, which can then be called in a junit runner.*/
    public static Test suite(){
        try {
            return JesComponentManagerFactory.getInstance().getSuite();
        } catch (ComponentManagerException e) {
            return new TestSuite("No tests available - component manager failed with " + e.getMessage());
        }
    }


}

