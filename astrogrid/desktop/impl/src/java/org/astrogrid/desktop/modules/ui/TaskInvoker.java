/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Interface to a simplified application launcher etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 27, 20072:32:30 PM
 */
public interface TaskInvoker {
	
	/** invoke this resource in some way */
	void invokeTask(Resource r);

}
