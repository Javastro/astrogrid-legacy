/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.AstroScopeInternal;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;

/** invoke astroscope.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20073:47:48 PM
 */
public final class AstroscopeActivity extends AbstractResourceActivity {
/**
 * 
 */
public AstroscopeActivity(AstroScopeInternal ai) {
	CSH.setHelpIDString(this, "resourceTask.astroscope");	
	this.ai = ai;
	setText("Astroscope");
	setIcon(IconHelper.loadIcon("scope16.png"));
	setToolTipText("Run astroscope over the selected services(s)");
}

private final AstroScopeInternal ai;


	/** Test whether it's something we can invoke.
	 * @todo implement a SpectrumService type too.
	 * @param r
	 * @return
	 */
	protected boolean invokable(Resource r) {
		boolean b =  r instanceof SiapService 
				|| r instanceof ConeService
				|| ConeProtocol.isCdsCatalogService(r)
				|| r instanceof Service && 
						r.getType().indexOf("Spectrum") != -1 
					;
		return b;
	}
public void actionPerformed(ActionEvent e) {
	List l = computeInvokable();
	ai.runSubset(l);
}


}