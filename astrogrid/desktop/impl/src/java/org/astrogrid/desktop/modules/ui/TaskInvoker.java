/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Interface to some kind of action that operates on a resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 27, 20072:32:30 PM
 */
public interface TaskInvoker {
	
	/** invoke this resource in some way */
	void invokeTask(Resource r);

}
