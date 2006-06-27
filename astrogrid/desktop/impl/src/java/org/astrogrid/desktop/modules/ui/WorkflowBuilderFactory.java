/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.ui.Lookout;
import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.JobsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ToolEditorInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20067:38:02 PM
 */
public class WorkflowBuilderFactory implements	WorkflowBuilder {

	/**
	 * @param apps
	 * @param jobs
	 * @param monitor
	 * @param vos
	 * @param browser
	 * @param toolEditor
	 * @param chooser
	 * @param ui
	 * @param hs
	 * @param conf
	 * @throws Exception
	 */
	public WorkflowBuilderFactory(ApplicationsInternal apps, JobsInternal jobs, Lookout monitor, MyspaceInternal vos, BrowserControl browser, ToolEditorInternal toolEditor, ResourceChooserInternal chooser, UIInternal ui, HelpServerInternal hs, Configuration conf) throws Exception {
		this.apps = apps;
		this.jobs = jobs;
		this.monitor = monitor;
		this.browser = browser;
		this.toolEditor = toolEditor;
		this.ui = ui;
		this.conf = conf;
		this.hs = hs;
		this.myspace =vos;
		this.chooser= chooser;
	}
	private final UIInternal ui;
	private final Configuration conf;
	private final HelpServerInternal hs;
	private final MyspaceInternal myspace;
	private final ResourceChooserInternal chooser;
	private final ApplicationsInternal apps;
	private final JobsInternal jobs;
	private final Lookout monitor;
	private final BrowserControl browser;
	private final ToolEditorInternal toolEditor;
	public void hide() {
		// do nothing.
	}

	public void show() {
		WorkflowBuilder b = createBuilder();
		b.show();
	}

	public void showTranscript(String arg0) {
		WorkflowBuilder b = createBuilder();
		b.showTranscript(arg0);
	}
	
	private WorkflowBuilder createBuilder() {
		return new WorkflowBuilderImpl(apps, jobs, monitor, myspace, browser, toolEditor, chooser, ui, hs, conf);
	}

	

}
