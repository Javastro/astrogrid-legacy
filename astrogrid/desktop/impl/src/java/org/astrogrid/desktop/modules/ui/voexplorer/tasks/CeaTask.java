/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TaskInvoker;

/** invoke a cea task.
 * @todo initialize the form in a backgrund thread
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:25:43 PM
 */
public class CeaTask extends ResourceTask {
/**
 * 
 */
public CeaTask(TaskInvoker t) {
	CSH.setHelpIDString(this, "resourceTask.cea");		
	this.t = t;
	setText("Execute Task");
	setIcon(IconHelper.loadIcon("exec16.png"));
	setToolTipText("Invoke the selected long running task");
}
private final TaskInvoker t;
	
	public void someSelected(List l) { // can't operate on more than one resource
		noneSelected();
	}
	
	protected boolean invokable(Resource r) {
		return r instanceof CeaApplication;
	}
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		final Resource r = (Resource)l.get(0);
		(new BackgroundWorker(uiParent.get(),"Opening task launcher") {

			protected Object construct() throws Exception {
				t.invokeTask(r);
				return null;
			}
		}).start();
	}

}
