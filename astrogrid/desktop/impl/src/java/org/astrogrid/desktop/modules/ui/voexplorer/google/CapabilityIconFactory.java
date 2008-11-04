/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import javax.swing.Icon;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Interface to a component that vends icons that describe registry resource capabilities.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 200711:39:11 AM
 */
public interface CapabilityIconFactory {

	/** build / retrieve an icon that represents the capabilities of this resource */
	public Icon buildIcon(Resource r);
	
	/** return a tooltip that describes this icon
	 * @param r
	 * @return
	 */
	public String getTooltip(Icon i);

}