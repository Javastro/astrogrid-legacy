/*$Id: JesResultsListener.java,v 1.4 2005/03/13 07:13:39 clq2 Exp $
 * Created on 01-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.resultlistener;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

import junit.framework.Test;

/** Jes Implementation of CEA's results listener interface.
 * <p>
 * stub component - delegates message to the scheduler, where it is processed
 * (so its entered into the task queue).
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Jul-2004
 *
 */
public class JesResultsListener implements ResultsListener, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JesResultsListener.class);

    /** Construct a new ResultListener
     * 
     */
    public JesResultsListener(JobScheduler scheduler) {
        this.scheduler = scheduler;
    }
    protected final JobScheduler scheduler;

    /**
     * @see org.astrogrid.jes.service.v1.cearesults.ResultsListener#putResults(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.ResultListType)
     */
    public void putResults(JobIdentifierType id, ResultListType resultList) {
        if (id  == null) {
            logger.info("null id object encountered");
            return;
        }
        if (resultList == null) {
            logger.info("null result list object encountered");
            return;
        }

        logger.debug("Received results for " + id.toString());
        try {
            scheduler.reportResults(id,resultList);
        } catch (Exception e) {
            // no point reporting this back to cea - it'll be ignored.
            logger.error("Could not pass on results message",e);
        }
          
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "JesResultListener";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Jes Implementation of CEA Results listener web interface";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: JesResultsListener.java,v $
Revision 1.4  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.3.124.1  2005/03/11 14:05:40  nw
minor change

Revision 1.3  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.2  2004/07/02 09:08:52  nw
improved logging

Revision 1.1  2004/07/01 21:15:00  nw
added results-listener interface to jes
 
*/