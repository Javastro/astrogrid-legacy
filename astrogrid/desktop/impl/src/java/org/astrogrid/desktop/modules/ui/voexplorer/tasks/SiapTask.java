/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TaskInvoker;

/** Invoke an image seach;
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:59:37 PM
 */
public class SiapTask extends ResourceTask {
/**
 * 
 */
public SiapTask(TaskInvoker t) {
	CSH.setHelpIDString(this, "resourceTask.siap");		
	this.t = t;
	setText("Image search");
	setIcon(IconHelper.loadIcon("search16.png"));	
	setToolTipText("Invoke an image search on the selected service");
}
private final TaskInvoker t;
public void someSelected(List l) {
	noneSelected();
}
	protected boolean invokable(Resource r) {
		return r instanceof SiapService;
	}

	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		Resource r = (Resource)l.get(0);
		t.invokeTask(r);
	}

}
