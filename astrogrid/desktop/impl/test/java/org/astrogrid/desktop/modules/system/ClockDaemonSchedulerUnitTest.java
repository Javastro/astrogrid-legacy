/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;
import org.easymock.IAnswer;
import org.joda.time.Duration;

import junit.framework.TestCase;
import static org.astrogrid.Fixture.*;
import static org.easymock.EasyMock.*;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20087:15:46 AM
 */
public class ClockDaemonSchedulerUnitTest extends TestCase {

    private UIContext context;
    private SessionManagerInternal session;
    private Principal principal;

    @Override
    protected void setUp() throws Exception {
        this.context = createMockContext();
        this.session = createMock(SessionManagerInternal.class);
        expect(session.getDefaultSessionId()).andStubReturn("defaultSession");
        this.principal = createMock(Principal.class);
        expect(session.findSessionForKey("defaultSession")).andStubReturn(principal);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        context = null;
        session = null;
        principal = null;
    }
 
    private void verifyAll() {
        verify(context,session,principal);
    }

    private void replayAll() {
        replay(context,session,principal);
    }

    public void testInstantiateNoTasks() throws Exception {
        
        replayAll();
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(Collections.EMPTY_LIST,context,session);
        
        scheduler.halting();
        verifyAll();
    }
    
    // start with scheduled task. -  - not testing timing of tasks very strictly here.
 public void testInstantiateScheduledTask() throws Exception {
        ScheduledTask task = createMock(ScheduledTask.class);
        expect(task.getName()).andStubReturn("testTask");
        expect(task.getPeriod()).andStubReturn(300);
        expect(task.getPrincipal()).andStubReturn(null);
        task.execute((WorkerProgressReporter)notNull());
        expectLastCall().times(2,5);
        replay(task);
        replayAll();
        List l = new ArrayList();
        l.add(task);
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(l,context,session);
        // should start up ok.
        // wait for it to execute twice
        Thread.sleep(1000);
        scheduler.halting();
        verifyAll();
        verify(task);
    }        
    
    // start with delayed continuation - not testing timing of tasks very strictly here.
 public void testInstantiateDelayedContinuation() throws Exception {
     DelayedContinuation task = createMock(DelayedContinuation.class);
     expect(task.getTitle()).andStubReturn("testTask");
     expect(task.getDelay()).andStubReturn(new Duration(300));
     expect(task.getPrincipal()).andStubReturn(null);
     expect(task.execute()).andReturn(task).times(2);
     expect(task.execute()).andReturn(null);

     replay(task);
     replayAll();
     List l = new ArrayList();
     l.add(task);
     ClockDaemonScheduler scheduler = new ClockDaemonScheduler(l,context,session);
     // should start up ok.
     // wait for it to execute
     Thread.sleep(1000);
     scheduler.halting();
     verifyAll();
     verify(task);
 }            
    // start with garbage.
    public void testInstantiateGarbageOnTaskList() throws Exception {
        
        replayAll();
        List l = new ArrayList();
        l.add(new Object());
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(l,context,session);
        // should start up ok.
        scheduler.halting();
        verifyAll();
    }    
    
    // execute AfterDelay.
    public void testAfterDelay() throws Exception {
        final StopWatch sw = new StopWatch();
        Runnable r = createMock(Runnable.class);
        r.run();
        expectLastCall().andAnswer(new IAnswer() { // no answer, just behaviour
            public Object answer() throws Throwable {
                sw.stop();
                return null;
            }
        });
        replay(r);
        replayAll();
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(Collections.EMPTY_LIST,context,session);
        
        sw.start();
        scheduler.executeAfterDelay(new Duration(500),r);
        Thread.sleep(1000);
        assertTrue(sw.getTime() > 300);
        
        scheduler.halting();
        verifyAll();
        verify(r);
    }    
    // executeAt
    public void testExecuteAt() throws Exception {
        final StopWatch sw = new StopWatch();
        Runnable r = createMock(Runnable.class);
        r.run();
        expectLastCall().andAnswer(new IAnswer() { // no answer, just behaviour
            public Object answer() throws Throwable {
                sw.stop();
                return null;
            }
        });
        replay(r);
        replayAll();
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(Collections.EMPTY_LIST,context,session);
        
        sw.start();        
        scheduler.executeAt(new Date(new Date().getTime() + 500) /* 500 ms from now*/,r);
        Thread.sleep(1000);
        assertTrue(sw.getTime() > 300);
        
        scheduler.halting();
        verifyAll();
        verify(r);
    }    
    
    // lastchance returns NUll always.
    public void testNoObjectctionsToShutdown() throws Exception {
        
        replayAll();
        ClockDaemonScheduler scheduler = new ClockDaemonScheduler(Collections.EMPTY_LIST,context,session);
        
        assertNull(scheduler.lastChance());
        scheduler.halting();
        verifyAll();
    }    
    
}
