/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/** Scavenger that builds a menu from all the available script-templates.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 8, 200710:12:17 AM
 */
public class GenerateScriptScavenger extends AbstractActivityScavenger {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(GenerateScriptScavenger.class);

	public GenerateScriptScavenger(ResourceChooserInternal chooser, FileSystemManager vfs) {
		super("Automation",IconHelper.loadIcon("wizard16.png"));
		this.chooser = chooser;
		this.vfs = vfs;
		//@future - load these lists dynamically - at moment are hard-coded		
	}
	// can't do this in constructor, as ui Parent hasn't be set at that point.
	// instead, initialize these lazily.
	protected void loadChildren() {
		add(GenerateScriptScavenger.class.getResource("adql.py"));
		add(GenerateScriptScavenger.class.getResource("app.py"));
		add(GenerateScriptScavenger.class.getResource("cone.py"));
		add(GenerateScriptScavenger.class.getResource("conesearch.py"));
		add(GenerateScriptScavenger.class.getResource("siap.py"));		
	}
	
	private final ResourceChooserInternal chooser;
	private final FileSystemManager vfs;
	
	private void add(final URL u) {
		if (u == null) {
			return;
		}
		(new BackgroundWorker(super.uiParent.get(),"Reading script template") {

			protected Object construct() throws Exception {
				BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
				// scan for a template header
				String line;
				StringBuffer header = new StringBuffer();
				while (( line = br.readLine()) != null && line.startsWith("#")) {
					header.append(line.substring(1));
					header.append("\n");
				}
				// ok, found first non-comment line (recommended to be a blank line
				// assume rest is the template.
				StringBuffer template = new StringBuffer();
				if (line != null && line.trim().length() > 0) {
					template.append(line).append("\n");
				}
				while ((line = br.readLine()) != null) {
					template.append(line).append("\n");
				}

				if (header.length() > 0) {
					shell = new GroovyShell();
					shell.evaluate(header.toString());
				}
				return template.toString();
			}
			
			protected GroovyShell shell;
			
			protected void doFinished(Object result) {
				String template = (String)result;
				GenerateScriptActivity a  =new GenerateScriptActivity(template,chooser,vfs);
				a.setUIParent(uiParent.get());
				// now, if we found a header, inspect the groovy script and initialize the generateScriptActivity from it.
				Object o = shell.getVariable("name");
				if (o != null) {
					a.setText( o.toString());
					shell.setVariable("name",null);
				}
				o = shell.getVariable("tooltip");
				if (o != null) {
					a.setToolTipText(o.toString());
					shell.setVariable("tooltip",null);
				}
				o = shell.getVariable("condition");
				if (o != null && o instanceof Closure) {
					a.setCondition((Closure)o);
					shell.setVariable("condition",null);
				}
				o = shell.getVariable("singleSelection");
				if (o != null && o instanceof Boolean) {
					a.setSingleSelection(((Boolean)o).booleanValue());
					shell.setVariable("singleSelection",null);
				}
				shell = null;
				getChildren().add(a);
			}
			protected void doError(Throwable ex) {
				logger.warn("Failed to parse template: " + u,ex);
			}
		}).start();
		
	}

	protected EventList createEventList() {
		return new BasicEventList();
	}
}
