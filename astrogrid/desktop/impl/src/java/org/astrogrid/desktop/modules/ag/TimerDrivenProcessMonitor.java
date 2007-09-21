/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;

/** A process monitor which (probably) polls, driven by a timer.
 * 
 * usure whether there's anythoing to add here - maybe not.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 12, 20074:42:10 PM
 */
public abstract class TimerDrivenProcessMonitor extends AbstractProcessMonitor implements DelayedContinuation {

    /**
     * @param vfs
     */
    public TimerDrivenProcessMonitor(FileSystemManager vfs) {
        super(vfs);
    }


	


}
