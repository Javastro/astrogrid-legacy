/*
 * @(#)JobMonitor.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobmonitor; 

import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.Test;


/**
 Job Monitor is a greatly reduced from what it used to be. now just acts as a conduit into the job scheduler.
 *@todo - maybe add synchronous service call, so clients that need to know can block until reaching confirmation?
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class JobMonitor implements org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor, ComponentDescriptor{

	private static Log
		logger = LogFactory.getLog( JobMonitor.class ) ;
         
        
    public JobMonitor(JobScheduler nudger) {
        assert nudger != null;
        this.nudger = nudger; 

    }

    protected final JobScheduler
     nudger;
    
    /** do a load of validity checks, then pass on to schedulerr 
     * @todo add validity checks*/
    public void monitorJob(JobIdentifierType id,MessageType info ) {
        if (id == null) {
            logger.info("Null id object encountered");
            return;
        }
        if (info == null) {
            logger.info("Null info object encountered");
            return;
        }



		try {	
            nudger.resumeJob(id,info);
        } catch (Exception e) {
            logger.error("Could not pass on notification",e);

        }
    }


    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Job Monitor";
    }


    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Standard Job Monitor";
    }


    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
        
   
         	
    } 
    
 
