/*$Id: RemoteProgressListener.java,v 1.5 2004/07/26 12:07:38 nw Exp $
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

/** A Progress Listener  that relays  state changes and other messages from an application back home to a
 * 'jobMonitor' interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class RemoteProgressListener extends AbstractProgressListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RemoteProgressListener.class);

    /** Construct a new RemoteProgressListener
     * @param endpoint the url endpoint for the service to relay to.
     * 
     */
    public RemoteProgressListener(URI endpoint) {
        super();
        delegate = JobMonitorDelegate.buildDelegate(endpoint.toString());
        this.endpoint = endpoint;
    }
    protected final JobMonitorDelegate delegate;
    protected final URI endpoint;

    /** relays a message back to the remote service.
     * 
     * any communication failure, logs a warning.
     */
    protected void reportMessage(Application app, MessageType message) {
        try {
            delegate.monitorJob(new JobIdentifierType( app.getJobStepID()),Castor2Axis.convert(message));
        }
        catch (JesDelegateException e) {
            logger.warn("Could not communicate with remote client" + endpoint,e);
        } catch (Throwable t) {// need to catch everything - otherwise I'm not sure what happens - think things get lost.
        logger.error("System problem in reportMessage() " + endpoint, t);
    }
    }
    /**Relays a status change back to the remote service
     * 
     * any communication failure, logs  a warning
     */
    protected void reportStatusChange(Application app, Status status) {
        try {
            MessageType message = app.createTemplateMessage();
            message.setPhase(status.toExecutionPhase());
            message.setLevel(LogLevel.INFO);
            message.setContent("Application enters new phase");
            delegate.monitorJob(new JobIdentifierType( app.getJobStepID()),Castor2Axis.convert(message));
        }
        catch (JesDelegateException e) {
            logger.warn("Could not communicate with remote client" + endpoint,e);
        } catch (Throwable t) {// need to catch everything - otherwise I'm not sure what happens - think things get lost.
        logger.error("System problem in reportStatusChange" + endpoint, t);
    }       
    }
}


/* 
$Log: RemoteProgressListener.java,v $
Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/20 02:03:08  nw
added abstract listener classes

Revision 1.3  2004/07/02 09:11:13  nw
improved logging

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/