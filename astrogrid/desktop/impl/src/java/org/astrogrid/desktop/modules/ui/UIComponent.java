/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import javax.swing.JFrame;

import org.astrogrid.desktop.modules.system.ui.UIContext;


/** Interface to all ui components in workbench.
 * @author noel
 * @since Apr 10, 20064:37:36 PM
 */
public interface UIComponent {
	
	public String getTitle();
	public void setVisible(boolean b);
	


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

	/** access the Swing frame this UIComponent is associated with.
	 * used to centering new windows, etc.
	 * in Headless mode, wil return null */
	
	JFrame getFrame();
    /** halt all tasks owned by this component (and not special) */
	public void haltMyTasks() ;

	/** access the singleton ui context object.
	 * @return the singleton context.
	 */
	UIContext getContext();
}