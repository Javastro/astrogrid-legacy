/*$Id: EmptyComponentManager.java,v 1.6 2004/03/15 23:45:07 nw Exp $
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
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.JobScheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/** Empty Component manager - needs components registered with it to be useful.
 * <p>
 * This class implements the abstract methods - subclasses register components with the picocontainer.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class EmptyComponentManager implements ComponentManager {

    protected static final Log log = LogFactory.getLog(ComponentManager.class);   
    /** Construct a new AbstractComponentManager
     * 
     */
    public EmptyComponentManager() {
        pico = new DefaultPicoContainer();
    }

        /** the picocontainer that manages the components */
      protected final MutablePicoContainer pico;
      
      public BeanFacade getFacade() {
          return (BeanFacade)pico.getComponentInstanceOfType(BeanFacade.class);
      }

      public JobScheduler getNotifier() {
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
    
      public String informationHTML() {
          getNotifier();
          getMonitor();
          getController();
       
              StringWriter sw = new StringWriter();
              PrintWriter p = new PrintWriter(sw);
              p.println("<h1>Component Information</h1>");
              for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
                  Object o = i.next();
                  if (o instanceof ComponentDescriptor) {
                      ComponentDescriptor descr = (ComponentDescriptor)o;
                      p.println("<b>" + descr.getName() + "</b> ");
                      p.println("<blockquote><pre>" + descr.getDescription() + "</pre></blockquote>");                
                  } else {
                      p.println(o.getClass().getName() + ":" + o.toString());
                  }
                  p.println("<hr/>");
              }
              return sw.toString();
      }
    
      public String information() {
          getNotifier();
          getMonitor();
          getController();
       
              StringWriter sw = new StringWriter();
              PrintWriter p = new PrintWriter(sw);
              p.println("Component Information");
              p.println("-------------------------------");
              for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
                  Object o = i.next();
                  if (o instanceof ComponentDescriptor) {
                      ComponentDescriptor descr = (ComponentDescriptor)o;
                      p.println(descr.getName());
                      p.println(descr.getDescription());              
                  } else { 
                      p.println(o.getClass().getName() + ":" + o.toString());
                  }
                  p.println();
              }
              return sw.toString();
      }
            
    public Test getSuite() {
        // drag in main components - ensure everythng is configured.
        getNotifier();
        getMonitor();
        getController();
               
        TestSuite result = new TestSuite("Installation Tests");
        for (Iterator i = getContainer().getComponentInstances().iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ComponentDescriptor) {
                ComponentDescriptor descr  = (ComponentDescriptor)o;
                Test test = descr.getInstallationTest();
                if (test != null) {
                    result.addTest(test);
                }
            }
        }
        return result;
    }
    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        log.info("Starting component manager");
        pico.start();
    }
    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        log.info("Stopping component manager");
        pico.stop();
    }

    /**
     * @see org.astrogrid.jes.component.ComponentManager#getContainer()
     */
    public MutablePicoContainer getContainer() {
        return pico;
    }      
    
}


/* 
$Log: EmptyComponentManager.java,v $
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