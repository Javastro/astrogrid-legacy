/*$Id: BasicComponentManager.java,v 1.2 2004/03/07 21:04:38 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.jes.comm.JobScheduler;
import org.astrogrid.jes.comm.MemoryQueueSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.jobscheduler.policy.LinearPolicy;

import java.net.URL;

/** Basic stand-alone component set up - no need for external config file.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class BasicComponentManager extends EmptyComponentManager {
    /** Construct a new BasicComponentManager
     * 
     */
    public BasicComponentManager() throws ComponentManagerException{
        super();
        try {
        defaultCallbackURL = new URL("http://localhost:8080/astrogrid-jes/services/JobMonitor");
        pico.registerComponentInstance("jes-meta",JES_META);
        pico.registerComponentImplementation(SchedulerNotifier.class,MemoryQueueSchedulerNotifier.class);
        pico.registerComponentImplementation(JobScheduler.class,org.astrogrid.jes.jobscheduler.JobScheduler.class);
        pico.registerComponentImplementation(Policy.class,LinearPolicy.class);
        pico.registerComponentImplementation(Dispatcher.class,ApplicationControllerDispatcher.class);
        pico.registerComponentInstance(ApplicationControllerDispatcher.MonitorEndpoint.class, 
            new ApplicationControllerDispatcher.MonitorEndpoint() {
            public URL getURL() {
                return defaultCallbackURL;
            }
        });
        pico.registerComponentImplementation(Locator.class,XMLFileLocator.class);
        pico.registerComponentInstance(XMLFileLocator.ToolList.class,
            new XMLFileLocator.ToolList() {

                public URL getURL() {
                    return this.getClass().getResource("/org/astrogrid/jes/jobscheduler/locator/tools.xml");
                }
            });
            
        pico.registerComponentImplementation(BeanFacade.class,CastorBeanFacade.class);
        pico.registerComponentImplementation(AbstractJobFactoryImpl.class,InMemoryJobFactoryImpl.class); // could possibly get away with file here..
        pico.registerComponentImplementation(JobMonitor.class,org.astrogrid.jes.jobmonitor.JobMonitor.class);
        pico.registerComponentImplementation(JobController.class,org.astrogrid.jes.jobcontroller.JobController.class);       
        } catch (Exception e) {
            log.fatal("Could not create component manager",e);
            throw new ComponentManagerException(e);
        }
    }

    // will do something better here later.
    protected final URL defaultCallbackURL; 
    
    /** @todo add config tests for a basic system in here */
    private static final ComponentDescriptor JES_META = new SimpleComponentDescriptor(
        "Basic Job Execution System",
        "job execution system - testing configuration"
    );
    
    
    
}


/* 
$Log: BasicComponentManager.java,v $
Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/