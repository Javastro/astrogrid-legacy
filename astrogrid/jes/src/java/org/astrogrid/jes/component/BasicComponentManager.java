/*$Id: BasicComponentManager.java,v 1.7 2004/03/18 10:53:54 nw Exp $
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

import org.astrogrid.jes.component.descriptor.*;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.impl.SchedulerTaskQueueDecorator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.jobscheduler.policy.JoinPolicy;
import org.astrogrid.jes.jobscheduler.policy.LinearPolicy;

import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;

import java.net.URL;

/** Basic stand-alone component set up - no need for external config file.
 * <p>
 * used for testing, etc.
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
        pico.registerComponentImplementation(JobScheduler.class,SchedulerTaskQueueDecorator.class,
        new Parameter[] {
            new ComponentParameter(SCHEDULER_ENGINE)
        }
        );
        
        pico.registerComponentImplementation(SCHEDULER_ENGINE,org.astrogrid.jes.jobscheduler.impl.SchedulerImpl.class);
        pico.registerComponentImplementation(Policy.class,JoinPolicy.class);
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
Revision 1.7  2004/03/18 10:53:54  nw
upgraded to use join policy

Revision 1.6  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.5  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.4  2004/03/15 00:30:19  nw
updaed to refer to moved classes

Revision 1.3  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/