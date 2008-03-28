/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** save one or more resources to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 */
public class SaveResourceActivity extends AbstractResourceActivity implements Activity.NoContext, Activity.NoTask{

	protected boolean invokable(Resource r) {
		return true;
	}

	/**
	 * 
	 */
	public SaveResourceActivity(UIContext cxt,ResourceChooserInternal chooser, RegistryInternal reg,FileSystemManager vfs) {
		this.chooser = chooser;
		setHelpID("activity.save");
		this.vfs = vfs;
		this.reg = reg;
		this.cxt = cxt;
		setText("Export Full XML" + UIComponentMenuBar.ELLIPSIS);
		setToolTipText("Export the full XML records for the selected resource(s)");
		setIcon(IconHelper.loadIcon("xml.gif"));
	}

private final ResourceChooserInternal chooser;
private final FileSystemManager vfs;
private final RegistryInternal reg;
private final UIContext cxt;

public void actionPerformed(ActionEvent e) {
	Component comp = null;
	if (e.getSource() instanceof Component) {
		comp = (Component)e.getSource();
	}	
	final List l = computeInvokable();
		final URI u = chooser.chooseResourceWithParent("Choose output file",true,true,true,comp);
		(new BackgroundWorker(uiParent.get(),"Exporting XML",BackgroundWorker.LONG_TIMEOUT) {

			protected Object construct() throws Exception {
				OutputStream os = null;
				int max = l.size() + 2;
				int count = 0;
				setProgress(count,max);
				try {
				    os = vfs.resolveFile(u.toString()).getContent().getOutputStream();
				    setProgress(++count,max);
				    if (l.size() > 1) {
				        os.write("<resources>\n".getBytes());
				    }
				    for (int i = 0; i < l.size(); i++) {
				        Resource res = (Resource)l.get(i);
				        Document doc = reg.getResourceXML(res.getId());
				        DomHelper.ElementToStream(doc.getDocumentElement(),os);
				        setProgress(++count,max);
				    }
                    if (l.size() > 1) {
                        os.write("\n</resources>".getBytes());
                    }				    
                    setProgress(++count,max);
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
			protected void doFinished(Object result) {
			    parent.showTransientMessage("Export complete","");
			}
		}).start();

	
	}
}
