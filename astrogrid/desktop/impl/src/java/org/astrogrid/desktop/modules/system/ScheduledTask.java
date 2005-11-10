/*$Id: ScheduledTask.java,v 1.1 2005/11/10 12:05:53 nw Exp $
 * Created on 05-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

/** interface to a schedulable task.
 * any instances of this interfae will be automagically registered with the scheduler on startup.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2005
 *
 */
public interface ScheduledTask extends Runnable {
   public long getPeriod();
   
}


/* 
$Log: ScheduledTask.java,v $
Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout
 
*/