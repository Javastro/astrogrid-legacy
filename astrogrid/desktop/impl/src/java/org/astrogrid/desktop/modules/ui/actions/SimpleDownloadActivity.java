/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.ConeProtocol;
import org.astrogrid.desktop.modules.ui.scope.VizModel;

/** Download files (or content from some resources) to local folder.
 * inspired by how downloads from a browser happen.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class SimpleDownloadActivity extends AbstractFileOrResourceActivity {

    private final FileSystemManager vfs;
    private final ResourceChooserInternal chooser;
	
	
    // applies to all non-local files and folders.
	@Override
    protected boolean invokable(final FileObjectView f) {
	    return ! "file".equals(f.getScheme());
	}
	
    @Override
    protected boolean invokable(final Resource r) {
        return ConeProtocol.isCdsCatalog(r);
    }

	public SimpleDownloadActivity(final FileSystemManager vfs, final ResourceChooserInternal chooser) {
		super();
		setHelpID("activity.download");
        this.vfs = vfs;
        this.chooser = chooser;
		setText("Download" + UIComponentMenuBar.ELLIPSIS);
		setIcon(IconHelper.loadIcon("filesave16.png"));		
		setToolTipText("Download the selected file(s) to local disk or VOSpace");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
	}

	@Override
    public void actionPerformed(final ActionEvent e) {
        final List<Resource> resources = computeInvokableResources();
        final List<FileObjectView> files = computeInvokableFiles();
        final int sz = resources.size() + files.size();
        confirmWhenOverThreshold(sz,"Download all " + sz + " files?",new Runnable() {
            public void run() {
                doit(resources,files);
            }
        });      
	}
	
	
	private void doit(final List<Resource> resources, final List<FileObjectView> files) {
	    final List<CopyCommand> commandList = new ArrayList<CopyCommand>();
	    if (resources.size() > 0) {
	        // utterly CDS specific
	        for (final Iterator i = resources.iterator(); i.hasNext();) {
	            final CatalogService vizCatalog = (CatalogService) i.next();
	            final URI s = findDownloadLinkForCDSResource(vizCatalog); // finds the right service capability.
	            if (s != null) {
	                final TableBean[] tables = vizCatalog.getTables(); // check how many tables we've got.
	                if (tables == null || tables.length == 1) { //just a single table - no need to do anything special.
	                    final String fname = VizModel.removeLineNoise(vizCatalog.getTitle()) + ".vot";
	                    commandList.add(new CopyAsCommand(s,fname));                                       
	                } else { // multiple tables  - emit a separate command for each.
	                    for (int t =0; t < tables.length; t++) {
	                        final String tableName = StringUtils.substringAfterLast(tables[t].getName(),"/"); // get trailing part of tablename.
	                        try {
	                        final URI tURI = new URI(s.toString() + "/" + tableName); // download url is the main url plus the tablename.
	                        final String fname = VizModel.removeLineNoise(vizCatalog.getTitle()) + " - " +  tableName + ".vot";
	                        commandList.add(new CopyAsCommand(tURI,fname));
	                        } catch (final URISyntaxException e) {
	                            logger.warn("Failed to construct download link",e);
	                        }
	                    }
	                }
 
	            }
	        }            
	    }	 
        
		for (int i = 0 ; i < files.size(); i++) {
		    commandList.add(new CopyCommand(files.get(i)));
		}
		
		if (commandList.isEmpty()) {
		    return; // nothing to do
		}
		
		// decided to use dirchooser here instead...
        final URI saveDir = chooser.chooseDirectoryWithParent("Choose destination folder to download into",true,true,false,uiParent.get().getComponent());
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
        final CopyCommand[] cmdArr = commandList.toArray(new CopyCommand[commandList.size()]);
		(new BulkCopyWorker(vfs,uiParent.get(),saveDir, cmdArr)).start();
		
	}
	
	/** Find the URL endpoint to enable download for a CDS Resource
	 * 
	 * @param r
	 * @return
	 */
	public static URI findDownloadLinkForCDSResource(final CatalogService r) {
	    /*
	     * This used to work fine by finding the ParamHTTP capability.
	     * However, this broke when the Vizier service changed sometime in Feb 2009.
	     * The capability URL found is something like
	     * http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A?-source=J/ApJ/613/682
	     * 
	     * But now, to just download the file, I must omit the /-dtd/-A part. I.e. the correct 
	     * download URL is
	     * http://vizier.u-strasbg.fr/viz-bin/votable?-source=J/ApJ/613/682
	     * 
	     * However, then Sebastien mentions
	     * 
" The clean way to do it : I should change all the ParamHttp in all the
VizieR resources. There are a few other improvements to do in the
resource descriptions, so this will come soon. But I'll send warnings
beforehand to all harvesters so they are aware that a few thousands
of resources will get updated, and therefore need to be downloaded
and updated in their service."
	     * 
	     * So I want to add a work-around which will adapt the current broken URL, but then will
	     * work fine if he changes the endpoint...
	     * 
	     * Therefore, am adding a hack to match exactly against the current form of the capability URL, 
	     * amd remove the /-dtd/-A if seen. If the URL returned doesn't match, it passes through.
	     */
	    final Capability[] caps = r.getCapabilities();
	    for (int i = 0; i < caps.length; i++) {
	        final Interface[] interfaces = caps[i].getInterfaces();
	        for (int j = 0; j < interfaces.length; j++) {
                final Interface iface = interfaces[j];
                
            if (StringUtils.contains(iface.getType(),"ParamHTTP")) {
                // assume a single accessURL
                 final URI uri = iface.getAccessUrls()[0].getValueURI();
                 // wrangle if it's the same as expected
                 if (uri.toString().startsWith(URL_NEEDS_MANGLING)) {
                     final String remainder = StringUtils.substringAfter(uri.toString(),URL_NEEDS_MANGLING);
                     return URI.create("http://vizier.u-strasbg.fr/viz-bin/votable?-source=" + remainder);
                 } else {
                     return uri;
                 }
            }
	        }
        }
	    return null;
	}
	
	/** Vizier URL prefix that indicates it needs mangling */
	private static final String URL_NEEDS_MANGLING = "http://vizier.u-strasbg.fr/viz-bin/votable/-dtd/-A?-source=";



	
	

}
