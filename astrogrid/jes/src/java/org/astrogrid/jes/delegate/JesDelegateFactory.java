/*$Id: JesDelegateFactory.java,v 1.5 2004/03/10 12:13:37 nw Exp $
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

import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.delegate.impl.JobControllerDelegate;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;

import java.net.URL;

/** Factory for jes delegates.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class JesDelegateFactory {
    
    /** default key to search config for a controller endpoint */
    public static final String JOB_CONTROLLER_ENDPOINT_KEY = "jes.job.controller.endpoint";
    /** default key to search config for a monitor endpoint */
    public static final String JOB_MONITOR_ENDPOINT_KEY = "jes.job.monitor.endpoint";
    
    /** create a job controller delegate
     * 
     * @param url endpoint 
     * @return a delegate
     */
    public static JobController createJobController(String url) {
        return JobControllerDelegate.buildDelegate(url);
    } 
    /** create a job controller delegate
     * 
     * @param url endpoint of service to connect to
     * @param timeout timeout value to use for connection
     * @return a delegate
     */
    public static JobController createJobController(String url, int timeout){ 
        return JobControllerDelegate.buildDelegate(url,timeout);
    }
    
    /**
     * create a job controller delegate, loading endpoint from Config
     * 
     * @return a delegate
      @throws PropertyNotFoundException if key {@link #JOB_CONTROLLER_ENDPOINT_KEY} is not found in default config
      @see SimpleConfig
     */
    public static JobController createJobController() throws PropertyNotFoundException{
        URL endpoint = SimpleConfig.getSingleton().getUrl(JOB_CONTROLLER_ENDPOINT_KEY);
        return JobControllerDelegate.buildDelegate(endpoint.toString()); 
    }
    
    /** create a job monitor delegate
     * 
     * @param url endpoint of monitor service
     * @return a delegate for this service
     */
    public static JobMonitor createJobMonitor(String url) {
        return JobMonitorDelegate.buildDelegate(url);
    }
    /** create a job monitor delegate
     * 
     * @param url endpoint of monitor service
     * @param timeout timeout value for the connection
     * @return a delegate for this service
     */
    public static JobMonitor createJobMonitor(String url, int timeout) {
        return JobMonitorDelegate.buildDelegate(url,timeout);
    }
    
    /** create a job monitor delegate, loading endpoint from Config
     * 
     * @return a delegate
     * @throws PropertyNotFoundException if key {@link #JOB_MONITOR_ENDPOINT_KEY} is not found in default config
     * @see SimpleConfig
     */
    public static JobMonitor createJobMonitor() throws PropertyNotFoundException {
        URL endpoint = SimpleConfig.getSingleton().getUrl(JOB_MONITOR_ENDPOINT_KEY);
        return JobMonitorDelegate.buildDelegate(endpoint.toString());
    }

}


/* 
$Log: JesDelegateFactory.java,v $
Revision 1.5  2004/03/10 12:13:37  nw
added default delegate factory methods

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