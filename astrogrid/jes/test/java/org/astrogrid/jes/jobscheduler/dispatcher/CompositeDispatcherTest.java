/*$Id: CompositeDispatcherTest.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
 * Created on 11-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

/** unit test for composite dispatcher. will be same as ceaApplicationDispatcherfor now.
 * @todo add in other dispatchers when they are ready.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Mar-2005
 *
 */
public class CompositeDispatcherTest extends CeaApplicationDispatcherTest {

    /** Construct a new CompositeDispatcherTest
     * @param arg
     */
    public CompositeDispatcherTest(String arg) {
        super(arg);
    }
    
    protected void setUp() throws Exception{
        super.setUp();
        compositeDispatcher = new CompositeDispatcher(ceaDispatcher,null,null,null);
    }
    
    protected CompositeDispatcher compositeDispatcher;
    
    public void testDispatchStep(Workflow w,Step js) throws Exception {
        compositeDispatcher.dispatchStep(w,js.getTool(),"someID");
    }

}


/* 
$Log: CompositeDispatcherTest.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:03:47  nw
tests for different dispatchers.
 
*/