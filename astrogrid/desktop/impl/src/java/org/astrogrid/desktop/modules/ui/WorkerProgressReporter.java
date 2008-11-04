/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

/** Interface used by a background worker to report progress to the UI.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 27, 200712:05:36 PM
 */
public interface WorkerProgressReporter {

    /** can be repeatedly called by background workers (from any thread) to 
     * report the progress of the task
     * @param s
     */
    public void reportProgress(String s);

    /** for workers which can determine how long a task is going to take,
     * use this method for determinate progress reporting
     * callable from any thread.
     * @param newCurrentValue current progress
     * @param newMaxValue maximum value for progress (i.e. when currentValue reaches this, task will have completed)
     */
    public void setProgress(final int newCurrentValue, final int newMaxValue);

}