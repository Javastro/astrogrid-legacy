/*$Id: JesDelegateFactory.java,v 1.4 2004/03/05 16:16:23 nw Exp $
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

import org.astrogrid.jes.delegate.impl.JobControllerDelegate;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;

/** Factory / facade class that draws all the different delegate factories together.
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

}


/* 
$Log: JesDelegateFactory.java,v $
Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2.2.1  2004/02/11 16:09:10  nw
refactored delegates (again)

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/