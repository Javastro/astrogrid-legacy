/*$Id: MockDispatcher.java,v 1.7 2004/03/15 01:29:13 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.Test;


/** Mock implementation of a dispatcher.
 * can be configured to fail.

 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public class MockDispatcher implements Dispatcher, ComponentDescriptor {
    /** Construct a new MockDispatcher
     * 
     */
    public MockDispatcher() {
        this(true);
    }
    
    public MockDispatcher(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    

    protected final boolean willSucceed;
    protected int callCount;

    public int getCallCount() {
        return callCount;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(java.lang.String, org.astrogrid.jes.job.JobStep)

     */
    public void dispatchStep(Workflow job, Step js) throws JesException {
        
        callCount ++;
            if (!willSucceed) {
                throw new JesException("You wanted me to barf");
            }
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Mock Step Dispatcher";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Mock, just counts number of method calls\n"
            + (willSucceed ? "configured to succeed on method call" : "configured to fail on method call, throwing an exception");
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: MockDispatcher.java,v $
Revision 1.7  2004/03/15 01:29:13  nw
factored component descriptor out into separate package

Revision 1.6  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.5.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.5  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/