/**
 * 
 */
package org.astrogrid.desktop.modules.voevent;

import org.astrogrid.acr.ivoa.Registry;

import uk.ac.estar.voevent.Inject;

/**
 * Implementation of the Inject service
 * 
 * 
 * @author Noel Winstanley CHANGE ME
 * @since 2006.3.rc5
 */
public class InjectImpl implements Inject {
	
	// sample use of constructor injection - feel free to remove if registry is not needed.
	public InjectImpl(Registry reg, String configValue) {
		this.reg = reg;
		this.sampleConfigValue = configValue;
	}
	
	private final Registry reg;
	private final String sampleConfigValue;
	
	
	// implement interface methods here.

}
