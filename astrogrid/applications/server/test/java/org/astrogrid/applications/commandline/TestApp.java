/*
 * $Id: TestApp.java,v 1.1 2008/08/29 07:28:27 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;


/**
 * This application does not do anything special - just here to test that the class loading is working...
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class TestApp extends CommandLineApplication {
   
   
   /** Construct a new TestApp
     * @param id
     * @param jobStepId
     * @param user
     * @param description
     */
    public TestApp(String jobStepId,Tool tool,ApplicationInterface description, ApplicationEnvironment env,ProtocolLibrary lib) {
        super(jobStepId, tool,description,env,lib);
    }

    static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(TestApp.class);


}
