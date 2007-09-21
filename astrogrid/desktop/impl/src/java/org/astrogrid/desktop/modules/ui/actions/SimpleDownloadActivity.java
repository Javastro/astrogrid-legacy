/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;

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
		setText("Download");
		setIcon(IconHelper.loadIcon("filesave16.png"));		
		setToolTipText("Download the selected file(s) to local disk.");
	}
//	
//	private static final JDirectoryChooser chooser = new JDirectoryChooser();
//	static {
//	    chooser.setShowingCreateDirectory(true);
//	}
//	

	public void actionPerformed(ActionEvent e) {
        List resources = computeInvokableResources();
        List files = new ArrayList();
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
//		if (chooser.showSaveDialog(uiParent.get().getFrame()) != JFileChooser.APPROVE_OPTION) {
//		    return;
//		}
//		final File saveDir = chooser.getSelectedFile();
//		logger.debug(saveDir);
        final URI saveDir = chooser.chooseDirectoryWithParent("Select directory to download to",false,true,false,uiParent.get().getFrame());
        if (saveDir == null) {
            return;
        }
		(new BulkCopyWorker(vfs,uiParent.get(),saveDir, files)).start();
		
	}
	
	public static URI findDownloadLinkForCDSResource(CatalogService r) {
	    Capability[] caps = r.getCapabilities();
	    for (int i = 0; i < caps.length; i++) {
            if (caps[i].getType().indexOf("ParamHTTP") != -1) {
                org.astrogrid.acr.ivoa.resource.Interface[] ifaces = caps[i].getInterfaces();
                // assume a single inteface
                return ifaces[0].getAccessUrls()[0].getValueURI();
            }
        }
	    return null;
	}



	
	

}
