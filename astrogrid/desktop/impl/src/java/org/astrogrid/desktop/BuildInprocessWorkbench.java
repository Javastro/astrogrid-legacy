/**
 * 
 */
package org.astrogrid.desktop;

import javax.swing.JFrame;

import org.astrogrid.desktop.modules.system.UIInternal;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;

/** 
 * used within test setup for unti tests.
 *  provides a reference to the main window - so it can be used in ui testing.
 * @author Noel Winstanley
 * @since Jun 6, 20061:02:10 AM
 */
public class BuildInprocessWorkbench extends BuildInprocessACR implements UISpecAdapter{

	/* temporarily override to control which gets instantiated 
	 * @todo comment out when done.*/
	//protected void configureLauncher() {
	//	HeadlessAstroRuntime.configureLauncherAsASR(launcher);
//	}

public Window getMainWindow() {
	UIInternal ui = (UIInternal) getHivemindRegistry().getService(UIInternal.class);
	return new Window((JFrame)ui.getComponent());
}
}
