/**
 * 
 */
package org.astrogrid.desktop;

import javax.swing.JFrame;

import org.astrogrid.Workbench1;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;

/** class that assebles and creates a new in-process Workbench.
 * 
 * used within test setup for unti tests.
 * also provides a reference to the main window - so it can be used in ui testing.
 * @author Noel Winstanley
 * @since Jun 6, 20061:02:10 AM
 */
public class BuildInprocessWorkbench extends BuildInprocessACR implements UISpecAdapter{
protected void configureLauncher() {
	Workbench1.configureLauncherAsWorkbench(launcher);
}

public Window getMainWindow() {
	UIInternal ui = (UIInternal) getHivemindRegistry().getService(UIInternal.class);
	return new Window((JFrame)ui.getComponent());
}
}
