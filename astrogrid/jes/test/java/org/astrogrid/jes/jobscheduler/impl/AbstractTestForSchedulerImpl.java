/*$Id: AbstractTestForSchedulerImpl.java,v 1.3 2004/07/30 15:42:34 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.jes.jobcontroller.AbstractTestForJobController;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;

/** Base class that creates framework for testing the scheduler. 
 * <p>
 * subclasses can override parts to get new component behaviours
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public abstract class AbstractTestForSchedulerImpl extends AbstractTestForJobController {
    /**
     * Constructor for JobSchedulerTest.
     * @param arg0
     */
    public AbstractTestForSchedulerImpl(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        dispatcher = createDispatcher();
        scheduler = createScheduler();
        
    }
    
 
    
    protected Dispatcher createDispatcher() {
        return new MockDispatcher();
    }
    
    protected abstract AbstractJobSchedulerImpl createScheduler() throws Exception ;
    
    protected Dispatcher dispatcher;
    protected AbstractJobSchedulerImpl scheduler;
    

}


/* 
$Log: AbstractTestForSchedulerImpl.java,v $
Revision 1.3  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.2.20.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.2  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.8.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter

Revision 1.1  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.3  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.2.4.1  2004/02/17 12:57:11  nw
improved documentation
 
*/