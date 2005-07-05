/*$Id: InProcessQueryService.java,v 1.4 2005/07/05 08:27:01 clq2 Exp $
 * Created on 07-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher.inprocess;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.manager.DefaultApplicationEnvironmentRetriever;
import org.astrogrid.applications.manager.DefaultQueryService;
import org.astrogrid.applications.manager.observer.AbstractProgressListener;
import org.astrogrid.applications.manager.observer.AbstractResultsListener;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

/** Query service that allows registration of inprocess listeners to cea applications.
 *  - if the urn 'jes:inprocess' is passed to one of the register*Listener methods, 
 * then a listener will be registered with an application that calls directly back to the jobmonitor component.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
 *
 */
public class InProcessQueryService extends DefaultQueryService {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(InProcessQueryService.class);

    /** Construct a new InProcessQueryService
     * @param arg0
     * @param jm job monitor component to call back to.
     * @param jrl results listner component to call back to.
     */
    public InProcessQueryService(ExecutionHistory arg0, JobMonitor jm, ResultsListener jrl) {

       //FIXME - this should be registered in pico - just a quick fix here - the defaultApplicationEnvironment listener might not be appropriate - it just does nothing - probably ok in most of the in-process cases...
       super(arg0, new DefaultApplicationEnvironmentRetriever(arg0));
        pl = new InprocessProgressListener(jm);
        rl = new InprocessResultsListener(jrl);
    }

    protected final InprocessProgressListener pl ;
    protected final InprocessResultsListener rl ;

    public String getDescription() {
        return "Query service that supports a short-cut inprocess listener";
    }
    public String getName() {
        return "InProcessQueryService";
    }
    
    /** recognize a special uri protocol  - 'jes:inprocess', and in this case, register a direct progress listener */
    public boolean registerProgressListener(String arg0, URI arg1)
            throws CeaException {
        if (arg1.equals(INPROCESS_URI)) {
            if (!executionHistory.isApplicationInCurrentSet(arg0)) {
                return false;
            }
            Application app = executionHistory.getApplicationFromCurrentSet(arg0);
            app.addObserver(pl);
            return true;
        } else {
            return super.registerProgressListener(arg0, arg1);
        }
    }
    /** recognize a special uri protocol - 'jes:inprocess', and in this case, register
     * a progress listener that calls directly back to the jes results listener
     * @see org.astrogrid.applications.manager.QueryService#registerResultsListener(java.lang.String, java.net.URI)
     */
    public boolean registerResultsListener(String arg0, URI arg1)
            throws CeaException {
        if (arg1.equals(INPROCESS_URI)) {
            if (!executionHistory.isApplicationInCurrentSet(arg0)) {
                return false;
            }
            Application app = executionHistory.getApplicationFromCurrentSet(arg0);
            app.addObserver(rl);
            return true;
        } else {        
            return super.registerResultsListener(arg0, arg1);
        }
    }
    
    //trying to make a constant uri - but difficult as the constructor may throw exceptions.
    /** constant value for 'jes:inprocess' */
    public static URI INPROCESS_URI ;
    static {
        try {
        INPROCESS_URI = new URI("jes","inprocess",null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    /** 
     * Shortcut listener that notifies job monitor component directly 
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
     *@todo refactor these back into cea server? code is the same, just the construction
     *of the jobMonitor is different..
     */
    protected static class InprocessProgressListener extends AbstractProgressListener {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory.getLog(InprocessProgressListener.class);

        public InprocessProgressListener(JobMonitor jm) {
            this.jm = jm;
        }
        protected final JobMonitor jm;

        /**
         * @see org.astrogrid.applications.manager.observer.AbstractProgressListener#reportMessage(org.astrogrid.applications.Application, org.astrogrid.applications.beans.v1.cea.castor.MessageType)
         */
        protected void reportMessage(Application arg0, MessageType arg1) {
            try {
                jm.monitorJob(new JobIdentifierType(arg0.getJobStepID()),Castor2Axis.convert(arg1));
            } catch (RemoteException e) { // very unlikeley to happen, as in-process.
                logger.warn("JesDelegateException",e);
            }
        }

        /**
         * @see org.astrogrid.applications.manager.observer.AbstractProgressListener#reportStatusChange(org.astrogrid.applications.Application, org.astrogrid.applications.Status)
         */
        protected void reportStatusChange(Application app, Status status) {
            MessageType message = app.createTemplateMessage();
            message.setPhase(status.toExecutionPhase());
            message.setLevel(LogLevel.INFO);
            message.setContent("Application enters new phase");
            try {
                jm.monitorJob(new JobIdentifierType( app.getJobStepID()),Castor2Axis.convert(message));
            } catch (RemoteException e) {
                logger.warn("JesDelegateException",e);
            }
              
        }

    }
    
    /** 
     * Shortcut listener that notifies results listener component directly
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Feb-2005
     *
     */
    protected static class InprocessResultsListener extends AbstractResultsListener {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory.getLog(InprocessResultsListener.class);

        public InprocessResultsListener(ResultsListener jrl) {
            this.jrl = jrl;
        }
            protected final ResultsListener jrl;
        /**
         * @see org.astrogrid.applications.manager.observer.AbstractResultsListener#notifyResultsAvailable(org.astrogrid.applications.Application)
         */
        protected void notifyResultsAvailable(Application app) {
            try {
                jrl.putResults(new JobIdentifierType(app.getJobStepID()),Castor2Axis.convert( app.getResult()));
            } catch (RemoteException e) { // unlikely to happen.
                logger.warn("RemoteException",e);
            }
            
        }
    }

}
/* 
$Log: InProcessQueryService.java,v $
Revision 1.4  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.3.10.2  2005/06/09 10:06:08  pah
added comment that the application retriever should be registered in pico - if it means anything to retrieve environment in process..

Revision 1.3.10.1  2005/06/09 10:01:15  pah
added default environment retriever to constructor

Revision 1.3  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.2.20.1  2005/04/11 13:56:30  nw
organized imports

Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:04:30  nw
in-process cea server
 
*/