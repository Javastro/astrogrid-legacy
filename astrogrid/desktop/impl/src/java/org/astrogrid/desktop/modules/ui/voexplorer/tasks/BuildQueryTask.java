/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TaskInvoker;

/** build a query from a selected resource.
 * @todo implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:13:13 PM
 */
public final class BuildQueryTask extends ResourceTask {
/**
 * 
 */
public BuildQueryTask(TaskInvoker t) {
	this.t = t;
	CSH.setHelpIDString(this, "resourceTask.buildQuery");		
	setText("Build query");
	setIcon(IconHelper.loadIcon("table16.png"));
	setToolTipText("Construct an ADQL query against the selected table descriptions");
	
}
private final TaskInvoker t;
public void someSelected(List l) { // can't operate on more than one resource
	noneSelected();
	
}
	protected boolean invokable(Resource r) {
		return r instanceof DataCollection
			|| (r instanceof CeaApplication && hasAdqlParameter((CeaApplication)r));
	}

	boolean hasAdqlParameter(CeaApplication a) {
		ParameterBean[] parameters = a.getParameters();	
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getType().equalsIgnoreCase("adql")) {
			return true;
		}
		}
	return false;
	}
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		Resource r = (Resource)l.get(0);
		t.invokeTask(r);
	}
	
}
