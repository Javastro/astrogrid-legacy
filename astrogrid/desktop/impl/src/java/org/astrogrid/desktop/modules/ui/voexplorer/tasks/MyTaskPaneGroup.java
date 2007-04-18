/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** an aggregation of tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:22:38 PM
 */
public class MyTaskPaneGroup extends JTaskPaneGroup{
	protected Activity[] activities = new Activity[0];
	
	public void setHelpId(String s) {
		CSH.setHelpIDString(this,s);
	}
	
	public void setIconName(String s) {
		setIcon(IconHelper.loadIcon(s));
	}


	/** subclasses should call this to cause component to redraw after 
	 * alterations have been completed.
	 *
	 */
	protected void done() {
		revalidate();
		repaint();
	}
	
	
	
	
}
