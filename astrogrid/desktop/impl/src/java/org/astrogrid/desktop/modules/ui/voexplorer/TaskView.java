/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** View for running tasks
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:17:50 AM
 */
public class TaskView extends ViewController {
	private static final String TASKS_VIEW = "Tasks";
	/**
	 * 
	 */
	public TaskView(UIComponent parent) {
		folders =  new JLabel("list of running tasks to go here.");
	    folders.setName(TASKS_VIEW);

	    actions =   new JLabel("Actions on tasks to go here");
	    
	    main =   new JLabel("simplified version of Lookout to go here");

	}
	private final JComponent  folders;
	private final JComponent actions;
	private final JComponent main;
	
	public JComponent getActionsPanel() {
		return actions;
	}

	public JComponent getHierarchiesPanel() {
		return folders;
	}

	public String getName() {
		return TASKS_VIEW;
	}

	public JComponent getMainPanel() {
		return main;
	}

}
