/*$Id: RemoteProgressListener.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 17-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.observer;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.util.Observable;
import java.util.Observer;

/** Observable that listens for state changes and other messages from an application and then relays them back home to a
 * 'jobMonitor' interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class RemoteProgressListener implements Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RemoteProgressListener.class);

    /** Construct a new RemoteProgressListener
     * 
     */
    public RemoteProgressListener(URI endpoint) {
        super();
        delegate = JobMonitorDelegate.buildDelegate(endpoint.toString());
        this.endpoint = endpoint;
    }
    protected final JobMonitorDelegate delegate;
    protected final URI endpoint;
    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        Application app = (Application)o;
        if (arg instanceof Status) {
            reportStatusChange(app,(Status)arg);
        } else if (arg instanceof MessageType) {
            reportMessage(app,(MessageType)arg);
            
        } else {
            logger.error("Unknown object in update notification " + arg.getClass().getName() + " " + arg.toString());
        }
    }
    /**
     * @param app
     * @param type
     */
    private void reportMessage(Application app, MessageType message) {
        try {
            delegate.monitorJob(new JobIdentifierType( app.getJobStepID()),Castor2Axis.convert(message));
        }
        catch (JesDelegateException e) {
            logger.warn("Could not communicate with remote client" + endpoint,e);
        }
    }
    /**
     * @param app
     * @param status
     */
    private void reportStatusChange(Application app, Status status) {
        try {
            MessageType message = app.createTemplateMessage();
            message.setPhase(status.toExecutionPhase());
            message.setLevel(LogLevel.INFO);
            message.setContent("Application enters new phase");
            delegate.monitorJob(new JobIdentifierType( app.getJobStepID()),Castor2Axis.convert(message));
        }
        catch (JesDelegateException e) {
            logger.warn("Could not communicate with remote client" + endpoint,e);
        }        
    }
}


/* 
$Log: RemoteProgressListener.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/