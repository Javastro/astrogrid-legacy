/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** factory for the simplified app launcher.
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class SimplifiedAppLauncherFactory implements TaskInvoker{

	/**
	 * @param panelFactories
	 * @param rChooser
	 * @param apps
	 * @param myspace
	 * @param lookout
	 * @param conf
	 * @param help
	 * @param ui
	 * @param preference 
	 */
	public SimplifiedAppLauncherFactory(UIContext context,List panelFactories, ResourceChooserInternal rChooser, ApplicationsInternal apps, MyspaceInternal myspace, Preference preference) {
		this.panelFactories = panelFactories;
		this.rChooser = rChooser;
		this.apps = apps;
		this.myspace = myspace;
		this.context = context;
		this.preference = preference;
	}
	private final List panelFactories;
	private final ResourceChooserInternal rChooser;
	private final ApplicationsInternal apps;
	private final MyspaceInternal myspace;
	private final UIContext context;
	private final Preference preference;


	public void invokeTask(Resource r) {
		TaskInvoker app = new SimplifiedAppLauncherImpl(panelFactories, rChooser, apps, myspace, context, preference);
		app.invokeTask(r);
	}

}
