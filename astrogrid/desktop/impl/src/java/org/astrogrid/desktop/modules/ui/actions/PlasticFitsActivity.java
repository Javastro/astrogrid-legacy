/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.TupperwareInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.VOExplorerFactoryImpl;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.SiapRetrieval;
import org.votech.plastic.CommonMessageConstants;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20074:42:47 PM
 */
public class PlasticFitsActivity extends AbstractFileActivity {
	private static final Log logger = LogFactory
	.getLog(PlasticRegistryActivity.class);

private final PlasticApplicationDescription plas;
private final TupperwareInternal tupp;
	/**
	 * @param plas
	 * @param tupp
	 */
	public PlasticFitsActivity(PlasticApplicationDescription plas, TupperwareInternal tupp) {
		super();
		this.plas = plas;
		this.tupp = tupp;
		PlasticScavenger.configureActivity("FITS",this,plas);
	}

	protected boolean invokable(FileObject f) {
		try {
			final FileContent content = f.getContent();
            return VoDataFlavour.MIME_FITS_IMAGE.equals(content.getContentInfo().getContentType());
		} catch (FileSystemException x) {
			return false;
		}
	}

	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		for (Iterator i = l.iterator(); i.hasNext();) {
			FileObject f = (FileObject) i.next();
            if (f instanceof DelegateFileObject) { // if we've got a delegate, get to the source here...
                f = ((DelegateFileObject)f).getDelegateFile();
            }			
			sendLoadImageMessage(f);
		}
	}

	private void sendLoadImageMessage(final FileObject f) {
		(new BackgroundWorker(uiParent.get(),"Sending to " + plas.getName()) {
			protected Object construct() throws Exception {
				List l = new ArrayList();
				
				URL url = f.getURL();
				l.add(url.toString());
				tupp.singleTargetPlasticMessage(CommonMessageConstants.FITS_LOAD_FROM_URL,l,plas.getId());
				return null;
			}
			protected void doFinished(Object result) {
				parent.setStatusMessage("Message sent to " + plas.getName());
			}			
		}).start();		
	}

	/*
	 * 
    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode t= (TreeNode)i.next();
            if (t.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE) != null) {
                setEnabled(true);
                return;
            }
        }
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        (new BackgroundWorker(ui,super.getText()) {        
            protected Object construct() throws Exception {        
                for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find each leaf node
                    if (tn.getChildCount() > 0 ) {
                        continue;
                    }
                        URL url = new URL(tn.getAttribute(SiapRetrieval.IMAGE_URL_ATTRIBUTE));
                        List args = new ArrayList();
                        args.add(url.toString()); // plastic expects a string, but we first construct a url to ensure it's valid.
                        tupperware.singleTargetPlasticMessage(
                                CommonMessageConstants.FITS_LOAD_FROM_URL
                                ,args,targetId);
                   }// end for each child node.
                return null;
            }
        }).start();
    }        
    

	 */
}
