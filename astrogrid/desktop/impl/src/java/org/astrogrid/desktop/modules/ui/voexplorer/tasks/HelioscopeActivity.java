/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.HelioScopeInternal;

/** invoke astroscope.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:47:48 PM
 */
public final class HelioscopeActivity extends AbstractResourceActivity {
/**
 * 
 */
public HelioscopeActivity(HelioScopeInternal ai) {
	CSH.setHelpIDString(this, "resourceTask.helioscope");	
	this.ai = ai;
	setText("Helioscope");
	setIcon(IconHelper.loadIcon("scope16.png"));
	setToolTipText("Run helioscope over the selected services(s)");
}

private final HelioScopeInternal ai;


	/** Test whether it's something we can invoke.
	 * @todo implement a SpectrumService type too.
	 * @param r
	 * @return
	 */
	protected boolean invokable(Resource r) {
		boolean b =  r instanceof Service && 
						r.getType().indexOf("SimpleTimeAccess") != -1 
							;
		return b;
	}
public void actionPerformed(ActionEvent e) {
	List l = computeInvokable();
	ai.runSubset(l);
}


}
