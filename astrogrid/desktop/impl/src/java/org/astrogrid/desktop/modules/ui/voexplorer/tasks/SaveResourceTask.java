/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.tasks;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

import com.l2fprod.common.swing.JDirectoryChooser;

/** save one or more resources to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 */
public class SaveResourceTask extends ResourceTask {

	protected boolean invokable(Resource r) {
		return true;
	}

	/**
	 * 
	 */
	public SaveResourceTask(ResourceChooserInternal chooser, RegistryInternal reg,MyspaceInternal ms) {
		this.chooser = chooser;
		this.ms = ms;
		this.reg = reg;
		setText("Save xml");
		setIcon(IconHelper.loadIcon("xml.gif"));
	}

private final ResourceChooserInternal chooser;
private final MyspaceInternal ms;
private final RegistryInternal reg;
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
					os = ms.getOutputStream(u);
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