/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.swing.JFrame;

import org.astrogrid.desktop.modules.system.UIInternal;


/** Interface to all ui components in workbench.
 * @author noel
 * @since Apr 10, 20064:37:36 PM
 */
public interface UIComponent {

	/** indicate execution of a background process.
	 * 
	 * @param b if if true, activity indicator will start throbbing. If false, activity indicator will stop.
	 */
	void setBusy(boolean b);

	/** set the status message at the bottom of this pane
	 * 
	 * @param s a message ("" to clear a previous message");
	 */
	void setStatusMessage(String s);

	/** display a well-formatted error message in a popup dialogue.
	 * 
	 * @param msg message
	 * @param e the exception that is the cause.
	 */

	void showError(String msg, Throwable e);

	/** set maximum value for progress bar */
	void setProgressMax(int i);

	/** set current value in progress bar - between <tt>0</tt> and <tt>getProgressMax()</tt> */
	void setProgressValue(int i);

	/** get the current progress value - between <tt>0</tt> and <tt>getProgressMax()</tt> */
	int getProgressValue();

	/** get the maximum value for the progress bar */
	int getProgressMax();

	/** called by a {@link BackgroundWorker} to notify the UI that it's started executing */
	void addBackgroundWorker(final BackgroundWorker w);

	/** called by {@link BackgroundWorker} to notify the UI it's finished running */
	void removeBackgroundWorker(BackgroundWorker w);

	/** access the central ui component */
	UIInternal getUI() ;
	
	/** access the Swing frame this UIComponentImpl is associated with
	 * in Headless mode, wil return null */
	
	JFrame getFrame();
}