/*$Id: BasicJesComponentManager.java,v 1.4 2004/08/03 16:31:25 nw Exp $
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

import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.jes.component.production.GroovyComponentManager;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher;
import org.astrogrid.jes.jobscheduler.impl.SchedulerTaskQueueDecorator;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator;
import org.astrogrid.jes.resultlistener.JesResultsListener;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;

import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;

import java.net.URI;
import java.net.URL;

/** Basic stand-alone component set up - no need for external config file.
 * <p>
 * used for testing, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class BasicJesComponentManager extends EmptyJesComponentManager {
    
    /** Construct a new BasicComponentManager
     * 
     */
    public BasicJesComponentManager() throws ComponentManagerException{
        super();
        try {
        defaultCallbackURL = new URI("http://localhost:8080/astrogrid-jes/services/JobMonitor");
        defaultResultListenerURL = new URI("http://localhost:8080/astrogrid-jes/service/ResultListenerService");
        pico.registerComponentInstance("jes-meta",JES_META);
        pico.registerComponentImplementation(JobScheduler.class,SchedulerTaskQueueDecorator.class,
        new Parameter[] {
            new ComponentParameter(SCHEDULER_ENGINE)
        }
        );

         GroovyComponentManager.registerGroovyEngine(pico);
        pico.registerComponentImplementation(Dispatcher.class,ApplicationControllerDispatcher.class);
        pico.registerComponentInstance(ApplicationControllerDispatcher.Endpoints.class, 
            new ApplicationControllerDispatcher.Endpoints() {
            public URI monitorEndpoint() {
                return defaultCallbackURL;
            }

            public URI resultListenerEndpoint() {
                return defaultResultListenerURL;
            }            
        });
        pico.registerComponentImplementation(Locator.class,XMLFileLocator.class);
        pico.registerComponentInstance(XMLFileLocator.ToolList.class,
            new XMLFileLocator.ToolList() {

                public URL getURL() {
                    return this.getClass().getResource("/org/astrogrid/jes/jobscheduler/locator/tools.xml");
                }
            });
            
        pico.registerComponentImplementation(AbstractJobFactoryImpl.class,InMemoryJobFactoryImpl.class); // could possibly get away with file here..
        pico.registerComponentImplementation(JobMonitor.class,org.astrogrid.jes.jobmonitor.JobMonitor.class);
        pico.registerComponentImplementation(ResultsListener.class,JesResultsListener.class);
        pico.registerComponentImplementation(JobController.class,org.astrogrid.jes.jobcontroller.JobController.class);       
        } catch (Exception e) {
            log.fatal("Could not create component manager",e);
            throw new ComponentManagerException(e);
        }
    }

    // will do something better here later.
    protected final URI defaultCallbackURL; 
    protected final URI defaultResultListenerURL;
    /** @todo add config tests for a basic system in here */
    private static final ComponentDescriptor JES_META = new SimpleComponentDescriptor(
        "Basic Job Execution System",
        "job execution system - testing configuration"
    );
    
    
    
}


/* 
$Log: BasicJesComponentManager.java,v $
Revision 1.4  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.3  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.2.20.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.2.20.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.2.20.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.9  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.8  2004/07/01 11:19:05  nw
updated interface with cea - part of cea componentization

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