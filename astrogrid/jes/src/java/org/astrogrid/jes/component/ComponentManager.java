/*$Id: ComponentManager.java,v 1.8 2004/03/15 23:45:07 nw Exp $
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
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.JobScheduler;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

import junit.framework.Test;
/** Interface to a container for components.
 * <p>
 * Based on an underlying <a href="http://www.picocontainer.org/">PicoContainer</a> implementation
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public interface ComponentManager extends Startable{
    /** get the picocontainer that manages the components */
    public abstract MutablePicoContainer getContainer();
    /** access the bean facade component
     * @return
     */
    public abstract BeanFacade getFacade();
    /** access the job scheduler component.
     * @todo rename to getScheduler
     */
    public abstract JobScheduler getNotifier();
    /**
     * @return
     */
    /** access the job monitor component */
    public abstract JobMonitor getMonitor();
    /** access the job controller component */
    public abstract JobController getController();
    /** output human-readable description of contents of container as HTML
     *  <p>
     * used for debugging / output to JSP 
     * Uses descriptions from components that implement the {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface*/
    public abstract String informationHTML();
    /** output human-readable description of contents of container */
    public abstract String information();
    /** return a suite of installation tests for the components in the container 
     * <p>
     * this suite is the composition of all installation tests returned by objects in the container that implement the
     * {@link org.astrogrid.jes.component.descriptor.ComponentDescriptor} interface
     * */
    public abstract Test getSuite();
}
/* 
$Log: ComponentManager.java,v $
Revision 1.8  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.7  2004/03/15 00:30:19  nw
updaed to refer to moved classes

Revision 1.6  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.5  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/