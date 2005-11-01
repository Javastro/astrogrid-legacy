/*$Id: SchedulerInternal.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public interface SchedulerInternal {
    /** execute a task periodically */
    public void executePeriodically(long milliseconds, Runnable task);
    
}


/* 
$Log: SchedulerInternal.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/