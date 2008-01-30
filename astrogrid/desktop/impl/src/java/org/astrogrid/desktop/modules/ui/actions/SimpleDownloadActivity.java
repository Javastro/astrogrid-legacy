/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;

import com.l2fprod.common.swing.JDirectoryChooser;

/** simplistic activity which just allows users to download files to a local directory.
 * may even keep around once I've debugged the save activity..
 * 
 * inspired by how downloads from a browser happen.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class SimpleDownloadActivity extends AbstractFileOrResourceActivity {

    private final FileSystemManager vfs;
    private final ResourceChooserInternal chooser;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
	    FileSystem fileSystem = f.getFileSystem();
	    return ! (fileSystem instanceof LocalFileSystem);
	}
	
    protected boolean invokable(Resource r) {
        return ConeProtocol.isCdsCatalog(r);
    }

	public SimpleDownloadActivity(final FileSystemManager vfs, ResourceChooserInternal chooser) {
		super();
        this.vfs = vfs;
        this.chooser = chooser;
		setText("Download" + UIComponentMenuBar.ELLIPSIS);
		setIcon(IconHelper.loadIcon("filesave16.png"));		
		setToolTipText("Download the selected file(s) to local disk or MySpace");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
	}

	public void actionPerformed(ActionEvent e) {
        final List resources = computeInvokableResources();
        final List files = new ArrayList();
        int sz = resources.size() + files.size();
        confirmWhenOverThreshold(sz,"Download all " + sz + " files?",new Runnable() {
            public void run() {
                doit(resources,files);
            }
        });      
	}
	private void doit(List resources, List files) {
        if (resources.size() > 0) {
            // very CDS swpecific at the moment
            for (Iterator i = resources.iterator(); i.hasNext();) {
                CatalogService vizCatalog = (CatalogService) i.next();
                URI s = findDownloadLinkForCDSResource(vizCatalog);
                if (s != null) {
                    files.add(s);
                }
            }            
        }	    
		files.addAll(computeInvokableFiles());
		logger.debug(files);
		
		// decided to use dirchooser here instead...
        final URI saveDir = chooser.chooseDirectoryWithParent("Choose destination for download",true,true,false,uiParent.get().getComponent());
        if (saveDir == null) {
            return;
        }
    /*    JDirectoryChooser chooser = new JDirectoryChooser(SystemUtils.getUserHome());
        chooser.setShowingCreateDirectory(false);
        chooser.setMultiSelectionEnabled(false);
        chooser.setApproveButtonText("Save");
        chooser.setDialogTitle("Choose destination for download");
        int code = chooser.showSaveDialog(uiParent.get().getComponent());
        if (code != JDirectoryChooser.APPROVE_OPTION) {
            return;
        }
        URI saveDir = chooser.getSelectedFile().toURI();
     */
        //@fixme - generate nicer file names 
		(new BulkCopyWorker(vfs,uiParent.get(),saveDir, files)).start();
		
	}
	
	public static URI findDownloadLinkForCDSResource(CatalogService r) {
	    Capability[] caps = r.getCapabilities();
	    for (int i = 0; i < caps.length; i++) {
	        Interface[] interfaces = caps[i].getInterfaces();
	        for (int j = 0; j < interfaces.length; j++) {
                Interface iface = interfaces[j];
                
            if (StringUtils.contains(iface.getType(),"ParamHTTP")) {
                // assume a single inteface
                return iface.getAccessUrls()[0].getValueURI();
            }
	        }
        }
	    return null;
	}



	
	

}
