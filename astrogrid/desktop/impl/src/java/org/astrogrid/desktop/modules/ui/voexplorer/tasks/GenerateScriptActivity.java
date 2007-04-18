/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;

/** Generate a script to invoke a bunch of services
 * @todo implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:16:47 PM
 */
public final class GenerateScriptActivity extends AbstractResourceActivity {
 /**
 * 
 */
public GenerateScriptActivity() {
	CSH.setHelpIDString(this, "resourceTask.script");		
	setText("Generate script");
	setIcon(IconHelper.loadIcon("wizard16.png"));
	setToolTipText("Generate a python script that automates calling the selected service(s)");
}

/** Test whether it's something we can invoke.
 * @todo implement a SpectrumService type too.
 * @param r
 * @return
 */
protected final boolean invokable(Resource r) {
	return r instanceof SiapService 
			|| r instanceof ConeService
			|| r instanceof CeaApplication
			//@todo add vizier stuff here too.
			|| (r instanceof Service && r.getType().indexOf("Spectrum") != -1
					
			);
}




}
