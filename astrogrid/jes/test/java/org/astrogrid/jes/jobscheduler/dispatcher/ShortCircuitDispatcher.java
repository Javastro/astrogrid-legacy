/*$Id: ShortCircuitDispatcher.java,v 1.2 2004/03/07 21:04:39 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.rmi.RemoteException;
import java.util.Calendar;

/** Monck dispatcher that 'short circuits' call to app controller, by directly calling a monitor component
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class ShortCircuitDispatcher extends MockDispatcher {

    public ShortCircuitDispatcher(JobMonitor jb) {
        this(jb,true);
    }

    /** Construct a new ShortCircuitDispatcher
     * @param jm
     * @param willSucceed
     */
    public ShortCircuitDispatcher(JobMonitor jm, boolean willSucceed) {
        super(willSucceed);
        this.monitor = jm;
    }
    protected JobMonitor monitor;
    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step)
     */
    public void dispatchStep(Workflow job, Step js) throws JesException {
        callCount++;
        // we've got a monitor, lets talk / barf through that.
        MessageType info = new MessageType();
        String xpath = job.getXPathFor(js) ;
        JobIdentifierType id = JesUtil.createJobId(job.getJobExecutionRecord().getJobId(),xpath);

        info.setSource("application");
        info.setTimestamp(Calendar.getInstance());            
        if (willSucceed) {
            info.setContent("OK");
            info.setPhase(ExecutionPhase.COMPLETED);
            info.setLevel(LogLevel.info);
        } else {
            info.setContent("You wanted me to fail");
            info.setPhase(ExecutionPhase.ERROR);
            info.setLevel(LogLevel.error);
        }
        try {
            monitor.monitorJob(id,info);
        } catch (RemoteException e) {
            throw new JesException("Mock Dispatcher had problems talking to job monitor",e);
        }
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Short Circuit Dispatcher";        
    }
    
        public String getDescription() {
            return "Test dispatcher, passed response directly back to job monitor component\n"
                + (willSucceed ? "configured to succeed on method call" : "configured to fail on method call, throwing an exception");
        }        
 

}


/* 
$Log: ShortCircuitDispatcher.java,v $
Revision 1.2  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer
 
*/