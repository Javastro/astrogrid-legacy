/*$Id: MessagingImpl.java,v 1.3 2006/05/26 15:23:23 nw Exp $
 * Created on 28-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.astrogrid.desktop.modules.system.SchedulerInternal;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;


/** Implementation of the messaging component.
 * main thing it has to consider is to allow various threads to inject messages without blocking or contention.
 * and then these messages are passed to all event processors - which, again, are executed in a concurrency-sensitive manner.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Mar-2006
 *
 */
public class MessagingImpl implements MessagingInternal{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(MessagingImpl.class);

    /** Construct a new MessagingImpl
     * 
     */
    public MessagingImpl() {
        super();
        this.listeners = new HashSet();
        this.singleThread = new QueuedExecutor(); // has a limited capacity - but think this shouldn't be a problem.
    }
    private final Executor singleThread;
    private final Set listeners;
    /**
     * @see org.astrogrid.desktop.modules.ag.MessagingInternal#injectMessage(org.astrogrid.desktop.modules.ag.MessagingInternal.Message)
     */
    public void injectMessage(final SourcedExecutionMessage m) {
        // queue can't accept multiple concurrent puts - so need to single-thread these
        //using the scheduler to do this.
            try {
				singleThread.execute(new Runnable() {
				    public void run() {
				        // should I create a copy of the list of listeners instead?
				        synchronized(MessagingImpl.this) { // ensures that the listeners list isn't modified while we're iterating over it.
				            for (Iterator i = listeners.iterator(); i.hasNext(); ) {
				                MessageListener l = (MessageListener)i.next();
				                // shold I clone the message? - nope. assume they're good citizens
				                // maybe make message an immutable interface, or value object
				                l.onMessage(m);
				            }
				        }
				    }
				});
			} catch (InterruptedException x) {
				logger.error("InterruptedException",x);
			}
    }

    /** 
     * @see org.astrogrid.desktop.modules.ag.MessagingInternal#addEventProcessor(java.lang.String, org.astrogrid.desktop.modules.ag.MessagingInternal.MessageListener)
     */
    public synchronized void addEventProcessor( MessageListener l) {
        if (! listeners.contains(l)) {
            listeners.add(l);
        }
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.MessagingInternal#removeEventProcessor(org.astrogrid.desktop.modules.ag.MessagingInternal.MessageListener)
     */
    public synchronized void removeEventProcessor(MessageListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }


}


/* 
$Log: MessagingImpl.java,v $
Revision 1.3  2006/05/26 15:23:23  nw
reworked scheduled tasks,

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/04/04 10:31:25  nw
preparing to move to mac.
 
*/