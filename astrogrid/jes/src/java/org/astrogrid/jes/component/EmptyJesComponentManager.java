/*$Id: EmptyJesComponentManager.java,v 1.2 2004/07/09 09:30:28 nw Exp $
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

import org.astrogrid.component.EmptyComponentManager;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;

/** Empty Component manager - needs components registered with it to be useful.
 * <p>
 * This class implements the abstract methods - subclasses register components with the picocontainer.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class EmptyJesComponentManager extends EmptyComponentManager implements JesComponentManager {


      public JobScheduler getScheduler() {
          return (JobScheduler)pico.getComponentInstance(JobScheduler.class);
      }
    /** key under which the back-end job scheduler engine should be registered (if different from the public interface of the job scheduler */
    protected static final String SCHEDULER_ENGINE = "scheduler-engine";
    
      public JobMonitor getMonitor() {
          return (JobMonitor)pico.getComponentInstanceOfType(JobMonitor.class);
      }
    
      public JobController getController() {
          return (JobController)pico.getComponentInstanceOfType(JobController.class);
      }
    /**
     * @see org.astrogrid.jes.component.ComponentManager#getResultsListener()
     */
    public ResultsListener getResultsListener() {
        return (ResultsListener)pico.getComponentInstanceOfType(ResultsListener.class);
    }     
   
    
}


/* 
$Log: EmptyJesComponentManager.java,v $
Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.7  2004/07/01 21:15:00  nw
added results-listener interface to jes

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