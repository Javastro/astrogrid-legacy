/*$Id: TheadPoolSystemTest.java,v 1.7 2009/05/15 22:51:20 pah Exp $
 * Created on 14-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import static org.junit.Assert.*;

import java.util.concurrent.ThreadPoolExecutor;

import net.ivoa.uws.ExecutionPhase;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.authorization.NullPolicyDecisionPoint;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.junit.Test;

/** same as the existing system test, but using the thread pool execution controller.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 21 Apr 2008
 *
 */
public class TheadPoolSystemTest extends SystemTest {

    public TheadPoolSystemTest(){
	System.out.println("construct TheadPoolSystemTest");
    }
    private ThreadPoolExecutionController threadpoolController;

    @Override
    public void setUp() throws Exception {
        super.setUp();
         
        ExecutionPolicy policy = new DefaultExecutionPolicy();
        ThreadPoolExecutor pool = new CeaThreadPoolExecutor(policy);
	threadpoolController = new ThreadPoolExecutionController(lib,history,policy, pool, new NullPolicyDecisionPoint());
        controller = threadpoolController;
    }
    
    @Test
    public void testConcurrent() throws CeaException, InterruptedException{
	String id[] = new String[10];
	for (int i = 0; i < id.length; i++) {
	    id[i] = controller.init(tool, "job"+i, secGuard);
	}
	for (int i = 0; i < id.length; i++) {
	  boolean ok = controller.execute(id[i], secGuard);
	  assertTrue("job did not start", ok);
	}
	assertEquals("first job should be executing by now - might just be a timing issue - rerun test", Status.RUNNING, history.getApplicationFromCurrentSet(id[0]).getStatus());
	assertEquals("fifth job should still be Queued", Status.QUEUED,history.getApplicationFromCurrentSet(id[4]).getStatus());
	
	//wait until all have chance to finish
	while(history.isApplicationInCurrentSet(id[9]))
	{  
	    System.err.println("queue length "+ threadpoolController.getQueue().size()); 
	    Thread.sleep(1000);
	}
	
	
    }

    @Override
    protected String sleeptime() {
	return "5";
    }
    
    @Test
    public void testAbort() throws CeaException, InterruptedException {
	String id = controller.init(tool, "abort1", secGuard );
	controller.execute(id, secGuard);
	boolean abort = controller.abort(id, secGuard);
	assertTrue("unsuccessful abort", abort);
    }
    
    @Test
    public void testAutoKill() throws CeaException, InterruptedException {
        
        ExecutionPolicy policy = new ExecutionPolicy() {

	    public int getMaxRunTime() {
		return 2;
	    }

	    public int getKillPeriod() {
		return 1;
	    }

	    public int getDefaultLifetime() {
		return 1;
	    }

	    public int getDestroyPeriod() {
		return 1;
	    }

	    public int getMaxConcurrent() {
		return 4;
	    }
	    
	    
            
        };
        ThreadPoolExecutor pool = new CeaThreadPoolExecutor(policy);
        
	threadpoolController = new ThreadPoolExecutionController(lib,history,policy, pool, new NullPolicyDecisionPoint());
        controller = threadpoolController;
	String id = controller.init(tool, "abort1", secGuard );
	controller.execute(id, secGuard);
	Thread.sleep(6000); // wait long enough for the abort to happen;
	assertTrue("application should not be in current set", !history.isApplicationInCurrentSet(id));
	ExecutionSummaryType eh = history.getApplicationFromArchive(id);
	assertNotNull(eh);
	assertEquals("phase for aborted application", ExecutionPhase.ABORTED, eh.getPhase());
    }

}


/* 
$Log: TheadPoolSystemTest.java,v $
Revision 1.7  2009/05/15 22:51:20  pah
ASSIGNED - bug 2911: improve authz configuration
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
combined agast and old stuff
refactored to a more specific CEA policy interface
made sure that there are decision points nearly everywhere necessary  - still needed on the saved history

Revision 1.6  2009/05/11 10:50:45  pah
ASSIGNED - bug 2911: improve authz configuration
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
get unit test working again

Revision 1.5  2008/09/13 09:51:05  pah
code cleanup

Revision 1.4  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.3  2008/09/03 14:19:04  pah
result of merge of pah_cea_1611 branch

Revision 1.2.212.5  2008/09/03 12:22:55  pah
improve unit tests so that they can run in single eclipse gulp

Revision 1.2.212.4  2008/08/29 07:28:30  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.2.212.3  2008/05/13 15:57:32  pah
uws with full app running UI is working

Revision 1.2.212.2  2008/05/08 22:40:53  pah
basic UWS working

Revision 1.2.212.1  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.2  2004/09/17 01:22:12  nw
updated tests

Revision 1.1.2.1  2004/09/14 13:44:38  nw
added tests for thread pool implementation
 
*/