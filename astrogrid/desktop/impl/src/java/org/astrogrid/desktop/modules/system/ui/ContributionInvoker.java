/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.system.contributions.UIActionContribution;

/** Interface to something that knows how to invoke a UIActionContribution.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 200711:28:56 AM
 */
public interface ContributionInvoker {
	/** invoke a UIActionContribution */
	void invoke(UIActionContribution action);
}
