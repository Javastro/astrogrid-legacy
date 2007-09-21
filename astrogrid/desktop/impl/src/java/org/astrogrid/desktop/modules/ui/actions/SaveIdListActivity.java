/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.io.Piper;

/** saves ids of the selection as a list to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 */
public class SaveIdListActivity extends AbstractActivity {


	public void selected(Transferable r) {
		if (r.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
			setEnabled(true);
			current = r;
		} else {
			noneSelected();
		}
	}
	private Transferable current;
	public  void noneSelected() {
		setEnabled(false);
		current = null;
	}

	/**
	 * 
	 */
	public SaveIdListActivity(ResourceChooserInternal chooser, FileSystemManager vfs) {
		this.chooser = chooser;
		this.vfs = vfs;
		setText("Save ID List");
		setToolTipText("Save the resource ids of the current selection to a textfile");
		setIcon(IconHelper.loadIcon("ascii16.png"));
	}

private final ResourceChooserInternal chooser;
private final FileSystemManager vfs;
public void actionPerformed(ActionEvent e) {
	Component comp = null;
	if (e.getSource() instanceof Component) {
		comp = (Component)e.getSource();
	}	
	final Transferable t = current;
		final URI u = chooser.chooseResourceWithParent("Save resource list",true,true,true,comp);
		if (u == null) {
			return;
		}
		(new BackgroundWorker(uiParent.get(),"Saving resource list") {
			protected Object construct() throws Exception {
				OutputStream os = null;
				InputStream is = null;
				try {
					is = (InputStream)t.getTransferData(VoDataFlavour.URI_LIST);
					os = vfs.resolveFile(u.toString()).getContent().getOutputStream();
					Piper.pipe(is,os);
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							// ignored
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							// ignored
						}
					}
				}
				return null;
			}
		}).start();
}

}