/*
 * $Id: JavaInternalApplication.java,v 1.3 2008/09/13 09:51:06 pah Exp $
 * 
 * Created on 21 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.javainternal;

import java.util.concurrent.FutureTask;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

public abstract class JavaInternalApplication extends AbstractApplication implements Runnable {

    public JavaInternalApplication( Tool tool,
	    ApplicationInterface applicationInterface, ApplicationEnvironment env,  ProtocolLibrary lib) {
	super(tool, applicationInterface, env, lib);
	
    }

    /** 
       * Makes the application executable.
       * Processes all input parameters, sets initial status, then nominates its
       * containing object as the execution task: i.e. the class containing this
       * method must implement Runnable.
       * @todo bug here - we assume our parameters are in the correct order to pass to the java method. should sort them into correct order first.
       * @see org.astrogrid.applications.Application#execute(org.astrogrid.applications.ApplicationExitMonitor)
       */
    @Override
    public FutureTask<String> createExecutionTask() throws CeaException {
        super.createAdapters();
        if(task == null)
        {
            task = new ApplicationTask(this, getId(), this);
        }
        return task;
      }

    @Override
    public Runnable createRunnable() {
	return this;
    }

    
    
}


/*
 * $Log: JavaInternalApplication.java,v $
 * Revision 1.3  2008/09/13 09:51:06  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:19:03  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.1.2.2  2008/05/01 15:22:48  pah
 * updates to tool
 *
 * Revision 1.1.2.1  2008/04/23 14:14:31  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 */
