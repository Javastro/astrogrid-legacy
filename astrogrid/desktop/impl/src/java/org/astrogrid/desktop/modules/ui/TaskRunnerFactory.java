/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;

/** factory for the simplified app launcher.
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class TaskRunnerFactory implements TaskRunnerInternal{

//@todo remove the things that aren't needed here.
	public TaskRunnerFactory(UIContext context,ResourceChooserInternal rChooser, ApplicationsInternal apps, MyspaceInternal myspace, RegistryGoogle rGoogle,Preference preference, RemoteProcessManager rpm) {
		this.rChooser = rChooser;
		this.apps = apps;
		this.myspace = myspace;
		this.context = context;
		this.preference = preference;
		this.rGoogle = rGoogle;
		this.rpm = rpm;
	}
	private final ResourceChooserInternal rChooser;
	private final RegistryGoogle rGoogle;
	private final ApplicationsInternal apps;
	private final MyspaceInternal myspace;
	private final UIContext context;
	private final Preference preference;
	private final RemoteProcessManager rpm;

	protected TaskRunnerInternal newInstance() {
		return new TaskRunnerImpl(context,apps,rChooser,rGoogle,myspace,rpm);
	}
	
	public void invokeTask(Resource r) {
		TaskRunnerInternal tr = newInstance();
		tr.invokeTask(r);
		tr.show();
	}


	public void hide() {
		// ignored.
	}


	public void show() {
		newInstance().show();
	}


	public Object create() {
		TaskRunnerInternal tr = newInstance();
		tr.show();
		return tr;
	}

}
