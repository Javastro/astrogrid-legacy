/*$Id: SchedulerInternal.java,v 1.2 2005/11/24 01:13:24 nw Exp $
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
    public void executePeriodically(long milliseconds, Runnable task);
    
    /** execute a task as soon as possible */
    public void runNow(Runnable task) ;
}


/* 
$Log: SchedulerInternal.java,v $
Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:44:14  nw
improved interface.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/