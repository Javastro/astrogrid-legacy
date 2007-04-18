/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TaskInvoker;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.FormLayout;

/** Invoke a cone search;
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:59:37 PM
 */
public class ConeSearchActivity extends AbstractResourceActivity {
/**
 * 
 */
public ConeSearchActivity(TaskInvoker t) {
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
			|| ConeProtocol.isCdsCatalogService(r)
			;
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable(); 
		Resource r = (Resource)l.get(0);
		t.invokeTask(r);
	//	ConeWindow cw = new ConeWindow(this.uiParent.get().getContext(),r);
	//	cw.setVisible(true);
	}
	
	private static class ConeWindow extends UIComponentImpl {

		/**
		 * @param context
		 * @throws HeadlessException
		 */
		public ConeWindow(UIContext context, Resource r) throws HeadlessException {
			super(context);
			this.resource = r;
			setTitle(r.getTitle());
			
			FormLayout layout = new FormLayout("cols","rows");
			PanelBuilder builder = new PanelBuilder(layout);
			
			this.getMainPanel().add(builder.getPanel(),BorderLayout.CENTER);
		}
		private final Resource resource;
	}

}
