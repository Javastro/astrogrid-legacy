/*$Id: SchedulerInternal.java,v 1.1 2005/11/10 12:05:53 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

/** Internal interface to a low-level scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public interface SchedulerInternal {
    /** execute a task periodically 
     * @return a key for this scheduled task*/
    public Object executePeriodically(long milliseconds, Runnable task);
    
    /** execute a previously-registered task as soon as possible */
    public void runNow(Object key) ;
}


/* 
$Log: SchedulerInternal.java,v $
Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/