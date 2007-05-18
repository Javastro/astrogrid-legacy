/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.AstroWizard;
import org.astrogrid.util.DomHelper;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Summary;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanel;
import org.w3c.dom.Document;

/** save one or more resources to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 */
public class SaveResourceActivity extends AbstractResourceActivity {

	protected boolean invokable(Resource r) {
		return true;
	}

	public void someSelected(Resource[] list) {
		// temporarily removed functio to export a set of resources - as filenames don't work.
		//@todo sort out filename generation, and re-enabled this.
		noneSelected();
	}
	/**
	 * 
	 */
	public SaveResourceActivity(UIContext cxt,ResourceChooserInternal chooser, RegistryInternal reg,MyspaceInternal ms) {
		this.chooser = chooser;
		this.ms = ms;
		this.reg = reg;
		this.cxt = cxt;
		setText("Save xml");
		setIcon(IconHelper.loadIcon("xml.gif"));
	}

private final ResourceChooserInternal chooser;
private final MyspaceInternal ms;
private final RegistryInternal reg;
private final UIContext cxt;

//@todo alter this to use a wizard.
public void actionPerformed1(ActionEvent e) {
	Component comp = null;
	if (e.getSource() instanceof Component) {
		comp = (Component)e.getSource();
	}	
	final List l = computeInvokable();
	WizardPage infoPage = new AstroWizard.InfoWizardPane("Save the XML definition of this resource to local disk");
	//if (l.size() == 1) {
	//	
	//} else {
		//todo implement for multople resources.
	//}
	AstroWizard.FileChooserWizardPane savePage = new AstroWizard.FileChooserWizardPane("Choose the save location","file",true);
	savePage.getFileChooser().setDialogType(JFileChooser.SAVE_DIALOG);
	Wizard wiz = AstroWizard.createWizard("Save Resource XML"
			,new WizardPage[]{infoPage,savePage}
			,new SaveResourceWizardBackgroundWorker(((Resource)l.get(0)))
			);
	WizardDisplayer.showWizard(wiz);
}

public class SaveResourceWizardBackgroundWorker extends AstroWizard.WizardBackgroundWorker {

	public SaveResourceWizardBackgroundWorker(Resource r) {
		super(cxt, "Save Resource XML");
		this.r = r;
	}
	private final Resource r;

	protected Object construct() throws Exception {
		OutputStream os = null;
		try {
			int i = 0;
			progress.setProgress("Retrieving XML",i++,2);
			Document doc = reg.getResourceXML(r.getId());
			progress.setProgress("Saving to Disk",i++,2);
			
			File f = (File)args.get("file");
			os = new FileOutputStream(f);
			DomHelper.DocumentToStream(doc,os);
			return Summary.create("XML Saved to ", f);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}
}

// old alternate version.
public void actionPerformed(ActionEvent e) {
	Component comp = null;
	if (e.getSource() instanceof Component) {
		comp = (Component)e.getSource();
	}	
	final List l = computeInvokable();
	if (l.size() == 1) {
		final URI u = chooser.chooseResourceWithParent("Save resource",true,true,true,comp);
		(new BackgroundWorker(uiParent.get(),"Saving resource document") {
			protected Object construct() throws Exception {
				OutputStream os = null;
				try {
					Resource res = (Resource)l.get(0);
					if (u.getScheme().equals("file")) { //@todo tidy this
						os = new FileOutputStream(new File(u));
					} else {
						os = ms.getOutputStream(u);
					}
					Document doc = reg.getResourceXML(res.getId());
					DomHelper.DocumentToStream(doc,os);
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							// ignored
						}
					}
				}
				return null;
			}
		}).start();
	} else {
		final URI u = chooser.chooseDirectoryWithParent("Select a directory to save resources to",true,true,true,comp);
		(new BackgroundWorker(uiParent.get(),"Saving resource documents") {
			protected Object construct() throws Exception {
				OutputStream os = null;
				for (Iterator i = l.iterator(); i.hasNext();) {
					Resource res = (Resource) i.next();
					try {
						String name = URLEncoder.encode(res.getId().toString());
						os = ms.getOutputStream(u.resolve(name));
						Document doc = reg.getResourceXML(res.getId());
						DomHelper.DocumentToStream(doc,os);
					} finally {
						if (os != null) {
							try {
								os.close();
							} catch (IOException e) {
								// ignored
							}
						}
					}
				}
				return null;
			}
		}).start();

	}
}

}