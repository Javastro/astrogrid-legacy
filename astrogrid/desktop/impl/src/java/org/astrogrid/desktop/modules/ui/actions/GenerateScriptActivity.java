/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import groovy.lang.Closure;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Abstract class for an activity that generates a script from a template and the user's selection of resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20074:16:47 PM
 */
public final class GenerateScriptActivity extends AbstractResourceActivity {

	public GenerateScriptActivity(String templateString,ResourceChooserInternal chooser, FileSystemManager vfs) {
		CSH.setHelpIDString(this, "resourceTask.script");		
		this.vfs = vfs;
		this.chooser = chooser;
		setText("Generate script");
		setIcon(IconHelper.loadIcon("wizard16.png"));
		setToolTipText("Generate a python script that automates calling the selected service(s)");
		TemplateEngine eng = new SimpleTemplateEngine();

		Template t = null;
		try {
			t = eng.createTemplate(templateString);
		} catch (Exception e) {
			functional = false;
		}
		template = t;
	}

	protected boolean functional = true;
	private final Template template;
	private final ResourceChooserInternal chooser;
	private final FileSystemManager vfs;


	/** Test whether it's something we can invoke.
	 * @fixme work out how to optimize this
	 */
	protected final boolean invokable(Resource r) {
		if (! functional) {
			return false;
		}
		if (condition != null) {
			Object object = condition.call(r);
			return object != null
			&& (object instanceof Boolean)
			&& ((Boolean)object).booleanValue();
		} else {
			return true;
		}

	}
	private Closure condition;

	public void setCondition(Closure clos) {
		this.condition = clos;
	}
	
	private boolean singleSelection = false;
	public void setSingleSelection(boolean b) {
		this.singleSelection = b;
	}
	
	public void someSelected(Resource[] list) {
		if (singleSelection) {
			noneSelected();
		} else {
			super.someSelected(list);
		}
	}
	

	public void actionPerformed(ActionEvent e) {
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}	
		final URI u = chooser.chooseResourceWithParent("Save generated script",true,true,true,comp);
		if (u == null) {
			return;
		}	
		(new BackgroundWorker(uiParent.get(),"Generating Script") {
			protected Object construct() throws Exception {
				List l = computeInvokable();
				Map binding = new HashMap();
				binding.put("resources",l);
				Writable w = template.make(binding);
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(vfs.resolveFile(u.toString()).getContent().getOutputStream());
					w.writeTo(writer);
				} catch (IOException x) {
					logger.error("IOException",x);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
				return null;
			}
		}).start();
	}

}
