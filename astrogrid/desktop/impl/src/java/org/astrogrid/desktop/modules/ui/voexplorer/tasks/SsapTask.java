/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TaskInvoker;

/** Invoke a spectrum service
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:59:37 PM
 */
public class SsapTask extends ResourceTask {
/**
 * 
 */
public SsapTask(TaskInvoker t) {
	CSH.setHelpIDString(this, "resourceTask.ssap");		
	this.t = t;
	setText("Spectrum search");
	setIcon(IconHelper.loadIcon("search16.png"));	
	setToolTipText("Invoke a spectrum search on the selected service");
}
private final TaskInvoker t;
public void someSelected(List l) {
	noneSelected();
}
	protected boolean invokable(Resource r) {
		return r instanceof Service && r.getType().indexOf("Spectrum") != -1;
	}
	/** @todo not working - fix me
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		Resource r = (Resource)l.get(0);
		t.invokeTask(r);
	}*/
}
