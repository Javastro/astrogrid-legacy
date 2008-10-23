/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.joda.time.Duration;

/** A process monitor which (probably) polls, driven by a timer.
 * 
 * comes with pre-canned timing stuff.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 12, 20074:42:10 PM
 */
public abstract class TimerDrivenProcessMonitor extends AbstractProcessMonitor implements DelayedContinuation {

    /**
     * @param vfs
     */
    public TimerDrivenProcessMonitor(final FileSystemManager vfs) {
        super(vfs);
    }

    /** factor to double the stand-off by */
    public static final long FACTOR = 2; // double every time
    
    /** increase the time before running again */
    protected final void standOff(final boolean increaseStandoff) {
        if (increaseStandoff) {
            final Duration nu =new Duration(runAgain.getMillis() *FACTOR);
            runAgain = nu.isLongerThan(LONGEST) ? LONGEST : nu;
           final int secs = (int)runAgain.getMillis() / 1000;
            info("No change: will re-check in " + (secs < 120 ? secs + " seconds" : secs/60 + " minutes" ));                
        } else {
            info("No change");
        }
    }
    
    /** field to record how long before we run again */
    protected Duration runAgain = SHORTEST;
    
    /** 
     * paert of sDelayedContinuation interface
     * access the value of <tt>runAgain</tt> */
    public final Duration getDelay() {
        return runAgain;
    }

    
    public final DelayedContinuation execute() {
        return execute(true);
    }
    /** do a poll, and if increaseStandoff=true, increate the standoff value if nothing has happended */    
    protected abstract DelayedContinuation execute(boolean increaseStandoff);
	
    public static final Duration SHORTEST = new Duration(1000); // 1 second
    public static final Duration LONGEST = new Duration(1000 * 60 * 10); // 10 minutes



}
