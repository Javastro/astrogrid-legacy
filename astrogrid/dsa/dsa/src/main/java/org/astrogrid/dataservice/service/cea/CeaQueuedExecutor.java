/*$Id: CeaQueuedExecutor.java,v 1.1 2009/05/13 13:20:30 gtr Exp $
 * Created on 17-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.dataservice.service.cea;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.picocontainer.Startable;

import junit.framework.Test;

import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

/** simple extension of hte standard queued executor - so it fits in with the cea component system.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Sep-2004
 *
 */
public class CeaQueuedExecutor extends QueuedExecutor implements
        ComponentDescriptor, Startable {

    /** Construct a new CeaQueuedExecutor
     * @param arg0
     */
    public CeaQueuedExecutor(Channel arg0) {
        super(arg0);
    }

    /** Construct a new CeaQueuedExecutor
     * 
     */
    public CeaQueuedExecutor() {
        super();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Queued Executor";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        super.shutdownNow();
    }

}


/* 
$Log: CeaQueuedExecutor.java,v $
Revision 1.1  2009/05/13 13:20:30  gtr
*** empty log message ***

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/09/17 01:27:21  nw
added thread management.
 
*/
