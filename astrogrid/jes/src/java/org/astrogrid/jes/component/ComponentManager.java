/*$Id: ComponentManager.java,v 1.5 2004/03/07 21:04:38 nw Exp $
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
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.job.BeanFacade;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

import junit.framework.Test;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public interface ComponentManager extends Startable{
    public abstract MutablePicoContainer getContainer();
    /**
     * @return
     */
    public abstract BeanFacade getFacade();
    /**
     * @return
     */
    public abstract SchedulerNotifier getNotifier();
    /**
     * @return
     */
    public abstract JobScheduler getScheduler();
    public abstract JobMonitor getMonitor();
    public abstract JobController getController();
    /** used for debugging / output to JSP */
    public abstract String informationHTML();
    public abstract String information();
    /** search components for tests to run */
    public abstract Test getSuite();
}
/* 
$Log: ComponentManager.java,v $
Revision 1.5  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/