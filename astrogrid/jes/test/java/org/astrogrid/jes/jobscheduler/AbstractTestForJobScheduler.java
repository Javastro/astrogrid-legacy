/*$Id: AbstractTestForJobScheduler.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.jobcontroller.AbstractTestForJobController;
import org.astrogrid.jes.jobscheduler.dispatcher.*;
import org.astrogrid.jes.jobscheduler.policy.MockPolicy;

/** Base class that creates framework for testing the scheduler. 
 * <p>
 * subclasses can override parts to get new component behaviours
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public abstract class AbstractTestForJobScheduler extends AbstractTestForJobController {
    /**
     * Constructor for JobSchedulerTest.
     * @param arg0
     */
    public AbstractTestForJobScheduler(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        dispatcher = createDispatcher();
        policy = createPolicy();
        js = new JobScheduler(facade,dispatcher,policy);
        
    }
    
    protected Policy createPolicy() {
        return new MockPolicy();
    }
    
    protected Dispatcher createDispatcher() {
        return new MockDispatcher();
    }
    
    protected Policy policy;
    protected Dispatcher dispatcher;
    protected JobScheduler js;
    

}


/* 
$Log: AbstractTestForJobScheduler.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.2.4.1  2004/02/17 12:57:11  nw
improved documentation
 
*/