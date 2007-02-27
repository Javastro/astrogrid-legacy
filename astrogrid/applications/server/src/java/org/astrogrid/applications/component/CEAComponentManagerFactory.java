/*
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.config.SimpleConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

/** static container that instantiates and  provides access to the component manager.
 * <p />
 * don't use this class willy-nilly - only at the top-leve of the CEA server, where clients need to get an instance. Otherwise the whole model breaks.
 * Within the server itself, the components shoudl gain access to each other through constructor-based dependency injection (which sounds more complex than it is ;)
 *
 * <p />
 *  uses a config key {@link #COMPONENT_MANAGER_IMPL} to determine which class of component manager to instantiate. 
 * This key is looked up in the {@link org.astrogrid.config.Config} system (its likely this key is set in jndi (i.e. in the <tt>context.xml</tt> or <tt>web.xml</tt>for that webapp).
 * @author Noel Winstanley nw@jb.man.ac.uk 04-May-2004
 *
 */
public class CEAComponentManagerFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CEAComponentManagerFactory.class);

    /** key to look in config for cea component manager implementation class */
    public static final String COMPONENT_MANAGER_IMPL = "cea.component.manager.class";


        /** get the instnace.
         * lazily initialized - parses configuration and creates components on first call
         * @return instance of cea component manager, which has been started.
         */
        public static synchronized CEAComponentManager getInstance() throws ComponentManagerException {
                    if (theInstance == null) {
                        logger.info("Creating component manager");
                        String componentManagerClass = null;
                        try {
                            componentManagerClass = SimpleConfig.getSingleton().getString(COMPONENT_MANAGER_IMPL, "org.astrogrid.applications.component.BaseCEAComponentManager");
                            logger.info("Will instantiate component manager class '" + componentManagerClass + "'");
                            theInstance = (CEAComponentManager)Class.forName(componentManagerClass).newInstance();
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
    
        protected static CEAComponentManager theInstance;

        /** package-protected 'clear' method - for testing */
        static void clearInstance() {
            theInstance = null;
        }
        public static final void stop() {
            if (theInstance != null) { 
                theInstance.getContainer().stop();
                theInstance.getContainer().dispose();
            }
        }    
        private CEAComponentManagerFactory() {    
        }
    
  
        /** static method - makes this look like a normal JUnit test, which can then be called in a junit runner.*/
        public static Test suite(){
            try {
                return CEAComponentManagerFactory.getInstance().getSuite();
            } catch (ComponentManagerException e) {
                return new TestSuite("No tests available - component manager failed with " + e.getMessage());
            }
        }

}