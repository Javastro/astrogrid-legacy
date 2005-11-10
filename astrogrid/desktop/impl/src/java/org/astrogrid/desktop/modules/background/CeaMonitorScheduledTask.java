/*$Id: CeaMonitorScheduledTask.java,v 1.1 2005/11/10 10:46:58 nw Exp $
 * Created on 05-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.desktop.modules.system.ScheduledTask;

/** periodically poll remote cea sevices, inject messages into the system
 * 
 * temporary class, until soap bridge enables us to get callbacks straight from remote cea servers
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2005
 *
 */
public class CeaMonitorScheduledTask implements ScheduledTask {

    public CeaMonitorScheduledTask() {
        super();
    }

    public long getPeriod() {
        return 1000 * 60 * 3;
    }

    public void run() {
        // need to maintain list of submitted cea servers here.. - so I know where and what to poll.
        // similar to what was done in job monitor - crib that
        //no - folders themselves maintain list of cea servers.!
        // this class iterates through them (filtering the incomplete ones), and polls each server
        // any change causes a new message to be emitted
        
        // means that new folder mst be added whenever a cea app is executed - i.e. something (tjis?)
        // must inject a message to start it all off.
    }

}


/* 
$Log: CeaMonitorScheduledTask.java,v $
Revision 1.1  2005/11/10 10:46:58  nw
big change around for vo lookout
 
*/