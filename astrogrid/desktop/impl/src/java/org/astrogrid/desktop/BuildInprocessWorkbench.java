/**
 * 
 */
package org.astrogrid.desktop;

import org.astrogrid.Workbench1;

/** class that assebles and creates a new in-process Workbench.
 * 
 * used within test setup for unti tests.
 * @author Noel Winstanley
 * @since Jun 6, 20061:02:10 AM
 */
public class BuildInprocessWorkbench extends BuildInprocessACR {
protected void configureLauncher() {
	Workbench1.configureLauncherAsWorkbench(launcher);
}
}
