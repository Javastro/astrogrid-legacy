/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** Internal interface to the task runner factory.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 27, 20072:32:30 PM
 */
public interface TaskRunnerInternal extends ApplicationLauncher, Factory{
	
	/** invoke this resource in some way */
	void invokeTask(Resource r);
	// deliberately not type constrained to CeaApplication - as later we
	// might want to pass TAP, and UWS services in here too.
	// in fact, this is still a general framework for running anything which
	// isn't coverd by astroscope.
	
	void edit(FileObjectView o);

}
