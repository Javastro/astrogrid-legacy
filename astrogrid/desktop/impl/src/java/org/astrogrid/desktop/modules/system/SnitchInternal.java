/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.util.Map;


/** Interface to the usage profiling.
 * @author Noel Winstanley
 * @since May 19, 200612:08:14 AM
 */
public interface SnitchInternal {
	/** report a message */
	public void snitch(String message);
	/** report a message, with parameters */
	public void snitch(final String message, final Map params);

}
