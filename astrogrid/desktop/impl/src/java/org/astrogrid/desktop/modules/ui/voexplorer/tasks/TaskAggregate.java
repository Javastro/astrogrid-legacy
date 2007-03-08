/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** an aggregation of tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20071:22:38 PM
 */
public class TaskAggregate extends JTaskPaneGroup{
	protected AbstractTask[] tasks = new AbstractTask[0];

	public final UIComponentBodyguard uiParent = new UIComponentBodyguard();


	/** create a new menu containing the tasks this object aggregates
	 * 
	 * @return a menu, or null if this object contains no suitable tasks
	 */
	public JMenu createMenu() {
		if (tasks == null || tasks.length ==0) {
			return null;
		}
		JMenu m = new JMenu(this.getTitle());
		for (int i = 0; i < tasks.length; i++) {
			m.add(new JMenuItem(tasks[i]){
				public void setEnabled(boolean b) {
					super.setEnabled(b);
					setVisible(b);
				}
			});
		}
		return m;
	}
	
	/** called by controller when no items were selected */
	public void noneSelected() {
		
	    for (int i = 0; i < tasks.length; i++) {
	    	tasks[i].noneSelected();
	    }		
		done();
	}
	
	
	/** called by controller when one item was selected */
	public void selected(Transferable r) {
	    for (int i = 0; i < tasks.length; i++) {
	    	tasks[i].selected(r);
	    }
	    done();
	}
	
	public void setHelpId(String s) {
		CSH.setHelpIDString(this,s);
	}
	
	public void setIconName(String s) {
		setIcon(IconHelper.loadIcon(s));
	}
	
	public void setTasks(IterableObjectBuilder o) {

		setTasksFromIterator(o.creationIterator());
	}
	public void setTasksFromIterator(Iterator t) {
		tasks = (AbstractTask[]) IteratorUtils.toArray(t,AbstractTask.class);
	    for (int i = 0; i < tasks.length; i++) {
	    	tasks[i].uiParent = uiParent;
	    	add(tasks[i].createLinkButton());
	    }		
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
