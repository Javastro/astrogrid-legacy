/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TaskInvoker;

/** Invoke a cone search;
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:59:37 PM
 */
public class ConeSearchTask extends ResourceTask {
/**
 * 
 */
public ConeSearchTask(TaskInvoker t) {
	CSH.setHelpIDString(this, "resourceTask.cone");		
	this.t = t;
	setText("Catalogue search");
	setIcon(IconHelper.loadIcon("search16.png"));
	setToolTipText("Perform a positional search on the selected catalog");
}
private final TaskInvoker t;
public void someSelected(List l) {
	 // can't operate on more than one resource
	noneSelected();
}
	protected boolean invokable(Resource r) {
		return r instanceof ConeService
			|| r instanceof Service 
				&& r.getType().indexOf("TabularSkyService") != -1
				&& r.getId().toString().indexOf("CDS") != -1
		;
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable(); 
		Resource r = (Resource)l.get(0);
		t.invokeTask(r);
		
	}

}
