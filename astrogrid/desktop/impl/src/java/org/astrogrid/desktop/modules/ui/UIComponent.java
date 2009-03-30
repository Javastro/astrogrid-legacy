/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.astrogrid.desktop.modules.system.ui.UIContext;


/** Common Interface to ui window.
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

    void showError(String msg);    

    /** display an error message in a popup, which will vanish after a few seconds */
    public void showTransientError(String title, String message);

    /** display a message in a popup, which will vanish after a few seconds */
    public void showTransientMessage(String title, String message);
    
    /** display a message in a popup which will vainish in a few seconds */
    public void showTransientMessage(Icon icon, String title, String message );
/** display a warning in a popup, which will vanish after a few seconds */
    public void showTransientWarning(String title, String message);
	
	/** set maximum value for progress bar */
	void setProgressMax(int i);

	/** set current value in progress bar - between <tt>0</tt> and <tt>getProgressMax()</tt> */
	void setProgressValue(int i);

	/** get the current progress value - between <tt>0</tt> and <tt>getProgressMax()</tt> */
	int getProgressValue();

	/** get the maximum value for the progress bar */
	int getProgressMax();

	/** access the AWT component this UIComponent is associated with.
	 * used to centering new windows, etc.
	 * in Headless mode, wil return null 
	 * other times, usually returns 'this'*/
	
	Component getComponent();
	
    /** access the main panel, where other components can be added..
     * @return a JPane with {@link BorderLayout}. The southern segment is already taken by the activity indicator & status message.
     * 
      */ 
    public JPanel getMainPanel() ;
	
    /** halt all tasks owned by this component (and not special) */
	public void haltMyTasks() ;

	/** access the singleton ui context object.
	 * @return the singleton context.
	 */
	UIContext getContext();
}