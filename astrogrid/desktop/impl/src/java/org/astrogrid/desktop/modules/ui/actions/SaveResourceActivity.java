/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
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

/** Save xml of resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 * @TEST
 */
public class SaveResourceActivity extends AbstractResourceActivity implements Activity.NoContext, Activity.NoTask{

	@Override
    protected boolean invokable(final Resource r) {
		return true;
	}

	/**
	 * 
	 */
	public SaveResourceActivity(final UIContext cxt,final ResourceChooserInternal chooser, final RegistryInternal reg,final FileSystemManager vfs) {
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

@Override
public void actionPerformed(final ActionEvent e) {
	Component comp = null;
	if (e.getSource() instanceof Component) {
		comp = (Component)e.getSource();
	}	
	final List<Resource> l = computeInvokable();
		final URI u = chooser.chooseResourceWithParent("Choose output file",true,true,true,comp);
        if (u == null) {
            return;
        }		
		(new BackgroundWorker<Void>(uiParent.get(),"Exporting XML",BackgroundWorker.LONG_TIMEOUT) {

			@Override
            protected Void construct() throws Exception {
				OutputStream os = null;
                FileObject fo = null;
				final int max = l.size() + 2;
				int count = 0;
				setProgress(count,max);
				try {
                    fo = vfs.resolveFile(u.toString());
				    os = fo.getContent().getOutputStream();
				    setProgress(++count,max);
				    if (l.size() > 1) {
				        os.write("<resources>\n".getBytes());
				    }
				    for (int i = 0; i < l.size(); i++) {
				        final Resource res = l.get(i);
				        final Document doc = reg.getResourceXML(res.getId());
				        DomHelper.ElementToStream(doc.getDocumentElement(),os);
				        setProgress(++count,max);
				    }
                    if (l.size() > 1) {
                        os.write("\n</resources>".getBytes());
                    }				    
                    setProgress(++count,max);
				} finally {
				    IOUtils.closeQuietly(os);
				    if (fo != null) {
				        fo.refresh();
				    }
				
				}
				return null;
			}
			@Override
            protected void doFinished(final Void result) {
			    parent.showTransientMessage("Export complete","");
			}
		}).start();

	
	}
}
