/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.ApplicationLauncher;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class ApplicationLauncherFactory implements ApplicationLauncher {

	/**
	 * @param panelFactories
	 * @param rChooser
	 * @param apps
	 * @param myspace
	 * @param lookout
	 * @param conf
	 * @param help
	 * @param ui
	 */
	public ApplicationLauncherFactory(List panelFactories, ResourceChooserInternal rChooser, ApplicationsInternal apps, MyspaceInternal myspace, Lookout lookout, Configuration conf, HelpServerInternal help, UIInternal ui) {
		this.panelFactories = panelFactories;
		this.rChooser = rChooser;
		this.apps = apps;
		this.myspace = myspace;
		this.lookout = lookout	;
		this.ui = ui;
		this.conf = conf;
		this.help = help;
		
	}
	private final List panelFactories;
	private final ResourceChooserInternal rChooser;
	private final ApplicationsInternal apps;
	private final MyspaceInternal myspace;
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal help;
	private final Lookout lookout;
	public void hide() {
		// ignore.
	}

	public void show() {
		ApplicationLauncher app = new ApplicationLauncherImpl(panelFactories, rChooser, apps, myspace, lookout, conf, help, ui);
		app.show();
	}

}
