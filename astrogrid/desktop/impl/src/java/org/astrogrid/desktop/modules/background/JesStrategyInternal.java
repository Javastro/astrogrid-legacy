/*$Id: JesStrategyInternal.java,v 1.1 2005/11/10 10:46:58 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.system.ScheduledTask;

/** interface to an internal service that is a strategy for dispatching workflows, and
 * also monitors progress of workflows
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *
 */
public interface JesStrategyInternal extends ScheduledTask, RemoteProcessStrategy {
    public void triggerUpdate();
}


/* 
$Log: JesStrategyInternal.java,v $
Revision 1.1  2005/11/10 10:46:58  nw
big change around for vo lookout
 
*/