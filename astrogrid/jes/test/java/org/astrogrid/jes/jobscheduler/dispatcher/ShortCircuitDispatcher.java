/*$Id: ShortCircuitDispatcher.java,v 1.4 2004/09/16 21:45:23 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.rmi.RemoteException;
import java.util.Calendar;

/** Monck dispatcher that 'short circuits' call to app controller, by directly calling a monitor component
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class ShortCircuitDispatcher extends MockDispatcher {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ShortCircuitDispatcher.class);

    public ShortCircuitDispatcher(JobMonitor jb, ResultsListener jrl) {
        this(jb,jrl,true);
    }

    /** Construct a new ShortCircuitDispatcher
     * @param jm
     * @param willSucceed
     */
    public ShortCircuitDispatcher(JobMonitor jm,  ResultsListener jrl, boolean willSucceed) {
        super(willSucceed);
        logger.info("Created short circuit dispatcher");
        this.monitor = jm;
        this.results = jrl;
    }
    protected JobMonitor monitor;
    protected ResultsListener results;
    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step)
     */
    public void dispatchStep(Workflow job, Tool t, String stepId) throws JesException {
        logger.info("Dispatching tool " +stepId + " in workflow " + job.getJobExecutionRecord().getJobId().getContent());
        callCount++;
        // we've got a monitor, lets talk / barf through that.
        MessageType info = new MessageType();
        JobIdentifierType id = JesUtil.createJobId(job.getJobExecutionRecord().getJobId(),stepId);

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
            throw new JesException("Short Circuit Dispatcher had problems talking to job monitor",e);
        }
        if (willSucceed) {
            try {
                ResultListType rl = new ResultListType();                
                results.putResults(id,rl);
            } catch (RemoteException e) {
                throw new JesException("Short circuite dispatcher had problems talking to results listener",e);
            }
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
Revision 1.4  2004/09/16 21:45:23  nw
got this test working again - had to add in result listener

Revision 1.3  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.2.46.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.2  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer
 
*/