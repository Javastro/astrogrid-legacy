/*$Id: JesDelegateFactory.java,v 1.2 2004/02/09 11:41:44 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;

import org.astrogrid.jes.delegate.jobController.JobControllerDelegate;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorDelegate;
import org.astrogrid.jes.delegate.jobScheduler.JobSchedulerDelegate;

/** Factory / facade class that draws all the different delegate factories together.
 * @future - draw all backend implementation classes together into a single package - too spread out at present.
 * have deprecated all the individual delegate classes to encourage use of this class.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class JesDelegateFactory {
    public static JobController createJobController(String url) {
        return JobControllerDelegate.buildDelegate(url);
    } 
    public static JobController createJobController(String url, int timeout){ 
        return JobControllerDelegate.buildDelegate(url,timeout);
    }
    public static JobMonitor createJobMonitor(String url) {
        return JobMonitorDelegate.buildDelegate(url);
    }
    public static JobMonitor createJobMonitor(String url, int timeout) {
        return JobMonitorDelegate.buildDelegate(url,timeout);
    }
    public static JobScheduler createJobScheduler(String url) {
        return JobSchedulerDelegate.buildDelegate(url);
    }
    public static JobScheduler createJobScheduler(String url, int timeout) {
        return JobSchedulerDelegate.buildDelegate(url,timeout);
    }
}


/* 
$Log: JesDelegateFactory.java,v $
Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/