/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.TapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.QueryBuilderInternal;

/** Perform a TAP Query on a resource (using TaskRunner)
 * @author Noel.Winstanley@manchester.ac.uk
 */
public final class TapQueryActivity extends AbstractResourceActivity {
/**
 * 
 */
public TapQueryActivity(final QueryBuilderInternal t) {
	this.t = t;
	setHelpID("activity.adql");	
	setText("Table Query");
	setIcon(IconHelper.loadIcon("db16.png"));
	setToolTipText("Construct a Table/Database (TAP) query against the selected table descriptions");
   // setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,UIComponentMenuBar.MENU_KEYMASK));	
}
// thing to invoke
private final QueryBuilderInternal t;

@Override
public void actionPerformed(final ActionEvent e) {
    final List<Resource> l = computeInvokable();
    final TapService r = (TapService)l.get(0);
    t.build(r);    
	}

	
	// accept a single database schema, or a single queriable service.
	@Override
    public boolean invokable(final Resource resource) {
	    return resource instanceof TapService;
		
	}

	// accept if any in selection are database schema.
	@Override
    public void someSelected(final Resource[] list) {
		noneSelected();
		// later - support multi-query later

	}

	
}
